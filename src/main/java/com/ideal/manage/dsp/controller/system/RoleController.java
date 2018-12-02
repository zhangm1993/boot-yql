package com.ideal.manage.dsp.controller.system;

import com.ideal.manage.dsp.bean.DTO.PageDto;
import com.ideal.manage.dsp.bean.DTO.Result;
import com.ideal.manage.dsp.bean.DTO.ZtreeDto;
import com.ideal.manage.dsp.bean.system.Parameter;
import com.ideal.manage.dsp.bean.system.Role;
import com.ideal.manage.dsp.bean.system.User;
import com.ideal.manage.dsp.repository.system.RoleMenuRepository;
import com.ideal.manage.dsp.service.system.ParameterService;
import com.ideal.manage.dsp.service.system.RoleMenuService;
import com.ideal.manage.dsp.service.system.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 角色管理菜单
 */
@Controller
@RequestMapping("system")
public class RoleController {

    @Resource
    private RoleService roleService;
    @Resource
    private ParameterService parameterService;
    @Resource
    private RoleMenuService roleMenuService;
    /**
     * Role首页
     */
    @RequestMapping("role_list")
    public void userList(Model model){
        //获取身份信息
        List<Parameter> parameters = parameterService.findChildByParentCode("10000");
        model.addAttribute("parameters",parameters);
    }

    /**
     * Role 表格数据
     * @param pageNum
     * @param request
     * @return
     */
    @RequestMapping("role_data")
    @ResponseBody
    public PageDto userData(int pageNum, HttpServletRequest request){
        Page<Role> page = roleService.findAll(pageNum,request);
        List<Role> roles = page.getContent();
        long total = page.getTotalElements();
        PageDto pageDto = new PageDto();
        pageDto.setRows(roles);
        pageDto.setTotal(total);
        return pageDto;
    }

    /**
     * Role新增/修改
     * @param model
     */
    @RequestMapping("role_add")
    public void roleAdd(Model model){
        //获取身份信息
        List<Parameter> parameters = parameterService.findChildByParentCode("10000");
        model.addAttribute("parameters",parameters);
    }

    @RequestMapping("role_edit")
    public void roleEdit(Long id,Model model){
        if(null != id){
            Role role = roleService.findById(id);
            model.addAttribute("role",role);
        }
        //获取身份信息
        List<Parameter> parameters = parameterService.findChildByParentCode("10000");
        model.addAttribute("parameters",parameters);
    }

    /**
     * 保存
     * @param role
     * @return
     */
    @RequestMapping("role_save")
    @ResponseBody
    public Result roleSave(Role role){
        roleService.saveOne(role);
        Result result = new Result();
        result.setType("alert");
        result.setMessage("保存成功");
        return result;
    }

    @RequestMapping("role_del")
    @ResponseBody
    public Result roleDel(Long[] ids){
        roleService.delRole(ids);
        Result result = new Result();
        result.setType("alert");
        result.setMessage("删除成功");
        return result;
    }

    @RequestMapping("role_authorization")
    public void roleAuthorization(Long id,Model model){
        model.addAttribute("roleId",id);
    }

    @RequestMapping("role_tree_data")
    @ResponseBody
    public List<ZtreeDto> roleTreeData(Long id){
        List<ZtreeDto> ztreeDtos = roleService.roleAuthorization(id);
        return ztreeDtos;
    }

    @RequestMapping("role_authorization_save")
    @ResponseBody
    public Result roleAuthorizationSave(Long roleId,String treeIds){
        roleMenuService.roleAuthorizationSave(roleId, treeIds);
        Result result = new Result();
        result.setType("alert");
        result.setMessage("授权成功");
        return result;
    }
}
