package com.sullivan.ear.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_sign")
public class SignUser {
    @Id
    @Column(name = "signID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer signID;
	
	@Column(name = "email")
    private String email;
	
	
	@Column(name = "password")
    private String password;
	
	@Column(name = "phone")
    private String phone;
	
	@Column(name = "address")
    private String address;
	
	/**
	 * 생성 일자
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REG_DATE")
	@CreationTimestamp
	private Date regDate;

	/**
	 * 업데이트 일자
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MOD_DATE")
	@UpdateTimestamp
	private Date modDate;
	
}
