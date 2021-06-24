package com.sullivan.ear.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
@Table(name = "reservation")
public class Reservation {
    @Id
    @Column(name = "rsID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer rsID;
	
	@Column(name = "date")
    private String date;
	
	@Column(name = "start_time")
    private String start_time;
	
	@Column(name = "end_time")
    private String end_time;
	
	@Column(name = "area")
    private String area;
	
	@Column(name = "address")
    private String address;
	
	@Column(name = "method")
    private Integer method;
	
	@Column(name = "status")
    private Integer status;
	
	@Column(name = "type")
    private Integer type;
	
	@Lob
	@Column(name = "request")
    private String request;
	
	@Lob
	@Column(name = "reject")
    private String reject;
	
	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "customerID", referencedColumnName = "customerID") })
	private CustomerUser customerUser;
	
	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "signID", referencedColumnName = "signID") })
	private SignUser signUser;
	
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
