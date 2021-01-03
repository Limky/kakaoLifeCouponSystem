package com.example.demo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.model.CouponVO;
import com.example.demo.model.CustVO;


@Repository
public interface CouponDao {
	
	CouponVO issueCoupon();
	boolean insertCoupon(List<CouponVO> list);
	boolean updateCouponIssueYn(CouponVO couponVO);
	List<CouponVO> getIssuedCoupon(CouponVO couponVO);
	CouponVO getCoupon(CouponVO couponVO);
	boolean updateCouponUse(CouponVO couponVO);
	boolean updateCouponCancel(CouponVO couponVO);
	List<CouponVO> getExpiredCoupon(CouponVO couponVO);
	List<CustVO> getCustInfoBefore3day(CouponVO couponVO);
	
}
