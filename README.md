# 🏰 332-blokey-land-service

<p align="center">
  <img src="https://github.com/user-attachments/assets/c1729a3c-8c1d-4d86-bbfd-afe080ccecab" width="400" />
</p>

### ✅ **개요**

- 프로젝트, 태스크, 마일스톤을 통합적으로 관리할 수 있는 서비스입니다.
- 데이터를 시각화하여 프로젝트 현황을 쉽게 파악할 수 있습니다.

### ⏱ **개발 기간**

- `v1.0.0` (`2025-06-06 ~ 2025-06-15`) : 서비스 구조 설계 및 초기 기능 구현
- `v1.0.1` (`2025-06-24 ~ 2025-07-07`) : 프론트엔드 개발 및 도메인 로직 정비
- `v1.0.2` (`2025-07-08`) : 개발 환경에서 웹 접속 시 무한 새로고침 버그 핫픽스

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
- 프로젝트 개발 버전 실행에 있어 `.env` 파일, `Docker`, `Docker-compose`, `CLI` 환경, 웹 브라우저가 필요합니다.

```bash
# 1. 인증 서버 프로젝트 클론
git clone "https://github.com/brobro332/332-sentinel-server.git"

# 2. Blokey-Land 서비스 프로젝트 클론
git clone "https://github.com/brobro332/332-blokey-land-service.git"

# 3. 환경변수 파일을 작성하여 각 프로젝트 루트 경로와 docker-compose.yml이 위치한 디렉터리에 저장해야 합니다.
#    .env 파일은 하단의 환경변수 파일 섹션을 참고해 작성합니다.

# 4. 인증 서버 실행
cd 332-sentinel-server             # 인증 서버 내 루트 경로로 이동
docker-compose up -d --build       # docker-compose 이미지 빌드 및 실행

# 5. Blokey-Land 서비스 실행
cd 332-blokey-land-service/docker  # Blokey-Land 서비스 내 /docker 디렉터리로 이동
docker-compose up -d --build       # docker-compose 이미지 빌드 및 실행

# 6. http://localhost 접속
```

### ⚙️ **환경변수 파일**
- 각 프로젝트별 `.env` 파일의 환경변수 목록입니다.
- 보안을 위해 `.env` 파일은 `.gitignore`에 추가하여 `Git` 저장소에 커밋되지 않도록 해주세요.

#### ✅ 인증 서버 (332-sentinel-server)

```bash
# SERVER
PORT                                 # 인증 서버 포트 번호
ADDRESS                              # 인증 서버 바인딩 IP

# ELASTICSEARCH
ELASTICSEARCH_EXTERNAL_PORT          # ElasticSearch 외부 포트
ELASTICSEARCH_INTERNAL_PORT          # ElasticSearch 내부 포트

# LOGSTASH
LOGSTASH_EXTERNAL_PORT_1             # Elastic Beats 계열 데이터 수집 도구 외부 포트
LOGSTASH_INTERNAL_PORT_1             # Elastic Beats 계열 데이터 수집 도구 내부 포트
LOGSTASH_EXTERNAL_PORT_2             # Logstash HTTP API 외부 포트
LOGSTASH_INTERNAL_PORT_2             # Logstash HTTP API 내부 포트

# KIBANA
KIBANA_EXTERNAL_PORT                 # Kibana 외부 포트
KIBANA_INTERNAL_PORT                 # Kibana 내부 포트

# MONGODB
DATA_MONGODB_URI                     # MongoDB 연결 URL
DATA_MONGODB_USERNAME                # MongoDB 사용자명
DATA_MONGODB_PASSWORD                # MongoDB 비밀번호
DATA_MONGODB_DATABASE                # MongoDB 데이터베이스명
DATA_MONGODB_AUTHENTICATION_DATABASE # MongoDB 인증 대상 데이터베이스명
MONGODB_EXTERNAL_PORT                # MongoDB 외부 포트
MONGODB_INTERNAL_PORT                # MongoDB 내부 포트

# REDIS
DATA_REDIS_HOST                      # Redis 연결 호스트
DATA_REDIS_PORT                      # Redis 연결 포트
REDIS_EXTERNAL_PORT                  # Redis 외부 포트
REDIS_INTERNAL_PORT                  # Redis 내부 포트

# SPRING_CLOUD_GATEWAY
GLOBALCORS_ALLOWED_ORIGINS           # Cross-Origin 요청 허용 도메인

# JWT
JWT_SECRET_KEY                       # JWT 시크릿 키 (Git 등 외부에 노출되지 않도록 주의하세요.)
```

#### ✅ Blokey-Land 서비스 (332-blokey-land-service)

```bash
# SERVER
PORT                                 # Blokey-Land 서비스 포트 번호
ADDRESS                              # Blokey-Land 서비스 바인딩 IP
FILE_UPLOAD_DIR                      # 첨부파일 업로드 시 저장 디렉터리 경로

# NGINX
NGINX_EXTERNAL_PORT                  # Nginx 외부 포트
NGINX_INTERNAL_PORT                  # Nginx 내부 포트

# POSTGRES
DATA_POSTGRES_URI                    # PostgreSQL 연결 URI
DATA_POSTGRES_USERNAME               # PostgreSQL 사용자명
DATA_POSTGRES_PASSWORD               # PostgreSQL 비밀번호
DATA_POSTGRES_DB                     # PostgreSQL 데이터베이스명
DATA_POSTGRES_AUTHENTICATION_DB      # PostgreSQL 인증 대상 데이터베이스명
POSTGRES_EXTERNAL_PORT               # PostgreSQL 외부 포트
POSTGRES_INTERNAL_PORT               # PostgreSQL 내부 포트
```

### 🚀 **`Swagger`**
- 각 프로젝트의 포트 번호 환경변수 값을 `URL` 에 포함해주세요.
- 인증 서버 : `http://localhost:${PORT}/webjars/swagger-ui/index.html`
- Blokey-Land 서비스 : `http://localhost:${PORT}/swagger-ui/index.html`
