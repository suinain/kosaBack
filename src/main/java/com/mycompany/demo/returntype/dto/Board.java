package com.mycompany.demo.returntype.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                   // Getter/Setter/equals/toString 메소드를 추가
@AllArgsConstructor     // 매개값있는 생성자 추가
@NoArgsConstructor      // 기본 생성자 추가
public class Board {
    private int bno;
    private String btitle;
    private String bcontent;
    private String bwriter;
    private Date bdate;
}
