# kakaoLifeCouponSystem
쿠폰API 시스템

# 개발 프레임 워크
  1.Spring boot 2.4.1
  2.java 1.8
  3.Oracle 12
  4.lombok, mybatis, log4j, log4jdbc 등..

# 문제해결 전략


# 빌드 및 실행 방법

1. Spring boot 내장 서버 run
2. 각 API 명세 순서대로 API 호출 (API 호출시마다 DB 조회를 통해 검증)
기타 : local 환경 db 연동을 위해 방화벽 해제, db 서비스 활성화, 포트 및 db 계정권한 체크 등..

-------API 명세------
1. 랜덤하게 쿠폰을 N개 생성하여 DB에 저장하는 API
API URL : http://localhost:8080/coupon/createcoupon
ex) : http://localhost:8080/coupon/createcoupon?amount=4
input data : 쿠폰생성수량
output data : boolean

2. 생성된 쿠폰 중 하나를 사용자에게 지급하는 API
API URL : http://localhost:8080/coupon/issuecoupon
ex) : http://localhost:8080/coupon/issuecoupon?custid=0001
input data : 고객ID
output data : boolean

3. 사용자에게 지급된 쿠폰번호를 조회하는 API
API URL : http://localhost:8080/coupon/getissuedcoupon
input data : N/A
output data : list

4, 지급된 쿠폰 중 하나를 사용하는 API
API URL : http://localhost:8080/coupon/usecoupon
ex) : http://localhost:8080/coupon/usecoupon?couponid=19481284825484035
input data : 쿠폰ID
output data : boolean

5. 지급된 쿠폰 중 하나를 취소하는 API
API URL : http://localhost:8080/coupon/cancelcoupon
ex) : http://localhost:8080/coupon/cancelcoupon?couponid=6329316255941411
input data : 쿠폰ID
output data : boolean

6. 발급된 쿠폰 중 당일 만료된 전체 쿠폰목록을 조회하는 API
API URL : http://localhost:8080/coupon/getexpiredcoupon
input data : N/A
output data : list

7. 발급된 쿠폰 중 만료 3일전 사용자에게 메세지를 발송하는 기능 API
API URL : http://localhost:8080/coupon/sendmsg
input data : N/A
output data : N/A

