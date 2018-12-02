package com.ideal.manage.dsp.controller.industry;

import com.ideal.manage.dsp.bean.DTO.PageDto;
import com.ideal.manage.dsp.bean.DTO.Result;
import com.ideal.manage.dsp.bean.industry.Document;
import com.ideal.manage.dsp.bean.system.Parameter;
import com.ideal.manage.dsp.service.industry.DocumentService;
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

/**
 * 2018-03-06 liuwei
 */
@Controller
@RequestMapping("industry")
public class DocumentController {

    @Resource
    private DocumentService documentService;

    @Resource
    private ParameterService parameterService;

    @RequestMapping("document_list")
    public void documentMain(Model model){

    }

    /**
     * 表格初始数据
     * @param pageNum
     * @param request
     */
    @RequestMapping("document_data")
    @ResponseBody
    public PageDto documentData(Integer pageNum,Integer pageSize, HttpServletRequest request){

        Page<Document> documentPage=documentService.findAndNum(pageNum,pageSize,request);

        return PageDtoUtil.getPageDto(documentPage);
    }

    @RequestMapping("document_edit")
    public void documentEdit(Model model,Long id){

        List<Parameter> params=parameterService.findChildByParentCode("140001");

        //文档类型
        model.addAttribute("docTypes",params);

        Document doc=documentService.findyId(id);
        model.addAttribute("doc",doc);


    }

    @RequestMapping("document_add")
    public void documentSave(Model model) throws IOException{

        List<Parameter> params=parameterService.findChildByParentCode("140001");

        //文档类型
        model.addAttribute("docTypes",params);



        /*documentService.saveFile(fileName,document);

        Result result = new Result();
        result.setType("alert");
        result.setMessage("保存成功");
        return result;*/

    }

    @RequestMapping("document_save")
    @ResponseBody
    public Result documentSave(@RequestParam(value="docName",required = false) MultipartFile fileName,
                               Document document){
        Long id=document.getId();

        Long i=documentService.saveFile(fileName,document);

        Result result = new Result();
        result.setType("alert");

        if(id==null){
            result.setMessage(i!=0?"上传成功":"上传失败");
        }else{
            result.setMessage(i!=0?"修改成功":"修改失败");
        }

        return result;

    }

    @RequestMapping("document_del")
    @ResponseBody
    public Result documentDel(Long[] ids){

        documentService.delId(ids);

        Result result = new Result();
        result.setType("alert");
        result.setMessage("删除成功");

        return result;
    }

    @RequestMapping("document_update")
    @ResponseBody
    public Result documentUpdate(Long[] ids,Long status){

        documentService.updateId(ids,status);

        Result result = new Result();
        result.setType("alert");
        result.setMessage("修改成功");

        return result;
    }

}
