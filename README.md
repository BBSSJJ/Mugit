# Mugit

## 목차

### 1. [주제](#1-프로젝트-주제)

### 2. [페르소나](#2-페르소나-분석)

### 3. [프로젝트 기획](#3-프로젝트-기획-1)

### 4. [프로젝트 설계](#4-프로젝트-설계-1)

### 5. [구현 화면](#5-구현-화면-1)

### 6. [기능 명세서](#6-기능-명세서-1)

### 7. [API 명세서](#7-api-명세서-1)

### 8. [프로젝트 관리](#8-프로젝트-관리-1)

## 1. 프로젝트 주제

### GIT 처럼 음악 관리, 협업할 수 있는 플랫폼

## 2. 페르소나 분석


## 3. 프로젝트 기획

### 기획의도

#### 자신의 창작물 프로젝트를 저장해서 관리할 수도 있고 다른 사람과도 쉽게 협업할 수 있도록 한다.

### 메인 기능

#### 1. 

#### 2. 

#### 3. 

#### 4. 

### 세부기능

#### 1. 

- 
- 

#### 2. 

- 
- 

#### 3. 

- 
- 

#### 4. 

- 
- 

#### 5. 
- 
-
-

[맨위로](#mugit)

## 4. 프로젝트 설계

### 시스템 아키텍쳐



### 기술 스택

<div align=center><h1>📚 STACKS</h1></div>

<div align=center> 
    <h2>Languages</h2>
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> 
    <h2>MarkUp</h2>  
  <img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white"> 
  <img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white"> 
  <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black">
    <h2>Databases</h2>  
  <img src="https://img.shields.io/badge/redis-E34F26?style=for-the-badge&logo=redis&logoColor=white"> 
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> 
    <h2>Frameworks</h2>  
  <img src="https://img.shields.io/badge/react-61DAFB?style=for-the-badge&logo=react&logoColor=black">
  <img src="https://img.shields.io/badge/next.js-000000?style=for-the-badge&logo=Next.js&logoColor=white">

  <br>
  
  <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> 
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> 
    <h2>Infrastructure</h2>  
  <img src="https://img.shields.io/badge/linux-FCC624?style=for-the-badge&logo=linux&logoColor=black">
  <img src="https://img.shields.io/badge/amazonaws-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white"> 
  <img src="https://img.shields.io/badge/jenkins-000000?style=for-the-badge&logo=jenkins&logoColor=white"> 
  <img src="https://img.shields.io/badge/docker-3776AB?style=for-the-badge&logo=docker&logoColor=white"> 
  <img src="https://img.shields.io/badge/kubernetes-3776AB?style=for-the-badge&logo=kubernetes&logoColor=white">
    <h2>Utility</h2>  
  <img src="https://img.shields.io/badge/swagger-6DB33F?style=for-the-badge&logo=swagger&logoColor=white">
    <h2>SCM</h2>  
  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">

</div>

### 역할 분담

| 성명         | 주 역할 | 기능                                                                                             | 주요 서버 |
| ------------ | ------- | ------------------------------------------------------------------------------------------------ | --------- |
| 배성준(팀장) | BE      | 인프라 구축, CI / CD 관리, Flow 관련 API | 메인 서버   |
| 남상엽       | BE      | User 관련 API, 인증 및 보안, SSE 통신을 이용한 알림                       | 메인 서버, SSE 서버 |
| 이준학       | BE      | Record 관련 API, 음악/사진 파일 관리, 파일 정리 자동화                             | 메인 서버, 파일 서버 |
| 이연우       | FE      |                                | 웹 서버   |
| 김승희       | FE      |                         | 추천 서버 |
| 김도희       | FE      |               | 채팅 서버 |

[맨위로](#mugit)

## 5. 구현 화면


[맨위로](#mugit)

## 6. 기능 명세서



[맨위로](#mugit)

## 7. API 명세서

### 서버

#### [해당 경로](https://mugit.site/swagger)에서 확인이 가능합니다.

![swagger](./assets/img/swagger.jpg)

### 로컬

해당 명세서를 다운로드 후, 크롬으로 실행하여 확인합니다.

> 로컬에서는 API요청 전송 및 응답이 불가능합니다.



[맨위로](#mugit)


## 8. 프로젝트 관리

### 컨벤션

#### 코드 컨벤션

##### 프론트엔드

###### [프론트엔드 코드 컨벤션](https://ui.toast.com/fe-guide/ko_CODING-CONVENTION)

##### 백엔드

###### 버전관리

- springboot : 3.2.3
- jdk : oracle jdk 17.0.10
- mysql : 8.3.0

###### 1) 변수명

- 상식적인 선에서 변수명 정하기

###### 2) 반복문

- 가급적 for문 사용(람다식 지양)

###### 3) 조건문

- if / else 문 사용(swich - case 지양)
- 예외처리는 if문 위에서 처리 후 로직 수행

###### 4) 유효성 검증

- DTO 유효성 검증 : validated
- Entity 유효성 검증 : DB 제약조건(Constrains)
- Optional은 Repository에서만 사용

###### 5) 공백

- 들여쓰기 4칸
- 이외 IDE(Intellij)가 제공하는 기능 사용

###### 6) 주석

- 컨트롤러 메서드에서만 javadocs 활용 API 명세
- 나머지 메서드에서는 기능 정도만 알아볼 수 있게 작성

###### 7) 테스트

- TDD 적용
- 기능테스트는 미수행

###### 8) API

- API 문서는 swagger 작성
- API 예외처리는 globalExceptionHandler로 수행

###### 9) DTO

- dto 이름 : request/response 나누고 Dto 붙이기
- List만 보내는 dto 별도로 생성
- Message만 보내는 dto 별도 생성

###### 10) 코드리뷰

- MR 단위로 코드리뷰 진행
- 정말 급하거나 중요한 경우를 제외하고는 MR 전에 코드리뷰
- 예외상황은 상호 동의 하에 MR 이후 코드리뷰

#### 커밋 컨벤션

##### :fire: 커밋 시 가급적 Body를 작성한다.

- feat: 새로운 기능 추가

```
git commit -m "feat : 로그인 기능 추가
# 로그인 기능 구현 완료
- 이제 사용자는 로그인을 할 수 있다.
"
```

- fix: 버그 수정

```
git commit -m "fix : 로그인 오류 수정
# 로그인 오류 수정
- 로그인 로직 중 00오류 확인 후 수정
- 이제 사용자는 정상적으로 로그인을 할 수 있다.
"
```

- docs: 문서 변경

```
git commit -m "docs : README.md 파일 수정"
```

- style: 코드 스타일 변경(공백, 포맷팅 등)

```
git commit -m "style : 코드 스타일 일부 변경
- 코드 가독성을 위해 컨벤션에 맞춰 코드 수정"
```

- refactor: 코드 리팩토링

```
git commit -m "refactor : 컨트롤러 코드 리팩토링
# 컨트롤러 코드 리펙토링하여 코드 가독성 증가"
```

- test: 테스트 코드 작성 또는 수정

```
git commit -m "test : 컨트롤러 테스트코드 추가
# 컨트롤러 기능테스트 추가
- 기능테스트 작성
- 기능테스트 00건 중 00건 통과 / 00건 수정중
"
```

- chore: 기타 작업(빌드 설정, 패키지 업데이트 등)

```
git commit -m "chore : 서버 포트 변경"
```

#### 브랜치 컨벤션

![git-graph](/assets/img/git-graph.png)

##### 브랜치 구분

- **master** : 제품의 버전 변경시 작업하기 위한 브랜치
- **hotfix** : 긴급한 오류 발생 시 작업하는 브랜치
- **develop** : 하위 아키텍쳐를 연결하는 브랜치
- **web-server** : 프론트엔드 웹서버
- **user-server** : 유저 비즈니스 로직을 수행하는 WAS(Web Application Server)
- **auth-server** : 인증 및 토큰 발행을 담당하는 WAS
- **chat-server** : 채팅과 관련된 비즈니스 로직을 수행하는 WAS
- **bigdata-server** : 빅데이터 추천 기능을 제공하는 WAS
- **statistics-server** : 통계처리를 하는 서버로 차후 해당 서버에서 처리 및 저장한 데이터를 기반으로 빅데이터 추천 서버가 작동함
- **api-gateway** : 다른 WAS(Web Application Server)로 API 요청을 전송하는 Gateway병합
- **file-server** : 정적 파일(이미지, 동영상 등)을 업로드 및 관리하기 위한 서버
- **alarm-server** : 친구요청, 채팅 등 알람 전송을 위한 서버
- **swagger-server** : API기능을 테스트하기 위한 서버

##### 브랜치 작업 순서

###### 1. master → develop branch 분기

→ 깃 담당자가 직접 분기

###### 2. develop → webserver, appserver, … 등 서버(포트)별 branch 분기

→ 깃 담당자가 직접 분기

###### 3. 서버별 branch → feature(기능)별 branch 분기 : Jira Issue 연동

→ 기능 담당하는 개인이 직접 분기

###### 4. 서버별 branch에서 각자 역할 나눠서 작업 및 MR

→ 기능 담당하는 인원들끼리 MR 수행 및 충돌 해결

###### 5. 작업 후 서버별 branch에서 develop으로 MR

→ 깃 담당자가 확인

###### 6. MR 시 충돌여부 확인 후 병합, CI / CD 확인

→ 깃 담당자 & 인프라 담당자가 확인

#### MR 컨벤션

MR 시 다음 양식에 맞춰 문서를 작성한다.

```
# MR이 필요한 이유
- MR이 수행되어야 하는 이유, 수행 후 변경사항 등을 작성한다.
ex)
 - 사용자 회원가입 로직 구현
 - 이제 사용자는 회원가입을 할 수 있다.

# 구현
- 다른 MR 참여자가 쉽게 이해할 수 있는 구현 내용을 추가한다.
ex) 웹페이지 화면 캡쳐, 기능 동작 동영상 첨부 등

# 주요 코드
- 해당 MR에서 변경되거나 핵심 로직 등을 코드로 첨부한다.
ex)
    // (생략)
    // 정적 참조 변수
    private static Singleton singletonObject;

    // 객체 생성자(private)
    private Singleton() {
    }

    // 객체 획득 메서드(public)
    public static Singleton getInstance() {
        if (singletonObject == null) {
            singletonObject = new Singleton();
        }

        return singletonObject;
    }
    // (생략)
```

[맨위로](#mugit)
