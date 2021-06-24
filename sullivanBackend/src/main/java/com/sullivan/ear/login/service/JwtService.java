package com.sullivan.ear.login.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Transactional
public class JwtService {

	@Value("${JWT.JwtExpireTime}")
	private int JwtExpireTime;
	
	@Value("${JWT.scretKey}")
	private String secretKey;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public String createToken(String email) {
		
		Map<String, Object> headers = new HashMap<>();
		headers.put("type", "JWT");
		headers.put("alg", "HS256");
		
		Map<String, Object> payloads = new HashMap<>();
		Date expireTime = new Date();
		long expiredTime = 1000 * 60 * 60 * 24 * JwtExpireTime;
		expireTime.setTime(System.currentTimeMillis() + expiredTime);
		
		System.out.println(System.currentTimeMillis());
		System.out.println(expireTime.getTime());
		
		payloads.put("exp", expireTime);
		payloads.put("data", email);
		
		String jwt = Jwts.builder()
				     .setHeader(headers)
				     .setClaims(payloads)
				     .signWith(SignatureAlgorithm.HS256, secretKey)
				     .compact();				     
		
	    return jwt;
	}
	
    public boolean verifyToken(String jwt) throws Exception {
    	
    	boolean result = true;
		
		Claims claims = Jwts.parser()
				        .setSigningKey(secretKey)
				        .parseClaimsJws(jwt)
				        .getBody();
		
		Date expiration = claims.get("exp", Date.class);
		
		Long currentTime = System.currentTimeMillis();
    	Long expirationTime = expiration.getTime();
    	
    	String data = claims.get("data", String.class);
    	
    	if (currentTime > expirationTime) {
    		
    		//throw new Exception("This JWT Token is not valid.");
    		result = false;
    	}
    	
    	System.out.println(currentTime);
    	System.out.println(expirationTime);
    	
    	return result;
    	
	}

}