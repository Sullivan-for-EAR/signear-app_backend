package com.sullivan.ear.reservation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sullivan.ear.vo.Reservation;



public interface ReservationRepository extends JpaRepository<Reservation, Integer> {   
		
	Reservation findByRsID(Integer rsID);
	
	List<Reservation> findByAreaAndStatusIn(String address, List<Integer> status);

	@Query("SELECT r FROM Reservation r join fetch r.customerUser c where c.customerID =:customerID and r.date = :date and r.status = 8")
	List<Reservation> findByCustomerIDDate(Integer customerID, String date);
	
    @Query("SELECT r FROM Reservation r join fetch r.customerUser c where c.customerID =:customerID and r.date >= :threshold")
    List<Reservation> findByCustomerID(Integer customerID, String threshold);
    
    @Query("SELECT r FROM Reservation r join fetch r.signUser c where c.signID =:signID and r.status = 3 and r.date >=:threshold")
    List<Reservation> findBySignID(Integer signID, String threshold);
	
}

