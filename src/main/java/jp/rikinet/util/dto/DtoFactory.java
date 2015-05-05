/*
The MIT License (MIT)

Copyright (c) 2015 Riki Network Systems Inc.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package jp.rikinet.util.dto;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Root 及び Root を継承した DTO へ JSON 文字列を変換する。
 */
public class DtoFactory {

	/**
	 * 型名を取得する際のキー。JSON中に必須の要素。
	 * アンダースコアで始まる名前はパッケージ外では使用しないこと。
	 */
	static final String KEY_CLASS = "_class_";

	/**
	 * クラス名からクラスオブジェクトへの対応表。
	 * この表に登録されていないクラスは処理の対象にならない。
	 *
	 * @see DtoFactory#register(Class)
	 */
	private static HashMap<String, Class<? extends Root>> classTable;

	static {
		classTable = new HashMap<String, Class<? extends Root>>();
	}

	private DtoFactory() {
		// singleton
	}

	/**
	 * 入れ子のオブジェクトや配列を持たない DTO を作成する。
	 * @param cl classTable に登録されている DTO クラスオブジェクト
	 * @param jo DTO の元または内部表現になる JSONObject
	 * @param <T> DTO のクラス
	 * @return 生成した DTO
	 */
	private static <T extends Root> T newSimpleDto(Class<?> cl, JSONObject jo) {
		T skeleton = null;
		try {
			Constructor<?> cons = cl.getConstructor(JSONObject.class);
			skeleton = (T) cons.newInstance(jo);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return skeleton;
	}

	/**
	 * 与えられた DTO にプロパティを設定する。
	 * @param dto プロパティ設定の対象になる DTO
	 * @param name プロパティ名。例：birthDate 使用する setter は setBirthDate()
	 * @param value プロパティの値
	 */
	private static void setProperty(Root dto, String name, Object value) {
		Class<?> cl = dto.getClass();
		String mName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
		try {
			Method m = cl.getMethod(mName, value.getClass());
			m.invoke(dto, value);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 与えられた JSDONObject から DTO を生成する。
	 * 入れ子のオブジェクトがあり、DTO に適切な setter があるなら、
	 * プロパティとして設定する。これを再帰的に行う。
	 * @param jo 生成の元データ兼生成した DTO の内部表現
	 * @param <T> 生成する DTO の型
	 * @return 生成した DTO
	 */
	private static <T extends Root> T convert(JSONObject jo) {
		String typeName = jo.getString(KEY_CLASS);
		if (typeName == null) {
			// 型ヒントを含んでない
			return null;
		}

		Class<?> cl = classTable.get(typeName);
		if (cl == null) {
			// 型が登録されていない
			return null;
		}
		T newDto = newSimpleDto(cl, jo);

		// 下位オブジェクトをスキャンして、あればプロパティとしてセットする
		Iterator<String> iter = jo.keys();
		while (iter.hasNext()) {
			String key = iter.next();
			Object val = jo.get(key);
			if (val instanceof JSONArray) {
				// construct array
				// set as property value
				setProperty(newDto, key, null);
				continue;
			}
			if (val instanceof JSONObject) {
				// construct sub object
				Root subObj = convert((JSONObject) val);
				// set as property value
				setProperty(newDto, key, subObj);
				continue;
			}
		}
		return newDto;
	}

	/**
	 * JSON 文字列から内包している型のヒントを使って DTO を生成する。
	 * @param str JSON 文字列。キーとして _class_ を持つこと。
	 * @param <T> 生成する DTO のクラス
	 * @return T 型の DTO
	 */
	public static <T extends Root> T deserialize(String str) {
		JSONObject jObj = new JSONObject(str);
		return convert(jObj);
	}

	/**
	 * JSON 文字列との変換の対象になるクラスを先立って登録する。
	 * 登録のないクラスは無視される。
	 * @param clazz Root を継承したクラス
	 */
	public static void register(Class<? extends Root> clazz) {
		try {
			Method m = clazz.getMethod("getClassName");
			String c = (String) m.invoke(null);
			classTable.put(c, clazz);
		} catch (Exception e) {
			throw new IllegalArgumentException("can't get class name.", e);
		}
	}
}
