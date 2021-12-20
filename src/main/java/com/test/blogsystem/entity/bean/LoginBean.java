package com.test.blogsystem.entity.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginBean {
    private String msg;
    public static LoginBean error(String msg){
        return new LoginBean(msg);
    }
}
