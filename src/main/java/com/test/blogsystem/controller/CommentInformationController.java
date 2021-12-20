package com.test.blogsystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.test.blogsystem.entity.ArticleInformation;
import com.test.blogsystem.entity.BlogPersonalInform;
import com.test.blogsystem.entity.CommentInformation;
import com.test.blogsystem.entity.param.ArticleParam;
import com.test.blogsystem.entity.param.BaseParam;
import com.test.blogsystem.entity.result.BaseResult;
import com.test.blogsystem.entity.result.OkResult;
import com.test.blogsystem.entity.result.PageResult;
import com.test.blogsystem.entity.result.ServerErrResult;
import com.test.blogsystem.mapper.BlogPersonalInformMapper;
import com.test.blogsystem.mapper.CommentInformationMapper;
import com.test.blogsystem.service.impl.CommentInformationServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 评论信息 前端控制器
 * </p>
 *
 * @author 11903990213李列伟
 * @since 2021-11-25
 */
@RestController
@RequestMapping("/commentInformation")
@Log4j
public class CommentInformationController {
    @Resource
    CommentInformationMapper commentInformationMapper;
    @Resource
    BlogPersonalInformMapper blogPersonalInformMapper;
    @Resource
    CommentInformationServiceImpl commentInformationService;
    /**
     * 后台管理接口
     */

    /**
     * 通过各种排序条件模糊查询接口
     * @return BaseResult
     */
    @ApiOperation(value = "通过各种排序条件模糊查询接口", notes = "")
    @GetMapping("/getcommentInformation")
    public BaseResult getcommentInformation(BaseParam baseParam) {

        PageResult commentInformationList = commentInformationService.getcommentInformation(baseParam);
        return new OkResult(commentInformationList);
    }

    /**
     * 插入评论
     * @return BaseResult
     */
    @ApiOperation(value = "插入评论", notes = "")
    @PostMapping("/insertComment")
    @Transactional(rollbackFor = Exception.class)
    public BaseResult insertComment(@RequestBody CommentInformation commentInformation){
        System.out.println(commentInformation);
        try {
            commentInformation.setCommentTime(LocalDateTime.now());
            int insert = commentInformationMapper.insertByComment(commentInformation);
            if (insert == 0) {
                throw new Exception("评论失败");
            }
            return new OkResult("评论成功！", "");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.error("评论失败！");
            return new ServerErrResult("评论失败！");
        }

    }
    /**
     * 获取文章评论
     * @return BaseResult
     */
    @ApiOperation(value = "获取文章评论", notes = "")
    @GetMapping("/getCommentsById")
    public BaseResult getCommentsById(Long id){
        System.out.println(id);
        try {

            List<CommentInformation> commentInformations = commentInformationMapper.selectList(new QueryWrapper<CommentInformation>().eq("article_id", id).eq("comment_level", 1));
//            if (commentInformations != null ) {
//                throw new Exception("获取文章评论失败");
//            }
            for (CommentInformation commentInformation : commentInformations) {
                BlogPersonalInform blogPersonalInform = blogPersonalInformMapper.selectById(commentInformation.getCommentBlogId());
                commentInformation.setNickname(blogPersonalInform.getNickname()).setAvatar(blogPersonalInform.getHeadPortrait());

                List<CommentInformation> commentInformationList = commentInformationMapper.selectList(new QueryWrapper<CommentInformation>().eq("article_id", id).eq("comment_level", 2).eq("comment_id", commentInformation.getId()).orderByAsc("comment_time"));
                if (commentInformationList != null && commentInformationList.size() != 0) {
                    for (CommentInformation information : commentInformationList) {
                        BlogPersonalInform blogPersonalInform1 = blogPersonalInformMapper.selectById(information.getCommentBlogId());
                        BlogPersonalInform toblogPersonalInform = blogPersonalInformMapper.selectById(information.getToId());
                        information.setNickname(blogPersonalInform1.getNickname()).setAvatar(blogPersonalInform1.getHeadPortrait()).setToPerName(toblogPersonalInform.getNickname());

                    }
                }
                commentInformation.setCommentInformations(commentInformationList);

            }

            return new OkResult("获取文章评论成功！", commentInformations);
        } catch (Exception e) {
            log.error("获取文章评论失败！");
            return new ServerErrResult("获取文章评论失败！");
        }

    }
    /**
     * 删除评论
     * @return
     */
    @ApiOperation(value = "删除评论", notes = "")
    @PostMapping("/deleteCommentById")
    @Transactional(rollbackFor = Exception.class)
    public BaseResult deleteCommentById(Long id) {
        System.out.println(id);
        try {
            int updateById = commentInformationMapper.deleteById(id);
            if (updateById == 0) {
                throw new Exception("删除评论失败！");
            }
            return new OkResult();
        } catch (Exception e) {
            log.error("删除评论失败",e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ServerErrResult("删除评论失败");
        }
    }
}
