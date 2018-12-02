package com.ideal.manage.dsp.service.industry;

import com.ideal.manage.dsp.bean.industry.Company;
import com.ideal.manage.dsp.bean.industry.Partner;
import com.ideal.manage.dsp.bean.industry.Produce;
import com.ideal.manage.dsp.bean.system.User;
import com.ideal.manage.dsp.config.shiro.MyShiroRealm;
import com.ideal.manage.dsp.repository.framework.MySpecification;
import com.ideal.manage.dsp.repository.framework.SpecificationOperator;
import com.ideal.manage.dsp.repository.industry.PartnerRepository;
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
public class PartnerService {

    @Resource
    private PartnerRepository partnerRepository;

    @Resource
    private ParameterService parameterService;

    @Resource
    private SftpConfig sftpConfig;


    /**
     * 列表数据
     * @param pageNum
     * @param request
     * @return
     */
    public Page<Partner> getPartnerData(int pageNum, HttpServletRequest request) {

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
    private Page<Partner> findAll(List<SpecificationOperator> operators, Pageable pageable) {

        MySpecification<Partner> mySpecifications = new MySpecification<Partner>(operators);
        return partnerRepository.findAll(mySpecifications, pageable);
    }


    /**
     * 根据id 获取实例
     * @param id
     * @return
     */
    public Partner getPartnerById(Long id) {

        return partnerRepository.findOne(id);
    }

    /**
     * 保存
     * @param picture
     * @param name
     * @param id
     */
    public void partnerSave(MultipartFile picture, String name, Long id) throws Exception {

        //上传文件
//        String pictureUrl = partnerFileUpload(picture);
        String pictureUrl = FileUploadUtils.uploadFile(picture, sftpConfig.getPath(), FileUploadUtils.PARTNER_PICTURE_DIR);

        //封装数据
        Partner partner = null;
        MyShiroRealm.ShiroUser user = (MyShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        if (id == null) {  //新建

            partner = new Partner();
            partner.setCreateTime(new Date());
            partner.setCreateUser(new User(user.getId()));
            partner.setStatus("0");
            partner.setDelFlag("0");

        } else {    //修改

            partner = partnerRepository.findOne(id);
        }

        partner.setUpdateTime(new Date());
        partner.setUpdateUser(new User(user.getId()));
        partner.setName(name);
        if (partner != null) {
            partner.setPictureUrl(pictureUrl);
        }

        //保存
        partnerRepository.save(partner);
    }

    /**
     * 合作伙伴logo文件上传
     * @param picture
     * @return
     * @throws Exception
     */
    public String partnerFileUpload(MultipartFile picture) throws Exception {

        if (picture != null) {

            //生成文件名
            String pictureName = picture.getOriginalFilename();
            String[] strArr = pictureName.split("\\.");
            String fileName = UUID.randomUUID() + "." + strArr[strArr.length - 1];

            //ftp上传文件
            SftpUtil sftpUtil = new SftpUtil();
            ChannelSftp sftp = sftpUtil.connect(sftpConfig);
            sftpUtil.cd(sftpConfig.getPath(), sftp);
            sftpUtil.mkDir(FileUploadUtils.PARTNER_PICTURE_DIR, sftp);
            sftpUtil.upload(picture, sftp, fileName);

            return "/" +  FileUploadUtils.PARTNER_PICTURE_DIR + "/" + fileName;
        }

        return null;
    }


    /**
     * 上线线控制
     * @param status
     * @param id
     * @return
     */
    public String partnerStatusUpdate(String status, Long id) {

        String msg = "";

        Partner partner = partnerRepository.findOne(id);

        //获取控制数量
        if (status.equals("1")) {

            //获取合作伙伴上线最大个数
            int num = Integer.parseInt(parameterService.findByCode("2300001").getRemark().trim());
            //获取在线数据
            List<Partner> partners = partnerRepository.getOnline();

            if (partners.size() == num) {

                msg = "操作失败!数据上线数已达到最高上限数";
                return msg;
            }
            msg = "上线成功";
        } else {

            msg = "下线成功";
        }

        MyShiroRealm.ShiroUser user = (MyShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        partner.setStatus(status);
        partner.setUpdateTime(new Date());
        partner.setUpdateUser(new User(user.getId()));

        //保存
        partnerRepository.save(partner);

        return msg;
    }


    /**
     * 批量删除
     * @param ids
     */
    @Transactional
    public void partnerDelete(Long[] ids) {

        List<Partner> partners = partnerRepository.findAll(Arrays.asList(ids));

        for (Partner partner : partners) {

            partner.setDelFlag("1");

        }

        partnerRepository.save(partners);
    }

    /**
     * 获取在线的数据
     * @return
     */
    public List<Partner> getOnline() {

        return partnerRepository.getOnline();
    }


}
