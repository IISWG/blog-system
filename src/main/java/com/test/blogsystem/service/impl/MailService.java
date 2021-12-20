package com.test.blogsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.test.blogsystem.entity.BlogPersonalInform;
import com.test.blogsystem.entity.param.MailVerifyParam;
import com.test.blogsystem.entity.result.BaseResult;
import com.test.blogsystem.entity.result.OkResult;
import com.test.blogsystem.entity.result.ServerErrResult;
import com.test.blogsystem.mapper.BlogPersonalInformMapper;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Log4j
public class MailService {
    @Autowired
    private JavaMailSender mailSender;//一定要用@Autowired

    @Resource
    private BlogPersonalInformMapper blogPersonalInformMapper;

    @Autowired
    RedisTemplate<String, String> redisTemplate;
    //application.properties中已配置的值
    @Value("${spring.mail.username}")
    private String from;

    /**
     * 给前端输入的邮箱，发送验证码
     * @param email
     * @param
     * @return
     */
    public boolean sendMimeMail(String email) {
        try {
//            HttpSession session = httpServletRequest.getSession();
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setSubject("验证码邮件");//主题
            //生成随机数
            String code = randomCode();
            redisTemplate.opsForValue().set(email, code,  60 * 10, TimeUnit.SECONDS);

            //将随机数放置到session中
//            session.setAttribute(email,email);
//            session.setAttribute(email+"code",code);
//            session.setMaxInactiveInterval(6000*1000);
//            Cookie cookie = new Cookie("JSESSIONID", session.getId());
//            cookie.setMaxAge(60*60*24);
//            httpServletResponse.addCookie(cookie);
//            httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");//是否支持cookie跨域
            System.out.println("code:"+code);
            mailMessage.setText("您收到的验证码是："+code);//内容

            mailMessage.setTo(email);//发给谁

            mailMessage.setFrom(from);//你自己的邮箱

            mailSender.send(mailMessage);//发送
            return  true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 随机生成6位数的验证码
     * @return String code
     */
    public String randomCode(){
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }

    /**
     * 检验验证码是否一致
     * @param
     * @param
     * @return
     */
    @Transactional
    public BaseResult registered(MailVerifyParam mailVerifyParam){
        //获取表单中的提交的验证信息
        String voCode = mailVerifyParam.getCode();
        String mail = mailVerifyParam.getMail();

        String code = redisTemplate.opsForValue().get(mail);
        System.out.println("getredis_code:"+code);
        BlogPersonalInform personalInform = blogPersonalInformMapper.selectOne(new QueryWrapper<BlogPersonalInform>().eq("mailbox", mail));

        if (personalInform != null) {
            log.error("该邮箱已被注册！");
            return new ServerErrResult("该邮箱已被注册！");
        }
        //如果email数据为空，或者不一致，注册失败
        if (code == null || code.isEmpty()){
            System.out.println("code:"+code);
            log.error("验证码失效或没发验证码！");
            return new ServerErrResult("验证码失效或没发验证码！");
        }else if (!code.equals(voCode)){
            //return "error,请重新注册";
            log.error("验证码错误！");
            return new ServerErrResult("验证码错误！");
        }

        BlogPersonalInform blogPersonalInform = new BlogPersonalInform();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        blogPersonalInform.setMailbox(mailVerifyParam.getMail())
                .setPassword(bCryptPasswordEncoder.encode(mailVerifyParam.getPassword()))
                .setNickname(mailVerifyParam.getMail()+"blog")
                .setCreateTime(LocalDateTime.now())
                .setUpdateTime(LocalDateTime.now());
        int insert = blogPersonalInformMapper.insert(blogPersonalInform);
        if (insert == 0) {
            log.error("注册失败！");
            return new ServerErrResult("注册失败！");
        }
        //跳转成功页面
        return new OkResult("注册成功！","");
    }

   public BaseResult changePassword(MailVerifyParam mailVerifyParam){
       String voCode = mailVerifyParam.getCode();
       String mail = mailVerifyParam.getMail();
       String password = mailVerifyParam.getPassword();
       String oldpassword = mailVerifyParam.getOldpassword();
       BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
       try {
           if (oldpassword != null && !oldpassword.equals("")) {
//               String encode = bCryptPasswordEncoder.encode(oldpassword);
//               System.out.println("encode:"+encode);
               BlogPersonalInform blogPersonalInform = blogPersonalInformMapper.selectOne(new QueryWrapper<BlogPersonalInform>().eq("mailbox", mail));
               System.out.println("blogPersonalInform:"+blogPersonalInform);
               if (blogPersonalInform != null) {
                   if (bCryptPasswordEncoder.matches(oldpassword, blogPersonalInform.getPassword())) {
                       blogPersonalInform.setPassword(bCryptPasswordEncoder.encode(password))
                                          .setUpdateTime(LocalDateTime.now());
                       int updateById = blogPersonalInformMapper.updateById(blogPersonalInform);
                       if (updateById == 0) {
                           log.error("修改密码失败！");
                           return new ServerErrResult("修改密码失败！");
                       }
                       //跳转成功页面
                       return new OkResult("修改密码成功！", "");
                   }
                   else {
                       return new ServerErrResult("旧密码输入错误！");
                   }

               } else {
                   return new ServerErrResult("账号问题！");
               }
           }
           String code = redisTemplate.opsForValue().get(mail);
           System.out.println("getredis_code:" + code);
           BlogPersonalInform personalInform = blogPersonalInformMapper.selectOne(new QueryWrapper<BlogPersonalInform>().eq("mailbox", mail));
           if (personalInform == null) {
               log.error("该邮箱没被注册！");
               return new ServerErrResult("该邮箱没被注册！");
           }
           //如果email数据为空，或者不一致，注册失败
           if (code == null || code.isEmpty()) {
               System.out.println("code:" + code);
               log.error("验证码失效或没发验证码！");
               return new ServerErrResult("验证码失效或没发验证码！");
           } else if (!code.equals(voCode)) {
               //return "error,请重新注册";
               log.error("验证码错误！");
               return new ServerErrResult("验证码错误！");
           }

           personalInform.setPassword(bCryptPasswordEncoder.encode(password));
           int updateById = blogPersonalInformMapper.updateById(personalInform);
           if (updateById == 0) {
               log.error("修改密码失败！");
               return new ServerErrResult("修改密码失败！");
           }
           //跳转成功页面
           return new OkResult("修改密码成功！", "");
       } catch (Exception e) {
           log.error(e.getMessage());
           TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
           return new ServerErrResult("修改密码失败！");
       }

    }
   public BaseResult changeName(MailVerifyParam mailVerifyParam){

       String mail = mailVerifyParam.getMail();
       String newname = mailVerifyParam.getNewname();

       try {
           BlogPersonalInform blogPersonalInform = blogPersonalInformMapper.selectOne(new QueryWrapper<BlogPersonalInform>().eq("mailbox", mail));
           blogPersonalInform.setNickname(newname).setUpdateTime(LocalDateTime.now());
           int updateById = blogPersonalInformMapper.updateById(blogPersonalInform);
           return new OkResult("修改昵称成功！", blogPersonalInform);
       } catch (Exception e) {
           log.error(e.getMessage());
           TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
           return new ServerErrResult("修改昵称失败！");
       }

    }
}
