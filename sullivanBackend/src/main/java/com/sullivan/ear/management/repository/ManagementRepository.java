package com.sullivan.ear.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sullivan.ear.vo.Reservation;



public interface ManagementRepository extends JpaRepository<Reservation, Integer> {   
	
    @Query("SELECT r FROM Reservation r join fetch r.customerUser c where c.customerID =:customerID and r.date < :threshold")
    List<Reservation> findByCustomerID(Integer customerID, String threshold);
    
    @Query("SELECT r FROM Reservation r join fetch r.signUser c where c.signID =:signID and r.date < :threshold")
    List<Reservation> findBySignID(Integer signID, String threshold);
	
}

