package jp.rikinet.util.dto;

import org.json.JSONArray;

/**
 * JSON の配列に相当する Root の補助クラス。
 * JSON の配列が任意の型の詰め合わせを許容しているので、
 * これも Object の配列とせざるを得ない。
 */
public class DtoArray {
	/**
	 * 復号化済みのオブジェクトを収める
	 */
	private Object[] dtoArray;
	/**
	 * dtoArray に収められたオブジェクトの元や内部表現を収める
	 */
	private JSONArray jsonArray;

	/**
	 * 配列のサイズを与えて初期化する。
	 * 大きさに関して静的な構造であることに注意。
	 */
	public DtoArray(int siz) {
		dtoArray = new Object[siz];
		jsonArray = new JSONArray();
	}

	/**
	 * 配列に長さを返す。
	 * 要素を収める領域の大きさなので、
	 * null でない要素の個数とは限らないので注意。
	 * @return 収められる要素の最大個数
	 */
	public long size() {
		return dtoArray.length;
	}

	/**
	 * 配列の要素へアクセスする。
	 * @param index 0 から始まる添え字
	 * @return index 番目の要素
	 */
	public Object get(int index) {
		return dtoArray[index];
	}

	/**
	 * 配列の要素を追加または置き換える。
	 * @param index 0 から始まる添え字
	 * @param obj index 番目の新しい要素
	 */
	public void put(int index, Object obj) {
		if (obj instanceof Boolean) {
			jsonArray.put(index, (Boolean) obj);
		} else if (obj instanceof Long) {
			jsonArray.put(index, (Long) obj);
		} else if (obj instanceof Double) {
			jsonArray.put(index, (Double) obj);
		} else if (obj instanceof String) {
			jsonArray.put(index, (String) obj);
		} else if (obj instanceof Root) {
			jsonArray.put(index, ((Root) obj).jsonObj);
		} else if (obj instanceof DtoArray) {
			jsonArray.put(index, ((DtoArray) obj).jsonArray);
		} else {
			throw new IllegalArgumentException("object type is unknown");
		}
		dtoArray[index] = obj;
	}
}
