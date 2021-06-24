package com.sullivan.ear.reservation.dao;

import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
    @Id
    private Integer user_no;
	
    private String email;

    private String phone;
	
}
