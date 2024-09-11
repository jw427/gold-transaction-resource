# 💰 금 거래 서비스
## 💬 프로젝트 소개
금을 한창 열심히 팔고 있던 알레테이아는, 금을 판매하고 구매하는 서비스를 제공하기로 결정했습니다! </br>
알레테이아는 앱을 통해 구매, 판매 주문을 관리하려고 합니다! </br>
또한 미래에 서비스가 확장될 것을 고려하여, 인증을 담당하는 서버를 별도로 구축하기로 결정합니다.
</br>

## 💡 요구사항
1. RESTful API를 활용하여 구매, 판매 주문 CRUD를 수행하는 서버 A 구현
2. 서버 A와 gRPC를 통해 소통하며, 인증만을 담당하는 서버 B 구현

### 👉 [인증 서버 Repository](https://github.com/jw427/gold-transaction-auth)
</br>

## 🛠️ 기술 스택
<div align=center>
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
  <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/springdatajpa-13C100?style=for-the-badge&logo=spring&logoColor=white">
  <img src="https://img.shields.io/badge/springsecurity-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">
  <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
  <img src="https://img.shields.io/badge/mariaDB-003545?style=for-the-badge&logo=mariaDB&logoColor=white">
  <img src="https://img.shields.io/badge/gRPC-4285F4?style=for-the-badge&logo=google&logoColor=white">
</div>
</br>

## 🏷️ 목차
1. [🏃‍♀️ Quick Start](#quick-start)
2. [📦 ERD](#erd)
3. [📁 디렉터리 구조](#디렉터리-구조)
4. [📑 구현 내용](#구현-내용)
5. [💌 API 명세](#api-명세)

</br>

## 🏃‍♀️ Quick Start <a id="quick-start"></a>
1. .env 파일
- 자원 서버
```
DB_URL=
DB_USERNAME=
DB_PASSWORD=
```
- 인증 서버
```
DB_URL=
DB_USERNAME=
DB_PASSWORD=

JWT_SECRET_KEY="Key 설정"
ACCESS_TOKEN_EXPIRATION=액세스 토큰 만료 시간
REFRESH_TOKEN_EXPIRATION=리프레시 토큰 만료 시간
```
2. gradle build
3. 실행
<details>
  <summary>✋ Java 버전 충돌로 오류가 발생하는 경우</summary>
  <div markdown="1">
    <ul>
      <div>현재 프로젝트는 Java 17 버전을 사용중입니다.</div>
      <div>따라서 버전이 다른 경우 17 버전 jdk 파일을 다운로드 후</div>
      <div>root 경로에 <code>gradle.properties</code> 파일을 생성해 아래와 같이 다운로드 받은 파일의 경로를 설정해주세요.</div>
      <div><code>org.gradle.java.home=C:/corretto-17.0.12</code></div>
    </ul>
  </div>
</details>
</br>

## 📦 ERD <a id="erd"></a>
#### ☝️ User 테이블과 Order 테이블이 1:N 관계로 연결되었지만 실제로는 서버를 분리했기 때문에 연결되어있지 않다.
![Untitled](https://github.com/user-attachments/assets/02381a86-c5b9-4c33-9015-5c6ce39bbf07)

</br>

## 📁 디렉터리 구조 <a id="디렉터리-구조"></a>
<details>
<summary>자원 서버 디렉터리 구조</summary>
<div markdown="1">

```
src
├─main
│  ├─java
│  │  └─com
│  │      └─wanted
│  │          └─gold
│  │              │  GoldApplication.java
│  │              │
│  │              ├─client
│  │              │  │  AuthGrpcClient.java
│  │              │  │
│  │              │  └─dto
│  │              │          UserResponseDto.java
│  │              │
│  │              ├─config
│  │              │      SwaggerConfig.java
│  │              │
│  │              ├─exception
│  │              │  │  BadRequestException.java
│  │              │  │  BaseException.java
│  │              │  │  ConflictException.java
│  │              │  │  ErrorCode.java
│  │              │  │  ErrorResponse.java
│  │              │  │  ForbiddenException.java
│  │              │  │  NotFoundException.java
│  │              │  │  UnauthorizedException.java
│  │              │  │
│  │              │  └─handler
│  │              │          GlobalExceptionHandler.java
│  │              │
│  │              ├─order
│  │              │  ├─controller
│  │              │  │      DeliveryController.java
│  │              │  │      OrderController.java
│  │              │  │      PaymentController.java
│  │              │  │
│  │              │  ├─domain
│  │              │  │      Delivery.java
│  │              │  │      DeliveryStatus.java
│  │              │  │      Order.java
│  │              │  │      OrderStatus.java
│  │              │  │      OrderType.java
│  │              │  │      Payment.java
│  │              │  │      PaymentStatus.java
│  │              │  │
│  │              │  ├─dto
│  │              │  │      CreateOrderRequestDto.java
│  │              │  │      DeliveryResponseDto.java
│  │              │  │      ModifyDeliveryRequestDto.java
│  │              │  │      ModifyPaymentRequestDto.java
│  │              │  │      OrderDetailResponseDto.java
│  │              │  │      OrderListPaginationResponseDto.java
│  │              │  │      OrderListResponseDto.java
│  │              │  │      PaymentResponseDto.java
│  │              │  │
│  │              │  ├─repository
│  │              │  │      DeliveryRepository.java
│  │              │  │      OrderRepository.java
│  │              │  │      PaymentRepository.java
│  │              │  │
│  │              │  └─service
│  │              │          DeliveryService.java
│  │              │          OrderService.java
│  │              │          OrderValidator.java
│  │              │          PaymentService.java
│  │              │
│  │              └─product
│  │                  ├─controller
│  │                  │      PriceController.java
│  │                  │
│  │                  ├─domain
│  │                  │      GoldType.java
│  │                  │      Price.java
│  │                  │      PriceType.java
│  │                  │      Product.java
│  │                  │
│  │                  ├─dto
│  │                  │      CreatePriceRequestDto.java
│  │                  │
│  │                  ├─repository
│  │                  │      PriceRepository.java
│  │                  │      ProductRepository.java
│  │                  │
│  │                  └─service
│  │                          PriceService.java
│  │
│  ├─proto
│  │      auth.proto
│  │
│  └─resources
│          application.yml
│
└─test
    └─java
        └─com
            └─wanted
                └─gold
                        GoldApplicationTests.java
```

</div>
</details>
<details>
<summary>인증 서버 디렉터리 구조</summary>
<div markdown="1">

```
src
├─main
│  ├─java
│  │  └─com
│  │      └─wanted
│  │          └─gold
│  │              │  GoldApplication.java
│  │              │
│  │              ├─config
│  │              │      SwaggerConfig.java
│  │              │
│  │              ├─exception
│  │              │  │  BadRequestException.java
│  │              │  │  BaseException.java
│  │              │  │  ConflictException.java
│  │              │  │  ErrorCode.java
│  │              │  │  ErrorResponse.java
│  │              │  │  NotFoundException.java
│  │              │  │  UnauthorizedException.java
│  │              │  │
│  │              │  └─handler
│  │              │          GlobalExceptionHandler.java
│  │              │
│  │              ├─server
│  │              │      AuthServer.java
│  │              │
│  │              └─user
│  │                  ├─config
│  │                  │      SecurityConfig.java
│  │                  │      TokenAuthenticationFilter.java
│  │                  │      TokenProvider.java
│  │                  │
│  │                  ├─controller
│  │                  │      TokenController.java
│  │                  │      UserController.java
│  │                  │
│  │                  ├─domain
│  │                  │      Role.java
│  │                  │      Token.java
│  │                  │      User.java
│  │                  │      UserDetail.java
│  │                  │
│  │                  ├─dto
│  │                  │      SignUpRequestDto.java
│  │                  │      SignUpResponseDto.java
│  │                  │      TokenRequestDto.java
│  │                  │      TokenResponseDto.java
│  │                  │      UserLoginRequestDto.java
│  │                  │      UserLoginResponseDto.java
│  │                  │
│  │                  ├─repository
│  │                  │      TokenRepository.java
│  │                  │      UserRepository.java
│  │                  │
│  │                  └─service
│  │                          AuthServiceGrpcImpl.java
│  │                          TokenService.java
│  │                          UserDetailService.java
│  │                          UserService.java
│  │                          UserValidator.java
│  │
│  ├─proto
│  │      auth.proto
│  │
│  └─resources
│          application.yml
│
└─test
    └─java
        └─com
            └─wanted
                └─gold
                        GoldApplicationTests.java
```

</div>
</details>

</br>

## 📑 구현 내용 <a id="구현-내용"></a>
### 회원가입
- 계정명과 비밀번호 유효성 검사
- 중복 계정명 방지 기능

### 로그인
- 로그인 시 액세스 토큰과 리프레시 토큰 발급
- 모든 주문 서비스에 로그인 한 회원만 접근 가능한 기능

### 액세스 토큰 재발급
- 액세스 토큰 만료 시 리프레시 토큰이 유효할 경우 액세스 토큰 재발급

### 주문 생성
- 주문 생성에 필요한 정보들 입력 시 주문 생성
- 주문하는 수량 입력 시 소수 둘째자리까지만 허용하기 위한 유효성 검사
- 재고의 효율적인 관리를 위해 구매 주문 시 주문 수량이 재고보다 많을 경우 주문 불가 및 주문이 가능한 경우 상품 재고 차감

### 주문 목록 조회
- 본인의 주문 목록만 조회 가능
- 관리자의 경우 모든 주문 목록 조회 가능
- 조회 시 데이터를 페이지네이션으로 응답

### 주문 상세 조회
- 주문 상세 조회 시 주문 정보 뿐만 아니라 주문에 해당하는 배송 정보와 결제 정보도 함께 응답
- 일반 회원의 경우 다른 회원의 주문 정보 조회 불가

### 주문 삭제
- 본인의 주문만 삭제 가능
- 결제가 진행된 경우 삭제 불가

### 가격 등록
- 관리자만 접근 가능
- 각 품목 별로 주문 유형별로 가격 등록 가능

### 결제 정보 수정
- 본인의 결제 정보만 수정 가능
- 회원에게서 정보 입력받아 수정하는 기능
- 수정할 정보를 하나도 입력하지 않을 경우 수정 불가
- 이미 결제가 완료된 상태에서는 수정 불가

### 배송 정보 수정
- 본인의 배송 정보만 수정 가능
- 회원에게서 정보 입력받아 수정하는 기능
- 수정할 정보를 하나도 입력하지 않을 경우 수정 불가
- 이미 배송이 완료도니 상태에서는 수정 불가

### 결제 상태 수정
- 관리자만 접근 가능
- 결제가 완료되면 관리자가 확인 후 결제 상태를 완료로 업데이트하는 기능

### 배송 상태 수정
- 관리자만 접근 가능
- 배송이 완료되면 관리자가 확인 후 배송 상태를 완료로 업데이트하는 기능

### 💡 자원 서버와 인증 서버 gRPC 연결
- 자원 서버의 RESTful API 포트는 9999번, grpc 포트는 50052번
- 인증 서버의 RESTful API 포트는 8888번, grpc 포트는 50051번
- 두 서버를 각각 분리해 gRPC 연결 후 자원 서버에서 회원 인증이 필요할 시 인증 서버에 인증 요청을 보내는 기능

</br>

## 💌 API 명세 <a id="api-명세"></a>
### 👉 [자원 서버 API 명세](https://documenter.getpostman.com/view/29531239/2sAXqmAQkQ)
### 👉 [인증 서버 API 명세](https://documenter.getpostman.com/view/29531239/2sAXqmA5Lp)
<details>
<summary>
  
#### 🔗 Swagger API 문서
  
</summary>
<div markdown="1">
<ul>
<div>
  
#### 서버 실행 후 접속 가능합니다.
  
</div>
<div>
  
  #### 👉 [자원 서버 Swagger API 문서](http://localhost:9999/swagger-ui/index.html#)
  
</div>
<div>
  
  #### 👉 [인증 서버 Swagger API 문서](http://localhost:8888/swagger-ui/index.html#)
  
</div>
</ul>
</div>
</details>
</br>
