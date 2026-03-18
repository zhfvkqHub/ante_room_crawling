# 청년안심주택 모집공고 수집

청년안심주택 및 공공임대주택의 모집공고를 자동으로 크롤링하여 한곳에서 확인할 수 있는 웹 서비스입니다.
새로운 공고가 등록되면 Firebase 푸시 알림으로 실시간 알려드립니다.

## 기술 스택

### Backend
- **Java 17** / **Spring Boot 3.3.1**
- **MariaDB** + **JPA** + **QueryDSL**
- **Selenium** (동적 페이지 크롤링)
- **Jsoup** (정적 HTML 파싱)
- **Firebase Admin SDK** (FCM 푸시 알림)
- **MapStruct** (객체 매핑)

### Frontend
- **Vue 3** + **Vue Router 4** + **Vuex 4**
- **Axios** (HTTP 통신)
- **Firebase SDK** (웹 푸시 알림)
- **date-fns** (날짜 포맷)

### Build & Deploy
- **Gradle** + Node Plugin (프론트엔드 통합 빌드)
- 프론트엔드 빌드 결과가 `src/main/resources/static`으로 복사되어 단일 JAR로 배포

## 주요 기능

### 크롤링
평일 08:00~19:00 사이 30분 간격으로 아래 사이트들의 공고를 자동 수집합니다.

| 크롤링 서비스 | 대상 사이트 | 방식 | 상태 |
|---|---|---|---|
| SeoulCrawlingService | 서울시 청년안심주택 (soco.seoul.go.kr) | Selenium | 활성 |
| ElyesCrawlingService | 엘리스 (elyes.co.kr/post/recruit) | Selenium | 활성 |
| DaebangCrawlingService | 신대방삼거리역 골든노블레스 (db40314.kr) | Selenium | 활성 |
| JamsilcentralparkCrawlingService | 잠실센트럴파크 (jamsilcentralpark.com) | Selenium | 활성 |
| ForenaTangsanCrawlingService | 포레나당산 | Jsoup | 활성 |
| SbnpartCrawlingService | 상봉동양엔파트 (sbnpart.co.kr) | Jsoup | 활성 |
| CentersquaresnuCrawlingService | 센터스퀘어등촌 (centersquaresnu.com) | Jsoup | 활성 |
| ModooCrawlingService | 모두 플랫폼 8개 사이트 | - | 비활성 (2025-06 서비스 종료) |
| ConestCrawlingService | 코네스트 | - | 비활성 |

### 공고 조회
- 사이트명 / 지역구 / 공고유형 필터링
- 제목 검색
- 페이지네이션 (15건 단위)
- 오늘 등록된 공고 하이라이트 표시

### 지역구 (30개)
서울 25개구 + 화성시, 용인시, 김포시, 부산, 기타

### 공고 유형
| 유형 | 설명 |
|---|---|
| 모집공고 | 신규 모집 및 추가모집 공고 |
| 접수현황 | 접수 경쟁률 현황 |
| 당첨자발표 | 당첨자 발표 결과 |
| 기타 | 그 외 공지사항 |

### 푸시 알림
- Firebase Cloud Messaging 기반
- 새 공고 등록 시 등록된 모든 기기에 알림 발송
- Service Worker를 통한 백그라운드 알림 지원

## 프로젝트 구조

```
ante_room_crawling/
├── frontend/                          # Vue 3 프론트엔드
│   ├── public/
│   │   └── firebase-messaging-sw.js   # FCM Service Worker
│   └── src/
│       ├── api/                       # API 호출 모듈
│       ├── components/                # Vue 컴포넌트
│       │   ├── notice/NoticeList.vue  # 공고 목록
│       │   └── common/               # Header, Footer
│       ├── views/                     # 페이지 뷰
│       ├── store/                     # Vuex 상태관리
│       ├── firebase.js                # Firebase 설정
│       └── main.js                    # 앱 진입점
│
└── src/main/java/com/anteprj/        # Spring Boot 백엔드
    ├── crawling/
    │   ├── service/                   # 크롤링 서비스 인터페이스 & 구현체
    │   ├── controller/                # 스케줄러
    │   └── repository/                # 공고 저장소
    ├── notice/                        # 공고 조회 API
    │   ├── controller/
    │   ├── service/
    │   └── dto/
    ├── push/                          # FCM 푸시 알림
    │   ├── controller/
    │   ├── service/
    │   └── repository/
    ├── entity/                        # JPA 엔티티
    │   └── constant/                  # Enum (SiteName, Constituency, NotiType)
    └── util/                          # Jsoup, WebDriver 유틸
```

## API 엔드포인트

| Method | URL | 설명 |
|---|---|---|
| GET | `/api/notice` | 공고 목록 조회 (필터 + 페이지네이션) |
| GET | `/api/notice/last-crawled-time` | 마지막 크롤링 시간 조회 |
| GET | `/api/notice/sites` | 사이트 목록 조회 |
| GET | `/api/notice/constituencies` | 지역구 목록 조회 |
| POST | `/api/push/token` | FCM 토큰 등록 |

## 실행 방법

### 사전 요구사항
- Java 17+
- MariaDB
- Chrome (Selenium 크롤링용)

### 데이터베이스 설정
```sql
CREATE DATABASE crawling_db;
```

### 로컬 실행
```bash
# 프론트엔드 + 백엔드 통합 빌드 & 실행
./gradlew bootRun

# 프론트엔드 개발 서버 (별도 실행 시)
cd frontend
npm install
npm run serve
```

### 환경별 설정
| 환경 | 포트 | 크롤링 주기 |
|---|---|---|
| local | 8085 | 1분 간격 (개발용) |
| dev | 8080 | 30분 간격 |
