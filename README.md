# transfer-object
data transfer objects represented by json internally

データ転送の構文に JSON を採用した場合、
Java オブジェクトへ変換するのに JSONObject を経由してフィールドに値をセットするのを繰り返すのが迂遠に思えた。
また、Java オブジェクトから JSON 文字列を生成するのに都度 JSONObject を生成する必要があるのにも疑問を覚えた。

JSONObject をフィールドの代わりに保持しながらプロパティを実現する手段としてこれを作ってみた。

作ってみたが、できた Java オブジェクトの使い勝手やアクセサーの性能的にどうなんだろう。

javax.json.JsonObject はイミュータブルなので DTO の内部表現として採用できなかった。

依存する JSON ライブラリ org.json.Json は Java 8 でコンパイルしてある。
このプロジェクトでも Java 8 が必要。