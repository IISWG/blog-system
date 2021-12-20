package com.test.blogsystem.exception;

import org.springframework.security.core.AuthenticationException;


/**
 * @Description :
 * @Author: 11903990213李列伟
 * @Date: 2021/11/25 7:24
 */

public class ValidateCodeException extends AuthenticationException  {

    public ValidateCodeException(String msg) {
        super(msg);
    }


}
