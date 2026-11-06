package com.mycompany.demo.database.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewWriteRequest {
    private String title;
    private String content;
    private String writer;
    private long productId;
    private MultipartFile file;
}
