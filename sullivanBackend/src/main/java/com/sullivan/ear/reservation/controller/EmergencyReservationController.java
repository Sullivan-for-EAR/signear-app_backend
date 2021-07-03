package com.sullivan.ear.reservation.controller;

import java.util.List;
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
@RequestMapping("reservation/emergency")
public class EmergencyReservationController {

	@Autowired
	ReservationService reservationService;
	
	@Autowired
	SignUserService signUserService;
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Reservation createReservation(@RequestBody Reservation Reservation) {
		
		Reservation.setStatus(8); //8: 긴급통역 연결중
		
		return reservationService.create(Reservation);
		
	}
	
	@RequestMapping(value = "/cancel/{rsID}", method = RequestMethod.POST)
	public Reservation cancleReservation(@PathVariable("rsID") Integer reservation_id) {
			
		return reservationService.emergencyCancel(reservation_id);
		
	}
	
	@RequestMapping(value = "/confirm/{rsID}", method = RequestMethod.POST)
	public Reservation confirmReservation(@PathVariable("rsID") Integer reservation_id, @RequestParam Integer sign_id) {
		
		Reservation reservationMap = reservationService.findOne(reservation_id).get();
		reservationMap.setStatus(10); //10: 긴급통역 승인
		
		SignUser singuser = signUserService.findOne(sign_id).get();			
		reservationMap.setSignUser(singuser);
		
		Reservation ReservationResult = reservationService.update(reservationMap);
		
		return ReservationResult;
		
	}
	
}
