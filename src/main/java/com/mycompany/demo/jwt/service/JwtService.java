package com.mycompany.demo.jwt.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mycompany.demo.database.entity.Member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Service 
@Slf4j 
public class JwtService {
    // JWT를 생성하거나, 해석할때 필요한 비밀키
    private final SecretKey secretKey;

    // 문자열을 주입하는 이유는 비밀키를 만드는 문자열은 노출되어서는 안됨.
    // 시스템 환경 변수의 값으로 대체
    public JwtService(@Value("${jwt.secret.key}") String jwtSecretKey) throws Exception {
        // 알파벳(특수기호) 총 32개 이상의 문자로 구성된 문자열을 사용
        byte[] bytes = (jwtSecretKey).getBytes("UTF-8");
        // 32byte 이상의 길이를 가지고 있어야함
        secretKey = Keys.hmacShaKeyFor(bytes);
    }

    public String createJwt(Member member) {
        // 유효기간(1년)
        Date date = new Date(new Date().getTime() + 1000*1*60*60*24*365);
        // JWT 생성
        String jwt = Jwts.builder()
                        // JWT의 subject에는 사용자의 식별값을 저장
                        .subject(member.getMemberId())
                        // 추가적으로 저장하고 싶은 키와 데이터 저장
                        .claim("email", member.getEmail())
                        // 유효기간 설정
                        .expiration(date)
                        // 암호화된 문자열을 만들기 위한 시크릿키 설정
                        .signWith(secretKey)
                        .compact();
        log.info("jwt: ", jwt);
        return jwt;
    }

    public void validateJwt(String jwt) {
        // JWT 해석자를 생성
        JwtParser jwtParder = Jwts.parser()
                                .verifyWith(secretKey)
                                .build();
        // JWT를 해석해서 크레임을 생성(3가지 예외가  발생할 수 있음)
        Jws<Claims> jws = jwtParder.parseSignedClaims(jwt);
        // 여기까지가 성공되면 유효한 JWT라고 하자.
    }

    public Map<String, String> getClaims(String jwt) {
        JwtParser jwtParder = Jwts.parser()
                                .verifyWith(secretKey)
                                .build();
        // JWT를 해석해서 크레임을 생성(3가지 예외가  발생할 수 있음)
        Jws<Claims> jws = jwtParder.parseSignedClaims(jwt);
        Claims claim = jws.getPayload();

        Map<String, String> map = new HashMap<>();
        map.put("memberId", claim.getSubject());
        map.put("email", claim.get("email").toString());

        return map;
    }
}
