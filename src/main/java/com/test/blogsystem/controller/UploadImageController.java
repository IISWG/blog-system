//package com.test.blogsystem.controller;
//
//import com.test.blogsystem.entity.result.BaseResult;
//import com.test.blogsystem.utils.QiniuCloudUtil;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.util.UUID;
//
//@Controller
//public class UploadImageController {
//    @ResponseBody
//    @RequestMapping(value="/uploadImg", method= RequestMethod.POST)
//    public BaseResult uploadImg(@RequestParam MultipartFile image, HttpServletRequest request) {
//        BaseResult result = new BaseResult();
//        if (image.isEmpty()) {
//            result.setCode(400);
//            result.setMsg("文件为空，请重新上传");
//            return result;
//        }
//
//        try {
//            byte[] bytes = image.getBytes();
//            String imageName = UUID.randomUUID().toString()+".jpg";
//
//            QiniuCloudUtil qiniuUtil = new QiniuCloudUtil();
//
//                //使用base64方式上传到七牛云
//                String url = qiniuUtil.put64image(bytes, imageName);
//                result.setCode(200);
//                result.setMsg("文件上传成功");
//                result.setData(url);
//
//            System.out.println("request："+request);
//            return result;
//        } catch (Exception e) {
//            e.printStackTrace();
//            result.setCode(500);
//            result.setMsg("文件上传发生异常！");
//            return result;
//        }
//    }
//}
