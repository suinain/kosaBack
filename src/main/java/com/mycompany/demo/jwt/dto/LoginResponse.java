package com.mycompany.demo.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class LoginResponse {
    private String status;
    private String jwt;
}
