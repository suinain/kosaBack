package com.mycompany.demo.exception;

public class NoAccountException extends RuntimeException {
    public NoAccountException() {}
    public NoAccountException(String message) {
        // 부모 생성자 호출
        super(message);
    }
}
