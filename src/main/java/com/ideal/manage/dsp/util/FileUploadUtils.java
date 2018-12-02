package com.ideal.manage.dsp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUploadUtils {

    public final static String PRODUCE_PICTURE_DIR = "producePicture";         //产品图片路径
    public final static String SOLUTION_PICTURE_DIR = "solutionPicture";       //解决方案图片路径
    public final static String CASE_PICTURE_DIR = "casePicture";               //优秀案例图片路径
    public final static String CAROUSEL_PICTURE_DIR = "carouselPicture";       //轮播图片路径
    public final static String FEATURE_PICTURE_DIR = "featurePicture";         //产品特点图片路径
    public final static String COMPANY_PICTURE_DIR = "companyPicture";         //公司简介图片路径
    public final static String PARTNER_PICTURE_DIR = "partnerPicture";         //合作伙伴图片路径
    public final static String SERVER_PICTURE_DIR = "serverPicture";           //产品服务图片路径
    public final static String CONSULT_PICTURE_DIR = "consultPicture";         //咨询附件路径

    private static final Logger logger = LoggerFactory.getLogger(FileUploadUtils.class);

    public static String uploadFile(MultipartFile multipartFile, String basicPath, String dir) throws IOException {

        if (multipartFile != null) {

            String pictureName = multipartFile.getOriginalFilename();
            String[] strArr = pictureName.split("\\.");
            String fileName = UUID.randomUUID() + "." + strArr[strArr.length - 1];

            String varPath = "/" + dir + "/" + fileName;

            File file = FileUtil.buildFile(basicPath + varPath, false);
            multipartFile.transferTo(file);

            return varPath;
        }

        return null;
    }
}
