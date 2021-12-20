package com.test.blogsystem.service.impl;

import com.test.blogsystem.entity.ArticleLabel;
import com.test.blogsystem.mapper.ArticleLabelMapper;
import com.test.blogsystem.service.IArticleLabelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章标签 服务实现类
 * </p>
 *
 * @author 11903990213李列伟
 * @since 2021-11-25
 */
@Service
public class ArticleLabelServiceImpl extends ServiceImpl<ArticleLabelMapper, ArticleLabel> implements IArticleLabelService {

}
