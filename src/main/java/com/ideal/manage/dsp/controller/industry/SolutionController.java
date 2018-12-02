package com.ideal.manage.dsp.controller.industry;

import com.ideal.manage.dsp.bean.DTO.PageDto;
import com.ideal.manage.dsp.bean.DTO.Result;
import com.ideal.manage.dsp.bean.industry.Solution;
import com.ideal.manage.dsp.bean.system.ParameterExtend;
import com.ideal.manage.dsp.service.industry.SolutionService;
import com.ideal.manage.dsp.util.PageDtoUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("industry")
public class SolutionController {

    @Resource
    private SolutionService solutionService;

    /**
     * 解决方案列表请求
     * @param model
     */
    @RequestMapping("solution_list")
    public void solutionList(Model model) {

    }

    /**
     * 解决方案列表数据
     * @param pageNum
     * @param request
     * @return
     */
    @RequestMapping("solution_data")
    @ResponseBody
    public PageDto solutionData(int pageNum, HttpServletRequest request) {

        Page<Solution> page = solutionService.getSolutionData(pageNum, request);
        return PageDtoUtil.getPageDto(page);

    }

    /**
     * 解决方案新增请求
     * @param model
     */
    @RequestMapping("solution_add")
    public void solutionAdd(Model model) {

    }

    /**
     * 解决方案保存
     */
    @RequestMapping("solution_save")
    @ResponseBody
    public Result solutionSave(HttpServletRequest request,
                              @RequestParam(value = "picture", required = false) MultipartFile picture,
                               @RequestParam(value = "homePicture", required = false) MultipartFile homePicture,
                              Solution solution) throws Exception {

        solutionService.SolutionSave(picture, homePicture, solution);

        Result result = new Result();
        result.setType("alert");
        result.setMessage("保存成功");
        return result;
    }

    /**
     *  解决方案状态修改
     * @param request
     * @param status   1:上线  0:下线
     * @param id
     * @return
     */
    @RequestMapping("solution_status_update")
    @ResponseBody
    public Result StatusUpdate(HttpServletRequest request,
                                      String status, Long id) {

        //保存操作
        solutionService.solutionStatusUpdate(status, id);

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
     * 解决方案修改请求
     * @param model
     */
    @RequestMapping("solution_edit")
    public void solutionEdit(Model model, Long id) {

        Solution solution = solutionService.getSolutionById(id);
        model.addAttribute("solution", solution);

    }

    /**
     * 解决方案批量删除
     * @param ids
     * @return
     */
    @RequestMapping("solution_delete")
    @ResponseBody
    public Result solutionDelete(Long[] ids){

        solutionService.solutionDelete(ids);

        Result result = new Result();
        result.setType("alert");
        result.setMessage("删除成功");
        return result;
    }

    /**
     * 产品首页展示请求
     * @param id
     * @return
     */
    @RequestMapping("solution_home")
    public void produceHome(Model model, Long id){

        Solution solution = solutionService.getSolutionById(id);
        model.addAttribute("solution", solution);

    }

    /**
     * 产品首页展示保存
     * @param homePicture
     * @return
     */
    @RequestMapping("solution_home_save")
    @ResponseBody
    public Result solutionHomeSave(@RequestParam(value = "homePicture", required = false) MultipartFile homePicture,
                                  Long id, String homeRank) throws Exception {

        solutionService.solutionHomeSave(homePicture, id, homeRank);

        Result result = new Result();
        result.setType("alert");
        result.setMessage("保存成功");
        return result;
    }
}
