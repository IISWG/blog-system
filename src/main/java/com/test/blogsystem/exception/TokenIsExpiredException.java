package com.test.blogsystem.exception;

public class TokenIsExpiredException extends Throwable {
    public TokenIsExpiredException(String s) {
        super(s);
    }
}
