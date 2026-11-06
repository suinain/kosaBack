package com.mycompany.demo.di.service;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

// 관리 빈의 이름: 클래스의 첫자를 소문자로 한 이름
@Service
@Slf4j 
public class DiMemberService {
    public void join() {
        log.info("회원 가입을 처리합니다.");
    }
}
