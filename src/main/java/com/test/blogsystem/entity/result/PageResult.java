package com.test.blogsystem.entity.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author dev-lizi
 * @date 2021/7/27
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult {
    /**
     * 总数
     */
    private Long total;
    /**
     * 总页数
     */
    private Long pages;

    /**
     * 结果集
     */
    private List<?> records;
}
