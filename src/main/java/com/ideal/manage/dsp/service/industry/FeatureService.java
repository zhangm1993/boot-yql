package com.ideal.manage.dsp.service.industry;

import com.ideal.manage.dsp.bean.industry.Feature;
import com.ideal.manage.dsp.bean.system.User;
import com.ideal.manage.dsp.config.shiro.MyShiroRealm;
import com.ideal.manage.dsp.repository.framework.MySpecification;
import com.ideal.manage.dsp.repository.framework.SpecificationOperator;
import com.ideal.manage.dsp.repository.industry.FeatureRepository;
import com.ideal.manage.dsp.util.FileUploadUtils;
import com.ideal.manage.dsp.util.HttpRequests;
import com.ideal.manage.dsp.util.SftpConfig;
import com.ideal.manage.dsp.util.SftpUtil;
import com.jcraft.jsch.ChannelSftp;
import org.apache.shiro.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class FeatureService {

    @Resource
    private FeatureRepository featureRepository;

    @Resource
    private SftpConfig sftpConfig;

    /**
     * 根据条件获取所有对象
     * @param operators
     * @param pageable
     * @return
     */
    public Page<Feature> findAll(List<SpecificationOperator> operators, Pageable pageable) {

        MySpecification<Feature> mySpecifications = new MySpecification<>(operators);
        return featureRepository.findAll(mySpecifications, pageable);
    }

    /**
     * 根据条件获取所有对象
     * @param pageNum
     * @param request
     * @return
     */
    public Page<Feature> getFeatureData(int pageNum, HttpServletRequest request) {

        List<SpecificationOperator> operators = HttpRequests.getParametersStartingWith(request,"Q_");
        Sort sort = new Sort(Sort.Direction.DESC,"updateTime");
        SpecificationOperator operator = new SpecificationOperator("delFlag","0","EQ");
        operators.add(operator);

        Pageable pageable = new PageRequest(pageNum,15,sort);

        return findAll(operators, pageable);
    }

    /**
     * 根据条件获取所有对象
     * @param pageNum
     * @param request
     * @return
     */
    public Page<Feature> getFeatureDataHtml(int pageNum, HttpServletRequest request) {

        List<SpecificationOperator> operators = HttpRequests.getParametersStartingWith(request,"Q_");
        Sort sort = new Sort(Sort.Direction.DESC,"updateTime");
        SpecificationOperator operator = new SpecificationOperator("delFlag","0","EQ");
        SpecificationOperator operator1 = new SpecificationOperator("status","1","EQ");
        operators.add(operator);
        operators.add(operator1);

        Pageable pageable = new PageRequest(pageNum,15,sort);

        return findAll(operators, pageable);
    }


    /**
     * 根据id 获取对象
     * @param id
     * @return
     */
    public Feature getFeatureById(Long id) {

        return featureRepository.findOne(id);
    }

    /**
     *  上下线修改
     * @param status
     * @param id
     */
    public void featureStatusUpdate(String status, Long id) {

        MyShiroRealm.ShiroUser user = (MyShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();

        Feature feature = featureRepository.findOne(id);
        feature.setStatus(status);
        feature.setUpdateTime(new Date());
        feature.setUpdateUser(new User(user.getId()));

        featureRepository.save(feature);
    }

    /**
     * 产品特点保存
     * @param picture
     * @param feature
     */
    public void featureSave(MultipartFile picture, Feature feature) throws Exception {

        String pictureUrl = FileUploadUtils.uploadFile(picture, sftpConfig.getPath(), FileUploadUtils.FEATURE_PICTURE_DIR);
        if (pictureUrl != null) {
            feature.setPictureUrl(pictureUrl);
        }
//        if (picture != null) {
//
//            //生成文件名
//            String pictureName = picture.getOriginalFilename();
//            String[] strArr = pictureName.split("\\.");
//            String fileName = UUID.randomUUID() + "." + strArr[strArr.length - 1];
//
//            //ftp上传文件
//            SftpUtil sftpUtil = new SftpUtil();
//            ChannelSftp sftp = sftpUtil.connect(sftpConfig);
//            sftpUtil.cd(sftpConfig.getPath(), sftp);
//            sftpUtil.mkDir(FileUploadUtils.FEATURE_PICTURE_DIR, sftp);
//            sftpUtil.upload(picture, sftp, fileName);
//
//            feature.setPictureUrl("/" +  FileUploadUtils.FEATURE_PICTURE_DIR + "/" + fileName);
//        }

        MyShiroRealm.ShiroUser user = (MyShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        if(feature.getId() == null) {      //新建

            feature.setCreateTime(new Date());
            feature.setCreateUser(new User(user.getId()));
            feature.setStatus("0");
            feature.setDelFlag("0");
        } else {                           //修改

            Feature one = featureRepository.findOne(feature.getId());
            feature.setCreateUser(one.getCreateUser());
            feature.setCreateTime(one.getCreateTime());
            if (picture == null) {
                feature.setPictureUrl(one.getPictureUrl());
            }
            feature.setDelFlag(one.getDelFlag());
            feature.setStatus(one.getStatus());
        }

        feature.setUpdateUser(new User(user.getId()));
        feature.setUpdateTime(new Date());
        featureRepository.save(feature);
    }

    /**
     * 产品特点 批量删除
     * @param ids
     */
    public void featureDelete(Long[] ids) {

        List<Feature> features = featureRepository.findAll(Arrays.asList(ids));

        for (Feature feature : features) {

            feature.setDelFlag("1");

        }

        featureRepository.save(features);
    }
}
