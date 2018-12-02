package com.ideal.manage.dsp.controller.industry;

import com.ideal.manage.dsp.bean.DTO.PageDto;
import com.ideal.manage.dsp.bean.DTO.Result;
import com.ideal.manage.dsp.bean.industry.Carousel;
import com.ideal.manage.dsp.bean.system.ParameterExtend;
import com.ideal.manage.dsp.service.industry.CarouselService;
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
public class CarouselController {

    @Resource
    private CarouselService carouselService;

    @Resource
    private ParameterService parameterService;

    /**
     * 轮播列表请求
     * @param model
     */
    @RequestMapping("carousel_list")
    public void carouselList(Model model) {

        //获取轮播类型
        List<ParameterExtend> parameters =  parameterService.findByAllChildListByCode("17000");
        model.addAttribute("parameters", parameters);
    }

    /**
     * 轮播列表数据
     * @param pageNum
     * @param request
     * @return
     */
    @RequestMapping("carousel_data")
    @ResponseBody
    public PageDto carouselData(int pageNum, HttpServletRequest request) {

        Page<Carousel> page = carouselService.getCarouselData(pageNum, request);
        return PageDtoUtil.getPageDto(page);
    }

    /**
     *  轮播状态修改
     * @param request
     * @param status   1:上线  0:下线
     * @param id
     * @return
     */
    @RequestMapping("carousel_status_update")
    @ResponseBody
    public Result carouselStatusUpdate(HttpServletRequest request,
                                   String status, Long id) {

        //保存操作
        carouselService.carouselStatusUpdate(status, id);

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
     * 轮播新增请求
     * @param model
     * @return
     */
    @RequestMapping("carousel_add")
    public void carouselAdd(Model model) {

        //获取轮播类型
        List<ParameterExtend> parameters =  parameterService.findByAllChildListByCode("17000");
        model.addAttribute("parameters", parameters);
    }

    /**
     * 轮播保存
     */
    @RequestMapping("carousel_save")
    @ResponseBody
    public Result carouselSave(HttpServletRequest request,
                           @RequestParam(value = "picture", required = false) MultipartFile picture,
                           Carousel carousel) throws Exception {

        carouselService.carouselSave(picture, carousel);

        Result result = new Result();
        result.setType("alert");
        result.setMessage("保存成功");
        return result;
    }

    /**
     * 轮播修改请求
     * @param model
     */
    @RequestMapping("carousel_edit")
    public void carouselEdit(Model model, Long id) {

        List<ParameterExtend> parameters =  parameterService.findByAllChildListByCode("17000");
        model.addAttribute("parameters", parameters);

        Carousel carousel = carouselService.findCarouselById(id);
        model.addAttribute("carousel", carousel);
    }

    /**
     * 轮播批量删除
     * @param ids
     * @return
     */
    @RequestMapping("carousel_delete")
    @ResponseBody
    public Result carouselDelete(Long[] ids){

        carouselService.carouselDelete(ids);

        Result result = new Result();
        result.setType("alert");
        result.setMessage("删除成功");
        return result;
    }

    /**
     * 首页轮播请求
     * @param model
     */
    @RequestMapping("carousel_home")
    public void carouselHome(Model model, Long id) {

        Carousel carousel = carouselService.findCarouselById(id);
        model.addAttribute("carousel", carousel);
    }


    /**
     * 首页轮播保存
     */
    @RequestMapping("carousel_home_save")
    @ResponseBody
    public Result carouselHomeSave(HttpServletRequest request,
                               @RequestParam(value = "id") Long id,
                                   @RequestParam(value = "homeRank") String homeRank) throws Exception {

        carouselService.carouselHomeSave(id, homeRank);

        Result result = new Result();
        result.setType("alert");
        result.setMessage("保存成功");
        return result;
    }
}
