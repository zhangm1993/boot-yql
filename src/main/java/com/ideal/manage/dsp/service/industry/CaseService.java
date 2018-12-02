package com.ideal.manage.dsp.service.industry;

import com.ideal.manage.dsp.bean.industry.Case;
import com.ideal.manage.dsp.bean.system.User;
import com.ideal.manage.dsp.config.shiro.MyShiroRealm;
import com.ideal.manage.dsp.repository.framework.MySpecification;
import com.ideal.manage.dsp.repository.framework.SpecificationOperator;
import com.ideal.manage.dsp.repository.industry.CaseRepository;
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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CaseService {

    @Resource
    private CaseRepository caseRepository;

    @Resource
    private SftpConfig sftpConfig;


    /**
     * 优秀案例首页展示
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Page<Case> getCaseHtml(int pageNum,int pageSize) {

//        List<SpecificationOperator> operators = HttpRequests.getParametersStartingWith(request,"Q_");
        Sort sort = new Sort(Sort.Direction.DESC,"homeRank","updateTime");
       /* SpecificationOperator operator = new SpecificationOperator("delFlag","0","EQ");
        SpecificationOperator operator1 = new SpecificationOperator("status","1","EQ");
        SpecificationOperator operator2 = new SpecificationOperator("homeRank",null,"ISNOTNULL");
        operators.add(operator);
        operators.add(operator1);
        operators.add(operator2);*/

        String sql="select id,name,title,description,home_url from t_case where del_flag=0 and status=1 " +
                "and home_rank is not null";

        String[] str=new String[]{"id","name","title","description","homeUrl"};

        Pageable pageable = new PageRequest(pageNum,pageSize,sort);

        Page<Case> pages=caseRepository.findAllBySql(sql,pageable,Case.class,str);

        return pages;
    }

    /**
     * 条件查询
     * @param pageNum
     * @param type
     * @return
     */
    public Page<Case> getCaseAll(int pageNum,int pageSize,String type) {

        //List<SpecificationOperator> operators = HttpRequests.getParametersStartingWith(request,"Q_");
        Sort sort = new Sort(Sort.Direction.DESC,"updateTime");
        /*SpecificationOperator operator = new SpecificationOperator("delFlag","0","EQ");
        SpecificationOperator operator1 = new SpecificationOperator("status","1","EQ");
        operators.add(operator);
        operators.add(operator1);*/

        String sql="select id,name,title,description,picture_url, home_url from t_case where del_flag=0 and status=1";

        if(!StringUtils.isEmpty(type)){
            sql+=" and industry_cate="+type;
        }
        Pageable pageable = new PageRequest(pageNum,pageSize,sort);

        String[] str=new String[]{"id","name","title","description","pictureUrl", "homeUrl"};

        Page<Case> pages=caseRepository.findAllBySql(sql,pageable,Case.class,str);

        return pages;
    }


    /**
     * 条件查询
     * @param pageNum
     * @param request
     * @return
     */
    public Page<Case> getCaseData(int pageNum, HttpServletRequest request) {

        //排序
        Sort sort = new Sort(Sort.Direction.DESC,"update_time");

        //分页
        Pageable pageable = new PageRequest(pageNum,15, sort);

        //字段
        String[] str=new String[]{"id", "name", "industryCateName", "title", "homeRank", "status"};

        //基础sql
        StringBuffer sql = new StringBuffer()
                .append("select ")
                .append("   a.id as id,")
                .append("   a.name as name,")
                .append("   (select b.name from t_parameter b where b.id = a.industry_cate) as industryCateName,")
                .append("   a.title as title,")
                .append("   a.home_rank as homeRank,")
                .append("   a.status as status ")
                .append("from ")
                .append("   t_case a ")
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

        return caseRepository.findAllBySql(sql.toString(), pageable, Case.class, str);
    }

    /**
     * 查询全部
     * @param operators
     * @param pageable
     * @return
     */
    private Page<Case> findAll(List<SpecificationOperator> operators, Pageable pageable) {

        MySpecification<Case> mySpecifications = new MySpecification<Case>(operators);
        return caseRepository.findAll(mySpecifications, pageable);
    }


    /**
     * 修改操作
     * @param status 0:下线 1:上线
     * @param id
     */
    public void caseStatusUpdate(String status, Long id) {

        Case caseObject = caseRepository.findOne(id);

        MyShiroRealm.ShiroUser user = (MyShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        caseObject.setStatus(status);
        caseObject.setUpdateTime(new Date());
        caseObject.setUpdateUser(new User(user.getId()));

        caseRepository.save(caseObject);
    }

    /**
     * 根据id获取对象
     * @param id
     * @return
     */
    public Case findCaseById(Long id) {

        return caseRepository.findOne(id);
    }

    /**
     * 优秀案例保存
     * @param picture
     * @param caseObject
     */
    public void caseSave(MultipartFile picture, MultipartFile homePicture, Case caseObject) throws Exception {

        //保存详情图片
//        String pictureUrl = caseFileUpload(picture);
        String pictureUrl = FileUploadUtils.uploadFile(picture, sftpConfig.getPath(), FileUploadUtils.CASE_PICTURE_DIR);
        if (pictureUrl != null) {
            caseObject.setPictureUrl(pictureUrl);
        }

        //保存简介图片
        String homeUrl = FileUploadUtils.uploadFile(homePicture, sftpConfig.getPath(), FileUploadUtils.CASE_PICTURE_DIR);
//        String homeUrl = caseFileUpload(homePicture);
        if (homeUrl != null) {
            caseObject.setHomeUrl(homeUrl);
        }

        MyShiroRealm.ShiroUser user = (MyShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        if(caseObject.getId() == null) {  //新增

            caseObject.setStatus("0");
            caseObject.setDelFlag("0");
            caseObject.setCreateTime(new Date());
            caseObject.setCreateUser(new User(user.getId()));
        } else {                         //修改

            //未修改的数据
            Case caseObject1 = caseRepository.findOne(caseObject.getId());
            if (picture == null) {
                caseObject.setPictureUrl(caseObject1.getPictureUrl());
            }
            caseObject.setStatus(caseObject1.getStatus());
            caseObject.setDelFlag(caseObject1.getDelFlag());
            caseObject.setCreateUser(caseObject1.getCreateUser());
            caseObject.setCreateTime(caseObject1.getCreateTime());
            caseObject.setHomeRank(caseObject1.getHomeRank());
        }

        //修改时间 人
        caseObject.setUpdateUser(new User(user.getId()));
        caseObject.setUpdateTime(new Date());

        caseRepository.save(caseObject);
    }

    /**
     * 优秀案例 批量删除
     * @param ids
     */
    @Transactional
    public void caseDelete(Long[] ids) {

        List<Case> cases = caseRepository.findAll(Arrays.asList(ids));

        for (Case caseObject : cases) {
            caseObject.setDelFlag("1");
        }

        caseRepository.save(cases);
    }

    /**
     * 优秀案例 首页展示保存
     * @param homePicture
     * @param id
     * @param homeRank
     */
    public void caseHomeSave(MultipartFile homePicture, Long id, String homeRank) throws Exception {

        Case one = caseRepository.findOne(id);

        MyShiroRealm.ShiroUser user = (MyShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();

        String homeUrl = FileUploadUtils.uploadFile(homePicture, sftpConfig.getPath(), FileUploadUtils.CASE_PICTURE_DIR);
        if (homeUrl != null) {
            one.setHomeUrl(homeUrl);
        }
//        String homeUrl = caseFileUpload(homePicture);
//        if (homeUrl != null) {
//            one.setHomeUrl(homeUrl);
//        }
        one.setHomeRank(homeRank);
        one.setUpdateTime(new Date());
        one.setUpdateUser(new User(user.getId()));

        caseRepository.save(one);
    }

    /**
     * 优秀案例 文件上传
     * @param picture
     * @return
     * @throws Exception
     */
    public String caseFileUpload(MultipartFile picture) throws Exception {

        if (picture != null) {

            //生成文件名
            String pictureName = picture.getOriginalFilename();
            String[] strArr = pictureName.split("\\.");
            String fileName = UUID.randomUUID() + "." + strArr[strArr.length - 1];

            //ftp上传文件
            SftpUtil sftpUtil = new SftpUtil();
            ChannelSftp sftp = sftpUtil.connect(sftpConfig);
            sftpUtil.cd(sftpConfig.getPath(), sftp);
            sftpUtil.mkDir(FileUploadUtils.CASE_PICTURE_DIR, sftp);
            sftpUtil.upload(picture, sftp, fileName);

            return "/" +  FileUploadUtils.CASE_PICTURE_DIR + "/" + fileName;
        }

        return null;
    }
}
