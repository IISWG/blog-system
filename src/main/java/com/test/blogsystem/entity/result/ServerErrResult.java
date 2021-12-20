package com.test.blogsystem.entity.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author 11903990213李列伟
 * @date 2021/7/26
 * @description: 后端代码异常
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerErrResult extends BaseResult{
    private Integer code;
    private String msg;
    private Object data;

    public ServerErrResult(String msg){
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.msg = msg;
    }

    public ServerErrResult(String msg, Object data){
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.msg = msg;
        this.data = data;
    }
}
