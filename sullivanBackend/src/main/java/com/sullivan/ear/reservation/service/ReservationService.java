package com.sullivan.ear.reservation.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sullivan.ear.exception.ApiException;
import com.sullivan.ear.exception.ExceptionEnum;
import com.sullivan.ear.reservation.repository.ReservationRepository;
import com.sullivan.ear.vo.Reservation;

@Service
@Transactional
public class ReservationService {

	ReservationRepository ReservationRepository;
	
	//1:읽지않음, 2:센터확인중, 3:예약확정, 4.예약취소, 5:예약거절, 
	//6: 통역취소, 7:통역 완료, 8: 긴급통역 연결중, 9: 긴급통역 취소, 10: 긴급통역 승인
	
	@Autowired
	public ReservationService(ReservationRepository ReservationRepository) {

		this.ReservationRepository = ReservationRepository;
	}

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public List<Reservation> findAll() {
		return ReservationRepository.findAll();
	}
	
	public List<Reservation> findAllSignReservation(String address) {
		
		List<Integer> status = new ArrayList<>();
		status.add(1); // 1:읽지않음
		status.add(2); // 2:센터확인중
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());		
		cal.add(Calendar.DATE, -1);
		String date = dateFormat.format(cal.getTime());
		
		return ReservationRepository.findByAreaAndStatusInAndDateGreaterThan(address, status, date);
	}

	public Optional<Reservation> findOne(Integer Reservation_id) {
		return ReservationRepository.findById(Reservation_id);
	}
	
	public Reservation update(Reservation Reservation) {
		return ReservationRepository.save(Reservation);
	}

	public void delete(Integer Reservation_id) {
		ReservationRepository.deleteById(Reservation_id);
	}

	public Reservation create(Reservation reservation) {
		
		Integer customerID = reservation.getCustomerUser().getCustomerID();
		String date = LocalDateTime.now().toString().substring(0, 10).replaceAll("-", "");
		List<Reservation> reservationList = ReservationRepository.findByCustomerIDDate(customerID, date);
		
		if (reservationList.size() > 0) {
			throw new ApiException(ExceptionEnum.SECURITY_03);
		}
		
		return ReservationRepository.save(reservation);
	}
	
	public Reservation cancel(Integer Reservation_id) {		
	   Reservation reservationMap = ReservationRepository.findById(Reservation_id).get();
	   reservationMap.setStatus(4); //4.예약취소
	   
	   return ReservationRepository.save(reservationMap);
	}
	
	public Reservation emergencyCancel(Integer Reservation_id) {
	   Reservation reservationMap = ReservationRepository.findById(Reservation_id).get();
	   reservationMap.setStatus(9); // 9: 긴급통역 취소

	   return ReservationRepository.save(reservationMap);
	}
	
	public Reservation getOneByRsID(Integer rsID) {
		return ReservationRepository.findByRsID(rsID);
	}
	
	public List<Reservation> getListByCustomerID(Integer customerID) {
		String localTime = LocalDateTime.now().toString().substring(0, 10).replaceAll("-", "");
		return ReservationRepository.findByCustomerID(customerID, localTime);
	}
	
	public List<Reservation> getListBySignID(Integer signID) {
		String localTime = LocalDateTime.now().toString().substring(0, 10).replaceAll("-", "");
		return ReservationRepository.findBySignID(signID, localTime);
	}
	
}
