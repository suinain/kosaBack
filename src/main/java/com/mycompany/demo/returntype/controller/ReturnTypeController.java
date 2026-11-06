package com.mycompany.demo.returntype.controller;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.demo.returntype.dto.Board;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


@RestController 
@RequestMapping("/returntype")
@Slf4j 
public class ReturnTypeController {
    @GetMapping("/string")
    public String getString() {
        return "문자열 결과";
    }

    @GetMapping(value="/dto", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Board getDto() {
        // Board board = new Board();
        // board.setBno(5);
        // board.setBtitle("제목");
        // board.setBcontent("내용");
        // board.setBwriter("홍길동");
        // board.setBdate(new Date());
        
        Board board = new Board(5, "제목", "내용", "홍길동", new Date());
        
        return board;
    }

    @RequestMapping(value="/list", method = RequestMethod.GET)
    // @GetMapping("/list")
    public List<Board> getList() {
        List<Board> list = new ArrayList<>();

        for(int i=0; i<5; i++) {
            Board board = new Board(i, "제목"+i, "내용"+i, "홍길동", new Date());
            list.add(board);
        }

        return list;
    }

    @GetMapping("/map")
    public Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("bno", 1);
        map.put("btitle", "제목");
        map.put("bcontent", "내용");
        map.put("bwriter", "홍길동");
        map.put("bdate", new Date());

        return map;
    }

    @GetMapping("/response-entity")
    public ResponseEntity<Board> getResponseEntity() {
        Board board = new Board(5, "제목", "내용", "홍길동", new Date());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .header("my-header", "my-value")
                .body(board);
    }

    @GetMapping("/http-entity")
    public HttpEntity<Board> getHttpEntity() {
        Board board = new Board(5, "제목", "내용", "홍길동", new Date());
        
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("my-header", "my-value");

        HttpEntity<Board> httpEntity = new HttpEntity<>(board, httpHeaders);
        return httpEntity;
    }

    @GetMapping("/download")
    public void download(HttpServletResponse response) throws Exception {
        log.info("실행");

        // 파일 경로
        String filePath= "C:/gen-ai-course/data/image/generatedImage-1-1536x1024.png";

        // 응답 헤더 추가 ==============================================
        response.addHeader("Content-Type", "image/png");  // MIME Type
        response.addHeader("Content-Disposition", "attachment; filename=\"generatedImage-1-1536x1024.png\"");

        // 응답 본문에 파일 데이터 추가 ==================================
        // 응답 본문에 출력하는 단위
        int chunkSize = 1024; // 1 KB

        // 데이터 버퍼 (1KB에 해당되는 데이터가 저장되는 배열)
        byte[] data = new byte[chunkSize];
        
        // 입력 스트림 얻기
        FileInputStream fis = new FileInputStream(filePath);

        // 출력 스트림 얻기(응답 본문에 출력)
        OutputStream os = response.getOutputStream();

        // 청크 크기 만큼 읽고 출력을 반복
        while(true) {
            // 최대로 청크 크기만큼 읽고, 실제로 읽은 바이트 수를 반환
            // 읽은 데이터는 데이터 버퍼 저장
            int num = fis.read(data);
            // 읽은 데이터를 본문에 출력
            if(num == -1) break;
            os.write(data, 0, num);
        }
        os.flush();
        fis.close();
        os.close();
    }
}
