package com.sullivan.ear.reservation.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sullivan.ear.reservation.repository.ReservationRepository;
import com.sullivan.ear.vo.CustomerUser;
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
	
	public List<Reservation> findAllSignReservation() {
		
		List<Integer> status = new ArrayList<>();
		status.add(3); // 3:예약확정
		status.add(4); // 4.예약취소
		status.add(5); // 5:예약거절
		
		return ReservationRepository.findByStatusNotIn(status);
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
		return ReservationRepository.save(reservation);
	}
	
	public Reservation cancle(Integer Reservation_id) {		
	   Reservation reservationMap = ReservationRepository.findById(Reservation_id).get();
	   reservationMap.setStatus(4); //4.예약취소
	   
	   return ReservationRepository.save(reservationMap);
	}
	
	public Reservation emergencyCancle(Integer Reservation_id) {
	   Reservation reservationMap = ReservationRepository.findById(Reservation_id).get();
	   reservationMap.setStatus(9); // 9: 긴급통역 취소

	   return ReservationRepository.save(reservationMap);
	}
	
	public Reservation getOneByRsID(Integer rsID) {
		return ReservationRepository.findByRsID(rsID);
	}
	
	public List<Reservation> getListByCustomerID(Integer customerID) {
		String localTime = LocalDateTime.now().toString().substring(0, 10);
		return ReservationRepository.findByCustomerID(customerID, localTime);
	}
	
	public List<Reservation> getListBySignID(Integer signID) {
		String localTime = LocalDateTime.now().toString().substring(0, 10);
		return ReservationRepository.findBySignID(signID, localTime);
	}
	
}
