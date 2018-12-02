package com.ideal.manage.dsp.controller.industry;


import com.ideal.manage.dsp.bean.DTO.PageDto;
import com.ideal.manage.dsp.bean.DTO.Result;
import com.ideal.manage.dsp.bean.industry.Company;
import com.ideal.manage.dsp.bean.system.ParameterExtend;
import com.ideal.manage.dsp.service.industry.CompanyService;
import com.ideal.manage.dsp.service.system.ParameterService;
import com.ideal.manage.dsp.util.PageDtoUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("industry")
public class CompanyController {

    @Resource
    private CompanyService companyService;

    @Resource
    private ParameterService parameterService;

    /**
     * 公司简介列表请求
     * @param model
     */
    @RequestMapping("company_list")
    public void companyList(Model model) {

    }


    /**
     * 公司简介列表数据
     * @param pageNum
     * @param request
     * @return
     */
    @RequestMapping("company_data")
    @ResponseBody
    public PageDto companyData(int pageNum, HttpServletRequest request) {

        Page<Company> page = companyService.getCompanyData(pageNum, request);
        return PageDtoUtil.getPageDto(page);

    }


    /**
     * 公司简介保存
     */
    @RequestMapping("company_save")
    @ResponseBody
    public Result companySave(HttpServletRequest request,
                           @RequestParam(value = "picture", required = false) MultipartFile picture,
                           Company company) throws Exception {

        companyService.saveCompany(picture, company);

        Result result = new Result();
        result.setType("alert");
        result.setMessage("保存成功");
        return result;
    }

    /**
     * 公司简介新增请求
     * @param model
     */
    @RequestMapping("company_add")
    public void companyAdd(Model model) {

        List<ParameterExtend> parameters =  parameterService.findByAllChildListByCode("20000");
        model.addAttribute("parameters", parameters);
    }

    /**
     * 公司简介修改请求
     * @param model
     */
    @RequestMapping("company_edit")
    public void companyEdit(Model model,  Long id) {

        List<ParameterExtend> parameters =  parameterService.findByAllChildListByCode("20000");
        model.addAttribute("parameters", parameters);

        Company company = companyService.getCompanyById(id);
        model.addAttribute("company", company);
    }

    /**
     *  公司简介状态修改
     * @param request
     * @param status   1:上线  0:下线
     * @param id
     * @return
     */
    @RequestMapping("company_status_update")
    @ResponseBody
    public Result companyStatusUpdate(HttpServletRequest request,
                                   String status, Long id) {

        //保存操作
        String msg = companyService.companyStatusUpdate(status, id);

        Result result = new Result();
        result.setType("alert");
        result.setMessage(msg);
        return result;
    }


    /**
     *  公司简介批量删除
     * @param ids
     * @return
     */
    @RequestMapping("company_delete")
    @ResponseBody
    public Result companyDelete(Long[] ids){

        companyService.companyDelete(ids);

        Result result = new Result();
        result.setType("alert");
        result.setMessage("删除成功");
        return result;
    }

}
