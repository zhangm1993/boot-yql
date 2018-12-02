package com.ideal.manage.dsp.controller.industry;

import com.ideal.manage.dsp.bean.DTO.BootstrapValidatorDto;
import com.ideal.manage.dsp.bean.DTO.PageDto;
import com.ideal.manage.dsp.bean.DTO.Result;
import com.ideal.manage.dsp.bean.industry.Produce;
import com.ideal.manage.dsp.bean.system.Parameter;
import com.ideal.manage.dsp.bean.system.ParameterExtend;
import com.ideal.manage.dsp.service.industry.ProduceService;
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
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("industry")
public class ProduceController {

    @Resource
    private ProduceService produceService;

    @Resource
    private ParameterService parameterService;


    /**
     * 产品列表请求
     * @param model
     */
    @RequestMapping("produce_list")
    public void produceList(Model model) {

    }


    /**
     * 产品列表数据
     * @param pageNum
     * @param request
     * @return
     */
    @RequestMapping("produce_data")
    @ResponseBody
    public PageDto produceData(int pageNum,HttpServletRequest request) {

        Page<Produce> page = produceService.getProduceData(pageNum, request);
        return PageDtoUtil.getPageDto(page);

    }


    /**
     * 产品新增请求
     * @param model
     */
    @RequestMapping("produce_add")
    public void produceAdd(Model model) {

        //获取产品类别
        List<ParameterExtend> parameters =  parameterService.findByAllChildListByCode("12000");
        model.addAttribute("parameters", parameters);

    }

    /**
     * 产品修改请求
     * @param model
     */
    @RequestMapping("produce_edit")
    public void produceEdit(Model model, Long id) {

        Produce produce = produceService.getProduceById(id);
        model.addAttribute("produce", produce);

        //获取产品类别 一级分类
        List<ParameterExtend> parameters =  parameterService.findByAllChildListByCode("12000");
        model.addAttribute("parameters", parameters);
        // Dai 2018-03-27 获取产品二级分类
        List<Parameter> childParameters = parameterService.findByParent(produce.getFirstCate());
        model.addAttribute("childParameters", childParameters);
    }

    /**
     * 产品保存
     */
    @RequestMapping("produce_save")
    @ResponseBody
    public Result produceSave(HttpServletRequest request,
                              @RequestParam(value = "picture", required = false) MultipartFile picture,
                              @RequestParam(value = "homePicture", required = false) MultipartFile homePicture,
                              Produce produce) throws Exception {

        produceService.produceSave(picture, homePicture, produce);


        Result result = new Result();
        result.setType("alert");
        result.setMessage("保存成功");
        return result;
    }

    /**
     *  产品状态修改
     * @param request
     * @param status   1:上线  0:下线
     * @param id
     * @return
     */
    @RequestMapping("produce_status_update")
    @ResponseBody
    public Result produceStatusUpdate(HttpServletRequest request,
                                      String status, Long id) {

        //保存操作
        produceService.produceStatusUpdate(status, id);

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
     * 产品批量删除
     * @param ids
     * @return
     */
    @RequestMapping("produce_delete")
    @ResponseBody
    public Result produceDelete(Long[] ids){

        produceService.produceDelete(ids);

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
    @RequestMapping("produce_home")
    public void produceHome(Model model, Long id){

        Produce produce = produceService.getProduceById(id);
        model.addAttribute("produce", produce);

    }

    /**
     * 产品首页展示保存
     * @return
     */
    @RequestMapping("produce_home_save")
    @ResponseBody
    public Result produceHomeSave(@RequestParam(value = "homePicture", required = false) MultipartFile homePicture,
                                  Long id, String homeRank) throws Exception {

        produceService.produceHomeSave(homePicture, id, homeRank);

        Result result = new Result();
        result.setType("alert");
        result.setMessage("保存成功");
        return result;
    }

    /**
     * 设置推荐商品请求
     * @param id
     * @return
     */
    @RequestMapping("produce_recommend")
    public void produceRecommend(Model model, Long id){

        Produce produce = produceService.getProduceById(id);
        model.addAttribute("produce", produce);

    }

    /**
     *  推荐商品保存
     * @param id
     * @param recommendStatus
     * @return
     */
    @RequestMapping("produce_recommend_save")
    @ResponseBody
    public Result produceRecommendSave(Long id, String recommendStatus) {

        produceService.produceRecommendSave(id, recommendStatus);
        Result result = new Result();
        result.setType("alert");
        result.setMessage("保存成功");
        return result;
    }

    /**
     * 查询产品code是否唯一
     * @param code
     * @return
     */
    @RequestMapping("produce_code_check")
    @ResponseBody
    public BootstrapValidatorDto produceCodeCheck(String code){

        BootstrapValidatorDto bootstrapValidatorDto = new BootstrapValidatorDto();
        if(code != null){
            Produce produce = produceService.finProduceCode(code);
            if(produce != null){
                bootstrapValidatorDto.setValid("false");
            }else {
                bootstrapValidatorDto.setValid("true");
            }
        }else{
            bootstrapValidatorDto.setValid("false");
        }
        return bootstrapValidatorDto;
    }
}
