package com.test.blogsystem.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.test.blogsystem.entity.ArticleInformation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 文章信息 Mapper 接口
 * </p>
 *
 * @author 11903990213李列伟
 * @since 2021-11-25
 */
public interface ArticleInformationMapper extends BaseMapper<ArticleInformation> {
   // public boolean getBlog(String accountNumber, String password);

    Long insertArticleInformation(ArticleInformation articleInformation);

    boolean updateIsOpenByIdAfter(Long id, Integer updateIsOpen);
}
