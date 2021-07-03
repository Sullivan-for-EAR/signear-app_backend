package com.sullivan.ear.login.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sullivan.ear.login.dao.CustomerUserDTO;
import com.sullivan.ear.login.dao.SignUserDTO;
import com.sullivan.ear.login.service.CustomerUserService;
import com.sullivan.ear.login.service.JwtService;
import com.sullivan.ear.login.service.SignUserService;
import com.sullivan.ear.vo.CustomerUser;
import com.sullivan.ear.vo.SignUser;

@RestController
@RequestMapping("user")
public class UserController {

	@Autowired
	CustomerUserService customerUserService;
	
	@Autowired
	SignUserService signUserService;
	
	@Autowired
	JwtService jwtService;

	@RequestMapping(value = "/customer/create", method = RequestMethod.POST)
	public Map<String, Object> createCustomerUser(@RequestBody CustomerUser user) {

		Map<String, Object> returnMap = new HashMap<>();
		
		CustomerUser userResult = customerUserService.create(user);
		
		CustomerUserDTO customerDTO = new CustomerUserDTO();
	    customerDTO.setCustomerID(userResult.getCustomerID());
	    customerDTO.setEmail(userResult.getEmail());
	    customerDTO.setPhone(userResult.getPhone());

		String jwtToken = jwtService.createToken(userResult.getEmail());

		returnMap.put("access_token", jwtToken);
		returnMap.put("userProfile", customerDTO);
		
		return returnMap;
	}

	@RequestMapping(value = "/sign/create", method = RequestMethod.POST)
	public Map<String, Object> createSignUser(@RequestBody SignUser user) {

		Map<String, Object> returnMap = new HashMap<>();
		
		SignUser userResult = signUserService.create(user);
		
		SignUserDTO signDTO = new SignUserDTO();
		signDTO.setSignID(userResult.getSignID());
		signDTO.setEmail(userResult.getEmail());
		signDTO.setAddress(userResult.getAddress());

		String jwtToken = jwtService.createToken(userResult.getEmail());

		returnMap.put("access_token", jwtToken);
		returnMap.put("userProfile", signDTO);
		
		return returnMap;
	}

	@RequestMapping(value = "/customer", method = RequestMethod.GET)
	public CustomerUserDTO getCustomerUserInfo(@RequestParam Integer customer_id) {
		Optional<CustomerUser> userResult = customerUserService.findOne(customer_id);
		CustomerUser userResultMap = userResult.get();
		
		CustomerUserDTO userDTO = new CustomerUserDTO();
		userDTO.setCustomerID(userResultMap.getCustomerID());
		userDTO.setEmail(userResultMap.getEmail());
		userDTO.setPhone(userResultMap.getPhone());

		return userDTO;
	}
	
	@RequestMapping(value = "/sign", method = RequestMethod.GET)
	public SignUserDTO getSignUserInfo(@RequestParam Integer sign_id) {
		Optional<SignUser> userResult = signUserService.findOne(sign_id);
		SignUser userResultMap = userResult.get();
		
		SignUserDTO userDTO = new SignUserDTO();
		userDTO.setSignID(userResultMap.getSignID());
		userDTO.setEmail(userResultMap.getEmail());
		userDTO.setAddress(userResultMap.getAddress());

		return userDTO;
	}
}
