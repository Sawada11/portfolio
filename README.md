# SNSアプリケーション

## プロジェクト名
SNS

## URL
160.16.116.189:8081

## 認証情報
- 名前: root
- Password: root

## プロジェクト概要
SNSアプリケーションの開発を行い、ユーザー登録、投稿機能、プロフィール管理機能を実装しました。
Spring Bootを使用してバックエンドを構築し、MySQLデータベースと連携しています。

## 主な機能
1. ユーザー管理
   - ユーザー登録: 新規ユーザーの登録機能
   - ログイン: 登録済みユーザーの認証機能
2. 投稿機能
   - ユーザーが記事を投稿できる機能
   - 記事に対するコメント機能
3. プロフィール管理
   - ユーザー情報の編集・表示機能

## ERD (エンティティ関係図)
- UserEntity: ユーザー情報 (id, username, password, role)
- ArticleEntity: 記事情報 (id, user_id, title, content, imageFileName, createAt)
- CommentEntity: コメント情報 (id, article_id, user_id, content, createAt)

![スクリーンショット 2025-03-22 215027](https://github.com/user-attachments/assets/2f34c99e-940f-4752-8dbd-68b683cca29a)


## 使用技術
- バックエンド
  - メイン言語・フレームワーク: Java 21 / Spring Boot 3.3.0
  - データベース: MySQL
  - セキュリティ: Spring Securityを使用した認証・認可機能の実装
- フロントエンド
  - Thymeleafを使用したテンプレートエンジン

## 技術的な課題と解決策
- 画像の投稿機能
  - ユーザーパースの指定のディレクトリに保存し、DBにはファイル名やパスを記録

## 追加予定機能
- N+1問題
  - EntityGraphの活用
- 投稿の「いいね」機能の実装
- WebSocketを用いたリアルタイム通信を導入
  - STOMPプロトコルを活用し、リアルタイムメッセージ送信機能を実装
