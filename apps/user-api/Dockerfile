# --------------- 1st stage build ---------------
# ビルド用JDKのベース
FROM eclipse-temurin:21-jdk AS builder

# 作業ディレクトリ
WORKDIR /build

# ビルドに必要なものをコピー
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# 依存関係を解決
RUN ./mvnw -q -B dependency:go-offline

# ソースをコピー
COPY src ./src

# テストはスキップしてビルド
RUN ./mvnw -q -B package -DskipTests

# --------------- 2nd stage runtime ---------------
# 実行用なのでJREのベース
FROM eclipse-temurin:21-jre AS runtime

# 作業ディレクトリ
WORKDIR /app

# builder から FAT-jar をコピー
COPY --from=builder build/target/user-api.jar ./user-api.jar

# 本番ポート
EXPOSE 8080

# java -jar で起動
CMD ["java","-jar","user-api.jar"]