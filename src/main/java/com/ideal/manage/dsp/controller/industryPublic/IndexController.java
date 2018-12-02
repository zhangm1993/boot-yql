package com.ideal.manage.dsp.controller.industryPublic;

import com.alibaba.fastjson.JSON;
import com.ideal.manage.dsp.bean.industry.*;
import com.ideal.manage.dsp.bean.system.Parameter;
import com.ideal.manage.dsp.config.Component.AuthorConfig;
import com.ideal.manage.dsp.service.industry.*;
import com.ideal.manage.dsp.service.system.ParameterService;
import com.ideal.manage.dsp.util.FileUploadUtils;
import com.ideal.manage.dsp.util.HttpClientUtil;
import com.ideal.manage.dsp.util.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

    @Resource
    private ProduceService produceService;

    @Resource
    private SolutionService solutionService;

    @Resource
    private CarouselService carouselService;

    @Resource
    private FeatureService featureService;

    @Resource
    private ServerManagerService serverManagerService;

    @Resource
    private PartnerService partnerService;

    @Resource
    private AuthorConfig authorConfig;

    @Resource
    private ParameterService parameterService;


    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);


//    /**
//     * 主页
//     * @param model
//     * @param request
//     */
//    @RequestMapping("index")
//    public void index(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes){
//
//        //首页轮播
//        Page<Carousel> carousels=carouselService.getCarouselHtml(0,request,null);
//
////        //首页商品
//        Page<Produce> producesIndex=produceService.findAllIndex(0,4);
//
//        List<ServerManager> serverManagers = serverManagerService.getOnline();
//
//
//        //首页优秀案例
////        Page<Case> cases=caseService.getCaseHtml(0,4);
//
//        //首页解决方案
//        Page<Solution> solution=solutionService.getSolutionIndex(0,10);
//        //主页产品特点
//        Page<Feature> features=featureService.getFeatureDataHtml(0,request);
//
//        List<Partner> partners = partnerService.getOnline();
//
//
//
//        model.addAttribute("carousels",carousels.getContent());
//        model.addAttribute("producesIndex",producesIndex.getContent());
//        model.addAttribute("solutionIndex",solution.getContent());
//        model.addAttribute("featureIndex",features.getContent());
//        model.addAttribute("serverManagers", serverManagers);
//        model.addAttribute("partners", partners);
//
//        //页面标题
//        model.addAttribute("name","主页");
//    }


    /**
     * 控制台登录
     */
    @RequestMapping("control_login")
    public String controlLogin(HttpServletRequest request) throws Exception {

        String accessToken = (String) request.getAttribute("accessToken");

        String url = "http://" + authorConfig.getIp() + ":" + authorConfig.getPort() + "/bigdatadesktop/protals/getUserNameFromAccessToken";
        Map<String,String> params = new HashMap<>();
        params.put("accessToken", accessToken);
        params.put("clientId", authorConfig.getClientId());

        logger.info("开始获取用户信息:url=" + url);
        String result = HttpUtils.post(url, JSON.toJSONString(params), "application/json;charset=UTF-8");
        logger.info("获取用户信息成功:result=" + result);
        Map<String, Map<String, String>> resultMap = JSON.parseObject(result, Map.class);

        request.getSession().setAttribute("user", resultMap.get("respContent"));

        return "redirect:html/index";
    }


    /**
     * 控制台注销
     */
    @RequestMapping("control_logout")
    public String controlLogout(HttpServletRequest request) throws Exception {

//        String accessToken = (String) request.getSession().getAttribute("accessToken");
//        String PSID = (String) request.getSession().getAttribute("PSID");
//        Map<String, String> user = (Map<String, String>) request.getSession().getAttribute("user");
//
//        String url = authorConfig.getAuthPath() + "/oauth/logout";
//        Map<String,String> params = new HashMap<>();
//        params.put("PSID", PSID);
//        params.put("access_token", accessToken);
//        params.put("username", user.get("userName"));
//        params.put("client_id", authorConfig.getClientId());
//        params.put("client_secret", authorConfig.getClientSecret());
//        System.out.println("JSON参数:" + JSON.toJSONString(params));
//
//        String result = HttpUtils.post(url, JSON.toJSONString(params), "application/json;charset=UTF-8");
//        System.out.println("登出:" + result);

        //accessToken
        request.getSession().removeAttribute("accessToken");
        request.getSession().removeAttribute("user");
        request.getSession().removeAttribute("PSID");
        return "redirect:html/index";
    }
}
