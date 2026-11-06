package com.mycompany.demo.httpmethod;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController 
@Slf4j 
@RequestMapping("/httpmethod")
public class HttpMethodController {
    @RequestMapping("/info") 
    public String info() {
        // log.debug("실행");
        log.info("실행");
        // log.warn("실행");
        // log.error("실행");
        return "정보 조회";
    }

    @GetMapping("/method1") 
    public String method1() {
        log.info("실행");
        return "실행 결과";
    }

    @PostMapping("/method2") 
    public String method2() {
        log.info("실행");
        return "실행 결과";
    }

    @PutMapping("/method3") 
    public String method3() {
        log.info("실행");
        return "실행 결과";
    }

    @DeleteMapping("/method4") 
    public String method4() {
        log.info("실행");
        return "실행 결과";
    }
}
