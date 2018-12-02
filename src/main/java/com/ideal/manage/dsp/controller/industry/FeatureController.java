package com.ideal.manage.dsp.controller.industry;

import com.ideal.manage.dsp.bean.DTO.PageDto;
import com.ideal.manage.dsp.bean.DTO.Result;
import com.ideal.manage.dsp.bean.industry.Feature;
import com.ideal.manage.dsp.bean.system.ParameterExtend;
import com.ideal.manage.dsp.service.industry.FeatureService;
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
public class FeatureController {

    @Resource
    private FeatureService featureService;

    /**
     * 特点列表请求
     * @param model
     */
    @RequestMapping("feature_list")
    public void featureList(Model model) {

    }

    /**
     * 特点列表数据
     * @param pageNum
     * @param request
     * @return
     */
    @RequestMapping("feature_data")
    @ResponseBody
    public PageDto featureData(int pageNum, HttpServletRequest request) {

        Page<Feature> page = featureService.getFeatureData(pageNum, request);
        return PageDtoUtil.getPageDto(page);
    }

    /**
     * 特点新增请求
     * @param model
     */
    @RequestMapping("feature_add")
    public void featureAdd(Model model) {

    }

    /**
     * 特点修改请求
     * @param model
     */
    @RequestMapping("feature_edit")
    public void featureEdit(Model model, Long id) {

        Feature feature = featureService.getFeatureById(id);
        model.addAttribute("feature", feature);
    }

    /**
     *  特点状态修改
     * @param request
     * @param status   1:上线  0:下线
     * @param id
     * @return
     */
    @RequestMapping("feature_status_update")
    @ResponseBody
    public Result featureStatusUpdate(HttpServletRequest request,
                                      String status, Long id) {

        //保存操作
        featureService.featureStatusUpdate(status, id);

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
     * 产品特点保存
     */
    @RequestMapping("feature_save")
    @ResponseBody
    public Result featureSave(HttpServletRequest request,
                              @RequestParam(value = "picture", required = false) MultipartFile picture,
                              Feature feature) throws Exception {

        featureService.featureSave(picture, feature);

        Result result = new Result();
        result.setType("alert");
        result.setMessage("保存成功");
        return result;
    }

    /**
     * 产品特点批量删除
     * @param ids
     * @return
     */
    @RequestMapping("feature_delete")
    @ResponseBody
    public Result featureDelete(Long[] ids){

        featureService.featureDelete(ids);

        Result result = new Result();
        result.setType("alert");
        result.setMessage("删除成功");
        return result;
    }

}
