package com.ideal.manage.dsp.service.industry;

import com.ideal.manage.dsp.bean.industry.Company;
import com.ideal.manage.dsp.bean.industry.Consult;
import com.ideal.manage.dsp.repository.framework.MySpecification;
import com.ideal.manage.dsp.repository.framework.SpecificationOperator;
import com.ideal.manage.dsp.repository.industry.ConsultRepository;
import com.ideal.manage.dsp.util.FileUploadUtils;
import com.ideal.manage.dsp.util.HttpRequests;
import com.ideal.manage.dsp.util.SftpConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class ConsultService {

    @Resource
    private ConsultRepository consultRepository;

    @Resource
    private SftpConfig sftpConfig;

    @Resource
    private ProduceService produceService;

    @Resource
    private SolutionService solutionService;

    @Resource
    private CaseService caseService;


    /**
     *  根据条件查询 用户咨询信息
     * @param pageNum
     * @param request
     * @return
     */
    public Page<Consult> getConsultData(int pageNum, HttpServletRequest request) {

        List<SpecificationOperator> operators = HttpRequests.getParametersStartingWith(request,"Q_");
        Sort sort = new Sort(Sort.Direction.DESC,"createTime");

        Pageable pageable = new PageRequest(pageNum,15,sort);

        return findAll(operators, pageable);
    }

    /**
     * 获取 所有用户咨询
     * @param operators
     * @param pageable
     * @return
     */
    private Page<Consult> findAll(List<SpecificationOperator> operators, Pageable pageable) {

        MySpecification<Consult> mySpecifications = new MySpecification<Consult>(operators);
        return consultRepository.findAll(mySpecifications, pageable);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public Consult getConsultById(Long id) {

        return consultRepository.findOne(id);
    }

    /**
     * 保存 咨询信息
     * @param accessory
     * @param consult
     */
    public void saveConsult(MultipartFile accessory, Consult consult) throws IOException {

        String accessoryUrl = FileUploadUtils.uploadFile(accessory, sftpConfig.getPath(), FileUploadUtils.CONSULT_PICTURE_DIR);
        if (accessoryUrl != null) {

            consult.setAccessoryUrl(accessoryUrl);
        }

        consult.setCreateTime(new Date());

        consultRepository.save(consult);
    }

    public Consult formatObjectName(Consult consult) {

        int type = Integer.parseInt(consult.getType());
        String objectName = "";
        switch (type) {
            case 1:    //产品
                objectName = produceService.getProduceById(consult.getObjectId()).getName();
                break;
            case 2:
                objectName = solutionService.getSolutionById(consult.getObjectId()).getName();
                break;
            case 3:
                objectName = caseService.findCaseById(consult.getObjectId()).getName();
                break;
        }

        consult.setObjectName(objectName);
        return consult;
    }
}
