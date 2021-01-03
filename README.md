# kakaoLifeCouponSystem
쿠폰API 시스템

# 개발 프레임 워크
1. Spring boot 2.4.1
2. java 1.8
3. Oracle 12
4. lombok, mybatis, log4j, log4jdbc 등..

# 문제해결 전략
#### 1. 쿠폰ID 생성 
* 쿠폰ID 최초 생성부터, 현재일시를 변수로 주어 중복이 발생하지 않는 랜덤 수 발생    
* 중복된 쿠폰ID 발생시 DB조회해서 중복 체크 여부를 따져야 하기 때문에 resource 비효율

#### 2. 쿠폰테이블 구성  
   |쿠폰ID|고객ID|발급여부|사용일자|쿠폰생성일자|쿠폰만료일자|등록자|등록일시|처리자|처리일시|
   |---|---|---|---|---|---|---|---|---|---|
              
    * 등록자,등록일시 최초 DB생성 주체자 정보 입력, 처리자,처리일시 데이터 처리자 정보 입력 (이력 추적 용이)  
    * 발급시엔 발급여부,고객ID UPDATE, 사용시엔 사용일자 UDATE  
    * 만료일자는 쿠폰생성일자 + 10일이 되도록 임의로 요건을 부여함.  
    * 고객안내메시지 기능을 위해 고객정보 테이블 별도 구현 (고객테이블 : 고객ID, 핸드폰정보, ...) 쿠폰테이블과 JOIN 처리

#### 3. 비즈니스 로직 구현 
* MVC 구조 철학에 맞게 serviceImpl 단에 비즈니스 로직을 구현 함.    
* 쿠폰생성, 만료일자 계산, 현재일자 구하기 등 로직은 별도 메소드로 구현하여 모듈화 구성   
* 쿠폰을 발급시, INPUT 데이터에 대한 요건이 없으나 쿠폰별 고객매핑관리를 위해, INPUT데이터에 고객정보 받는 것으로 구현   
 

# 빌드 및 실행 방법

1. Spring boot 내장 서버 run
2. 각 API 명세 순서대로(1 ~ 7) API 호출 (API 호출시마다 DB 조회를 통해 검증)
3. 기타 local 환경 db 연동을 위해 방화벽 해제, db 서비스 활성화, 포트 및 db 계정권한 체크 등..

* * *

#### 1. 랜덤하게 쿠폰을 N개 생성하여 DB에 저장하는 API
> API URL : http://localhost:8080/coupon/createcoupon
> > ex) : http://localhost:8080/coupon/createcoupon?amount=4
> > > input data : 쿠폰생성수량
> > > output data : boolean

#### 2. 생성된 쿠폰 중 하나를 사용자에게 지급하는 API
> API URL : http://localhost:8080/coupon/issuecoupon
> > ex) : http://localhost:8080/coupon/issuecoupon?custid=0001
> > > input data : 고객ID
> > > output data : boolean

#### 3. 사용자에게 지급된 쿠폰번호를 조회하는 API
> API URL : http://localhost:8080/coupon/getissuedcoupon
> > input data : N/A
> > output data : list

#### 4, 지급된 쿠폰 중 하나를 사용하는 API
> API URL : http://localhost:8080/coupon/usecoupon
> > ex) : http://localhost:8080/coupon/usecoupon?couponid=19481284825484035
> > > input data : 쿠폰ID
> > > output data : boolean

#### 5. 지급된 쿠폰 중 하나를 취소하는 API
> API URL : http://localhost:8080/coupon/cancelcoupon
> > ex) : http://localhost:8080/coupon/cancelcoupon?couponid=6329316255941411
> > > input data : 쿠폰ID
> > > output data : boolean

#### 6. 발급된 쿠폰 중 당일 만료된 전체 쿠폰목록을 조회하는 API
> API URL : http://localhost:8080/coupon/getexpiredcoupon
> > input data : N/A
> > output data : list

#### 7. 발급된 쿠폰 중 만료 3일전 사용자에게 메세지를 발송하는 기능 API
> API URL : http://localhost:8080/coupon/sendmsg
> > input data : N/A
> > output data : N/A


# 보완해야할 점(미비사항) 
현재 HTTP GET방식을 일괄적으로 사용하기 때문에 보안상 취약함, (POST방식으로 보완 개발 필요)       
JWT 구현 적용 필요함.  
대량건에 대한 일괄 처리에 대한 기술적인 검토 필요함.
