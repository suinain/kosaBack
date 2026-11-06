package com.mycompany.demo.di.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service("diInterfaceImplA") 
@Primary 
@Slf4j 
public class DiInterfaceImplA implements DiInterface {
    @Override
    public void method() {
        log.info("실행");
    }
}
