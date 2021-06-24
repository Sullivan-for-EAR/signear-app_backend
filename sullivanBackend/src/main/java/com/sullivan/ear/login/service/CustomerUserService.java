package com.sullivan.ear.login.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.sullivan.ear.exception.ApiException;
import com.sullivan.ear.exception.ExceptionEnum;
import com.sullivan.ear.vo.CustomerUser;
import com.sullivan.ear.login.repository.CustomerUserRepository;

@Service
@Transactional
public class CustomerUserService {

	CustomerUserRepository userRepository;

	PasswordEncoder passwordEncoder;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${JWT.initailPW}")
	private String initailPW;

	@Autowired
	public CustomerUserService(CustomerUserRepository userRepository, PasswordEncoder passwordEncoder) {

		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Autowired
	JwtService jwtService;

	public List<CustomerUser> findAll() {
		return userRepository.findAll();
	}

	public Optional<CustomerUser> findOne(Integer user_id) {
		return userRepository.findById(user_id);
	}

	public CustomerUser findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	/**
	 * 회원가입
	 *
	 * @return null
	 */

	public CustomerUser create(CustomerUser user) {

		CustomerUser newuser = new CustomerUser();
		try {
			// 아이디 중복 체크
			CustomerUser currentuser = userRepository.findByEmail(user.getEmail());

			// 이미 유저가 존재할 때
			if (currentuser != null) {
				throw new ApiException(ExceptionEnum.SECURITY_02);
			}
			// 비밀번호 암호화
			String encodePassword = passwordEncoder.encode(user.getPassword());
			user.setPassword(encodePassword);
			
			newuser = userRepository.save(user);

		} catch (Exception e) {
			// Rollback
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			logger.error(e.getMessage());
			throw new ApiException(ExceptionEnum.SECURITY_01);
		}
		return newuser;
	}

	/**
	 * 로그인
	 *
	 * @return user
	 */

	public CustomerUser signIn(CustomerUser user) {

		CustomerUser newuser = new CustomerUser();
		try {
			// 회원아이디 체크
			CustomerUser currentuser = userRepository.findByEmail(user.getEmail());

			// 아이디가 틀렸을 때
			if (currentuser == null) {
				throw new ApiException(ExceptionEnum.SECURITY_01);
			}

			// parameter1 : rawPassword, parameter2 : encodePassword
			boolean check = passwordEncoder.matches(user.getPassword(), currentuser.getPassword());

			// 로그인 성공
			if (check) {
//				return new DefaultRes(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS);
				newuser = currentuser;
			}
			
		} catch (Exception e) {
			// Rollback
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			logger.error(e.getMessage());
			throw new ApiException(ExceptionEnum.SECURITY_01);
		}
		return newuser;
	}

	public void restPW(String email) {

		try {
			// 아이디 중복 체크
			CustomerUser currentuser = userRepository.findByEmail(email);

			// 이미 유저가 존재할 때
			if (currentuser == null) {
				throw new ApiException(ExceptionEnum.SECURITY_01);
			}
			// 비밀번호 암호화
			String encodePassword = passwordEncoder.encode(initailPW);
			currentuser.setPassword(encodePassword);
			
			userRepository.save(currentuser);

		} catch (Exception e) {
			// Rollback
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			logger.error(e.getMessage());
			throw new ApiException(ExceptionEnum.SECURITY_01);
		}
	}
	
	public CustomerUser update(CustomerUser User) {
		return userRepository.save(User);
	}

	public void delete(Integer user_id) {
		userRepository.deleteById(user_id);
	}

}
