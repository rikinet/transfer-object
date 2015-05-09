package jp.rikinet.util.dto;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * DtoFactory に型を登録するときに使用する名前を与える。
 * アノテーションの方がメソッドや interface よりも簡潔に書けると思われる。
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DtoType {
	/**
	 * {@link DtoFactory#deserialize} の対象になる型を識別する名前を与える。
	 * アンダースコアで始まる名前はこのパッケージで予約している。
	 * それ以外の命名ルールは定めない。但し、DtoFactory から見て一意であること。
	 * @return 型の名前
	 */
	String value() default Root.CLASS_NAME;
}
