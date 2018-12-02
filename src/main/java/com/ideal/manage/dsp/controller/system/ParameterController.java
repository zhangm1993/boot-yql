package com.ideal.manage.dsp.controller.system;

import com.ideal.manage.dsp.bean.DTO.BootstrapValidatorDto;
import com.ideal.manage.dsp.bean.DTO.PageDto;
import com.ideal.manage.dsp.bean.DTO.Result;
import com.ideal.manage.dsp.bean.system.Parameter;
import com.ideal.manage.dsp.service.system.ParameterService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("system")
public class ParameterController {

    @Resource
    private ParameterService parameterService;

    @RequestMapping("parameter_list")
    public void parameterList(){

    }

    /**
     * parameter 数据
     * @param pageNum
     * @param request
     * @param parentId
     * @return
     */
    @RequestMapping("parameter_data")
    @ResponseBody
    public PageDto parameterData(int pageNum,HttpServletRequest request,Long parentId){
        Page<Parameter> page = parameterService.findAllParent(pageNum, request,parentId);
        List<Parameter> parameters = page.getContent();
        long total = page.getTotalElements();
        PageDto pageDto = new PageDto();
        pageDto.setRows(parameters);
        pageDto.setTotal(total);
        return pageDto;
    }

    /**
     * parameter 添加
     * @param parentId
     * @param model
     * @param tableId
     */
    @RequestMapping("parameter_add")
    public void parameterAdd(Long parentId,Model model,String tableId){
        if(parentId != null){
            model.addAttribute("parentId",parentId);
        }
        if(tableId != null){
            model.addAttribute("tableId",tableId);
        }
    }

    /**
     * parameter 修改
     * @param id
     * @param model
     */
    @RequestMapping("parameter_edit")
    public void parameterEdit(Long id,Model model,Long parentId,String tableId){
        if(parentId != null){
            model.addAttribute("parentId",parentId);
        }
        if(tableId != null){
            model.addAttribute("tableId",tableId);
        }
        Parameter parameter = parameterService.findById(id);
        model.addAttribute("parameter",parameter);
    }

    /**
     * 保存
     * @param parentId
     * @return
     */
    @RequestMapping("parameter_save")
    @ResponseBody
    public Result parameterSave(Long id,String code,String remark,String name,Long parentId){
        parameterService.parameterSave(id,code,remark,name,parentId);
        Result result = new Result();
        result.setType("alert");
        result.setMessage("保存成功");
        return result;
    }

    @RequestMapping("parameter_del")
    @ResponseBody
    public Result parameterDel(Long[] ids){
        parameterService.delParameter(ids);
        Result result = new Result();
        result.setType("alert");
        result.setMessage("删除成功");
        return result;
    }

    /**
     * 查询code是否唯一
     * @param code
     * @return
     */
    @RequestMapping("parameter_code_check")
    @ResponseBody
    public BootstrapValidatorDto parameterCodeCheck(String code){
        BootstrapValidatorDto bootstrapValidatorDto = new BootstrapValidatorDto();
        if(code != null){
            Parameter parameter = parameterService.findByCode(code);
            if(parameter != null){
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
