FROM maven:3.9-eclipse-temurin-21

# アプリケーションの作業ディレクトリを設定
WORKDIR /portfolio

# ソースコードをコンテナにコピー
COPY . .

# Maven でビルド
RUN mvn clean package -DskipTests

# 実行時の JAR ファイルを指定
CMD ["java", "-jar", "target/portfolio-0.0.1-SNAPSHOT.jar"]