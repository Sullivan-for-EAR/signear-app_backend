package com.sullivan.ear.reservation.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sullivan.ear.vo.Reservation;



public interface ReservationRepository extends JpaRepository<Reservation, Integer> {   
		
	Reservation findByRsID(Integer rsID);
	
	List<Reservation> findByStatusNotIn(List<Integer> status);
	
//	@Query("SELECT r FROM Reservation r INNER JOIN r.user_customers u WHERE u.customerID = :customerID")	
//	List<Reservation> findByCustomerID(@Param("customerID") Integer customerID);
	
    @Query("SELECT r FROM Reservation r join fetch r.customerUser c where c.customerID =:customerID and r.date >= :threshold")
    List<Reservation> findByCustomerID(Integer customerID, String threshold);
    
    @Query("SELECT r FROM Reservation r join fetch r.signUser c where c.signID =:signID and r.status = 3 and r.date >=:threshold")
    List<Reservation> findBySignID(Integer signID, String threshold);
	
}

