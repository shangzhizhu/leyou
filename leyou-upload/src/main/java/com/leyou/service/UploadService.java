package com.leyou.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadService.class);

    @Autowired
    private FastFileStorageClient storageClient;

    // 定义支持的图片类型
    private static final List<String> CONTENT_TYPE_LIST = Arrays.asList("image/png", "image/jpeg", "image/jpg");

    public String uploadFile(MultipartFile file) {

        /* 限制上传图片的大小  yml中已经配置 */

        try {
            String originalFilename = file.getOriginalFilename();

            // 验证图片类型是否合法 以什么后缀类型结尾
            if (!CONTENT_TYPE_LIST.contains(file.getContentType())) {
                LOGGER.info(originalFilename + "图片类型不匹配,上传失败,类型为" + file.getContentType());
                return null;
            }

            // 验证图片内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                LOGGER.info(originalFilename + "上传失败,图片内容不符合要求");
                return null;
            }

            // 保存图片
            //file.transferTo(new File("F:\\2018\\images\\upload\\" + originalFilename));

            /* ---将图片上传到FastDFS--- */

            // 获取文件后缀名
            String ext = StringUtils.substringAfterLast(originalFilename, ".");

            // 上传并获取图片在FastDFS系统中的路径
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), ext, null);
            String fullPath = storePath.getFullPath();

            // 拼接图片url并返回
            return "http://image.leyou.com/" + fullPath;

        } catch (IOException e) {
            LOGGER.info("图片上传失败");
            e.printStackTrace();
            return null;
        }

    }

}
