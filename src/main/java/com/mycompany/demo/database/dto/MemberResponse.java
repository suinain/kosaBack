package com.mycompany.demo.database.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor 
@NoArgsConstructor 
public class MemberResponse {
    private String memberId;
    private String memberName;
    private String email;
    private Date joinedAt;
}
