package com.test.blogsystem.service;

import com.test.blogsystem.entity.ArticleTypes;
import com.baomidou.mybatisplus.extension.service.IService;
import com.test.blogsystem.entity.param.ArticleParam;
import com.test.blogsystem.entity.param.PageParam;
import com.test.blogsystem.entity.result.PageResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 11903990213李列伟
 * @since 2021-11-27
 */
public interface IArticleTypesService extends IService<ArticleTypes> {
    PageResult select(PageParam pageParam);
}
