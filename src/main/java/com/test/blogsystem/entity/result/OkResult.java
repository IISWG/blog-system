package com.test.blogsystem.entity.result;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * @author 11903990213李列伟
 * @date 2021/7/26
 * @description: 正常
 */
@Data
public class OkResult extends BaseResult{
    private Integer code;
    private String msg;
    private Object data;
    private String token;

    public OkResult() {
        this.code = HttpStatus.OK.value();
        this.msg = HttpStatus.OK.getReasonPhrase();
    }

    public OkResult(Object data){
        this.code = HttpStatus.OK.value();
        this.msg = HttpStatus.OK.getReasonPhrase();
        this.data = data;
    }
    public OkResult(String msg,Object data){
        this.code = HttpStatus.OK.value();
        this.msg = msg;
        this.data = data;
    }
}
