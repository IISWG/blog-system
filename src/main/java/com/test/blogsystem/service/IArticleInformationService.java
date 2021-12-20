package com.test.blogsystem.service;

import com.test.blogsystem.entity.ArticleInformation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.test.blogsystem.entity.param.ArticleParam;
import com.test.blogsystem.entity.param.TypeParam;
import com.test.blogsystem.entity.result.PageResult;

import java.util.List;

/**
 * <p>
 * 文章信息 服务类
 * </p>
 *
 * @author 11903990213李列伟
 * @since 2021-11-25
 */
public interface IArticleInformationService extends IService<ArticleInformation> {
    public boolean getBlog(String accountNumber,String password);

    /**
     * 通过一系列条件查询出想要的文章
     * @param articleParam
     * @return
     */
    PageResult select(ArticleParam articleParam,Long id);

    /**
     * 插入一条文章信息
     * @param articleInformation
     * @return
     */
    Integer insertArticle(ArticleInformation articleInformation);
    /**
     * 更新文章信息
     * @param articleInformation
     * @return
     */
    Integer updateArticle(ArticleInformation articleInformation);
    /**
     * 删除文章信息
     * @param id
     * @return
     */
    Integer deleteArticle(Long id);
    /**
     * 改变文章开放状态
     * @param id
     * @return
     */
    boolean changeArticleOpen(Long id,Integer isOpen);

    /**
     * 通过文章类别查询
     * @param
     * @return
     */
    public PageResult selectByType(TypeParam typeParam);

}
