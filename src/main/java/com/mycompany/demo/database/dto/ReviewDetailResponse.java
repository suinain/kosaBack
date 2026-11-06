package com.mycompany.demo.database.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDetailResponse {
    private long reviewId;
    private String title;
    private String content;
    private String writer;
    private long productId;
    private String fileName;
    private Date writedAt;
}
