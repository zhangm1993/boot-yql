package com.ideal.manage.dsp.controller.industryPublic;

import com.ideal.manage.dsp.bean.industry.NewsInformation;
import com.ideal.manage.dsp.bean.system.Parameter;
import com.ideal.manage.dsp.bean.system.ParameterExtend;
import com.ideal.manage.dsp.service.industry.NewsInformationService;
import com.ideal.manage.dsp.service.system.ParameterService;
import com.ideal.manage.dsp.util.PageDtoUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by qq583138329 on 2018/3/12.
 */

@Controller
@RequestMapping("html")
public class NewsInformationHtml {

    @Resource
    private NewsInformationService newsInformationService;

    @Resource
    private ParameterService parameterService;

    /**
     *  新闻页面
     */

    @RequestMapping("news_main")
    @ResponseBody
    public Map<String,Object> newsMain(Long typeId, Integer pageNum, Integer pageSize, HttpServletRequest request){

        Map<String,Object> map = new HashedMap();

        //查询出所有的分类
        List<ParameterExtend> parameters = parameterService.findByAllChildListByCode("15000");
        map.put("parameters",parameters);
        //通过条件，查询出所有的上线新闻资讯
        Page<NewsInformation> newsInformationPage = newsInformationService.findAllOnType(typeId,pageNum,pageSize);
        map.put("newsInformationPage",newsInformationPage);

        return map;
    }

}
