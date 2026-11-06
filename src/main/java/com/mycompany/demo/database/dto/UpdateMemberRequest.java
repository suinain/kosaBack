package com.mycompany.demo.database.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class UpdateMemberRequest {
    private String memberId;
    private String password;
    private String email;
}
