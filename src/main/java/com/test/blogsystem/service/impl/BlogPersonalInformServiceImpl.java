package com.test.blogsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.blogsystem.entity.BlogPersonalInform;
import com.test.blogsystem.entity.CommentInformation;
import com.test.blogsystem.entity.JwtUser;
import com.test.blogsystem.entity.param.BaseParam;
import com.test.blogsystem.entity.result.PageResult;
import com.test.blogsystem.mapper.BlogPersonalInformMapper;
import com.test.blogsystem.service.IBlogPersonalInformService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 博客基本信息 服务实现类
 * </p>
 *
 * @author 11903990213李列伟
 * @since 2021-11-25
 */
@Service
public class BlogPersonalInformServiceImpl extends ServiceImpl<BlogPersonalInformMapper, BlogPersonalInform> implements IBlogPersonalInformService, UserDetailsService {
    @Resource
    BlogPersonalInformMapper blogPersonalInformMapper;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        System.out.println("s:"+s);
        JwtUser jwtUser = null;
        BlogPersonalInform blogPersonalInform = blogPersonalInformMapper.selectOne(new QueryWrapper<BlogPersonalInform>().eq("mailbox", s));
        System.out.println("blogPersonalInform:"+blogPersonalInform);
        if (blogPersonalInform == null) {

            UsernameNotFoundException usernameNotFoundException = new UsernameNotFoundException("没有该邮箱的账号！");
            log.error("没有该邮箱的账号!",usernameNotFoundException);
            throw usernameNotFoundException;
        }
        return new JwtUser(blogPersonalInform);
    }

    @Override
    public PageResult getBlogPersonal(BaseParam baseParam) {
        QueryWrapper<BlogPersonalInform> blogPersonalInformQueryWrapper = new QueryWrapper<BlogPersonalInform>().ne("role","root");
        PageResult pageResult = new PageResult();
        Page<BlogPersonalInform> page = new Page<>(baseParam.getPageNum(), baseParam.getPageSize());
        IPage<BlogPersonalInform> selectPage = blogPersonalInformMapper.selectPage(page, blogPersonalInformQueryWrapper);
        pageResult.setPages(selectPage.getPages());
        pageResult.setTotal(selectPage.getTotal());
        List<BlogPersonalInform> records = selectPage.getRecords();
//        for (BlogPersonalInform record : records) {
//            BlogPersonalInform blogPersonalInform = blogPersonalInformMapper.selectById(record.getCommentBlogId());
//            record.setNickname(blogPersonalInform.getNickname())
//                    .setAvatar(blogPersonalInform.getHeadPortrait())
//                    .setEmail(blogPersonalInform.getMailbox());
//        }
        pageResult.setRecords(records);
        return pageResult;
    }
}
