package com.mycompany.demo.di.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.demo.di.service.AiService;
import com.mycompany.demo.di.service.BoardService;
import com.mycompany.demo.di.service.DiInterface;
import com.mycompany.demo.di.service.DiMemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController 
@RequestMapping("/di3")
@Slf4j 
@RequiredArgsConstructor 
public class DiController3 {
    private final DiMemberService memberService;
    private final BoardService boardService;
    private final AiService aiService;
    // @Qualifier("diInterfaceImplB") (x)
    // @Primary 가 기본적으로 주입
    private final DiInterface diInterface;

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
