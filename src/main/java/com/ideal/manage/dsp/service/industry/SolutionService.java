package com.ideal.manage.dsp.service.industry;

import com.ideal.manage.dsp.bean.industry.Solution;
import com.ideal.manage.dsp.bean.system.Parameter;
import com.ideal.manage.dsp.bean.system.User;
import com.ideal.manage.dsp.config.shiro.MyShiroRealm;
import com.ideal.manage.dsp.repository.framework.MySpecification;
import com.ideal.manage.dsp.repository.framework.SpecificationOperator;
import com.ideal.manage.dsp.repository.industry.SolutionRepository;
import com.ideal.manage.dsp.service.system.ParameterService;
import com.ideal.manage.dsp.util.*;
import com.jcraft.jsch.ChannelSftp;
import org.apache.shiro.SecurityUtils;
import org.springframework.context.ApplicationContext;
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
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class SolutionService {

    @Resource
    private SolutionRepository solutionRepository;

    @Resource
    private ParameterService parameterService;

    @Resource
    private SftpConfig sftpConfig;


    public Page<Solution> getSolutionIndex(int pageNum,Integer pageSize) {

        //List<SpecificationOperator> operators = HttpRequests.getParametersStartingWith(request,"Q_");
        Sort sort = new Sort(Sort.Direction.DESC,"homeRank","updateTime");
        /*SpecificationOperator operator = new SpecificationOperator("delFlag","0","EQ");
        SpecificationOperator operator1 = new SpecificationOperator("status","1","EQ");
        SpecificationOperator operator2 = new SpecificationOperator("homeRank",null,"ISNOTNULL");
        operators.add(operator);
        operators.add(operator1);
        operators.add(operator2);*/

        String sql="select id,name,title,home_url,description from t_solution where del_flag=0 and status=1 " +
                "and home_rank is not null";

        Pageable pageable = new PageRequest(pageNum,pageSize,sort);

        String[] str=new String[]{"id","name","title","homeUrl","description"};

        Page<Solution> pages=solutionRepository.findAllBySql(sql,pageable,Solution.class,str);
        return pages;
    }

    /**
     * 查询商品列表
     * @param pageNum
     * @param request
     * @return
     */
    public Page<Solution> getSolutionAll(int pageNum, HttpServletRequest request) {

       /* List<SpecificationOperator> operators = HttpRequests.getParametersStartingWith(request,"Q_");

        SpecificationOperator operator = new SpecificationOperator("delFlag","0","EQ");
        SpecificationOperator operator1 = new SpecificationOperator("status","1","EQ");
        operators.add(operator);
        operators.add(operator1);*/

        Sort sort = new Sort(Sort.Direction.DESC,"updateTime");

        String sql="select id,name from t_solution where del_flag=0 and status=1";

        Pageable pageable = new PageRequest(pageNum,50,sort);

        String[] str=new String[]{"id","name"};

        Page<Solution> pages=solutionRepository.findAllBySql(sql,pageable,Solution.class,str);

        return pages;
    }


    /**
     * 获取 方案数据
     * @param operators
     * @param pageable
     * @return
     */
    private Page<Solution> findAll(List<SpecificationOperator> operators, Pageable pageable) {

        MySpecification<Solution> mySpecifications = new MySpecification<>(operators);
        return solutionRepository.findAll(mySpecifications, pageable);

    }


    public Page<Solution> getSolutionData(int pageNum, HttpServletRequest request) {

        //排序
        Sort sort = new Sort(Sort.Direction.DESC,"updateTime");

        //分页
        Pageable pageable = new PageRequest(pageNum,15, sort);

        //字段
        String[] str=new String[]{"id", "name", "title", "homeRank", "status"};

        //基础sql
        StringBuffer sql = new StringBuffer()
                .append("select ")
                .append("   a.id as id,")
                .append("   a.name as name,")
                .append("   a.title as title,")
                .append("   a.home_rank as homeRank,")
                .append("   a.status as status ")
                .append("from ")
                .append("   t_solution a ")
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

        return solutionRepository.findAllBySql(sql.toString(), pageable, Solution.class, str);
    }


    /**
     * 解决方案保存
     * @param picture
     * @param homePicture
     * @param picture
     * @param solution
     */
    public void SolutionSave(MultipartFile picture, MultipartFile homePicture, Solution solution) throws Exception {

        //保存详情图片
//        String pictureUrl = solutionFileUpload(picture);
        String pictureUrl = FileUploadUtils.uploadFile(picture, sftpConfig.getPath(), FileUploadUtils.SOLUTION_PICTURE_DIR);
        if (pictureUrl != null) {
            solution.setPictureUrl(pictureUrl);
        }

        //保存简介图片
        String homeUrl = FileUploadUtils.uploadFile(homePicture, sftpConfig.getPath(), FileUploadUtils.SOLUTION_PICTURE_DIR);
        if (homeUrl != null) {
            solution.setHomeUrl(homeUrl);
        }

        MyShiroRealm.ShiroUser user = (MyShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();

        if (solution.getId() == null) {  //新建

            solution.setDelFlag("0");
            solution.setStatus("0");
            solution.setCreateTime(new Date());
            solution.setCreateUser(new User(user.getId()));

        } else {                        //修改
            //未修改的数据
            Solution solution1 = solutionRepository.findOne(solution.getId());
            if (picture == null) {
                solution.setPictureUrl(solution1.getPictureUrl());
            }
            solution.setDelFlag(solution1.getDelFlag());
            solution.setStatus(solution1.getStatus());
            solution.setCreateUser(solution1.getCreateUser());
            solution.setCreateTime(solution1.getCreateTime());
            solution.setHomeRank(solution1.getHomeRank());

        }

        //修改时间 人
        solution.setUpdateTime(new Date());
        solution.setUpdateUser(new User(user.getId()));

        solutionRepository.save(solution);
    }

    /**
     * 解决方案状态修改
     * @param status 1:上线  0:下线
     * @param id
     */
    public void solutionStatusUpdate(String status, Long id) {

        Solution solution = solutionRepository.findOne(id);

        MyShiroRealm.ShiroUser user = (MyShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        solution.setStatus(status);
        solution.setUpdateTime(new Date());
        solution.setUpdateUser(new User(user.getId()));

        solutionRepository.save(solution);

    }

    /**
     * 根据id 获取Solution对象
     * @param id
     * @return
     */
    public Solution getSolutionById(Long id) {

        return solutionRepository.findOne(id);
    }

    /**
     * 批量删除 解决方案
     * @param ids
     */
    @Transactional
    public void solutionDelete(Long[] ids) {

        List<Solution> solutions = solutionRepository.findAll(Arrays.asList(ids));

        for (Solution solution : solutions) {

            solution.setDelFlag("1");
        }

        solutionRepository.save(solutions);
    }

    /**
     * 解决方案 首页展示保存
     * @param homePicture
     * @param id
     * @param homeRank
     */
    public void solutionHomeSave(MultipartFile homePicture, Long id, String homeRank) throws Exception {

        Solution solution = solutionRepository.findOne(id);

        MyShiroRealm.ShiroUser user = (MyShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
//        String homeUrl = solutionFileUpload(homePicture);
        String homeUrl = FileUploadUtils.uploadFile(homePicture, sftpConfig.getPath(), FileUploadUtils.SOLUTION_PICTURE_DIR);
        if (homeUrl != null) {
            solution.setHomeUrl(homeUrl);
        }
        solution.setHomeRank(homeRank);
        solution.setUpdateTime(new Date());
        solution.setUpdateUser(new User(user.getId()));
        solutionRepository.save(solution);
    }

    /**
     * 解决方案 文件上传
     * @param picture
     * @return
     * @throws Exception
     */
    public String solutionFileUpload(MultipartFile picture) throws Exception {

        if (picture != null) {

            //生成文件名
            String pictureName = picture.getOriginalFilename();
            String[] strArr = pictureName.split("\\.");
            String fileName = UUID.randomUUID() + "." + strArr[strArr.length - 1];

            //ftp上传文件
            SftpUtil sftpUtil = new SftpUtil();
            ChannelSftp sftp = sftpUtil.connect(sftpConfig);
            sftpUtil.cd(sftpConfig.getPath(), sftp);
            sftpUtil.mkDir(FileUploadUtils.SOLUTION_PICTURE_DIR, sftp);
            sftpUtil.upload(picture, sftp, fileName);

            return "/" +  FileUploadUtils.SOLUTION_PICTURE_DIR + "/" + fileName;
        }

        return null;
    }


}
