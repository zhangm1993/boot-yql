package com.ideal.manage.dsp.service.industry;

import com.ideal.manage.dsp.bean.industry.Produce;
import com.ideal.manage.dsp.bean.system.Parameter;
import com.ideal.manage.dsp.config.shiro.MyShiroRealm;
import com.ideal.manage.dsp.repository.framework.MySpecification;
import com.ideal.manage.dsp.repository.framework.SpecificationOperator;
import com.ideal.manage.dsp.repository.industry.ProduceRepository;
import com.ideal.manage.dsp.service.system.ParameterService;
import com.ideal.manage.dsp.util.*;
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
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class ProduceService {

    @Resource
    private ProduceRepository produceRepository;

    @Resource
    private ParameterService parameterService;

    @Resource
    private SftpConfig sftpConfig;


    /**
     * 查询首页显示产品
     * @return
     */
    public Page<Produce> findAllIndex(Integer page,Integer pageSize){

        /*List<SpecificationOperator> operators = HttpRequests.getParametersStartingWith(request,"Q_");*/

        Sort sort = new Sort(Sort.Direction.DESC,"homeRank","updateTime");
        /*SpecificationOperator operator = new SpecificationOperator("delFlag","0","EQ");
        SpecificationOperator operator1 = new SpecificationOperator("status","1","EQ");
        SpecificationOperator operator2 = new SpecificationOperator("homeRank",null,"ISNOTNULL");
        operators.add(operator);
        operators.add(operator1);
        operators.add(operator2);*/

        String sql="select id,name,first_cate,second_cate,title,home_url,description from t_produce where del_flag=0 " +
                " and status=1 and home_rank is not null";

        String[] str=new String[]{"id","name","firstCatel","secondCatel","title","homeUrl","description"};
        Pageable pageable = new PageRequest(page,pageSize,sort);
        Page<Produce> pages=produceRepository.findAllBySql(sql,pageable,Produce.class,str);


        return pages;
    }



    /**
     * 查询商品列表
     * @param request
     * @return
     */
    public Page<Produce> findAllSql(HttpServletRequest request){

        List<SpecificationOperator> operators = HttpRequests.getParametersStartingWith(request,"Q_");

        Sort sort = new Sort(Sort.Direction.DESC,"secondCate.id");
        SpecificationOperator operator = new SpecificationOperator("delFlag","0","EQ");
        SpecificationOperator operator1 = new SpecificationOperator("status","1","EQ");
        operators.add(operator);
        operators.add(operator1);

        Pageable pageable = new PageRequest(0,200,sort);

        
        return findAll(operators, pageable);
    }

    /**
     * 查询商品列表
     * @param request
     * @return
     */
    public Page<Produce> findAllDesc(HttpServletRequest request){



        Sort sort = new Sort(Sort.Direction.DESC,"secondCate.id");


        Pageable pageable = new PageRequest(0,200,sort);


        String sql="select id,name,first_cate,second_cate from t_produce where del_flag=0 and status=1";

        String[] str=new String[]{"id","name","firstCatel","secondCatel"};


        Page<Produce> pages=produceRepository.findAllBySql(sql,pageable,Produce.class,str);

        System.out.println(pages);


        return pages;
    }

    /**
     * 根据条件获取所有对象
     * @param operators
     * @param pageable
     * @return
     */
    public Page<Produce> findAll(List<SpecificationOperator> operators, Pageable pageable) {

        MySpecification<Produce> mySpecifications = new MySpecification<>(operators);
        return produceRepository.findAll(mySpecifications, pageable);

    }

    /**
     * 获取产品数据
     * @param pageNum
     * @param request
     * @return
     */
    public Page<Produce> getProduceData(int pageNum, HttpServletRequest request) {

        //排序
        Sort sort = new Sort(Sort.Direction.DESC,"updateTime");

        //分页
        Pageable pageable = new PageRequest(pageNum,15, sort);

        //字段
        String[] str=new String[]{"id", "name", "firstCateName", "secondCateName", "homeRank", "status", "code", "recommendStatus"};

        //基础sql
        StringBuffer sql = new StringBuffer()
                .append("select ")
                .append("   a.id as id,")
                .append("   a.name as name,")
                .append("   (select b.name from t_parameter b where b.id = a.first_cate) as firstCateName,")
                .append("   (select b.name from t_parameter b where b.id = a.second_cate) as secondCateName,")
                .append("   a.home_rank as homeRank,")
                .append("   a.status as status,")
                .append("   a.code as code,")
                .append("   a.recommend_status as recommendStatus ")
                .append("from ")
                .append("   t_produce a ")
                .append("where ")
                .append("   a.del_flag = '0' ");

        //条件sql 拼接
        String name = request.getParameter("name");
        if (!name.equals("")) {
            sql.append(" and a.name like '%")
                    .append(name)
                    .append("%' ");
        }

        //排序
        sql.append("order by ")
                .append("   update_time desc");

        return produceRepository.findAllBySql(sql.toString(), pageable, Produce.class, str);
    }

    /**
     * 产品保存
     * @param picture
     * @param homePicture
     * @param produce
     * @throws Exception
     */
    public void produceSave(MultipartFile picture, MultipartFile homePicture, Produce produce) throws Exception {

        //保存详情图片
        String pictureUrl = FileUploadUtils.uploadFile(picture, sftpConfig.getPath(), FileUploadUtils.PRODUCE_PICTURE_DIR);
        if (pictureUrl != null) {
            produce.setPictureUrl(pictureUrl);
        }

        //保存简介图片
        String homeUrl = FileUploadUtils.uploadFile(homePicture, sftpConfig.getPath(), FileUploadUtils.PRODUCE_PICTURE_DIR);
        if (pictureUrl != null) {
            produce.setHomeUrl(homeUrl);
        }

        MyShiroRealm.ShiroUser user = (MyShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        if (produce.getId() == null) {  //新建

            produce.setDelFlag("0");   //正常
            produce.setStatus("0");    //未上线
            produce.setCreateTime(new Date());
            produce.setCreateName(user.getUsername());
            produce.setRecommendStatus("0");
        } else {                        //修改

            //为修改的数据
            Produce produceInfo = produceRepository.getOne(produce.getId());
            if (picture == null) {
                produce.setPictureUrl(produceInfo.getPictureUrl());
            }
            if (homePicture == null) {
                produce.setHomeUrl(produceInfo.getHomeUrl());
            }
            produce.setStatus(produceInfo.getStatus());
            produce.setDelFlag(produceInfo.getDelFlag());
            produce.setCreateTime(produceInfo.getCreateTime());
            produce.setCreateName(produceInfo.getCreateName());
            produce.setHomeRank(produceInfo.getHomeRank());               //首页排序
            produce.setRecommendStatus(produceInfo.getRecommendStatus()); //推荐商品
        }

        //修改时间 人
        produce.setUpdateTime(new Date());
        produce.setUpdateName(user.getUsername());

        //保存产品信息
        produceRepository.save(produce);

    }

    /**
     * 产品状态改变
     * @param status  1:上线  0:下线
     * @param id
     */
    public void produceStatusUpdate(String status, Long id) {

        Produce produce = produceRepository.findOne(id);

        MyShiroRealm.ShiroUser user = (MyShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        produce.setStatus(status);
        produce.setUpdateTime(new Date());
        produce.setUpdateName(user.getUsername());

        produceRepository.save(produce);

    }

    /**
     * 获取 产品对象
     * @param id
     * @return
     */
    public Produce getProduceById(Long id) {

        return produceRepository.findOne(id);
    }

    /**
     * 产品批量删除
     * @param ids
     */
    @Transactional
    public void produceDelete(Long[] ids) {

        List<Produce> produces = produceRepository.findAll(Arrays.asList(ids));

        for (Produce produce : produces) {
            produce.setDelFlag("1");
        }

        produceRepository.save(produces);
    }

    /**
     * 保存 产品首页数据
     * @param homePicture
     * @param id
     * @param homeRank
     */
    public void produceHomeSave(MultipartFile homePicture, Long id, String homeRank) throws Exception {

        Produce produce = produceRepository.findOne(id);

        MyShiroRealm.ShiroUser user = (MyShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        String homeUrl = produceFileUpload(homePicture);
        if (homeUrl != null) {
            produce.setHomeUrl(homeUrl);
        }
        produce.setHomeRank(homeRank);
        produce.setUpdateTime(new Date());
        produce.setUpdateName(user.getUsername());
        produceRepository.save(produce);
    }

    /**
     * 产品 文件上传
     * @param picture
     * @return
     * @throws Exception
     */
    public String produceFileUpload(MultipartFile picture) throws Exception {

        if (picture != null) {

            //生成文件名
            String pictureName = picture.getOriginalFilename();
            String[] strArr = pictureName.split("\\.");
            String fileName = UUID.randomUUID() + "." + strArr[strArr.length - 1];

            //ftp上传文件
            SftpUtil sftpUtil = new SftpUtil();
            ChannelSftp sftp = sftpUtil.connect(sftpConfig);
            sftpUtil.cd(sftpConfig.getPath(), sftp);
            sftpUtil.mkDir(FileUploadUtils.PRODUCE_PICTURE_DIR, sftp);
            sftpUtil.upload(picture, sftp, fileName);

            return "/" +  FileUploadUtils.PRODUCE_PICTURE_DIR + "/" + fileName;
        }

        return null;
    }

    /**
     *  推荐商品保存
     * @param id
     * @param recommendStatus
     */
    public void produceRecommendSave(Long id, String recommendStatus) {

        Produce produce = produceRepository.findOne(id);
        produce.setRecommendStatus(recommendStatus);
        produce.setUpdateTime(new Date());
        produceRepository.save(produce);

    }

    /**
     * 获取推荐商品
     * @return
     */
    public Page<Produce> getRecommendProduce() {

        List<SpecificationOperator> operators = new ArrayList<>();
        operators.add(new SpecificationOperator("delFlag","0","EQ"));
        operators.add(new SpecificationOperator("status","1","EQ"));
        operators.add(new SpecificationOperator("recommendStatus","1","EQ"));

        Sort sort = new Sort(Sort.Direction.DESC,"updateTime");

        int num = Integer.parseInt(parameterService.findByCode("2300002").getRemark().trim());
        Pageable pageable = new PageRequest(0,num, sort);

        return findAll(operators, pageable);
    }

    /**
     * 根据产品code 获取对象
     * @param code
     * @return
     */
    public Produce finProduceCode(String code) {

        return produceRepository.findByCode(code);
    }
}
