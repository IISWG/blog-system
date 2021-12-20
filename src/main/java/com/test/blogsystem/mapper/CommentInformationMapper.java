package com.test.blogsystem.mapper;

import com.test.blogsystem.entity.CommentInformation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 评论信息 Mapper 接口
 * </p>
 *
 * @author 11903990213李列伟
 * @since 2021-11-25
 */
public interface CommentInformationMapper extends BaseMapper<CommentInformation> {
    public Integer insertByComment(CommentInformation commentInformation);


}
