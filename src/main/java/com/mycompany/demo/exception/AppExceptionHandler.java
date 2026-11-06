package com.mycompany.demo.exception;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice 
@Slf4j 
public class AppExceptionHandler {
    @ExceptionHandler(NoAccountException.class)
    public ResponseEntity<Map<String, Object>> handleNoAccountException(NoAccountException e) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "fail");
        map.put("message", e.getMessage());
        map.put("methodName", "handleNoAccountException");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(map);
    }

    @ExceptionHandler(DuplicatedMemberIDException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicatedMemberIDException(DuplicatedMemberIDException e) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "fail");
        map.put("message", e.getMessage());
        map.put("methodName", "handleDuplicatedMemberIDException");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(map);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Map<String, Object>> handleSQLException(SQLException e) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "fail");
        map.put("message", e.getMessage());
        map.put("methodName", "handleSQLException");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(map);
    }

    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<Map<String, Object>> handlePSQLException(PSQLException e) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "fail");
        map.put("message", e.getMessage());
        map.put("methodName", "handlePSQLException");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(map);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Map<String, Object>> handleNullPointerException(NullPointerException e) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "fail");
        map.put("message", e.getMessage());
        map.put("methodName", "handleNullPointerException");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(map);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoResourceFoundException(NoResourceFoundException e) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "fail");
        map.put("message", "너 장난칠래?");
        map.put("methodName", "handleNoResourceFoundException");
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(map);
    }    

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        log.info(e.toString());
        Map<String, Object> map = new HashMap<>();
        map.put("status", "fail");
        map.put("message", e.getMessage());
        map.put("methodName", "handleException");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(map);
    }
}
