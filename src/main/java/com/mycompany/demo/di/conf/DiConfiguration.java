package com.mycompany.demo.di.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mycompany.demo.di.service.BoardService;

import lombok.extern.slf4j.Slf4j;

@Configuration 
@Slf4j 
public class DiConfiguration {
    // 반환 객체를 관리하는 이름: 메소드 이름, 클래스의 첫자를 소문자로한 이름이 아님.
    @Bean
    public BoardService getBoardService() {
        log.info("실행");
        return new BoardService();
    }
}
