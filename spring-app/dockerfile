FROM openjdk:17-jdk-slim

# 기본 포트 설정
EXPOSE 8080

# JAR 파일 복사
COPY ./build/libs/co-working-0.0.1-SNAPSHOT.jar /usr/local/app/co-working-0.0.1-SNAPSHOT.jar

# 환경 변수 설정
ENV ENV_DB=jdbc:oracle:thin:@oracle-db:1521:xe
ENV ENV_USERNAME=system

# 작업 디렉터리 설정
WORKDIR /usr/local/app

# 애플리케이션 실행
ENTRYPOINT ["sh", "-c", "sleep 60 && java -jar co-working-0.0.1-SNAPSHOT.jar"]