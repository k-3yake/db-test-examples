#DONE
* サービス層のテスト（Repositoryのモック化）
* サービス層のテスト（DbSetup）
* サービス層のテスト（DbUnit）
* サービス層のテスト（DbUnitのDbSetup風）
* APIレイヤーテスト（spring-boot-test + DbSetup）
* APIレイヤーテスト（Controllerインジェクション + DbSetup）
* APIレイヤーテスト（Springでバリデーションやらリクエストの組み立てやらしている場合）

#DOING
Entityのリファクタ（こんなにgetter要る？）

#TODO
マスタデータの戦略の説明のため、カントリーテーブルを作成
HibernateValidatorを使えば上手く行くか？
バリデーションエラーをもうちょっとマシな例にする
アノテーションの理解と整理（何のために何が必要なのか）
コンテナ起動高速化

* springを使用したユニットテスト、インテグレーションテスト、APIレイヤーテストの例のプロジェクトを作成
    ユニットテスト(Junit,AssertJ,Jmocikt)
    インテグレーションテスト(ユニットテストのやつ + DbSetup(RepositoryとDBのインテグレーション),),SpringTestRunner
    APIレイヤーテスト(ユニットテストのやつ + spring-boot-test(DBSetup等は使わない))postのテストをDBまで見るか、再度findするか検討（APIの振る舞いのテストなのでDBを見ないのが正しい気がする）
