package com.mycompany.demo.database.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    private String memberId;
    private String memberName;
    @ToString.Exclude
    private String password;
    private String email;
    private Date joinedAt;
}
