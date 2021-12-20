package com.test.blogsystem.entity.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleParam extends BaseParam {
    /**
     * 文章类别
     */
    @ApiModelProperty(value = "文章类别")
    private String type;

    /**
     * 模糊查询内容
     */
    @ApiModelProperty(value = "模糊查询内容")
    private String like;
}
