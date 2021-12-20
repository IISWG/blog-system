package com.test.blogsystem.controller;


import com.test.blogsystem.entity.BlogPersonalInform;
import com.test.blogsystem.entity.result.BaseResult;
import com.test.blogsystem.entity.result.OkResult;
import com.test.blogsystem.entity.result.ServerErrResult;
import com.test.blogsystem.mapper.BlogPersonalInformMapper;
import com.test.blogsystem.utils.QiniuUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("")
public class UploadController {

    @Resource
    private QiniuUtils qiniuUtils;

    @Resource
    BlogPersonalInformMapper blogPersonalInformMapper;

    @PostMapping("/uploadImg")
    // @Log4j(module = "图片文件",operation = "上传文章图片，返回URL链接")
    public BaseResult upload(@RequestParam("image") MultipartFile image) {
        if (image.isEmpty()) {
            ServerErrResult serverErrResult = new ServerErrResult("文件为空，请重新上传");
            serverErrResult.setCode(400);
            return serverErrResult;
        }
            //原始文件名称 比如 aa.png
            String originalFilename = image.getOriginalFilename();
            //唯一的文件名称
            String fileName = UUID.randomUUID().toString() + "." + StringUtils.substringAfterLast(originalFilename, ".");
            //上传文件 上传到哪呢？ 七牛云 云服务器 按量付费 速度快 把图片发放到离用户最近的服务器上
            // 降低 我们自身应用服务器的带宽消耗
            System.out.println("fileName:"+fileName);
            boolean upload = qiniuUtils.upload(image, fileName);
            if (upload) {
                return new OkResult(QiniuUtils.url + fileName);
            }
            return new ServerErrResult("上传失败！");
        }
    @PostMapping("/uploadHead")
    // @Log4j(module = "图片文件",operation = "上传文章图片，返回URL链接")
    public BaseResult uploadHead(@RequestParam("image") MultipartFile image, BlogPersonalInform user) {
        if (image.isEmpty()) {
            ServerErrResult serverErrResult = new ServerErrResult("文件为空，请重新上传");
            serverErrResult.setCode(400);
            return serverErrResult;
        }

            //原始文件名称 比如 aa.png
            String originalFilename = image.getOriginalFilename();
            //唯一的文件名称
            String fileName = UUID.randomUUID().toString() + "." + StringUtils.substringAfterLast(originalFilename, ".");
            //上传文件 上传到哪呢？ 七牛云 云服务器 按量付费 速度快 把图片发放到离用户最近的服务器上
            // 降低 我们自身应用服务器的带宽消耗
            System.out.println("fileName:"+fileName);
            boolean upload = qiniuUtils.upload(image, fileName);
            if (upload) {
                user.setHeadPortrait(QiniuUtils.url + fileName);
                int updateById = blogPersonalInformMapper.updateById(user);
                return new OkResult(user);
            }
            return new ServerErrResult("上传失败！");
        }
}

