package com.mycompany.demo.exception;

public class DuplicatedMemberIDException extends RuntimeException {
    public DuplicatedMemberIDException() {}
    public DuplicatedMemberIDException(String message) {
        // 부모 생성자 호출
        super(message);
    }
}
