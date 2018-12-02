package com.ideal.manage.dsp.service.industry;

import com.ideal.manage.dsp.bean.industry.Carousel;
import com.ideal.manage.dsp.bean.industry.Produce;
import com.ideal.manage.dsp.bean.system.Parameter;
import com.ideal.manage.dsp.bean.system.User;
import com.ideal.manage.dsp.config.shiro.MyShiroRealm;
import com.ideal.manage.dsp.repository.framework.MySpecification;
import com.ideal.manage.dsp.repository.framework.SpecificationOperator;
import com.ideal.manage.dsp.repository.industry.CarouselRepository;
import com.ideal.manage.dsp.service.system.ParameterService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class CarouselService {

    @Resource
    private CarouselRepository carouselRepository;

    @Resource
    private SftpConfig sftpConfig;

    @Resource
    private ParameterService parameterService;
    /**
     * 根据条件获取轮播数据
     * @param pageNum
     * @param request
     * @return
     */
    public Page<Carousel> getCarouselHtml(int pageNum, HttpServletRequest request,Parameter type) {

        List<SpecificationOperator> operators = HttpRequests.getParametersStartingWith(request,"Q_");
        Sort sort = null;
        SpecificationOperator operator = new SpecificationOperator("delFlag","0","EQ");
        SpecificationOperator operator1 = new SpecificationOperator("status","1","EQ");
        SpecificationOperator operator2=null;
        if(type==null){
            operator2= new SpecificationOperator("homeRank",null,"ISNOTNULL");
            sort= new Sort(Sort.Direction.DESC,"homeRank","updateTime");
        }else{
            sort= new Sort(Sort.Direction.DESC,"cateRank","updateTime");
            operator2= new SpecificationOperator("category.id",type.getId(),"EQ");
        }


        operators.add(operator);
        operators.add(operator1);
        operators.add(operator2);

        Parameter parameter=parameterService.findByCode("2300003");
        int pageSize=3;
        try{
            pageSize=Integer.valueOf(parameter.getRemark().trim());
        }catch (Exception e){
            e.printStackTrace();
        }

        Pageable pageable = new PageRequest(pageNum,pageSize,sort);

        return findAll(operators, pageable);
    }

    /**
     * 根据条件获取轮播数据
     * @param pageNum
     * @param request
     * @return
     */
    public Page<Carousel> getCarouselData(int pageNum, HttpServletRequest request) {

        List<SpecificationOperator> operators = HttpRequests.getParametersStartingWith(request,"Q_");
        Sort sort = new Sort(Sort.Direction.ASC,"category.id", "cateRank");
        SpecificationOperator operator = new SpecificationOperator("delFlag","0","EQ");
        operators.add(operator);

        Pageable pageable = new PageRequest(pageNum,15,sort);

        return findAll(operators, pageable);
    }

    /**
     * 获取所有数据
     * @param operators
     * @param pageable
     * @return
     */
    private Page<Carousel> findAll(List<SpecificationOperator> operators, Pageable pageable) {

        MySpecification<Carousel> mySpecifications = new MySpecification<Carousel>(operators);
        return carouselRepository.findAll(mySpecifications, pageable);
    }


    /**
     * 上下线管理
     * @param status
     * @param id
     */
    public void carouselStatusUpdate(String status, Long id) {

        Carousel carousel = carouselRepository.findOne(id);

        MyShiroRealm.ShiroUser user = (MyShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        carousel.setStatus(status);
        carousel.setUpdateTime(new Date());
        carousel.setUpdateUser(new User(user.getId()));

        carouselRepository.save(carousel);
    }


    /**
     * 轮播保存
     * @param picture
     * @param carousel
     */
    public void carouselSave(MultipartFile picture, Carousel carousel) throws Exception {

        String pictureUrl = FileUploadUtils.uploadFile(picture, sftpConfig.getPath(), FileUploadUtils.CAROUSEL_PICTURE_DIR);
        if (pictureUrl != null) {
            carousel.setPictureUrl(pictureUrl);
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
//            sftpUtil.mkDir(FileUploadUtils.CAROUSEL_PICTURE_DIR, sftp);
//            sftpUtil.upload(picture, sftp, fileName);
//
//            //设置文件路径
//            carousel.setPictureUrl("/" +  FileUploadUtils.CAROUSEL_PICTURE_DIR + "/" + fileName);
//        }

        MyShiroRealm.ShiroUser user = (MyShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();

        if (carousel.getId() == null) {         //新增

            carousel.setStatus("0");
            carousel.setDelFlag("0");
            carousel.setCreateTime(new Date());
            carousel.setCreateUser(new User(user.getId()));
        } else {

            //未修改的数据
            Carousel carousel1 = carouselRepository.findOne(carousel.getId());
            if (picture == null) {
                carousel.setPictureUrl(carousel1.getPictureUrl());
            }
            carousel.setHomeRank(carousel1.getHomeRank());
            carousel.setStatus(carousel1.getStatus());
            carousel.setDelFlag(carousel1.getDelFlag());
            carousel.setCreateTime(carousel1.getCreateTime());
            carousel.setCreateUser(carousel1.getCreateUser());
        }

        //修改时间 人
        carousel.setUpdateUser(new User(user.getId()));
        carousel.setUpdateTime(new Date());

        carouselRepository.save(carousel);
    }

    /**
     * 获取 轮播对象
     * @param id
     * @return
     */
    public Carousel findCarouselById(Long id) {

        return carouselRepository.findOne(id);
    }

    /**
     * 批量删除
     * @param ids
     */
    @Transactional
    public void carouselDelete(Long[] ids) {

        List<Carousel> carousels = carouselRepository.findAll(Arrays.asList(ids));

        for (Carousel carousel : carousels) {

            carousel.setDelFlag("1");
        }

        carouselRepository.save(carousels);
    }

    /**
     * 保存 首页轮播
     * @param id
     * @param homeRank
     */
    public void carouselHomeSave(Long id, String homeRank) {

        Carousel carousel = carouselRepository.findOne(id);

        MyShiroRealm.ShiroUser user = (MyShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        if (homeRank.equals("")) {
            carousel.setHomeRank(null);
        } else {
            carousel.setHomeRank(homeRank);
        }
        carousel.setUpdateTime(new Date());
        carousel.setUpdateUser(new User(user.getId()));

        carouselRepository.save(carousel);
    }


}
