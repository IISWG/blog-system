package com.test.blogsystem.entity.result;


import lombok.Data;

/**
 * @author 11903990213李列伟
 * @version 1.0
 * @date
 * @description:基本返回值
 */
@Data
public class BaseResult {
    private Integer code;
    private String msg;
    private Object data;
    public BaseResult(){
    }

    public BaseResult(Integer code, String msg, Object data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public BaseResult(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
