package com.mycompany.demo.receivedata.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.demo.receivedata.dto.Dto1;
import com.mycompany.demo.receivedata.dto.Dto2;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


@RestController 
@RequestMapping("/receivedata")
@Slf4j 
public class ReceiveDataController {
    @GetMapping(value="/method1", params = "bno")
    public String method1(@RequestParam("bno") String bno) {
        log.info("실행");
        log.info("bno: {}", bno);
        return "실행 결과";
    }

    @GetMapping(value="/method2", headers = "Authorization")
    public String method2(@RequestHeader("Authorization") String authorization) {
        log.info("실행");
        log.info("Authorization: {}", authorization);
        return "실행 결과";
    }

    @PostMapping(value="/method3", produces = MediaType.TEXT_XML_VALUE)
    public Dto1 method3(
        @RequestParam("bno") String bno, 
        @RequestParam("btitle") String btitle, 
        @RequestParam("bcontent") String bcontent) {
        Dto1 dto = new Dto1();
        dto.setBno(bno);
        dto.setBtitle(btitle);
        dto.setBcontent(bcontent);
        return dto; 
    }

    @PostMapping(value="/method4", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Dto1 method4(@RequestBody Dto1 dto) {
        return dto; 
    }

    @GetMapping("/method5/{data1}/{data2}")
    public String method5(
        @PathVariable("data1")  String data1, 
        @PathVariable("data2")  String data2) {
        log.info("data1: {}, data2: {}", data1, data2);
        return "실행 결과";
    }

    @PostMapping(value="/method6", 
                 consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public Dto2 method6(
        @RequestPart("bno") String bno, 
        @RequestPart("btitle") String btitle, 
        @RequestPart("bcontent") String bcontent, 
        @RequestPart("battach") MultipartFile battach) {
        Dto2 dto = new Dto2();
        dto.setBno(bno);
        dto.setBtitle(btitle);
        dto.setBcontent(bcontent);
        dto.setFileName(battach.getOriginalFilename());
        dto.setFileType(battach.getContentType());
        return dto;
    }

    @GetMapping("/method7")
    public String method7(@RequestParam(value="bno", defaultValue = "1") int bno) {
        return bno + "게시물 내용";
    }

    @GetMapping("/method8-cookie-create")
    public String method8CookieCreate(HttpServletResponse response) {
        Cookie cookie = new Cookie("num", "8");
        response.addCookie(cookie);
        return "브라우저에게 쿠키(번호표)를 줬습니다.";
    }

    @GetMapping("/method8-cookie-check")
    public String method8CookieCheck(@CookieValue("num") String num) {
        return num + "번 확인되었어요. 맛있게 드세요.";
    } 
}
