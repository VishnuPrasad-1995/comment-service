package com.mavericsystems.commentservice.exception;

public class CommentNotFoundException extends RuntimeException{
    public CommentNotFoundException(String s) {
        super(s);
    }
}
