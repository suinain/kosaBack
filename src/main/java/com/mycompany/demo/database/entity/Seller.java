package com.mycompany.demo.database.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seller {
    private long sellerId;
    private String sellerName;
    private String email;
    private String phone;
    private Date registeredAt;
}
