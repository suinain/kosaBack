package com.mycompany.demo.di.service;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j 
public class DiInterfaceImplB implements DiInterface {
    @Override
    public void method() {
        log.info("실행");
    }
}
