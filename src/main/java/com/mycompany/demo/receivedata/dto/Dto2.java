package com.mycompany.demo.receivedata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class Dto2 {
    private String bno;
    private String btitle;
    private String bcontent;
    private String fileName;
    private String fileType;
}
