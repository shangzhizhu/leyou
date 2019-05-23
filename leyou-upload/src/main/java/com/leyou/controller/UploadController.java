package com.leyou.controller;

import com.leyou.service.UploadService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private UploadService uploadService;

    /**
     * 图片上传功能
     * @param file
     * @return
     */
    @PostMapping("upload/image")
    public ResponseEntity<String> uploadFile(@RequestParam("file")MultipartFile file){

        if (file == null){
            LOGGER.info("上传的图片是null");
            return ResponseEntity.badRequest().build();
        }

        String url = uploadService.uploadFile(file);

        if (StringUtils.isBlank(url))
            return ResponseEntity.badRequest().build();

        LOGGER.info("图片的地址是" + url);
        return ResponseEntity.ok(url);

    }

}
