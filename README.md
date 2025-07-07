# 🏰 332-blokey-land-service

<p align="center">
  <img src="https://github.com/user-attachments/assets/c1729a3c-8c1d-4d86-bbfd-afe080ccecab" width="400" />
</p>

### ✅ **개요**
- 프로젝트, 태스크, 마일스톤을 통합적으로 관리할 수 있는 서비스입니다.
- 데이터를 시각화하여 프로젝트 현황을 쉽게 파악할 수 있습니다.

### ⏱ **개발 기간**
- `v1.0.0` 2025-06-06 ~ 2025-06-15 : 서비스 구조 설계 및 초기 기능 구현
- `v1.0.1` 2025-06-24 ~ 2025-07-07 : 프론트엔드 개발 및 도메인 로직 정비

### 🛠 **프로젝트 환경**
- 언어: `Java 21`, `Typescript`
- 프레임워크 : `Spring boot 3.5.0`
- 데이터베이스: `PostgreSQL`
- `IDE`: `IntelliJ IDEA`
- `CSR` : `React`
- 빌드 도구: `Gradle`
- `ORM`: `JPA`
- 동적쿼리 라이브러리: `QueryDSL`
- `DevOps`: `Docker`, `Docker-compose`

### 📃 **개발 환경 구축 매뉴얼**
```bash
# 프로젝트 개발 버전 실행에 있어 Docker, Docker-compose, CLI 환경, 웹 브라우저가 필요합니다.

# 인증 서버 프로젝트 클론
git clone "https://github.com/brobro332/332-sentinel-server.git"

# Blokey-Land 서비스 프로젝트 클론
git clone "https://github.com/brobro332/332-blokey-land-service.git"

# 인증 서버 실행
cd 332-sentinel-server         # 인증 서버 내 루트 경로로 이동
docker-compose up -d --build   # docker-compose 이미지 빌드 및 실행

# Blokey-Land 서비스 실행
cd 332-blokey-land-service/docker  # Blokey-Land 서비스 내 /docker 디렉터리로 이동
docker-compose up -d --build       # docker-compose 이미지 빌드 및 실행

# http://localhost 접속
```

### ⚙️ **`Swagger`**
- 인증 서버 : `http://localhost:8080/webjars/swagger-ui/index.html`
- Blokey-Land 서비스 : `http://localhost:8081/swagger-ui/index.html`
