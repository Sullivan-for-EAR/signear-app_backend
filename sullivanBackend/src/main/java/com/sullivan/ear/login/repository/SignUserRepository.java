package com.sullivan.ear.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sullivan.ear.vo.SignUser;



public interface SignUserRepository extends JpaRepository<SignUser, Integer> {

	SignUser findByEmail(String email);

	
//	@Query("SELECT x FROM Customer x ORDER BY x.user_id")
//  List<FeedMessage> findAllOrderByName();

//	@Query("SELECT x FROM User x WHERE x.kakaoId=?1")
//	User findOneByKakao(String kakaoId);

	//User findByKakaoId(String kakaoId);    
    
}

