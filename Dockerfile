# JRE 베이스 이미지 선택
FROM eclipse-temurin:17-jre

# 작업 디렉토리 설정
WORKDIR /app

# 애플리케이션의 빌드 결과물 복사
ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} /app/

# 로컬에서 Docker 실행시 Env 파일 복사 필요
#COPY ./env/backend/.env /app/env/backend/.env

# 애플리케이션 실행
CMD ["java", "-jar", "backend.jar"]