package jp.rikinet.util.dto;

import org.json.JSONObject;

/**
 * DtoFactory に登録する全てのデータ型はこの型を継承して作成する。
 *
 * string や number 型の値については JSONObject の getXXX を
 * getter としてラップしたり、put を setter としてラップする。
 * 値が object の時はキーが名前となるプロパティを定義する。
 *
 * @see DtoFactory
 */
@DtoType(Root.CLASS_NAME)
public class Root {

	/**
	 * JSON に埋め込むデータ型名。
	 * アンダースコアで始まる名前はこのパッケージ内でのみ使用する。
	 */
	static final String CLASS_NAME = "_root_";

	/**
	 * フィールドを設ける代わりに各プロパティを保持する。
	 * フィールド名をキーにするマップのように扱う。
	 * 拡張クラスでのアクセスを許可する。
	 */
	protected JSONObject jsonObj;


	/**
	 * 引数なしコンストラクター。
	 * 導出クラスでも必ずオーバーライドする。
	 */
	public Root() {
		jsonObj = new JSONObject();
		jsonObj.put(DtoFactory.KEY_CLASS, CLASS_NAME);
	}

	/**
	 * 内部表現のオブジェクトを外部から与える場合のコンストラクター。
	 * 導出クラスでもかならずオーバーライドする。
	 * @param obj そのまま内部表現として保持する
	 */
	public Root(JSONObject obj) {
		jsonObj = obj;
		jsonObj.put(DtoFactory.KEY_CLASS, CLASS_NAME);
	}

	/**
	 * このオブジェクトの JSON 表現を返す。
	 * @return JSON 文字列
	 */
	@Override
	public String toString() {
		return jsonObj.toString();
	}
}
