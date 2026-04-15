<h1 align="center">🚭 無연</h1>

<p align="center">
  <b>흡연 이력 기반 금연 관리 및 커뮤니티 웹 서비스</b>
</p>

<p align="center">
  사용자의 흡연 이력을 바탕으로 금연 경과, 절약 금액, 건강 변화 지표를 시각화하고,<br/>
  커뮤니티 기능으로 금연 지속 동기를 높이는 서비스입니다.
</p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/95cda3d6-7a6e-47b4-bcb3-54fa0c8ec9af" width="800" alt="무연 대표 이미지" />
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-007396?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.2.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" />
  <img src="https://img.shields.io/badge/MySQL-Database-4479A1?style=for-the-badge&logo=mysql&logoColor=white" />
  <img src="https://img.shields.io/badge/JWT-Auth-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white" />
  <img src="https://img.shields.io/badge/Swagger-API_Docs-85EA2D?style=for-the-badge&logo=swagger&logoColor=black" />
  <img src="https://img.shields.io/badge/JavaMailSender-Mail-FF6F61?style=for-the-badge" />
</p>

---

## 1. 프로젝트 소개

無연은 해커톤 주제인 “IT 기술을 활용하여 현대인의 건강(wellness) 문제를 해결하는 웹 서비스”에 맞춰 기획한 금연 관리 서비스입니다.  
금연은 건강과 직결되지만 혼자 꾸준히 실천하기 어렵다는 점에 주목했고,
사용자가 자신의 변화를 직접 확인하고 다른 사용자와 소통할 수 있도록 서비스를 설계했습니다.

### 핵심 가치
- 금연 경과와 건강 변화를 **수치로 확인**
- 절약 금액, 피우지 않은 담배 수 등 **직관적인 피드백 제공**
- 커뮤니티를 통한 **지속 동기 부여**
- 이메일 인증과 프로필 관리까지 포함한 **사용자 중심 서비스**

---

## 2. 기획 배경

흡연은 심혈관 질환, 호흡기 질환, 중독 및 금단 증상, 암 등 다양한 건강 문제와 연결됩니다.  
하지만 금연은 단순한 결심만으로 오래 유지하기 어렵고, 자신의 변화가 눈에 보이지 않으면 동기를 잃기 쉽습니다.

**無연**은 이런 문제를 해결하기 위해,
- 금연 경과 시간
- 피우지 않은 담배 개수
- 절약 금액
- 늘어난 수명
- 총 흡연 기간
- 누적 소비 금액
- 누적 타르량

등의 정보를 한 화면에서 보여주도록 설계했습니다.

---

## 3. 주요 기능

### 3-1. 사용자 인증 및 회원 관리
- BCrypt 기반 비밀번호 암호화
- JWT 기반 로그인 인증
- 회원가입 / 로그인 / 회원정보 수정 / 회원 탈퇴
- 아이디 / 이메일 중복 확인
- 아이디 찾기 기능

### 3-2. 이메일 인증 및 비밀번호 재설정
- JavaMailSender를 활용한 이메일 인증번호 발송
- 이메일별 인증번호 검증 로직 구현
- 임시 비밀번호 발급 및 메일 전송 기능 구현

### 3-3. 금연 데이터 기반 대시보드
- 금연 시작일, 흡연 시작일, 하루 흡연량, 담배 가격, 한 갑당 개비 수, 타르량 관리
- 금연 경과 시간, 피우지 않은 담배 수, 절약 금액, 늘어난 수명 계산
- 총 흡연 기간, 누적 소비 금액, 누적 타르량 조회

### 3-4. 커뮤니티 게시판
- 게시글 생성 / 조회 / 수정 / 삭제
- 사용자별 게시글 조회
- 댓글 기능 및 좋아요 기능
- 사용자 간 금연 경험 공유 및 소통 지원

### 3-5. 이미지 업로드
- 게시글 이미지 업로드 지원
- 회원 프로필 이미지 업로드 및 조회
- `multipart/form-data` 기반 처리

---

## 4. 백엔드 구현 포인트

### 인증/인가
- Spring Security 기반 인증 흐름 구성
- JWT 발급 및 검증 로직 구현

### 메일 처리
- 인증번호 발송 및 비밀번호 재설정 메일 전송 기능 구현

### 도메인 설계
- 회원 정보에 흡연/금연 관련 데이터를 포함해 개인화된 금연 지표 계산 가능하도록 설계

### API 문서화
- Swagger(OpenAPI) 기반 API 문서화 지원

---

## 5. 기술 스택

| Category | Stack |
| --- | --- |
| Language | Java 17 |
| Framework | Spring Boot 3.2.2 |
| Database | MySQL |
| ORM | Spring Data JPA |
| Security | Spring Security, JWT |
| Mail | JavaMailSender |
| API Docs | Swagger / Springdoc OpenAPI |
| Build Tool | Gradle |

---

## 6. 시연 화면

### 서비스 개요
| 개발 배경 | 서비스 구조 |
| --- | --- |
| <img src="https://github.com/user-attachments/assets/faba512f-57f4-4c5b-8e1a-90e1a2e46ef8" width="420" alt="개발 배경" /> | <img src="https://github.com/user-attachments/assets/a1af033e-ab5b-4a21-b17d-525b7d4ea982" width="420" alt="서비스 구조" /> |

### 주요 기능
| 메인 대시보드 | 커뮤니티 |
| --- | --- |
| <img src="https://github.com/user-attachments/assets/fd37f88a-f9d2-4b24-9013-22f3db681944" width="420" alt="메인 대시보드" /> | <img src="https://github.com/user-attachments/assets/b1d6448e-2da5-4596-adde-e34665f0d4d6" width="420" alt="커뮤니티" /> |

---

## 7. 트러블슈팅

### 1) 텍스트 + 파일 동시 전송 문제
게시물 생성/수정 시 텍스트 데이터와 이미지 파일을 함께 전송할 때 `@RequestBody` 방식으로는 정상 처리되지 않는 문제가 발생했습니다.  
이를 해결하기 위해 `multipart/form-data` 기반으로 요청을 변경하고, DTO와 파일을 분리 처리하도록 수정했습니다.

### 2) 도메인 분리 환경에서 발생한 CORS 오류
프론트와 백엔드 주소를 분리해 관리하는 환경에서 CORS 오류가 발생했습니다.  
전역 CORS 설정을 적용하고 요청 경로를 재점검하여 프론트-백엔드 연동 문제를 해결했습니다.

---

## 8. 팀 구성

| 이름 | 역할 |
| --- | --- |
| 김찬주 | 기획 / 디자인 |
| 구혜원 | 프론트엔드 |
| 김채연 | 프론트엔드 |
| 안흥수 | 프론트엔드 |
| 황성윤 | 백엔드 |
| 홍은택 | 백엔드 |
