package com.sullivan.ear.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sullivan.ear.management.dao.ManagementDTO;
import com.sullivan.ear.management.service.ManagementService;

@RestController
@RequestMapping("management")
public class ManagementController {

	@Autowired
	ManagementService managementService;
	
	@RequestMapping(value = "/customer/list", method = RequestMethod.GET)
	public List<ManagementDTO> getCustomerManagementList(@RequestParam Integer customer_id) {
		
		List<ManagementDTO> ReservationResult = managementService.getListByCustomerID(customer_id);

		return ReservationResult;
	}
	
	@RequestMapping(value = "/sign/list", method = RequestMethod.GET)
	public List<ManagementDTO> getSignManagementList(@RequestParam Integer sign_id) {
		
		List<ManagementDTO> ReservationResult = managementService.getListBySignID(sign_id);

		return ReservationResult;
	}
	
	@RequestMapping(value = "/delete/{rsID}", method = RequestMethod.POST)
	public void deleteReservation(@PathVariable("rsID") Integer reservation_id) {
		
		managementService.delete(reservation_id);
		
	}
	
}
