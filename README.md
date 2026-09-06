# 플래닛 (Plant it)

희귀·약용 식물 정보 제공과 보호 활동 참여를 돕는 **안드로이드 커뮤니티 앱**입니다. 🌱

## 🎯 프로젝트 소개

플래닛은 희귀 식물과 약용 식물에 대한 정보를 손쉽게 확인하고, 사용자가 직접 식물 관찰과 보호 활동에 참여할 수 있도록 돕는 플랫폼입니다. 식물 도감 검색, 개인 식물 일지 작성, 게시판 공유 커뮤니티, 보호 식물 현황 방사형 그래프 시각화, 사용자 맞춤 설정 등의 기능을 제공합니다.

* **팀원 구성**: 개인 프로젝트 (1인 개발)
* **진행 방식**: GitHub를 활용한 코드 관리

## 👩🏻‍💻 담당 역할

기획부터 UI/UX 디자인, 프론트엔드 및 백엔드 개발까지 **프로젝트 전 과정을 1인 전담**하여 제작했습니다.

* **서비스 기획 & UI/UX 디자인**:
  * 서비스 기능 구상 및 화면 레이아웃 설계 (총 13개 화면)
  * UI 테마 색상, 프레임 레이아웃, 텍스트 스타일 등 디자인 요소의 일관성 확보
* **Front-end 개발**:
  * `DrawerLayout`, `Navigation`, `ViewPager2`, `Fragment`, `RecyclerView` 기반 UI 및 스플래시 화면 구현
  * 방사형 그래프 시각화 및 UI 애니메이션, 커스텀 레이팅바 구현
  * 외부 유틸리티 기능 연동 (유튜브 동영상 검색/재생, 웹사이트 방문, 길찾기, 전화, 메일 등)
* **Back-end & API 연동**:
  * `Firebase Auth`를 통한 이메일 회원가입 및 Google/네이버 SNS 로그인 구현
  * `Firebase Firestore` & `Storage`를 활용한 이미지 포함 게시글 데이터 관리
  * 공공 API(2개) 연동, 다음 우편번호 API 및 PHP 기반 외부 서버 연동
  * `SharedPreferences`를 활용한 사용자 맞춤 설정(검색 버튼 색상, 글씨 크기 등) Local 저장

## 🖥️ 사용 기술

- **Language / IDE**: Kotlin, Android Studio
- **Backend / Database**: Firebase (Auth, Firestore, Storage), PHP
- **API & Library**: Public Open API, Daum Postcode API, SharedPreferences

## 🌱 성장 경험

- **A to Z 1인 풀스택 개발 역량 함양**: 기획, 디자인, 안드로이드 UI 개발, 백엔드 데이터베이스 구축 및 API 연동까지 전 과정을 직접 수행하며 전체적인 서비스 개발 흐름을 체득했습니다.
- **디자인 일관성 및 사용자 맞춤 UI/UX 구현**: 컬러 시스템과 레이아웃 일관성을 유지하고, 검색 버튼 색상 변경/글씨 크기 조정 등 맞춤형 설정을 제공하여 사용자 경험을 크게 개선했습니다.
- **다양한 외부 데이터 연동 및 문제 해결력 향상**: Firebase 실시간 데이터 처리, 공공 API 연동 과정에서 발생하는 기술적 문제와 예외 상황을 해결하며 논리적 사고력을 길렀습니다.

## 🔗 참고 링크

- [프로젝트 노션](https://app.notion.com/p/Plant-it-1814ceab4f8280fcadecf152fac1e19f?source=copy_link)
