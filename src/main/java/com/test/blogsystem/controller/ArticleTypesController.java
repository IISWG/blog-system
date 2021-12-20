package com.test.blogsystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.test.blogsystem.entity.ArticleInformation;
import com.test.blogsystem.entity.ArticleTypes;
import com.test.blogsystem.entity.param.ArticleParam;
import com.test.blogsystem.entity.param.PageParam;
import com.test.blogsystem.entity.result.BaseResult;
import com.test.blogsystem.entity.result.OkResult;
import com.test.blogsystem.entity.result.PageResult;
import com.test.blogsystem.entity.result.ServerErrResult;
import com.test.blogsystem.mapper.ArticleTypesMapper;
import com.test.blogsystem.service.impl.ArticleTypesServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 11903990213李列伟
 * @since 2021-11-27
 */
@RestController
@RequestMapping("/articleTypes")
@Log4j
public class ArticleTypesController {
    @Resource
    ArticleTypesMapper articleTypesMapper;
    @Resource
    ArticleTypesServiceImpl articleTypesService;
    /**
     * 博客后台管理相关接口
     */
    /**
     * 通过各种排序条件模糊查询接口
     * @return BaseResult
     */
    @ApiOperation(value = "通过各种排序条件模糊查询接口", notes = "")
    @GetMapping("/getArticleType")
    public BaseResult getArticleType(PageParam pageParam) {
//        List<ArticleTypes> articleTypesList = articleTypesMapper.selectList(new QueryWrapper<ArticleTypes>().orderByAsc("id"));
        PageResult select = articleTypesService.select(pageParam);
        return new OkResult(select);
    }

    @ApiOperation(value = "all", notes = "")
    @GetMapping("/getTypeAll")
    public BaseResult getTypeAll() {
        List<ArticleTypes> articleTypesList = articleTypesMapper.selectList(new QueryWrapper<ArticleTypes>().orderByAsc("id"));
        return new OkResult(articleTypesList);
    }
    /**
     * 添加文章类别
     * @return
     */
    @ApiOperation(value = "添加文章类别", notes = "")
    @PostMapping("/insertArticleType")
    @Transactional(rollbackFor = Exception.class)
    public BaseResult insertArticleType(@RequestBody ArticleTypes articleTypes) {
        System.out.println(articleTypes);
        try {
            int insert = articleTypesMapper.insert(articleTypes);
            if (insert == 0) {
                throw new Exception("添加文章类别失败！");
            }
            return new OkResult();
        } catch (Exception e) {
            log.error("添加文章类别失败",e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ServerErrResult("添加文章类别失败");
        }
    }
    /**
     * 添加文章类别
     * @return
     */
    @ApiOperation(value = "更新文章类别", notes = "")
    @PostMapping("/updateArticleType")
    @Transactional(rollbackFor = Exception.class)
    public BaseResult updateArticleType(@RequestBody ArticleTypes articleTypes) {
        System.out.println(articleTypes);
        try {
            int updateById = articleTypesMapper.updateById(articleTypes);
            if (updateById == 0) {
                throw new Exception("更新文章类别失败！");
            }
            return new OkResult();
        } catch (Exception e) {
            log.error("更新文章类别失败",e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ServerErrResult("更新文章类别失败");
        }
    }
    /**
     * 删除文章类别
     * @return
     */
    @ApiOperation(value = "删除文章类别", notes = "")
    @GetMapping("/deleteArticleType")
    @Transactional(rollbackFor = Exception.class)
    public BaseResult deleteArticleType(Integer id) {
        System.out.println(id);
        try {
            int updateById = articleTypesMapper.deleteById(id);
            if (updateById == 0) {
                throw new Exception("删除文章类别失败！");
            }
            return new OkResult();
        } catch (Exception e) {
            log.error("删除文章类别失败",e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ServerErrResult("删除文章类别失败");
        }
    }

    /**
     * 博客展示端相关接口
     */
}
