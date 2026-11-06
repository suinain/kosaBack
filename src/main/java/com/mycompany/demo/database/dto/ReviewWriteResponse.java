package com.mycompany.demo.database.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class ReviewWriteResponse {
    private String state;
    private String message;
}
