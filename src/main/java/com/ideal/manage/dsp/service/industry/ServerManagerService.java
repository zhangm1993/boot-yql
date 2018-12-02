package com.ideal.manage.dsp.service.industry;

import com.ideal.manage.dsp.bean.industry.ServerManager;
import com.ideal.manage.dsp.bean.system.User;
import com.ideal.manage.dsp.config.shiro.MyShiroRealm;
import com.ideal.manage.dsp.repository.framework.MySpecification;
import com.ideal.manage.dsp.repository.framework.SpecificationOperator;
import com.ideal.manage.dsp.repository.industry.ServerManagerRepository;
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
public class ServerManagerService {

    @Resource
    private ServerManagerRepository serverManagerRepository;

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
    public Page<ServerManager> getServerManagerData(int pageNum, HttpServletRequest request) {
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
    private Page<ServerManager> findAll(List<SpecificationOperator> operators, Pageable pageable) {

        MySpecification<ServerManager> mySpecifications = new MySpecification<ServerManager>(operators);
        return serverManagerRepository.findAll(mySpecifications, pageable);
    }


    /**
     * 根据id 获取实例
     * @param id
     * @return
     */
    public ServerManager getServerManagerById(Long id) {

        return serverManagerRepository.findOne(id);
    }

    /**
     * 保存 服务管理数据
     * @param picture
     * @param name
     * @param description
     * @param id
     */
    public void serverManagerSave(MultipartFile picture, MultipartFile backgroundPicture, String name, String description, Long id) throws Exception {

        //上传文件
//        String pictureUrl = serverManagerFileUpload(picture);
        String pictureUrl = FileUploadUtils.uploadFile(picture, sftpConfig.getPath(), FileUploadUtils.SERVER_PICTURE_DIR);
        String backgroundUrl = FileUploadUtils.uploadFile(backgroundPicture, sftpConfig.getPath(), FileUploadUtils.SERVER_PICTURE_DIR);

        //封装数据
        ServerManager serverManager = null;
        MyShiroRealm.ShiroUser user = (MyShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        if (id == null) {    //新建

            serverManager = new ServerManager();
            serverManager.setStatus("0");
            serverManager.setDelFlag("0");
            serverManager.setCreateTime(new Date());
            serverManager.setCreateUser(new User(user.getId()));
        } else {   //修改

            serverManager = serverManagerRepository.findOne(id);
        }

        if (pictureUrl != null) {
            serverManager.setPictureUrl(pictureUrl);
        }
        if (backgroundUrl != null) {
            serverManager.setBackgroundUrl(backgroundUrl);
        }
        serverManager.setName(name);
        serverManager.setDescription(description);
        serverManager.setUpdateTime(new Date());
        serverManager.setUpdateUser(new User(user.getId()));

        //保存数据
        serverManagerRepository.save(serverManager);
    }

    /**
     * logo文件上传
     * @param picture
     * @return
     * @throws Exception
     */
    public String serverManagerFileUpload(MultipartFile picture) throws Exception {

        if (picture != null) {

            //生成文件名
            String pictureName = picture.getOriginalFilename();
            String[] strArr = pictureName.split("\\.");
            String fileName = UUID.randomUUID() + "." + strArr[strArr.length - 1];

            //ftp上传文件
            SftpUtil sftpUtil = new SftpUtil();
            ChannelSftp sftp = sftpUtil.connect(sftpConfig);
            sftpUtil.cd(sftpConfig.getPath(), sftp);
            sftpUtil.mkDir(FileUploadUtils.SERVER_PICTURE_DIR, sftp);
            sftpUtil.upload(picture, sftp, fileName);

            return "/" +  FileUploadUtils.SERVER_PICTURE_DIR + "/" + fileName;
        }

        return null;
    }

    /**
     * 上下线控制
     * @param status
     * @param id
     * @return
     */
    public String serverManagerStatusUpdate(String status, Long id) {

        String msg = "";

        ServerManager serverManager = serverManagerRepository.findOne(id);
        if (status.equals("1")) {

            //获取产品服务管理上线最大个数
            int num = Integer.parseInt(parameterService.findByCode("2300004").getRemark().trim());
            //获取上线的数据
            List<ServerManager> serverManagers = serverManagerRepository.findOnline();

            if (serverManagers.size() == num) {

                msg = "操作失败!数据上线数已达到最高上限数";
                return msg;
            }
            msg = "上线成功";
        } else {

            msg = "下线成功";
        }

        MyShiroRealm.ShiroUser user = (MyShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        serverManager.setStatus(status);
        serverManager.setUpdateTime(new Date());
        serverManager.setUpdateUser(new User(user.getId()));

        //保存数据
        serverManagerRepository.save(serverManager);

        return msg;
    }

    /**
     *  批量删除
     * @param ids
     */
    @Transactional
    public void serverManagerDelete(Long[] ids) {

        List<ServerManager> serverManagers = serverManagerRepository.findAll(Arrays.asList(ids));

        for (ServerManager serverManager : serverManagers) {

            serverManager.setDelFlag("1");
        }

        serverManagerRepository.save(serverManagers);
    }

    /**
     * 获取线上数据
     * @return
     */
    public List<ServerManager> getOnline() {

        return serverManagerRepository.findOnline();
    }
}
