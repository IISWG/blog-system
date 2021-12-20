package com.test.blogsystem.controller;

import com.test.blogsystem.entity.param.MailVerifyParam;
import com.test.blogsystem.entity.result.BaseResult;
import com.test.blogsystem.entity.result.OkResult;
import com.test.blogsystem.entity.result.ServerErrResult;
import com.test.blogsystem.service.impl.MailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@CrossOrigin
public class LoginController {
    @Resource
    MailService mailService;
    @GetMapping("/tologin")
    public String tologin(HttpServletRequest httpServletRequest){

        return "redirect:http://localhost:8080/login";
    }
    @GetMapping("/sendEmail")
    @ResponseBody
    public BaseResult sendEmail(String email){
        System.out.println("email:"+email);
        boolean sendMimeMail = mailService.sendMimeMail(email);
        if (sendMimeMail) {
            return new OkResult("发送成功！","");
        } else {
            return new ServerErrResult("发送失败！");
        }
    }
    @PostMapping("/regist")
    @ResponseBody
    public BaseResult regist(@RequestBody MailVerifyParam userVo){
//        HttpSession session = request.getSession();
        System.out.println("userVo:"+userVo);
        BaseResult registered = mailService.registered(userVo);
        return registered;
    }
    @PostMapping("/changePassword")
    @ResponseBody
    public BaseResult changePassword(@RequestBody MailVerifyParam userVo){

        System.out.println("userVo:"+userVo);
        return mailService.changePassword(userVo);
    }
    @PostMapping("/changeName")
    @ResponseBody
    public BaseResult changeName(@RequestBody MailVerifyParam userVo){

        System.out.println("userVo:"+userVo);
        return mailService.changeName(userVo);
    }


}
