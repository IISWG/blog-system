package com.test.blogsystem.entity.param;

import lombok.Data;

@Data
public class MailVerifyParam {
    private String newname;
    private String oldpassword;
    private String mail;
    private String password;
    private String code;
}
