package com.test.blogsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.test.blogsystem.entity.ArticleInformation;
import com.test.blogsystem.entity.ArticleLabel;
import com.test.blogsystem.entity.ArticleTypes;
import com.test.blogsystem.entity.param.ArticleParam;
import com.test.blogsystem.entity.param.TypeParam;
import com.test.blogsystem.entity.result.BaseResult;
import com.test.blogsystem.entity.result.OkResult;
import com.test.blogsystem.entity.result.PageResult;
import com.test.blogsystem.entity.result.ServerErrResult;
import com.test.blogsystem.mapper.ArticleLabelMapper;
import com.test.blogsystem.mapper.ArticleTypesMapper;
import com.test.blogsystem.service.impl.ArticleInformationServiceImpl;
import com.test.blogsystem.service.impl.ArticleTypesServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 文章信息 前端控制器
 * </p>
 *
 * @author 11903990213李列伟
 * @since 2021-11-25
 */
@RestController
@Slf4j
@RequestMapping("/articleInformation")
public class ArticleInformationController {
    @Resource
    ArticleInformationServiceImpl articleInformationService;
    @Resource
    ArticleTypesServiceImpl articleTypesService;
    @Resource
    ArticleLabelMapper articleLabelMapper;
    /**
     * 通过各种排序条件模糊查询接口
     * @return BaseResult
     */
    @ApiOperation(value = "通过各种排序条件模糊查询接口", notes = "")
    @GetMapping("/getArticleInformation")
    public BaseResult getArticleInformation(ArticleParam articleParam,Long id) {

        PageResult articleList = articleInformationService.select(articleParam,id);
        List<ArticleInformation> records = (List<ArticleInformation>) articleList.getRecords();
        for (ArticleInformation record : records) {
            record.setArticleContent(null).setBriefDescription(null);
        }

        return new OkResult(articleList);
    }

    /**
     * 通过文章id查询接口
     * @return BaseResult
     */
    @ApiOperation(value = "通过文章id查询接口", notes = "")
    @GetMapping("/getArticleInformationById")
    public BaseResult getArticleInformationById(Long id) {
        ArticleInformation articleInformation = articleInformationService.getById(id);
        List<ArticleLabel> articleLabelList = articleLabelMapper.selectList(new QueryWrapper<ArticleLabel>().eq("article_id", id));
        ArrayList<String> list = new ArrayList<>();
        for (ArticleLabel articleLabel : articleLabelList) {
            list.add(articleLabel.getLabelContent());
        }
        articleInformation.setLabelList(articleLabelList);
        articleInformation.setArticleLabelList(list);
        return new OkResult(articleInformation);
    }

    /**
     * 插入文章
     * @return BaseResult
     */
    @ApiOperation(value = "插入文章", notes = "")
    @PostMapping("/insertArticle")
    @Transactional(rollbackFor = Exception.class)
    public BaseResult insertArticle(@RequestBody ArticleInformation articleInformation) {
        System.out.println("articleInformation:"+articleInformation);
        LocalDateTime now = LocalDateTime.now();
        articleInformation.setCreateTime(now).setLatestUpdateTime(now);
        try {
            Integer insertNumber = articleInformationService.insertArticle(articleInformation);
//            if (insertNumber == 0) {
//                throw new RuntimeException("插入文章失败！");
//            }
            return new OkResult("");
        } catch (Exception e) {
            log.error("插入文章失败",e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ServerErrResult("插入文章失败");
        }
    }
    /**
     * 插入文章
     * @return BaseResult
     */
    @ApiOperation(value = "更新文章", notes = "")
    @PostMapping("/updateArticle")
    @Transactional(rollbackFor = Exception.class)
    public BaseResult updateArticle(@RequestBody ArticleInformation articleInformation) {
        System.out.println(articleInformation);
        try {
            Integer updateNumber = articleInformationService.updateArticle(articleInformation);
            if (updateNumber == 0) {
                throw new RuntimeException("更新文章失败！");
            }
            return new OkResult("");
        } catch (Exception e) {
            log.error("更新文章失败",e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ServerErrResult("更新文章失败");
        }
    }

    /**
     * 删除文章
     * @return id
     */
    @ApiOperation(value = "删除文章", notes = "")
    @PostMapping("/deleteArticle")
    @Transactional(rollbackFor = Exception.class)
    public BaseResult deleteArticle(Long id) {
        System.out.println(id);
        try {
            Integer deleteArticleNumber = articleInformationService.deleteArticle(id);
            if (deleteArticleNumber == 0) {
                throw new RuntimeException("删除文章失败！");
            }
            return new OkResult("");
        } catch (Exception e) {
            log.error("删除文章失败",e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ServerErrResult("删除文章失败");
        }
    }
    /**
     * 改变文章开放状态
     * @return id
     */
    @ApiOperation(value = "改变文章开放状态", notes = "")
    @PostMapping("/changeArticleOpen")
    @Transactional(rollbackFor = Exception.class)
    public BaseResult changeArticleOpen(Long id) {
        System.out.println(id);
        ArticleInformation articleInformation = articleInformationService.getById(id);
        int isOpen=0;
        if (articleInformation.getIsOpen() == 0) {
            isOpen = 1;
        }
        try {
            boolean changeArticleOpen = articleInformationService.changeArticleOpen(id, isOpen);
            if (!changeArticleOpen) {
                throw new RuntimeException("改变文章开放状态失败！");
            }
            return new OkResult("");
        } catch (Exception e) {
            log.error("改变文章开放状态失败",e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ServerErrResult("改变文章开放状态失败");
        }
    }

/**
 * 以下是展示接口
 */
    /**
     * 通过各种排序条件模糊查询接口
     * @return BaseResult
     */
    @ApiOperation(value = "通过各种排序条件模糊查询接口", notes = "")
    @GetMapping("/view/getArticleInformation")
    public BaseResult getViewArticleInformation(ArticleParam articleParam) {
        PageResult articleList = articleInformationService.select(articleParam,null);
        List<ArticleInformation> records = (List<ArticleInformation>) articleList.getRecords();
        return new OkResult(articleList);
    }
    /**
     * 通过文章类别查询
     * @return BaseResult
     */
    @ApiOperation(value = "通过文章类别查询", notes = "")
    @GetMapping("/view/getArticleInformationByType")
    public BaseResult getArticleInformationByType(TypeParam typeParam) {
        PageResult articleList = articleInformationService.selectByType(typeParam);
        List<ArticleInformation> records = (List<ArticleInformation>) articleList.getRecords();
        return new OkResult(articleList);
    }
}
