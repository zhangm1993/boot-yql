package com.ideal.manage.dsp.config.servletFilter;

import com.ideal.manage.dsp.bean.industry.Produce;
import com.ideal.manage.dsp.bean.industry.Solution;
import com.ideal.manage.dsp.bean.system.Parameter;
import com.ideal.manage.dsp.bean.system.ParameterExtend;
import com.ideal.manage.dsp.config.Component.AuthorConfig;
import com.ideal.manage.dsp.service.industry.ProduceService;
import com.ideal.manage.dsp.service.industry.SolutionService;
import com.ideal.manage.dsp.service.system.ParameterService;
import org.springframework.data.domain.Page;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class HtmlFilter implements HandlerInterceptor {

    @Resource
    private ParameterService parameterService;

    @Resource
    private ProduceService produceService;

    @Resource
    private SolutionService solutionService;

    @Resource
    private AuthorConfig authorConfig;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //查询产品分类
        List<ParameterExtend> parameter=parameterService.findByAllChildListByCode("12000");
        //图片链接前缀
        Parameter paHttp=parameterService.findByCode("1300006");
        //导航商品
        Page<Produce> producesAll=produceService.findAllDesc(request);
        //导航解决方案
        Page<Solution> solutionsh=solutionService.getSolutionAll(0,request);
        //查询尾部分类信息
        List<ParameterExtend> anExtends=parameterService.findByAllChildListByCode("21000");
        //控制台系统ip
        String ip = parameterService.findByCode("24000").getRemark().trim();

        request.setAttribute("producel",parameter);
        request.setAttribute("paHttp",paHttp);

        request.setAttribute("producesAll",producesAll);
        request.setAttribute("solutionsh",solutionsh);

        request.setAttribute("footTit",anExtends);
        request.setAttribute("control",authorConfig.getControl());

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
