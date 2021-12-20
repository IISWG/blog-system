package com.test.blogsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.blogsystem.entity.ArticleTypes;
import com.test.blogsystem.entity.param.PageParam;
import com.test.blogsystem.entity.result.PageResult;
import com.test.blogsystem.mapper.ArticleTypesMapper;
import com.test.blogsystem.service.IArticleTypesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 11903990213李列伟
 * @since 2021-11-27
 */
@Service
public class ArticleTypesServiceImpl extends ServiceImpl<ArticleTypesMapper, ArticleTypes> implements IArticleTypesService {
    @Resource
    ArticleTypesMapper articleTypesMapper;
    @Override
    public PageResult select(PageParam pageParam) {
        QueryWrapper<ArticleTypes> articleTypesQueryWrapper = new QueryWrapper<ArticleTypes>().orderByAsc("id");
        PageResult pageResult = new PageResult();
        Page<ArticleTypes> articleTypesPage = new Page<>(pageParam.getPageNum(), pageParam.getPageSize());
        IPage<ArticleTypes> selectPage = articleTypesMapper.selectPage(articleTypesPage, articleTypesQueryWrapper);
        pageResult.setPages(selectPage.getPages());
        pageResult.setTotal(selectPage.getTotal());
        pageResult.setRecords(selectPage.getRecords());
        return pageResult;
    }
}
