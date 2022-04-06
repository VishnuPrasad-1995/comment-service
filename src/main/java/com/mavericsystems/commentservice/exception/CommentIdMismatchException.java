package com.mavericsystems.commentservice.exception;

public class CommentIdMismatchException extends RuntimeException{
    public CommentIdMismatchException(String s){
        super(s);
    }
}
