package com.ideal.manage.dsp.controller.industry;

import com.ideal.manage.dsp.bean.DTO.PageDto;
import com.ideal.manage.dsp.bean.DTO.Result;
import com.ideal.manage.dsp.bean.industry.Issue;
import com.ideal.manage.dsp.service.industry.IssueService;
import com.ideal.manage.dsp.util.PageDtoUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/*
* 2018-03-12 liuwei
* */
@Controller
@RequestMapping("industry")
public class IssueController {

    @Resource
    private IssueService issueService;


    @RequestMapping("issue_list")
    public void issueList(Model model){

    }

    @RequestMapping("issue_add")
    public void issueAdd(Model model){

    }
    @RequestMapping("issue_update")
    @ResponseBody
    public Result issueUpdate(Long status,Long id){

       int i= issueService.issUpdate(status,id);

        Result result = new Result();
        result.setType("alert");
        if(i!=0){
            result.setMessage("修改成功");
        }else{
            result.setMessage("修改失败");
        }
        return result;
    }

    @RequestMapping("issue_del")
    @ResponseBody
    public Result issueDel(@RequestParam(name = "ids",required = true) Long[] ids){
        int i=issueService.issueDel(ids);

        Result result = new Result();
        result.setType("alert");

        if(i!=0){
            result.setMessage("删除成功");
        }else{
            result.setMessage("删除失败");
        }
        return result;
    }

    @RequestMapping("issue_edit")
    public void issueEdit(Model model,@RequestParam(name= "id",required = true) Long id){
        model.addAttribute("issone",issueService.findId(id));
    }

    @RequestMapping("issue_data")
    @ResponseBody
    public PageDto issueData(Integer pageNum,Integer pageSize, HttpServletRequest request){
        Page<Issue> iss=issueService.findByAll(pageNum,pageSize,request);

        return PageDtoUtil.getPageDto(iss);
    }


    @RequestMapping("issue_save")
    @ResponseBody
    public Result issueSave(Issue issue){

        Long id=issue.getId();

        issueService.save(issue);

        Result result = new Result();
        result.setType("alert");

        if(id==null){
            result.setMessage("保存成功");
        }else{
            result.setMessage("修改成功");
        }

        return result;
    }

}
