# 💰 금 거래 서비스
## 💬 프로젝트 소개
금을 한창 열심히 팔고 있던 알레테이아는, 금을 판매하고 구매하는 서비스를 제공하기로 결정했습니다! </br>
알레테이아는 앱을 통해 구매, 판매 주문을 관리하려고 합니다! </br>
또한 미래에 서비스가 확장될 것을 고려하여, 인증을 담당하는 서버를 별도로 구축하기로 결정합니다.
</br>

### 👉 [인증 서버](https://github.com/jw427/gold-transaction-auth)
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
4. [💌 API 명세](#api-명세)

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

## 💌 API 명세 <a id="api-명세"></a>
### 👉 [자원 서버 API 명세](https://documenter.getpostman.com/view/29531239/2sAXqmAQkQ)
### 👉 [인증 서버 API 명세](https://documenter.getpostman.com/view/29531239/2sAXqmA5Lp)
</br>
