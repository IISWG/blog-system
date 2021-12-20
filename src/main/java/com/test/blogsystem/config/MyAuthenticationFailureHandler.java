package com.test.blogsystem.config;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.blogsystem.entity.bean.LoginBean;
import com.test.blogsystem.entity.result.OkResult;
import com.test.blogsystem.exception.ValidateCodeException;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Description :
 * @Author: 11903990213李列伟
 * @Date: 2021/11/25 7:24
 */
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        LoginBean loginBean = LoginBean.error(e.getMessage());

        // 验证码自定义异常的处理
        if (e instanceof ValidateCodeException){
            loginBean.setMsg(e.getMessage());
            //Security内置的异常处理
        }else if (e instanceof LockedException) {
            loginBean.setMsg("账户被锁定请联系管理员!");
        } else if (e instanceof CredentialsExpiredException) {
            loginBean.setMsg("密码过期请联系管理员!");
        } else if (e instanceof AccountExpiredException) {
            loginBean.setMsg("账户过期请联系管理员!");
        } else if (e instanceof DisabledException) {
            loginBean.setMsg("账户被禁用请联系管理员!");
        } else if (e instanceof BadCredentialsException) {
            loginBean.setMsg("邮箱账号密码输入错误,请重新输入!");
        }
        String s1 = JSONObject.toJSONString(loginBean);
        System.out.println(s1);
        out.append(s1);
        //out.write(new ObjectMapper().writeValueAsString(loginBean));
//        out.write(s1);
        out.flush();
        out.close();
    }
    @Bean
    public  MyAuthenticationFailureHandler getMyAuthenticationFailureHandler(){
        return new MyAuthenticationFailureHandler();
    }
}
