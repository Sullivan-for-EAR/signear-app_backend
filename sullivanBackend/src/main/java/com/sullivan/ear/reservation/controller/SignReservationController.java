package com.sullivan.ear.reservation.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sullivan.ear.login.service.SignUserService;
import com.sullivan.ear.reservation.service.ReservationService;
import com.sullivan.ear.vo.Reservation;
import com.sullivan.ear.vo.SignUser;

@RestController
@RequestMapping("reservation/sign")
public class SignReservationController {

	@Autowired
	ReservationService reservationService;
	
	@Autowired
	SignUserService signUserService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public Reservation getSignReservationInfo(@RequestParam Integer reservation_id) {
		
		
		//예약 상태 한번이라도 조회로 업데이트
		Reservation reservationResult = reservationService.getOneByRsID(reservation_id);
		
		if (reservationResult.getStatus() == 1) {
			reservationResult.setStatus(2);
			reservationResult = reservationService.update(reservationResult);
		}
	
		return reservationResult;
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<Reservation> getSignReservationList(@RequestParam Integer sign_id) {
		
		SignUser singuser = signUserService.findOne(sign_id).get();
		String address = singuser.getAddress();
			
		List<Reservation> ReservationResult = reservationService.findAllSignReservation(address);

		return ReservationResult;
	}
	
	@RequestMapping(value = "/myList", method = RequestMethod.GET)
	public List<Reservation> getMySignReservationList(@RequestParam Integer sign_id) {
		
		List<Reservation> ReservationResult = reservationService.getListBySignID(sign_id);

		return ReservationResult;
	}
	
	@RequestMapping(value = "/confirm/{rsID}", method = RequestMethod.POST)
	public Reservation confirmReservation(@PathVariable("rsID") Integer reservation_id, @RequestParam Integer sign_id) {
		
		Reservation reservationMap = reservationService.findOne(reservation_id).get();
		reservationMap.setStatus(3); //3:예약확정
		
		SignUser singuser = signUserService.findOne(sign_id).get();			
		reservationMap.setSignUser(singuser);
		
		return reservationService.update(reservationMap);
		
	}
	
	@RequestMapping(value = "/reject/{rsID}", method = RequestMethod.POST)
	public Reservation cancleReservation(@PathVariable("rsID") Integer reservation_id, @RequestBody Map<String, Object> params) {
		
		String reject = params.get("reject").toString();
	    int sign_id = Integer.parseInt(params.get("sign_id").toString());
		
		Reservation reservationMap = reservationService.findOne(reservation_id).get();
		reservationMap.setStatus(5); //5:예약거절
		reservationMap.setReject(reject);
		
		SignUser singuser = signUserService.findOne(sign_id).get();			
		reservationMap.setSignUser(singuser);
		
		return reservationService.update(reservationMap);
		
	}
	
}
