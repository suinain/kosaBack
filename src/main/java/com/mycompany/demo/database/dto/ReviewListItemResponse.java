package com.mycompany.demo.database.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class ReviewListItemResponse {
    private long reviewId;
    private String title;
    private String writer;
    private Date writedAt;
    private long productId;
    private String fileName;
}
