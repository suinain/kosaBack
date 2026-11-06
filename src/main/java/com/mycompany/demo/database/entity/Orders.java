package com.mycompany.demo.database.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {
    private long orderId;
    private String memberId;
    private Date orderedAt;
}
