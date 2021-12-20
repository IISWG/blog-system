package com.test.blogsystem.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author 11903990213李列伟
 * @since 2021-11-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("article_types")
@ApiModel(value="ArticleTypes对象", description="")
public class ArticleTypes implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "文章的类型")
    @TableField("article_type")
    private String articleType;

    @ApiModelProperty(value = "文章编号")
    @TableField("serial_number")
    private Integer serialNumber;


}
