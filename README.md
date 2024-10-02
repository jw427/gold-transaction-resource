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
6. [⚡ 트러블 슈팅](#트러블-슈팅)
7. [🤔 고민 흔적](#고민-흔적)

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

## ⚡ 트러블 슈팅 <a id="트러블-슈팅"></a>
<details>
<summary>⚡ 빌드 시 Java 버전이 다른 문제</summary>
<div markdown="1">
<ul>
<div>

gRPC 사용을 위해서는 `.proto` 파일을 컴파일해야한다. 그런데 빌드 시도를 하니 오류가 발생했다. 현재 프로젝트를 위해서 Java와 gradle 모두 17 버전을 사용하고 있고 설정도 그에 맞게 해줬지만 빌드를 시도하면 계속 자바 11 버전을 가져와 충돌하는 문제였다.</br>
그래서 root 경로에 `gradle.properties` 파일을 생성해 자바 설치 경로를 아래와 같이 명시해줬다. </br>
`org.gradle.java.home=C:/corretto-17.0.12`
</div>
</ul>
</div>
</details>
<details>
<summary>⚡ JPA <code>@ColumnDefault</code>에 대한 오해</summary>
<div markdown="1">
<ul>
<div>

주문이나 결제, 배송 객체 생성 시 주문 타입, 주문 상태 등의 초기값을 자동으로 넣고 싶었다. </br>
그래서 `@ColumnDefault` 어노테이션으로 필드의 초기값을 아래와 같이 설정해줬다. </br>
```java
@ColumnDefault("'PENDING'")
private DeliveryStatus deliveryStatus; 
```
이렇게 지정해주면 객체가 생성될 때 해당 필드의 값이 자동으로 어노테이션 안의 값으로 바뀌는 줄 알았다. (예를 들면 `deliveryStatus`의 값은 `PENDING`이라는 값이 자동으로 생성) </br>
그러나 생성 요청을 보낸 결과 해당 필드의 값은 `null` 값이었다. </br>
왜 설정해준 값이 자동으로 들어가지 않을까 궁금해서 찾아본 결과, `@ColumnDefault`에 대해서 완전히 오해하고 있었다. </br>
`@ColumnDefault`는 테이블을 처음 생성할 때, default값을 생성해 주는 역할을 하는 어노테이션이었다. 따라서 해당 어노테이션이 붙은 컬럼에 다른 값을 넣으려고 하면 오류가 생기는 것이다. </br>
객체 생성 시 엔티티 상단에 `@Builder`를 사용했기 때문에 값을 지정하지 않은 필드는 `null` 값이 들어가게 되므로, `@ColumnDefault`로 설정한 값이 들어가지 않게 된 것이다. </br>
이를 해결하기 위해 명시적으로 해당 필드에 기본값을 넣어줬다.
</div>
</ul>
</div>
</details>
</br>

## 🤔 고민 흔적 <a id="고민-흔적"></a>
<details>
<summary>💭 ERD는 어떻게 설계하는 것이 좋을까?</summary>
<div markdown="1">
<ul>
<div>

요구사항을 분석하며 모델링을 진행하면서 어떻게 주문 로직을 짜는 것이 좋을지 고민이 됐다. </br>
해당 서비스는 서비스 주인이 회원에게 금 구매와 판매 주문을 제공하는 서비스로, 구매 주문과 판매 주문의 주문 과정이 상이하기 때문에 배송과 주문 테이블을 구매 주문과 판매 주문 따로 나누는 것이 좋을지 고민했다. </br>
그러나 주문의 세부 로직은 다르지만 주문 → 결제 → 배송의 과정을 거치는 것은 동일했기 때문에 테이블을 같이 쓰는 것이 효율적이라고 생각했다. </br>
따라서 구매 주문과 판매 주문 둘 다 같은 주문, 결제, 배송 테이블을 쓰는 대신 주문 타입을 식별하기 위해 각각 식별할 수 있는 `Enum` 클래스를 사용하기로 했다. </br>
(주문의 경우 주문 타입, 결제의 경우 결제 상태, 배송의 경우 배송 상태) </br>
그리고 결제 상태와 배송 상태의 경우 서비스 관리자가 변경할 수 있도록 하는 것이 보안성 측면에서 좋다고 생각해 관리자만 변경할 수 있게 설정하기로 했다.
</div>
</ul>
</div>
</details>
<details>
<summary>💭 Grpc Server와 Grpc Client를 각각 어떤 서버로 설정할까?</summary>
<div markdown="1">
<ul>
<div>

gRPC는 구글이 최초로 개발한 오픈 소스 원격 프로시저 호출 시스템이다. gRPC를 사용하면 서로 다른 위치에 존재하는 공간에서 동일한 객체를 가져다 와서 사용할 수 있다. </br>
이 gRPC를 사용해 구매, 판매 주문을 담당하는 서버와 인증만을 담당하는 서버를 소통하게 하려고 한다. 이때 Grpc Server와 Grpc Client를 각각 어떤 서버로 설정해야할지 고민이 됐다. </br>
요구사항을 다시 읽어보니 ***유저의 권한 확인이 필요한 경우, grpc를 통해 인증을 담당하는 서버 B에 JWT 토큰을 보내어 인증 여부를 확인해야 합니다.*** 라는 부분이 있었다. </br>
구매, 판매 주문 서버에서의 모든 요청은 권한 확인이 필요하다. 다시 말해 구매, 판매 주문 서버에서 인증 서버로 권한 요청을 하는 과정이 필요하다. </br>
따라서 구매, 판매 주문 서버를 Grpc Client로, 인증 서버를 Grpc Server로 설정하고 구매, 판매 주문 서버에서 모든 요청은 인증 서버에 요청을 해 권한 인증 후 진행하기로 했다.
</div>
</ul>
</div>
</details>
