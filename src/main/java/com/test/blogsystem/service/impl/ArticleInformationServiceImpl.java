package com.test.blogsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.blogsystem.entity.ArticleInformation;
import com.test.blogsystem.entity.ArticleLabel;
import com.test.blogsystem.entity.BlogPersonalInform;
import com.test.blogsystem.entity.param.ArticleParam;
import com.test.blogsystem.entity.param.TypeParam;
import com.test.blogsystem.entity.result.PageResult;
import com.test.blogsystem.entity.result.ServerErrResult;
import com.test.blogsystem.mapper.ArticleInformationMapper;
import com.test.blogsystem.mapper.ArticleLabelMapper;
import com.test.blogsystem.mapper.BlogPersonalInformMapper;
import com.test.blogsystem.service.IArticleInformationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 文章信息 服务实现类
 * </p>
 *
 * @author 11903990213李列伟
 * @since 2021-11-25
 */
@Service
public class ArticleInformationServiceImpl extends ServiceImpl<ArticleInformationMapper, ArticleInformation> implements IArticleInformationService {
    @Resource
    BlogPersonalInformMapper blogPersonalInformMapper;
    @Resource
    ArticleInformationMapper articleInformationMapper;
    @Resource
    ArticleLabelMapper articleLabelMapper;
    @Override
    public boolean getBlog(String accountNumber, String password) {

        BlogPersonalInform blogPersonalInformByPhone = blogPersonalInformMapper.selectOne(new QueryWrapper<BlogPersonalInform>().eq("phone_number", accountNumber).eq("password", password));
        BlogPersonalInform blogPersonalInformByMailbox = blogPersonalInformMapper.selectOne(new QueryWrapper<BlogPersonalInform>().eq("mailbox", accountNumber).eq("password", password));
        return blogPersonalInformByMailbox != null || blogPersonalInformByPhone != null;
    }

    @Override
    public PageResult select(ArticleParam articleParam, Long id) {
       //articleInformationMapper.selectList(new QueryWrapper<ArticleInformation>())
        if (articleParam.getOrder() != null) {
            switch (articleParam.getOrder()){
                case "createTime":
                { articleParam.setOrder("create_time");break; }
                case "latestUpdateTime":
                { articleParam.setOrder("latest_update_time");break; }
                default:
                {
                    articleParam.setOrder("create_time");
                    break;
                }
            }
        }

        QueryWrapper<ArticleInformation> articleInformationQueryWrapper = new QueryWrapper<>();
        if (articleParam.getType() != null && !"".equals(articleParam.getType())) {
            articleInformationQueryWrapper.eq("article_type", articleParam.getType());
        }
        if (id != null) {
            articleInformationQueryWrapper.eq("blog_id", id);
        }
        if (articleParam.getLike() != null && !"".equals(articleParam.getLike())) {
            StringBuilder sql = new StringBuilder();
            int i;
            for (i = 0; i < articleParam.getLike().length() - 1; i++) {
                sql.append(articleParam.getLike().charAt(i)).append("%");
            }
            sql.append(articleParam.getLike().charAt(i));
            String finalSql = sql.toString();
            articleInformationQueryWrapper.and(
                    wrapper -> wrapper.like("article_title", finalSql).or().like("brief_description",finalSql)
            );
        }
        if (articleParam.isAsc()) {
            if(articleParam.getOrder() !=null && !"".equals(articleParam.getOrder()))
            {
                articleInformationQueryWrapper.orderByAsc(articleParam.getOrder());
            }
        }
        else {
            if(articleParam.getOrder() !=null && !"".equals(articleParam.getOrder()))
            {
                articleInformationQueryWrapper.orderByDesc(articleParam.getOrder());
            }
        }
        PageResult pageResult = new PageResult();
        Page<ArticleInformation> page = new Page<>(articleParam.getPageNum(), articleParam.getPageSize());
        IPage<ArticleInformation> selectPage = articleInformationMapper.selectPage(page, articleInformationQueryWrapper);
        pageResult.setPages(selectPage.getPages());
        pageResult.setTotal(selectPage.getTotal());
        List<ArticleInformation> records = selectPage.getRecords();
        for (ArticleInformation record : records) {
            List<ArticleLabel> articleLabelList = articleLabelMapper.selectList(new QueryWrapper<ArticleLabel>().eq("article_id", record.getId()));
            record.setLabelList(articleLabelList);
        }
        pageResult.setRecords(records);
        return pageResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer insertArticle(ArticleInformation articleInformation) {
        System.out.println("articleInformation:"+articleInformation);
        Long id = articleInformationMapper.insertArticleInformation(articleInformation);
        System.out.println("insertArticleInformation:"+articleInformation);
        Long articleId = articleInformation.getId();
        if (id != 0) {
            List<String> articleLabelList = articleInformation.getArticleLabelList();
            if (articleLabelList != null && articleLabelList.size() != 0) {
                for (String articleLabel : articleLabelList) {
                    ArticleLabel articleLabel1 = new ArticleLabel();
                    articleLabel1.setLabelContent(articleLabel).setArticleId(articleId);
                    int insert = articleLabelMapper.insert(articleLabel1);
                    if (insert == 0) {
                        log.error("添加标签失败！");
                        throw new RuntimeException("添加标签失败！");
                    }
                }
            }

        } else {
            throw new RuntimeException("添加文章失败！");
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateArticle(ArticleInformation articleInformation) {
        int deleteByArticleId = articleLabelMapper.delete(new QueryWrapper<ArticleLabel>().eq("article_id", articleInformation.getId()));
        Long id = articleInformation.getId();
        articleInformation.setLatestUpdateTime(LocalDateTime.now());
        List<String> articleLabelList = articleInformation.getArticleLabelList();
        if (articleLabelList != null && articleLabelList.size() != 0) {
            for (String articleLabel : articleLabelList) {
                ArticleLabel articleLabel1 = new ArticleLabel();
                articleLabel1.setLabelContent(articleLabel).setArticleId(id);
                int insert = articleLabelMapper.insert(articleLabel1);
                if (insert == 0) {
                    throw new RuntimeException("更新添加标签失败！");
                }
            }
        }
        int updateById = articleInformationMapper.updateById(articleInformation);
        if (updateById == 0) {
            throw new RuntimeException("更新文章失败！");
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteArticle(Long id) {
        try {
            int deleteLabelByArticleId = articleLabelMapper.delete(new QueryWrapper<ArticleLabel>().eq("article_id", id));
            int deleteArticleByArticleId = articleInformationMapper.deleteById(id);
            return deleteArticleByArticleId;
        } catch (Exception e) {
            log.error("删除文章失败",e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return 0;
        }
    }

    @Override
    public boolean changeArticleOpen(Long id, Integer isOpen) {
        return articleInformationMapper.updateIsOpenByIdAfter(id, isOpen);
    }

    @Override
    public PageResult selectByType(TypeParam typeParam) {
        QueryWrapper<ArticleInformation> articleInformationQueryWrapper = new QueryWrapper<>();
        articleInformationQueryWrapper.eq("article_type", typeParam.getArticleType());
        PageResult pageResult = new PageResult();
        Page<ArticleInformation> page = new Page<>(typeParam.getPageNum(), typeParam.getPageSize());
        IPage<ArticleInformation> selectPage = articleInformationMapper.selectPage(page, articleInformationQueryWrapper);
        pageResult.setPages(selectPage.getPages());
        pageResult.setTotal(selectPage.getTotal());
        List<ArticleInformation> records = selectPage.getRecords();
        for (ArticleInformation record : records) {
            List<ArticleLabel> articleLabelList = articleLabelMapper.selectList(new QueryWrapper<ArticleLabel>().eq("article_id", record.getId()));
            record.setLabelList(articleLabelList);
        }
        pageResult.setRecords(records);
        return pageResult;
    }

}
