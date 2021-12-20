package com.test.blogsystem.controller;


import com.test.blogsystem.config.MyAuthenticationFailureHandler;
import com.test.blogsystem.entity.ArticleInformation;
import com.test.blogsystem.entity.BlogPersonalInform;
import com.test.blogsystem.entity.VerifyCode;
import com.test.blogsystem.entity.param.BaseParam;
import com.test.blogsystem.entity.result.BaseResult;
import com.test.blogsystem.entity.result.OkResult;
import com.test.blogsystem.entity.result.PageResult;
import com.test.blogsystem.entity.result.ServerErrResult;
import com.test.blogsystem.mapper.BlogPersonalInformMapper;
import com.test.blogsystem.mapper.CommentInformationMapper;
import com.test.blogsystem.service.IVerifyCodeGen;
import com.test.blogsystem.service.impl.ArticleInformationServiceImpl;

import com.test.blogsystem.service.impl.BlogPersonalInformServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * <p>
 * 博客基本信息 前端控制器
 * </p>
 *
 * @author 11903990213李列伟
 * @since 2021-11-25
 */
@RestController
@Slf4j
@RequestMapping("/blogPersonalInform")
public class BlogPersonalInformController {
    @Resource
    ArticleInformationServiceImpl articleInformationService;
    @Resource
    MyAuthenticationFailureHandler myAuthenticationFailureHandler;
    @Resource
    BlogPersonalInformServiceImpl blogPersonalInformService;
    @Resource
    BlogPersonalInformMapper blogPersonalInformMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     * 插入用户
     * @return BaseResult
     */
    @ApiOperation(value = "插入用户", notes = "")
    @PostMapping("/insertBlogPersonalInform")
    @Transactional(rollbackFor = Exception.class)
    public BaseResult insertBlogPersonalInform(@RequestBody BlogPersonalInform blogPersonalInform){
        System.out.println(blogPersonalInform);
        try {
            blogPersonalInform.setPassword(passwordEncoder.encode(blogPersonalInform.getPassword()))
                                .setCreateTime(LocalDateTime.now())
                                .setUpdateTime(LocalDateTime.now());
            System.out.println(blogPersonalInform);
            int insert = blogPersonalInformMapper.insert(blogPersonalInform);
            if (insert == 0) {
                throw new Exception("插入用户失败");
            }
            return new OkResult("插入成功！", "");
        } catch (Exception e) {
            log.error("插入失败！");
            return new ServerErrResult("插入用户失败！");
        }

    }
    /**
     * 更新用户信息
     * @return BaseResult
     */
    @ApiOperation(value = "更新用户信息", notes = "")
    @PostMapping("/updateBlogPersonalInform")
    @Transactional(rollbackFor = Exception.class)
    public BaseResult updateBlogPersonalInform(@RequestBody BlogPersonalInform blogPersonalInform){
        System.out.println(blogPersonalInform);
        try {
            BlogPersonalInform blogPersonalInformData = blogPersonalInformMapper.selectById(blogPersonalInform.getId());
            blogPersonalInform.setUpdateTime(LocalDateTime.now());
            if (!blogPersonalInform.getPassword().equals(blogPersonalInformData.getPassword())) {
                blogPersonalInform.setPassword(passwordEncoder.encode(blogPersonalInform.getPassword()));
            }
            System.out.println(blogPersonalInform);
            int updateById = blogPersonalInformMapper.updateById(blogPersonalInform);
            if (updateById == 0) {
                throw new Exception("更新用户信息失败");
            }
            return new OkResult("更新用户信息成功！", "");
        } catch (Exception e) {
            log.error("更新用户信息失败！");
            return new ServerErrResult("更新用户信息失败！");
        }

    }
    /**
     * 通过各种排序条件模糊查询接口
     * @return BaseResult
     */
    @ApiOperation(value = "通过各种排序条件模糊查询接口", notes = "")
    @GetMapping("/getBlogPersonal")
    public BaseResult getBlogPersonal(BaseParam baseParam) {
        PageResult blogPersonalList = blogPersonalInformService.getBlogPersonal(baseParam);
        return new OkResult(blogPersonalList);
    }

    /**
     * 删除用户
     * @return id
     */
    @ApiOperation(value = "删除用户", notes = "")
    @PostMapping("/deleteBlogUser")
    @Transactional(rollbackFor = Exception.class)
    public BaseResult deleteBlogUser(Long id) {
        System.out.println(id);
        try {
            boolean removeUserById = blogPersonalInformService.removeById(id);
            if (!removeUserById) {
                throw new RuntimeException("删除用户失败！");
            }
            return new OkResult("删除用户成功","");
        } catch (Exception e) {
            log.error("删除用户失败",e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ServerErrResult("删除用户失败");
        }
    }
    /**
     * 改变用户状态
     * @return id
     */
    @ApiOperation(value = "改变用户状态", notes = "")
    @PostMapping("/changeState")
    @Transactional(rollbackFor = Exception.class)
    public BaseResult changeState(Long id) {
        System.out.println(id);
        try {
            BlogPersonalInform getBlogPersonalbyId = blogPersonalInformService.getById(id);

            if (getBlogPersonalbyId.getState() == 1) {
                getBlogPersonalbyId.setState(0);
            } else {
                getBlogPersonalbyId.setState(1);
            }
            boolean b = blogPersonalInformService.updateById(getBlogPersonalbyId);
            if (!b) {
                throw new RuntimeException("改变用户状态!");
            }
            return new OkResult("改变用户状态成功!","");
        } catch (Exception e) {
            log.error("改变用户状态失败!",e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ServerErrResult("改变用户状态失败!");
        }
    }


}
