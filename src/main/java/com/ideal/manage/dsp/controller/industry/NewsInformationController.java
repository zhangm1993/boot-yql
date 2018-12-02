package com.ideal.manage.dsp.controller.industry;

import com.ideal.manage.dsp.bean.DTO.PageDto;
import com.ideal.manage.dsp.bean.DTO.Result;
import com.ideal.manage.dsp.bean.industry.NewsInformation;
import com.ideal.manage.dsp.bean.system.Parameter;
import com.ideal.manage.dsp.bean.system.ParameterExtend;
import com.ideal.manage.dsp.service.industry.NewsInformationService;
import com.ideal.manage.dsp.service.system.ParameterService;
import com.ideal.manage.dsp.util.PageDtoUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Created by qq583138329 on 2018/3/9.
 */

@Controller
@RequestMapping("industry")
public class NewsInformationController {

    @Resource
    private NewsInformationService newsInformationService;

    @Resource
    private ParameterService parameterService;

    /**
     *  新闻资讯
     */
    @RequestMapping("news_information_list")
    public void newsInformationList(Model model){

    }

    /**
     * 新闻资讯列表数据
     * @param pageNum
     * @param request
     * @return
     */
    @RequestMapping("news_information_data")
    @ResponseBody
    public PageDto newsInformationData(int pageNum, HttpServletRequest request) {

        Page<NewsInformation> page = newsInformationService.getNewsInformationData(pageNum, request);
        return PageDtoUtil.getPageDto(page);

    }

    /**
     * 新闻资讯新增请求
     * @param model
     */
    @RequestMapping("news_information_add")
    public void newsInformationAdd(Model model) {

        //在参数管理里面获取分类
        List<ParameterExtend> parameters =  parameterService.findByAllChildListByCode("15000");
        model.addAttribute("parameters", parameters);
    }

    /**
     * 新闻资讯修改请求
     * @param model
     */
    @RequestMapping("news_information_edit")
    public void newsInformationEdit(Model model, Long id) {

        //在参数管理里面获取分类
        List<ParameterExtend> parameters = parameterService.findByAllChildListByCode("15000");
        model.addAttribute("parameters", parameters);

        //通过id找到一组数据
        NewsInformation newsInformation = newsInformationService.findNewsInformationById(id);
        model.addAttribute("newsInformation",newsInformation);
    }

    /**
     * 新闻资讯保存
     */
    @RequestMapping("news_information_save")
    @ResponseBody
    public Result newsInformationSave(HttpServletRequest request,
                                      @RequestParam(required = false)Long id,
                                      @RequestParam(required = true)String name,
                                      @RequestParam(required = true)Long type,
                                      @RequestParam(required = false)String content) throws IOException {

        NewsInformation newsInformation = new NewsInformation();
        if(id == null){ //新增
            newsInformation.setName(name);
            newsInformation.setType(new Parameter(type));
            newsInformation.setContent(content);
            newsInformation = newsInformationService.addNewsInformation(newsInformation);
        }else{          //修改
            newsInformation = newsInformationService.findNewsInformationById(id);
            newsInformation.setName(name);
            newsInformation.setType(new Parameter(type));
            newsInformation.setContent(content);
            newsInformation = newsInformationService.editNewsInformation(newsInformation);
        }

        Result result = new Result();
        result.setType("success");
        result.setMessage("保存成功");
        return result;
    }

    /**
     *  新闻资讯状态修改
     * @param request
     * @param status   1:上线  0:下线
     * @param id
     * @return
     */
    @RequestMapping("news_information_status_update")
    @ResponseBody
    public Result newsInformationStatusUpdate(HttpServletRequest request,
                                              Long status, Long id) {

        //保存操作
        newsInformationService.statusUpdate(id,status);

        Result result = new Result();
        result.setType("alert");
        if (status.equals(1L)) {
            result.setMessage("上线成功");
        } else if (status.equals(0L)){
            result.setMessage("下线成功");
        }
        return result;
    }


    /**
     * 新闻资讯批量删除
     * @param ids
     * @return
     */
    @RequestMapping("news_information_del")
    @ResponseBody
    public Result produceDelete(Long[] ids){

        newsInformationService.delNewsInformation(ids);

        Result result = new Result();
        result.setType("alert");
        result.setMessage("删除成功");
        return result;
    }


}
