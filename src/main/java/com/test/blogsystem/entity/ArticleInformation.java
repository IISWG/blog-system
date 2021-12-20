package com.test.blogsystem.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.time.LocalDateTime;
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
 * 文章信息
 * </p>
 *
 * @author 11903990213李列伟
 * @since 2021-11-25
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("article_information")
@ApiModel(value = "ArticleInformation对象", description = "文章信息")
public class ArticleInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "文章所属的博客id")
    @TableField("blog_id")
    private Long blogId;

    @ApiModelProperty(value = "文章内容")
    @TableField("article_content")
    private String articleContent;

    @ApiModelProperty(value = "文章封面")
    @TableField("article_cover")
    private String articleCover;

    @ApiModelProperty(value = "文章标题")
    @TableField("article_title")
    private String articleTitle;

    @ApiModelProperty(value = "文章类别")
    @TableField("article_type")
    private String articleType;

    @ApiModelProperty(value = "浏览次数")
    @TableField("view_count")
    private Integer viewCount;

    @ApiModelProperty(value = "是否公开（0，不公开，1，公开）")
    @TableField("is_open")
    private Integer isOpen;

    @ApiModelProperty(value = "文章字数")
    @TableField("article_word_number")
    private Integer articleWordNumber;

    @ApiModelProperty(value = "文章创建时间")
    @TableField("create_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "文章最近一次修改时间")
    @TableField("latest_update_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime latestUpdateTime;

    @ApiModelProperty(value = "文章简单描述")
    @TableField("brief_description")
    private String briefDescription;

    @ApiModelProperty(value = "文章的标签",name = "articleLabelList")
    @TableField(exist = false)
    private List<String> articleLabelList;

    @ApiModelProperty(value = "文章的标签",name = "labelList")
    @TableField(exist = false)
    private List<ArticleLabel> labelList;
}
