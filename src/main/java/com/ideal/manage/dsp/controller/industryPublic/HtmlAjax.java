package com.ideal.manage.dsp.controller.industryPublic;

import com.ideal.manage.dsp.bean.industry.Case;
import com.ideal.manage.dsp.bean.industry.Document;
import com.ideal.manage.dsp.bean.industry.Issue;
import com.ideal.manage.dsp.bean.system.Parameter;
import com.ideal.manage.dsp.service.industry.CaseService;
import com.ideal.manage.dsp.service.industry.DocumentService;
import com.ideal.manage.dsp.service.industry.IssueService;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("html")
public class HtmlAjax {

    @Resource
    private DocumentService documentService;

    @Resource
    private IssueService issueService;

    @Resource
    private CaseService caseService;

    @RequestMapping("document_like_ajax")
    public Map<String,Object> doc(Integer pageNum,Integer pageSize, HttpServletRequest request){

        List<Page<Document>> doc=documentService.findType(pageNum,pageSize,request);

        Map<String,Object> map=new HashMap<>();
        map.put("doc_like",doc);
        return map;

    }

    @RequestMapping("iss_sel_id_ajax")
    public Map<String,Object> iss(@RequestParam(name = "id") Long id){

        Issue iss=issueService.findId(id);
        Map<String,Object> map=new HashMap<>();
        map.put("issbean",iss);

        return map;

    }

    @RequestMapping("example_data_ajax")
    public Map<String,Object> example(Integer pageNum,Integer pageSize,@RequestParam(required = false) String type){


        Page<Case> cases= caseService.getCaseAll(pageNum,pageSize,type);

        Map<String,Object> map=new HashMap<>();

        map.put("cases",cases);

        return map;
    }
}
