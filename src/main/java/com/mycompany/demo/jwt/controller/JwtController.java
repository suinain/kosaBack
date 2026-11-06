package com.mycompany.demo.jwt.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.demo.database.entity.Member;
import com.mycompany.demo.jwt.dto.LoginResponse;
import com.mycompany.demo.jwt.dto.OrderRequest;
import com.mycompany.demo.jwt.dto.OrderResponse;
import com.mycompany.demo.jwt.service.JwtService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController 
@RequestMapping("/jwt")
@Slf4j 
public class JwtController {
    private final JwtService jwtService;


    public JwtController(JwtService jwtService){
        this.jwtService = jwtService;

    }
    @PostMapping("/login")
    public LoginResponse login() {
        Member member = new Member();
        member.setMemberId("user1");
        member.setEmail("user1@mycompany.com");
        String jwt = jwtService.createJwt(member);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setStatus("success");
        loginResponse.setJwt(jwt);
        return loginResponse;
    }

    @PostMapping("/order")
    public OrderResponse order(@RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setStatus("fail");

        try {
            String jwt = orderRequest.getJwt();
            // jwt 유효성검사
            jwtService.validateJwt(jwt);
            orderResponse.setStatus("success");
            orderResponse.setMessage("유효한 토큰");
            //jwt에서 사용자 id, email 얻기
            String memberId= jwtService.getClaims(jwt).get("memberId"); 
            String email =jwtService.getClaims(jwt).get("email"); 
            log.info("memberId : {} " , memberId);
            log.info("email : {}" , email);
        } catch(MalformedJwtException e) {
            // JWT의 형식에 위배되었을 경우
            orderResponse.setMessage("JWT의 형식에 위배");
        } catch(SignatureException e) {
            // 비밀키로 복호화를 할 수 없을 경우
            orderResponse.setMessage("비밀키로 복호화를 할 수 없음");
        } catch(ExpiredJwtException e) {
            // 유효기간이 만료되었을 경우
            orderResponse.setMessage("유효기간이 만료됨");
        }   
        
        return orderResponse;
    }

}
