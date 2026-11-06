package com.mycompany.demo.validation.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.demo.validation.dto.JoinRequest;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController 
@RequestMapping("/validation")
@Slf4j 
public class ValidationController {

    public ValidationController() {
        log.info("실행");
    }

    @PostMapping("/join")
    public String join(@Valid JoinRequest joinRequest) {
        log.info(joinRequest.toString());
        return "회원 가입";
    }
}
