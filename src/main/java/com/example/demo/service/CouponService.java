package com.example.demo.service;

import java.util.List;

import com.example.demo.model.CouponVO;

public interface CouponService {
	CouponVO issueCoupon(String custId);
	boolean createCoupon(String amount);
	List<CouponVO> getIssuedCoupon();
	boolean useCoupon(String couponId);
	boolean cancelCoupon(String couponId);
	List<CouponVO> getExpiredCoupon();
	void sendMsg();
	
	
}
