package com.mycompany.demo.di.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.demo.di.service.AiService;
import com.mycompany.demo.di.service.BoardService;
import com.mycompany.demo.di.service.DiInterface;
import com.mycompany.demo.di.service.DiMemberService;

import lombok.extern.slf4j.Slf4j;


@RestController 
@RequestMapping("/di1")
@Slf4j 
public class DiController1 {
    @Autowired 
    private DiMemberService memberService;

    @Autowired 
    private BoardService boardService;

    @Autowired 
    private AiService aiService;

    @Autowired @Qualifier("diInterfaceImplA")
    private DiInterface diInterface;

    @PostMapping("/join")
    public String join() {
        memberService.join();
        return "회원 가입";
    }

    @PostMapping("/board-write")
    public String boardWrite() {
        boardService.write();
        return "게시물 저장";
    }

    @GetMapping("/chat")
    public String chat() {
        aiService.chat();
        return "답변 내용";
    }

    @GetMapping("/qualifier")
    public String qualifier() {
        diInterface.method();
        return "실행 결과";
    }    
    
}
