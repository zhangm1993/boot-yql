package com.ideal.manage.dsp.service.industry;

import com.ideal.manage.dsp.bean.industry.Document;
import com.ideal.manage.dsp.bean.system.Parameter;
import com.ideal.manage.dsp.bean.system.User;
import com.ideal.manage.dsp.config.shiro.MyShiroRealm;
import com.ideal.manage.dsp.repository.framework.MySpecification;
import com.ideal.manage.dsp.repository.framework.SpecificationOperator;
import com.ideal.manage.dsp.repository.industry.DocumentRepository;
import com.ideal.manage.dsp.service.system.ParameterService;
import com.ideal.manage.dsp.util.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * 2018-03-08 liuwei
 */
@Service
public class DocumentService {

    @Resource
    private DocumentRepository documentRepository;

    @Resource
    private ParameterService parameterService;

    @Resource
    private SftpConfig sftpConfig;

    /**
     * 下载文件
     * @param response
     * @param id
     */
    public void download(HttpServletResponse response,Long id){

        Document dt=findyId(id);
        Parameter prss=parameterService.findByCode("1300006");

        CloseableHttpClient httpClient=HttpClients.createDefault();

        String url=prss.getRemark().trim()+dt.getUrl();

        HttpGet get=new HttpGet(url);

//        response.setCharacterEncoding("utf-8");
//        response.setContentType("multipart/form-data");
//        response.setHeader("Content-Disposition", "attachment;fileName=" + dt.getName());

        try {
            CloseableHttpResponse cp=httpClient.execute(get);

            HttpEntity he=cp.getEntity();

           InputStream in=he.getContent();
           InputStreamReader inr= new InputStreamReader(in);
            char[] by=new char[1024];

            int len=0;
            PrintWriter pr=response.getWriter();



            while ((len=inr.read(by))!=-1){
                pr.write(by,0,len);
            }

            pr.flush();
            pr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 分类查询文档
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     */
    public List<Page<Document>> findType(Integer pageNum,Integer pageSize, HttpServletRequest request){

        List<Parameter> parameters=parameterService.findChildByParentCode("140001");

        List<Page<Document>> maps=new ArrayList<>();
        for(Parameter pr:parameters) {
            List<SpecificationOperator> operators = HttpRequests.getParametersStartingWith(request, "Q_");
            Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
            SpecificationOperator operatorStart = new SpecificationOperator("status", "1", "EQ");
            SpecificationOperator operator = new SpecificationOperator("delFlag", "0", "EQ");
            operators.add(operatorStart);
            operators.add(operator);

            operators.add(new SpecificationOperator("type.id", pr.getId(), "EQ"));
            if (pageSize == null) {
                pageSize = 10;
            }
            Pageable pageable = new PageRequest(pageNum, pageSize, sort);

            maps.add(findAll(operators, pageable));
        }
        return maps;
    }

    /**
     * 根据条件查询所有对象
     * @param operators
     * @param pageable
     * @return
     */
    public Page<Document> findAll(List<SpecificationOperator> operators, Pageable pageable){

        MySpecification<Document> mySpecification=new MySpecification<>(operators);

        return documentRepository.findAll(mySpecification,pageable);
    }

    public Page<Document> findAndNum(Integer pageNum,Integer pageSize, HttpServletRequest request){
        List<SpecificationOperator> operators = HttpRequests.getParametersStartingWith(request,"Q_");
        Sort sort = new Sort(Sort.Direction.DESC,"updateTime");
        SpecificationOperator operator = new SpecificationOperator("delFlag","0","EQ");
        operators.add(operator);

        if(pageSize==null){
            pageSize=10;
        }
        Pageable pageable = new PageRequest(pageNum,pageSize,sort);

        return findAll(operators, pageable);
    }


    public Long saveFile(MultipartFile fileName, Document document){
        MyShiroRealm.ShiroUser user = (MyShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();

        if(document.getId()!=null){
            Document docUpdate=findyId(document.getId());

            docUpdate.setName(document.getName());
            docUpdate.setRemark(document.getRemark());
            docUpdate.setType(document.getType());
            docUpdate.setUpdateUser(new User(user.getId()));
            docUpdate.setUpdateTime(new Date());
            save(docUpdate);
            return 1l;
        }


        //生成文件地址
        Parameter parameter = parameterService.findByCode("140002");

        String serverPath = parameter.getRemark().trim();
       // String dirPath = "F:/" + serverPath + "/" +  FileUploadUtils.PRO_PICTURE_DIR;

        //生成文件名
        String pictureName = fileName.getOriginalFilename();
        String[] strArr = pictureName.split("\\.");
        String fName = UUID.randomUUID() + "." + strArr[strArr.length - 1];



        //上传产品图片文件
//        File file = FileUtil.buildFile( dirPath + "/" + fileName, false);
//        try{
//            fileName.transferTo(file);
//        }catch (IOException e){
//            e.printStackTrace();
//            return 0l;
//        }


        //设置文件路径



        document.setName(pictureName);

        document.setCreateUser(new User(user.getId()));
        document.setCreateTime(new Date());

        document.setUpdateUser(new User(user.getId()));
        document.setUpdateTime(new Date());
        document.setStatus(1l);
//
//        SftpUtil sftpUtil = new SftpUtil();
//
//        ChannelSftp sftp = null;

        try {
//            sftp = sftpUtil.connect(sftpConfig);
//            sftpUtil.cd(sftpConfig.getPath(),sftp);
//
//
//            sftpUtil.mkDir(serverPath,sftp);
//            boolean upload = sftpUtil.upload(fileName, sftp,fName);

            String pictureUrl = FileUploadUtils.uploadFile(fileName, sftpConfig.getPath(), serverPath);

            if (pictureUrl != null) {
                document.setUrl(pictureUrl);
            }

            save(document);

        }catch(Exception e){

            e.printStackTrace();
            return 0l;
        }finally {
//            sftpUtil.disconnect(sftp);
        }
        return 1l;
    }

    public Document findyId(Long id){

        return documentRepository.findOne(id);
    }

    public Document save(Document document){
        return documentRepository.save(document);
    }
    @Transactional
    public int updateId(Long[] ids,Long status){

        List<Long> lids=Arrays.asList(ids);

        return documentRepository.updateStatusDoc(lids,status);
    }

    @Transactional
    public int delId(Long[] ids){

        List<Long> lids=Arrays.asList(ids);

        return documentRepository.updateDoc(lids,1l,0l);
    }
}
