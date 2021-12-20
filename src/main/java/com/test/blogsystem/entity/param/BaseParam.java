package com.test.blogsystem.entity.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;

/**
 * @author 11903990213李列伟
 * @version 1.0
 * @date 2021/7/26
 * @description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseParam extends PageParam{
    /**
     * 排序字段
     */
    @ApiModelProperty(value = "排序字段")
    private String order;

    /**
     * 是升序还降序（false是降序，true是升序）
     */
    @ApiModelProperty(value = "排序顺序(默认降序)")
    private boolean asc = false;

    /**
     * 每页的大小
     */
    @ApiModelProperty(value = "分页规格(默认为10)")
    private Integer pageSize = 10;

    /**
     *分页第几页
     */
    @ApiModelProperty(value = "分页页码(默认为1)")
    @Max(200)
    private Integer pageNum = 1;

    public BaseParam(Integer pageSize, Integer pageNum){
        this.pageSize = pageSize;
        this.pageNum = pageNum;
    }
}
