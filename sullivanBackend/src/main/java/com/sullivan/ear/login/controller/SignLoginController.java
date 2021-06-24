package com.sullivan.ear.login.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sullivan.ear.exception.ApiException;
import com.sullivan.ear.exception.ExceptionEnum;
import com.sullivan.ear.login.dao.CustomerUserDTO;
import com.sullivan.ear.login.dao.SignUserDTO;
import com.sullivan.ear.login.service.JwtService;
import com.sullivan.ear.login.service.SignUserService;
import com.sullivan.ear.vo.CustomerUser;
import com.sullivan.ear.vo.SignUser;

@RestController
@RequestMapping("sign")
public class SignLoginController {

	@Autowired
	SignUserService signUserService;

	@Autowired
	JwtService jwtService;
	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> verifyToken(@RequestHeader Map<String, String> requestHeader) throws Exception {

        Map<String, Object> returnMap = new HashMap<>();
		
		boolean isValidToken = false;
		
		if (requestHeader.containsKey("token")) {
			isValidToken = jwtService.verifyToken(requestHeader.get("token"));
		}	
		
		returnMap.put("isValidToken", isValidToken);
		
		return returnMap;
	}
	
	@RequestMapping(value = "/check", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> checkEmail(@RequestParam String email) throws Exception {

		Map<String, Object> returnMap = new HashMap<>();

		SignUser userMap = signUserService.findByEmail(email);

		if (userMap != null && userMap.getSignID() != null) {
			returnMap.put("isNext", true);		
		} else {
			returnMap.put("isNext", false);
		}
		
		return returnMap;
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> login(@RequestBody SignUser user) throws Exception {

		Map<String, Object> returnMap = new HashMap<>();

		SignUser userMap = signUserService.signIn(user);
		SignUserDTO signDTO = new SignUserDTO();
		signDTO.setSignID(userMap.getSignID());
		signDTO.setEmail(userMap.getEmail());
		signDTO.setPhone(userMap.getPhone());

		if (userMap != null && userMap.getSignID() != null) {
			// JWT 발급
			String jwtToken = jwtService.createToken(user.getEmail());
			returnMap.put("userProfile", signDTO);
			returnMap.put("access_token", jwtToken);
			return returnMap;

		} else {
			// throw new Exception();
			throw new ApiException(ExceptionEnum.SECURITY_01);
		}
	}

	@RequestMapping(value = "/{no}", method = RequestMethod.GET)
	public Optional<SignUser> getUserInfo(@PathVariable("no") Integer no) {
		Optional<SignUser> userResult = signUserService.findOne(no);
		return userResult;
	}

	@RequestMapping(value = "/resetPW", method = RequestMethod.POST)
	public void resetPassword(@RequestBody SignUser user) throws Exception {
		signUserService.restPW(user.getEmail());

	}
}
