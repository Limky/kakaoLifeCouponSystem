package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.CouponVO;
import com.example.demo.service.CouponService;

@CrossOrigin
@RestController
@RequestMapping("/coupon")
public class CouponController {
	
	@Autowired
	CouponService couponService;

	//1.랜덤한쿠폰 N개를 생성하여 DB에 저장하는 API
		//input : amount
		//output : 
		@RequestMapping("/createcoupon")
		public boolean createCoupon(HttpServletRequest request) {	
			String amount = request.getParameter("amount");
			System.out.println(amount);
			return couponService.createCoupon(amount);
		}
	
	
	//2.생성된 쿠폰 중 하나를 사용자에게 지급하는 API
	//input : 
	//outur : couponId(쿠폰id)
	@RequestMapping("/issuecoupon")
	public CouponVO issueCoupon(HttpServletRequest request) {	
		String custId = request.getParameter("custid");
		CouponVO couponVO = couponService.issueCoupon(custId);
		if(couponVO == null) {
			return null;
		}
		
		return couponVO;
	}
	
	//3. 사용자에게 지급된 쿠폰번호를 조회하는 api
	@RequestMapping("/getissuedcoupon")
	public List<CouponVO> getIssueCoupon(HttpServletRequest request) {	
	
		List<CouponVO> couponList= couponService.getIssuedCoupon();
		if(couponList == null) {
			return null;
		}
		
		return couponList;
	}
	
	//4. 사용자에게 지급된 쿠폰번호 중 하나를 사용하는 api
	@RequestMapping("/usecoupon")
	public boolean useCoupon(HttpServletRequest request) {	
		String couponId = request.getParameter("couponid");
		return couponService.useCoupon(couponId);
	}
	
	
	//5. 지급된 쿠폰 중 하나를 취소하는 api
	@RequestMapping("/cancelcoupon")
	public boolean cancelCoupon(HttpServletRequest request) {	
		String couponId = request.getParameter("couponid");
		return couponService.cancelCoupon(couponId);
	}
				
	//6. 발급된 쿠폰 중 당일 만료된 전체 쿠폰 목록을 조회하는 api
	@RequestMapping("/getexpiredcoupon")
	public List<CouponVO> getExpiredCoupon(HttpServletRequest request) {	
		return couponService.getExpiredCoupon();
	}

	
	//7. 발급된 쿠폰 중 만료 3일전 사용자에게 메세지를 발송하는 api
	//메시지 발송은 고객 데이터와 쿠폰이 매칭되어야하는데 쿠폰 지급시에 고객데이터를 input으로 넘겨준다는 요건이 없음
	//따라서, 고객 관련 테이블까지는 따로 구현하거나 해당 프로세스에 대해서는 고려하지 않았음
	//위 사항을 고려하여 발급된 쿠폰 중 만료 3일전 쿠폰리스트를 리턴하는 것으로 해당 api를 대체함
	@RequestMapping("/sendmsg")
	public void sendmsg(HttpServletRequest request) {	
		couponService.sendMsg();

	}
	
}

