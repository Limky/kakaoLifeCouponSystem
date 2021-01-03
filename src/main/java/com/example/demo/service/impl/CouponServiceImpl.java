package com.example.demo.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.CouponDao;
import com.example.demo.model.CouponVO;
import com.example.demo.model.CustVO;
import com.example.demo.service.CouponService;

@Service
public class CouponServiceImpl implements CouponService {
	private final Logger logger = LoggerFactory.getLogger(CouponServiceImpl.class);


	@Autowired
	private CouponDao couponDao;
	
	//1. 랜덤한쿠폰을 N개 생성하여 DB에 저장하는 API
	@Override
	public boolean createCoupon(String amount) {
		
		try {
			
		String currentYmd = getCurrentYmd();
		
		ArrayList<Double> couponIdList = new ArrayList<>();
		ArrayList<CouponVO> couponList = new ArrayList<CouponVO>();
		
		couponIdList = createCouponId(amount);
		
		for(int i=0; i<couponIdList.size(); i++) {
			CouponVO couponVO = new CouponVO();
			System.out.println(Double.toString(couponIdList.get(i)).split("\\.")[1]);
			
			couponVO.setCOUPON_ID(Double.toString(couponIdList.get(i)).split("\\.")[1]);
			couponVO.setISSUE_YN("N"); //발급여부N
			couponVO.setCREATE_YMD(currentYmd); //쿠폰생성일자
			couponVO.setEND_YMD(plusYmd(10));//★★쿠폰만료일자
			
			couponVO.setREG_ENO("createCoupon");	
			couponVO.setTRT_ENO("createCoupon");
			
			couponList.add(couponVO);	
		}
		
		couponDao.insertCoupon(couponList);
		
		return true;
		
		} catch (Exception e) {
			return false;
		}
	}
	
	
	//2.생성된 쿠폰 중 하나를 사용자에게 지급하는 API 구현
	@Override
	public CouponVO issueCoupon(String custId) {
		CouponVO couponVO = couponDao.issueCoupon();
		
		//발급할 쿠폰이 있고, 만료일자 안지난
		if(couponVO != null && couponVO.getEND_YMD().compareTo(getCurrentYmd()) >= 0) {
			//쿠폰 발급이 가능
			//1.발급여부 Y처리
			couponVO.setCUST_ID(custId);
			couponVO.setISSUE_YN("Y");
			couponVO.setTRT_ENO("getcoupon");
			
			couponDao.updateCouponIssueYn(couponVO);
			
			//2.쿠폰값 리턴
			return couponVO;		
		}

		//발급할 쿠폰이 없음
		return null;
	
	}

	//3. 사용자에게 지급된 쿠폰번호를 조회하는 api
	@Override
	public List<CouponVO> getIssuedCoupon() {
		CouponVO couponVO = new CouponVO();
		couponVO.setISSUE_YN("Y");
		List<CouponVO> list = couponDao.getIssuedCoupon(couponVO);
		
		if(list != null)
		{
			for(int i=0; i<list.size(); i++) {
				System.out.println(list.get(i).getCOUPON_ID());
			}
			return list;
		}
	
		return null;
	}

	
	//4. 사용자에게 지급된 쿠폰번호 중 하나를 사용하는 api
	@Override
	public boolean useCoupon(String couponId) {
		CouponVO couponVO = new CouponVO();
		couponVO.setCOUPON_ID(couponId);
		//1.쿠폰번호로 단건 조회
		couponVO = couponDao.getCoupon(couponVO);
		
		//2.조회되고 && 발급되었고 && 만료일자 안지났고 && 사용하지 않았다면
		//사용일자 update
		if(couponVO != null && couponVO.getISSUE_YN().equals("Y") 
				&& couponVO.getEND_YMD().compareTo(getCurrentYmd()) >= 0
				&& couponVO.getUSE_YMD() == null) {
			couponVO.setUSE_YMD(getCurrentYmd());
			couponVO.setTRT_ENO("useCoupon");
			
			couponDao.updateCouponUse(couponVO);
			
			return true;
		}
		
		return false;
	}

	
	//5. 지급된 쿠폰 중 하나를 취소하는 api
	@Override
	public boolean cancelCoupon(String couponId) {
		CouponVO couponVO = new CouponVO();
		couponVO.setCOUPON_ID(couponId);
		//1.쿠폰번호로 단건 조회
		couponVO = couponDao.getCoupon(couponVO);
		logger.info("조회");
		
		//2.조회되고 && 사용하지 말아야함
		//발급일자, 발행여부, custid update
		//만료일자 지났다면, 취소한날기준으로 재세팅 / 만료일자 안지났으면 그냥 둠
		if(couponVO != null && couponVO.getUSE_YMD() == null) {
			
			logger.info("조회되고 && 사용하지 말아야함");
			couponVO.setISSUE_YN("N");
			couponVO.setCUST_ID("");
			
			//만료일자 지났고
			if(!(couponVO.getEND_YMD().compareTo(getCurrentYmd()) >= 0)) {
			logger.info("만료일자 지났고");
				
				couponVO.setCREATE_YMD(getCurrentYmd()); //쿠폰생성일자 = 취소일자로 세팅
				couponVO.setEND_YMD(plusYmd(10));//★★쿠폰만료일자
				
			}
			//couponVO.setREG_ENO("cancelCoupon");	
			couponVO.setTRT_ENO("cancelCoupon");
			
			couponDao.updateCouponCancel(couponVO);
			
			return true;
		}
		
	
		return false;
	}
	
	//6. 발급된 쿠폰 중 당일 만료된 전체 쿠폰 목록을 조회하는 api
	@Override
	public List<CouponVO> getExpiredCoupon() {
		CouponVO couponVO = new CouponVO();
		couponVO.setEND_YMD(getCurrentYmd()); //조회 당일기준으로 만료일자 세팅
		//couponVO.setEND_YMD("20210113"); //조회 당일기준으로 만료일자 세팅
		
		return couponDao.getExpiredCoupon(couponVO);
	}


	@Override
	public void sendMsg() {
		CouponVO couponVO = new CouponVO();
		//요청한 당일 만료일자-3일자가 같으면 해당 고객전화번호로 메시지 발송
		couponVO.setEND_YMD(plusYmd(3));
		List<CustVO> list = couponDao.getCustInfoBefore3day(couponVO);
		
		if(list != null) {
			for(int i=0; i<list.size(); i++) {
				logger.info("대상고객 : "+list.get(i).getCUST_ID()+
						"("+list.get(i).getPHONE_NUMBER()+")"+"※※쿠폰이 3일 후 만료됩니다.※※");
			}
		}else {
			logger.info("대상고객이 없습니다.");
		}
		
	}


	
	//--------------------------------------------------------
		//현재날짜구하기
		private String getCurrentYmd() {
			SimpleDateFormat format = new SimpleDateFormat ("YYYYMMDD");
			Date time = new Date();
			
			return format.format(time);
		}
		
		//날짜,더하기
		private String plusYmd(int day){
			SimpleDateFormat format = new SimpleDateFormat ("YYYYMMDD");
			Date date = new Date();
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, day);
			
			return format.format(cal.getTime());
	
		}

		//쿠폰id 난수 생성
		public ArrayList<Double> createCouponId(String amount){
			
			int n = Integer.parseInt(amount);
			ArrayList<Double> couponIdList = new ArrayList<>();
			Random rn = new Random();
		    //시드를 설정하지 않을경우
		    //현재프로그램을 실행해보면 매번 다른 값이 아닌 같은 난수가 표시됨을 알 수 있음-->가짜난수!! 
		    System.out.println(n);
		    //시드를 설정
		    rn.setSeed(System.currentTimeMillis());
		    for(int i = 0 ; i < n ; i++) {
		      double a = rn.nextDouble(); // 0.0~1.0사이의 임의의 실수값을 반환
		      System.out.print("실수형 난수 : " + a);
		      couponIdList.add(a); //0~49의 임의의 정수가 반환     
		      }
	    
		    return couponIdList;

		}



	
}