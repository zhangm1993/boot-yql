package com.ideal.manage.dsp.controller.industry;

import com.ideal.manage.dsp.bean.DTO.PageDto;
import com.ideal.manage.dsp.bean.industry.Company;
import com.ideal.manage.dsp.bean.industry.Consult;
import com.ideal.manage.dsp.bean.system.Parameter;
import com.ideal.manage.dsp.bean.system.ParameterExtend;
import com.ideal.manage.dsp.service.industry.CaseService;
import com.ideal.manage.dsp.service.industry.ConsultService;
import com.ideal.manage.dsp.service.industry.ProduceService;
import com.ideal.manage.dsp.service.industry.SolutionService;
import com.ideal.manage.dsp.service.system.ParameterService;
import com.ideal.manage.dsp.util.PageDtoUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("industry")
public class ConsultController {

    @Resource
    private ConsultService consultService;

    @Resource
    private ParameterService parameterService;

    /**
     * 用户咨询列表请求
     * @param model
     */
    @RequestMapping("consult_list")
    public void consultList(Model model) {

    }

    /**
     * 用户咨询列表数据
     * @param pageNum
     * @param request
     * @return
     */
    @RequestMapping("consult_data")
    @ResponseBody
    public PageDto consultData(Model model, int pageNum, HttpServletRequest request) {

        //附件 基础路径
        String url = parameterService.findByCode("1300006").getRemark();

        Page<Consult> page = consultService.getConsultData(pageNum, request);

        //格式化 对象名称
        for (Consult consult : page.getContent()) {

            if (consult.getAccessoryUrl() != null) {
                consult.setAccessoryUrl(url + consult.getAccessoryUrl());
            }
            consultService.formatObjectName(consult);
        }
        return PageDtoUtil.getPageDto(page);

    }

    /**
     *  用户咨询展示
     * @param model
     */
    @RequestMapping("consult_show")
    public void consultShow(Model model,  Long id) {

        Consult consult = consultService.getConsultById(id);
        model.addAttribute("consult", consultService.formatObjectName(consult));

        List<Parameter> parameters=parameterService.findChildByParentCode("25000");
        model.addAttribute("parameters",parameters);
    }

}
