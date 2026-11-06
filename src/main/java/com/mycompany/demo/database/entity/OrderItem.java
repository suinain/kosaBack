package com.mycompany.demo.database.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private long orderId;
    private long productId;
    private long quantity;
    private long unitPrice;
}
