package com.test.blogsystem.filter;


import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.blogsystem.entity.BlogPersonalInform;
import com.test.blogsystem.entity.JwtUser;
import com.test.blogsystem.entity.param.LoginParam;
import com.test.blogsystem.entity.result.OkResult;
import com.test.blogsystem.entity.result.ServerErrResult;
import com.test.blogsystem.mapper.BlogPersonalInformMapper;
import com.test.blogsystem.utils.JwtTokenUtils;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

//进行用户账号的验证
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Resource
    BlogPersonalInformMapper blogPersonalInformMapper;

    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        //设置登录请求的url
        super.setFilterProcessesUrl("/login");
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,

                                                HttpServletResponse response) throws AuthenticationException{

        // 从输入流中获取到登录的信息
        try {
            LoginParam loginUser = new ObjectMapper().readValue(request.getInputStream(), LoginParam.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword(), new ArrayList<>()));

	//密码错误时抛出异常		
        }catch (BadCredentialsException b){
            System.out.println("密码错误");
            try {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=utf-8");
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                ServerErrResult serverErrResult = new ServerErrResult("密码错误!");
                String s = JSON.toJSONString(serverErrResult);
                response.getWriter().write(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        //无此用户时抛出异常
        }catch (UsernameNotFoundException i){
            System.out.println("没有此用户");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ServerErrResult serverErrResult = new ServerErrResult("该邮箱没被注册!");
            String s = JSON.toJSONString(serverErrResult);
            response.getWriter().write(s);
            return null;
        }

        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 成功验证后调用的方法
    // 如果验证成功，就生成token并返回
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        System.out.println("登录成功");
        JwtUser jwtUser = (JwtUser) authResult.getPrincipal();
        System.out.println("jwtUser:" + jwtUser.toString());
        String role = "";
        Collection<? extends GrantedAuthority> authorities = jwtUser.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            role = authority.getAuthority();
        }
        String token = JwtTokenUtils.createToken(jwtUser.getUsername(), role);
        // 返回创建成功的token
        // 但是这里创建的token只是单纯的token
        // 按照jwt的规定，最后请求的时候应该是 `Bearer token`
        System.out.println(token);
        response.setHeader("authorization", JwtTokenUtils.TOKEN_PREFIX + token);
	//这里我还将该用户的id进行返回了
       // response.setIntHeader("id",jwtUser.getId().intValue());
//        Long id = jwtUser.getId();
//        System.out.println("id:"+id);
//
//        BlogPersonalInform blogPersonalInform = blogPersonalInformMapper.selectOne(new QueryWrapper<BlogPersonalInform>().eq("id", id));
        BlogPersonalInform blogPersonalInform = jwtUser.getBlogPersonalInform();
        String s;
        response.setContentType("application/json;charset=utf-8");
        if (blogPersonalInform.getState() == 0) {
            ServerErrResult serverErrResult = new ServerErrResult("该账号被封号了！请联系管理员解封！");
            s = JSON.toJSONString(serverErrResult);
        } else {
            OkResult okResult = new OkResult("登陆成功！", blogPersonalInform);
            okResult.setToken(JwtTokenUtils.TOKEN_PREFIX + token);
            s = JSON.toJSONString(okResult);
        }
        PrintWriter out = response.getWriter();


        System.out.println("s:"+s);
        out.write(s);
        out.flush();
        out.close();
    }
    //配置失败返回的信息，当然成功的也可以返回思路一样，重写successful，这里我就不写了
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
	    System.out.println("用户登陆失败  AjaxAuthFailHandler");
	    response.setContentType("application/json;charset=utf-8");
//	    response.setStatus(HttpStatus.s.value());
	    PrintWriter out = response.getWriter();
        ServerErrResult serverErrResult = new ServerErrResult("请检查邮箱、密码是否正确");
        String s = JSON.toJSONString(serverErrResult);
        System.out.println("s:"+s);
        out.write(s);
	    out.flush();
	    out.close();
    }
	
	
}