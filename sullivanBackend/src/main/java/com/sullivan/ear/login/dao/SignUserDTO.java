package com.sullivan.ear.login.dao;

import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUserDTO {
    @Id
    private Integer signID;
	
    private String email;

    private String phone;
	
}
