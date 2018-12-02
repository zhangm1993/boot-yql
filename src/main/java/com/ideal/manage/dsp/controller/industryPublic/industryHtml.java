package com.ideal.manage.dsp.controller.industryPublic;

import com.ideal.manage.dsp.bean.DTO.Result;
import com.ideal.manage.dsp.bean.industry.*;
import com.ideal.manage.dsp.bean.system.Parameter;
import com.ideal.manage.dsp.bean.system.ParameterExtend;
import com.ideal.manage.dsp.service.industry.*;
import com.ideal.manage.dsp.service.system.ParameterService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("html")
public class industryHtml {

    @Resource
    private ParameterService parameterService;

    @Resource
    private ProduceService produceService;

    @Resource
    private DocumentService documentService;

    @Resource
    private SolutionService solutionService;

    @Resource
    private CaseService caseService;

    @Resource
    private IssueService issueService;

    @Resource
    private NewsInformationService newsInformationService;

    @Resource
    private CarouselService carouselService;

    @Resource
    private FeatureService featureService;

    @Resource
    private CompanyService companyService;

    @Resource
    private ServerManagerService serverManagerService;

    @Resource
    private PartnerService partnerService;

    @Resource
    private ConsultService consultService;

    /**
     * 主页
     * @param model
     * @param request
     */
    @RequestMapping("index")
    public void index(Model model,HttpServletRequest request){

        if (request.getSession().getAttribute("PSID") == null) {
            request.getSession().setAttribute("PSID", UUID.randomUUID().toString());
        }

        if (request.getSession().getAttribute("state") == null) {
            request.getSession().setAttribute("state", UUID.randomUUID().toString());
        }


        //首页轮播
        Page<Carousel> carousels=carouselService.getCarouselHtml(0,request,null);

//        //首页商品
        Page<Produce> producesIndex=produceService.findAllIndex(0,4);

        List<ServerManager> serverManagers = serverManagerService.getOnline();


        //首页优秀案例
//        int number = Integer.parseInt(parameterService.findByCode("2300005").getRemark());
        Page<Case> cases=caseService.getCaseHtml(0, 4);

        //首页解决方案
//        Page<Solution> solution=solutionService.getSolutionIndex(0,10);
        //主页产品特点
//        Page<Feature> features=featureService.getFeatureDataHtml(0,request);

        List<Partner> partners = partnerService.getOnline();

        //平台数据
        Parameter p1 = parameterService.findByCode("110000001");   //平台用户总数
        Parameter p2 = parameterService.findByCode("110000002");  //平台服务的企业用户数
        Parameter p3 = parameterService.findByCode("110000003");  //平台工业数据量
        Parameter p4 = parameterService.findByCode("110000004");  //设备在线连接数

        model.addAttribute("carousels",carousels.getContent());
        model.addAttribute("producesIndex",producesIndex.getContent());
        model.addAttribute("cases",cases.getContent());
//        model.addAttribute("featureIndex",features.getContent());
        model.addAttribute("serverManagers", serverManagers);
        model.addAttribute("partners", partners);

        model.addAttribute("p1", p1);
        model.addAttribute("p2", p2);
        model.addAttribute("p3", p3);
        model.addAttribute("p4", p4);

        //页面标题
        model.addAttribute("name","翼联工业互联网门户");
//        model.addAttribute("name","江西翼联工业云平台");

    }

    /**
     * 帮助文档
     * @param model
     * @param request
     */
    @RequestMapping("document")
    public void document(Model model, HttpServletRequest request){

        List<Parameter> parameters=parameterService.findChildByParentCode("140001");

        Parameter parameter=parameterService.findByCode("1300006");

        List<Page<Document>> pages=new ArrayList<>();


        Page<Issue> iss=issueService.findByAllHtml(0,15,request);

        model.addAttribute("iss",iss);

        //页面标题
        model.addAttribute("name","帮助文档");

        List<Page<Document>> doc=documentService.findType(0,10,request);

        model.addAttribute("url",parameter.getRemark().trim());
        model.addAttribute("pages",doc);
        model.addAttribute("docTypes",parameters);

    }

    /**
     * 文档下载
     * @param response
     * @param id
     */
    @RequestMapping("doc_download")
    public void download(HttpServletResponse response,Long id){
        documentService.download(response,id);
    }

    /**
     * 优秀案例
     * @param model
     * @param request
     */
    @RequestMapping("example")
    public void example(Model model,HttpServletRequest request){

        List<Parameter> paCaseType=parameterService.findChildByParentCode("16000");


        Parameter par=parameterService.findByCode("1700002");
        //优秀案例轮播
        Page<Carousel> cars=carouselService.getCarouselHtml(0,request,par);

        Page<Case> cases= caseService.getCaseAll(0,6,null);


        model.addAttribute("paCaseType",paCaseType);

        model.addAttribute("caseh",cases);
        model.addAttribute("carsl",cars.getContent());

        //页面标题
        model.addAttribute("name","优秀案例");

    }

    /**
     * 新闻列表页面
     * @param model
     * @param request
     */
    @RequestMapping("news")
    public void news(Model model,HttpServletRequest request){

        Parameter par=parameterService.findByCode("1700003");
        //优秀案例轮播
        Page<Carousel> cars=carouselService.getCarouselHtml(0,request,par);

        //页面标题
        model.addAttribute("name","新闻资讯");
        model.addAttribute("caseNews",cars.getContent());

        //获取推荐商品
        Page<Produce> produces = produceService.getRecommendProduce();
        model.addAttribute("produces", produces.getContent());

    }

    /**
     * 详细新闻页面
     * @param model
     * @param id
     */
    @RequestMapping("news-detail")
    public void newsDetail(Model model,@RequestParam(value="id",required = false)Long id,
                           @RequestParam(value = "preview", required = false) String preview) throws Exception {

        //页面标题
        model.addAttribute("name","文章信息");

        if (id != null && preview!= null) {   //预览

            NewsInformation newsInformation = newsInformationService.findNewsInformationById(id);
            if (newsInformation.getDelFlag() == 0L) {

                model.addAttribute("newsInformation",newsInformation);
                return;
            }
        }

        //如果id有值
        if(id != null){
            NewsInformation newsInformation = newsInformationService.findNewsInformationById(id);

            if (newsInformation.getStatus() == 1L && newsInformation.getDelFlag() == 0L) {

                model.addAttribute("newsInformation",newsInformation);
                //获取推荐商品
                Page<Produce> produces = produceService.getRecommendProduce();
                model.addAttribute("produces", produces.getContent());
                return;
            }
        }
        throw new Exception();
    }

    /**
     * 产品服务
     * @param model
     * @param id
     */
    @RequestMapping("product")
    public void product(Model model, @RequestParam(value = "id", defaultValue = "6") Long id,
                        @RequestParam(value = "preview", required = false) String preview) throws Exception {

        //页面标题
        model.addAttribute("name","产品服务");

        if (id != null && preview!= null) {   //预览

            Produce produce = produceService.getProduceById(id);

            if (produce.getDelFlag().equals("0")) {  //没有被删除

                model.addAttribute("produce", produce);
                return;
            }

        }

        if (id != null) {

            Produce produce = produceService.getProduceById(id);

            if (produce.getStatus().equals("1") && produce.getDelFlag().equals("0")) {    //上线 非删除

                model.addAttribute("produce", produce);
                return;
            }

        }
        throw new Exception();
    }

    /**
     * 解决方案
     * @param model
     * @param id
     */
    @RequestMapping("solve_plan")
    public void solve_plan(Model model, @RequestParam(value = "id", required = false) Long id,
                           @RequestParam(value = "preview", required = false) String preview) throws Exception {

        //页面标题
        model.addAttribute("name","解决方案");

        if (id != null && preview!= null) {   //预览

            Solution solution = solutionService.getSolutionById(id);
            if (solution.getDelFlag().equals("0")) {
                model.addAttribute("solution", solution);
                return;
            }
        }

        if (id != null) {

            Solution solution = solutionService.getSolutionById(id);
            if (solution.getStatus().equals("1") && solution.getDelFlag().equals("0")) {  //已上线

                model.addAttribute("solution", solution);
                return;
            }
        }
        throw new Exception();
    }

    /**
     * 优秀案例
     * @param model
     * @param id
     */
    @RequestMapping("example-detail")
    public void exampleDetail(Model model, @RequestParam(value = "id", required = false) Long id,
                              @RequestParam(value = "preview", required = false) String preview) throws Exception {

        model.addAttribute("name","优秀案例具体信息");

        if (id != null && preview!= null) {   //预览

            Case caseObject = caseService.findCaseById(id);
            model.addAttribute("case", caseObject);
            return;
        }

        if (id != null) {

            Case caseObject = caseService.findCaseById(id);
            if (caseObject.getStatus().equals("1") && caseObject.getDelFlag().equals("0")) {

                model.addAttribute("case", caseObject);
                return;
            }
        }

        throw new Exception();
    }


    /**
     *  联系我们
     * @param model
     */
    @RequestMapping("contact")
    public void contact(Model model){

        List<Parameter> parameters=parameterService.findChildByParentCode("22000");
        model.addAttribute("conts",parameters);
    }


    /**
     *  联系我们
     * @param model
     */
    @RequestMapping("company")
    public void company(Model model,
                        @RequestParam(value = "code", required = false) String code,
                        @RequestParam(value = "id", required = false) Long id,
                        @RequestParam(value = "preview", required = false) String preview) throws Exception {

        if (id != null && preview!= null) {     //预览

            Company company = companyService.getCompanyById(id);
            model.addAttribute("company", company);
            return;
        }

        if (code != null) {                     //页脚请求

            Company company = companyService.getOneCompany(code);
            model.addAttribute("company", company);
            return;
        }

        throw new Exception();
    }

    /**
     *  咨询我们
     * @param model
     */
    @RequestMapping("consult")
    public void consult(Model model, Consult consult){

        List<Parameter> parameters=parameterService.findChildByParentCode("25000");
        model.addAttribute("parameters",parameters);

        if (consult.getType().equals("1")) {    //产品

            Produce produce = produceService.getProduceById(consult.getId());
            consult.setObjectName(produce.getName());
        } else if (consult.getType().equals("2")) {  //解决方案

            Solution solution = solutionService.getSolutionById(consult.getId());
            consult.setObjectName(solution.getName());
        } else {   //成功阿里

            Case caseById = caseService.findCaseById(consult.getId());
            consult.setObjectName(caseById.getName());
        }
    }


    /**
     * 咨询信息保存
     */
    @RequestMapping("consult_save")
    @ResponseBody
    public Result caseSave(HttpServletRequest request,
                           @RequestParam(value = "accessory", required = false) MultipartFile accessory,
                           Consult consult) throws Exception {

        consultService.saveConsult(accessory, consult);

        Result result = new Result();
        result.setType("alert");
        result.setMessage("咨询成功");
        return result;
    }
}
