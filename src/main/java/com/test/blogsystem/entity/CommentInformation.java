package com.test.blogsystem.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 评论信息
 * </p>
 *
 * @author 11903990213李列伟
 * @since 2021-11-25
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("comment_information")
@ApiModel(value="CommentInformation对象", description="评论信息")
public class CommentInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "评论内容")
    @TableField("comment_content")
    private String commentContent;

    @ApiModelProperty(value = "评论回复时间，")
    @TableField("comment_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime commentTime;

    @ApiModelProperty(value = "评论回复人id")
    @TableField("comment_blog_id")
    private Long commentBlogId;

    @ApiModelProperty(value = "评论文章id")
    @TableField("article_id")
    private Long articleId;

    @ApiModelProperty(value = "对那条评论回复回复id")
    @TableField("comment_id")
    private Long commentId;

    @ApiModelProperty(value = "对谁评论回复")
    @TableField("to_id")
    private Long toId;

    @ApiModelProperty(value = "是评论还是回复（1表示评论，2表示回复）")
    @TableField("comment_level")
    private Integer commentLevel;

    @ApiModelProperty(value = "评论人头像",name = "avatar")
    @TableField(exist = false)
    private String avatar;

    @ApiModelProperty(value = "评论人名称",name = "nickname")
    @TableField(exist = false)
    private String nickname;

    @ApiModelProperty(value = "对谁评论人的名称",name = "toPerName")
    @TableField(exist = false)
    private String toPerName;

    @ApiModelProperty(value = "评论人的邮箱",name = "email")
    @TableField(exist = false)
    private String email;

    @ApiModelProperty(value = "该评论下的回复",name = "commentInformations")
    @TableField(exist = false)
    private List<CommentInformation> commentInformations;
}
