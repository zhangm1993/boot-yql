package com.ideal.manage.dsp.service.industry;

import com.ideal.manage.dsp.bean.industry.Company;
import com.ideal.manage.dsp.bean.system.Parameter;
import com.ideal.manage.dsp.bean.system.User;
import com.ideal.manage.dsp.config.shiro.MyShiroRealm;
import com.ideal.manage.dsp.repository.framework.MySpecification;
import com.ideal.manage.dsp.repository.framework.SpecificationOperator;
import com.ideal.manage.dsp.repository.industry.CompanyRepository;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CompanyService {


    @Resource
    private CompanyRepository companyRepository;

    @Resource
    private SftpConfig sftpConfig;

    @Resource
    private ParameterService parameterService;

    /**
     * 根据id 获取实例
     * @param id
     * @return
     */
    public Company getCompanyById(Long id) {

        return companyRepository.findOne(id);
    }

    /**
     * 获取线上 实例
     * @return
     */
    public Company getCompanyOnline() {

        return null;
    }

    /**
     * 根据类型获取 数据
     * @param code
     * @return
     */
    public Company getOneCompany(String code) {

        return companyRepository.findParAndFelAndStatus(code);
    }

    /**
     * 根据条件获取 数据
     * @param pageNum
     * @param request
     * @return
     */
    public Page<Company> getCompanyData(int pageNum, HttpServletRequest request) {

        List<SpecificationOperator> operators = HttpRequests.getParametersStartingWith(request,"Q_");
        Sort sort = new Sort(Sort.Direction.DESC,"updateTime");
        SpecificationOperator operator = new SpecificationOperator("delFlag","0","EQ");
        operators.add(operator);

        Pageable pageable = new PageRequest(pageNum,15,sort);

        return findAll(operators, pageable);
    }

    /**
     * 获取数据
     * @param operators
     * @param pageable
     * @return
     */
    private Page<Company> findAll(List<SpecificationOperator> operators, Pageable pageable) {

        MySpecification<Company> mySpecifications = new MySpecification<Company>(operators);
        return companyRepository.findAll(mySpecifications, pageable);
    }


    /**
     * 保存 公司简介
     * @param picture
     * @param company
     */
    public void saveCompany(MultipartFile picture, Company company) throws Exception {

//        String pictureUrl = companyFileUpload(picture);
        String pictureUrl = FileUploadUtils.uploadFile(picture, sftpConfig.getPath(), FileUploadUtils.COMPANY_PICTURE_DIR);

        MyShiroRealm.ShiroUser user = (MyShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        if (company.getId() == null) {     //新建

            company.setCreateTime(new Date());
            company.setCreateUser(new User(user.getId()));
            company.setDelFlag("0");
            company.setStatus("0");

        } else {                            //修改

            Company company1 = companyRepository.findOne(company.getId());
            company.setStatus(company1.getStatus());
            company.setDelFlag(company1.getDelFlag());
            company.setCreateTime(company1.getCreateTime());
            company.setCreateUser(company1.getCreateUser());
            if (pictureUrl != null) {
                company.setPictureUrl(pictureUrl);
            } else {
                company.setPictureUrl(company1.getPictureUrl());
            }
        }

        company.setUpdateTime(new Date());
        company.setUpdateUser(new User(user.getId()));

        companyRepository.save(company);
    }


    /**
     * 优秀案例 文件上传
     * @param picture
     * @return
     * @throws Exception
     */
    public String companyFileUpload(MultipartFile picture) throws Exception {

        if (picture != null) {

            //生成文件名
            String pictureName = picture.getOriginalFilename();
            String[] strArr = pictureName.split("\\.");
            String fileName = UUID.randomUUID() + "." + strArr[strArr.length - 1];

            //ftp上传文件
            SftpUtil sftpUtil = new SftpUtil();
            ChannelSftp sftp = sftpUtil.connect(sftpConfig);
            sftpUtil.cd(sftpConfig.getPath(), sftp);
            sftpUtil.mkDir(FileUploadUtils.COMPANY_PICTURE_DIR, sftp);
            sftpUtil.upload(picture, sftp, fileName);

            return "/" +  FileUploadUtils.COMPANY_PICTURE_DIR + "/" + fileName;
        }

        return null;
    }

    /**
     *  上下线
     * @param status
     * @param id
     */
    public String companyStatusUpdate(String status, Long id) {

        String msg = "";

        Company company = companyRepository.findOne(id);

        MyShiroRealm.ShiroUser user = (MyShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        company.setStatus(status);
        company.setUpdateTime(new Date());
        company.setUpdateUser(new User(user.getId()));

        if (status.equals("1")) {

            //控制上线的数据
            List<Company> companyList = companyRepository.findCompanyByCatagory(company.getCategory().getId());
            if (companyList.size() > 0) {

                msg = "操作失败!" + company.getCategory().getName() + "类别已存在上线数据,不能重复上线";
                return msg;
            }

            msg = "上线成功";
        } else if (status.equals("0")){

            msg = "下线成功";
        }

        companyRepository.save(company);
        return msg;
    }

    /**
     * 公司简介 批量删除
     * @param ids
     */
    @Transactional
    public void companyDelete(Long[] ids) {

        List<Company> companies = companyRepository.findAll(Arrays.asList(ids));

        for (Company company : companies) {

            company.setDelFlag("1");
        }

        companyRepository.save(companies);
    }
}
