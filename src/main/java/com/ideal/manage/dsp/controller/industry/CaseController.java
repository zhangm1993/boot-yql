package com.ideal.manage.dsp.controller.industry;

import com.ideal.manage.dsp.bean.DTO.PageDto;
import com.ideal.manage.dsp.bean.DTO.Result;
import com.ideal.manage.dsp.bean.industry.Case;
import com.ideal.manage.dsp.bean.system.ParameterExtend;
import com.ideal.manage.dsp.service.industry.CaseService;
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
public class CaseController {

    @Resource
    private CaseService caseService;

    @Resource
    private ParameterService parameterService;

    /**
     * 优秀案例列表请求
     * @param model
     */
    @RequestMapping("case_list")
    public void caseList(Model model) {

    }

    /**
     * 优秀案例列表数据
     * @param pageNum
     * @param request
     * @return
     */
    @RequestMapping("case_data")
    @ResponseBody
    public PageDto caseData(int pageNum, HttpServletRequest request) {

        Page<Case> page = caseService.getCaseData(pageNum, request);
        return PageDtoUtil.getPageDto(page);

    }


    /**
     *  优秀案例状态修改
     * @param request
     * @param status   1:上线  0:下线
     * @param id
     * @return
     */
    @RequestMapping("case_status_update")
    @ResponseBody
    public Result caseStatusUpdate(HttpServletRequest request,
                                      String status, Long id) {

        //保存操作
        caseService.caseStatusUpdate(status, id);

        Result result = new Result();
        result.setType("alert");
        if (status.equals("1")) {
            result.setMessage("上线成功");
        } else if (status.equals("0")){
            result.setMessage("下线成功");
        }
        return result;
    }


    /**
     * 优秀案例保存
     */
    @RequestMapping("case_save")
    @ResponseBody
    public Result caseSave(HttpServletRequest request,
                               @RequestParam(value = "picture", required = false) MultipartFile picture,
                               @RequestParam(value = "homePicture", required = false) MultipartFile homePicture,
                               Case caseObject) throws Exception {

        caseService.caseSave(picture, homePicture, caseObject);

        Result result = new Result();
        result.setType("alert");
        result.setMessage("保存成功");
        return result;
    }


    /**
     * 优秀案例新增请求
     * @param model
     */
    @RequestMapping("case_add")
    public void caseAdd(Model model) {

        List<ParameterExtend> parameters =  parameterService.findByAllChildListByCode("16000");
        model.addAttribute("parameters", parameters);
    }

    /**
     * 优秀案例修改请求
     * @param model
     */
    @RequestMapping("case_edit")
    public void caseEdit(Model model, Long id) {

        List<ParameterExtend> parameters =  parameterService.findByAllChildListByCode("16000");
        model.addAttribute("parameters", parameters);

        Case caseObject = caseService.findCaseById(id);
        model.addAttribute("case", caseObject);
    }

    /**
     * 优秀案例批量删除
     * @param ids
     * @return
     */
    @RequestMapping("case_delete")
    @ResponseBody
    public Result caseDelete(Long[] ids){

        caseService.caseDelete(ids);

        Result result = new Result();
        result.setType("alert");
        result.setMessage("删除成功");
        return result;
    }

    /**
     * 优秀案例首页展示请求
     * @param id
     * @return
     */
    @RequestMapping("case_home")
    public void caseHome(Model model, Long id){

        Case caseObject = caseService.findCaseById(id);
        model.addAttribute("case", caseObject);

    }

    /**
     * 优秀案例首页展示保存
     * @param homePicture
     * @return
     */
    @RequestMapping("case_home_save")
    @ResponseBody
    public Result caseHomeSave(@RequestParam(value = "homePicture", required = false) MultipartFile homePicture,
                                  Long id, String homeRank) throws Exception {

        caseService.caseHomeSave(homePicture, id, homeRank);

        Result result = new Result();
        result.setType("alert");
        result.setMessage("保存成功");
        return result;
    }
}
