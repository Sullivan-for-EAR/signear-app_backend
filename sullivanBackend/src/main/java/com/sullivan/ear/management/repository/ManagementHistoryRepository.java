package com.sullivan.ear.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sullivan.ear.vo.ReservationHistory;



public interface ManagementHistoryRepository extends JpaRepository<ReservationHistory, Integer> {   
	
}

