package com.test.blogsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.blogsystem.entity.ArticleInformation;
import com.test.blogsystem.entity.BlogPersonalInform;
import com.test.blogsystem.entity.CommentInformation;
import com.test.blogsystem.entity.param.BaseParam;
import com.test.blogsystem.entity.result.BaseResult;
import com.test.blogsystem.entity.result.PageResult;
import com.test.blogsystem.mapper.BlogPersonalInformMapper;
import com.test.blogsystem.mapper.CommentInformationMapper;
import com.test.blogsystem.service.ICommentInformationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 评论信息 服务实现类
 * </p>
 *
 * @author 11903990213李列伟
 * @since 2021-11-25
 */
@Service
public class CommentInformationServiceImpl extends ServiceImpl<CommentInformationMapper, CommentInformation> implements ICommentInformationService {
    @Resource
    CommentInformationMapper commentInformationMapper;
    @Resource
    BlogPersonalInformMapper blogPersonalInformMapper;
    @Override
    public PageResult getcommentInformation(BaseParam baseParam) {
        QueryWrapper<CommentInformation> commentInformationQueryWrapper = new QueryWrapper<>();
        PageResult pageResult = new PageResult();
        Page<CommentInformation> page = new Page<>(baseParam.getPageNum(), baseParam.getPageSize());
        IPage<CommentInformation> selectPage = commentInformationMapper.selectPage(page, commentInformationQueryWrapper);
        pageResult.setPages(selectPage.getPages());
        pageResult.setTotal(selectPage.getTotal());
        List<CommentInformation> records = selectPage.getRecords();
        for (CommentInformation record : records) {
            BlogPersonalInform blogPersonalInform = blogPersonalInformMapper.selectById(record.getCommentBlogId());
            record.setNickname(blogPersonalInform.getNickname())
                    .setAvatar(blogPersonalInform.getHeadPortrait())
                    .setEmail(blogPersonalInform.getMailbox());
        }
        pageResult.setRecords(records);
        return pageResult;
    }
}
