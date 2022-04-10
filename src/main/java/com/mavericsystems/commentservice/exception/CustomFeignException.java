package com.mavericsystems.commentservice.exception;


public class CustomFeignException extends RuntimeException{
    public CustomFeignException(String s) {
        super(s);
    }
}
