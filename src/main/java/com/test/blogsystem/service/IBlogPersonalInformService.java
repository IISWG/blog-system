package com.test.blogsystem.service;

import com.test.blogsystem.entity.BlogPersonalInform;
import com.baomidou.mybatisplus.extension.service.IService;
import com.test.blogsystem.entity.param.BaseParam;
import com.test.blogsystem.entity.result.PageResult;

/**
 * <p>
 * 博客基本信息 服务类
 * </p>
 *
 * @author 11903990213李列伟
 * @since 2021-11-25
 */
public interface IBlogPersonalInformService extends IService<BlogPersonalInform> {
    public PageResult getBlogPersonal(BaseParam baseParam);
}
