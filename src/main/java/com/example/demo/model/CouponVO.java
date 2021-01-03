package com.example.demo.model;


import lombok.Data;

@Data
public class CouponVO {
	
	private String COUPON_ID;
	private String CUST_ID;
	private String ISSUE_YN;
	private String USE_YMD;
	private String CREATE_YMD;
	private String END_YMD;
	private String REG_ENO;
	private String REG_DATE;
	private String TRT_ENO;
	private String TRT_DATE;
	
}
