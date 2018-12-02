package com.ideal.manage.dsp.controller.system;

import com.ideal.manage.dsp.bean.DTO.PageDto;
import com.ideal.manage.dsp.bean.DTO.Result;
import com.ideal.manage.dsp.bean.system.Customer;
import com.ideal.manage.dsp.bean.system.Role;
import com.ideal.manage.dsp.bean.system.User;
import com.ideal.manage.dsp.service.system.CustomerService;
import com.ideal.manage.dsp.service.system.RoleService;
import com.ideal.manage.dsp.service.system.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户管理菜单
 */
@Controller
@RequestMapping("system")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;
    @Resource
    private CustomerService customerService;

    @RequestMapping("user_list")
    public void userList(Model model){
        List<Role> roles = roleService.findAllRoles();
        model.addAttribute("roles",roles);
        List<Customer> customers = customerService.findAllCustomer();
        model.addAttribute("customers",customers);
    }

    @RequestMapping("user_data")
    @ResponseBody
    public PageDto userData(int pageNum, HttpServletRequest request){
        Page<User> page = userService.findAll(pageNum,request);
        List<User> users = page.getContent();
        long total = page.getTotalElements();
        PageDto pageDto = new PageDto();
        pageDto.setRows(users);
        pageDto.setTotal(total);
        return pageDto;
    }

    @RequestMapping("user_add")
    public void userAdd(Model model){
        List<Role> roles = roleService.findAllRoles();
        model.addAttribute("roles",roles);
        List<Customer> customers = customerService.findAllCustomer();
        model.addAttribute("customers",customers);
    }

    @RequestMapping("user_edit")
    public void userEdit(Long id,Model model){
        User user = userService.findById(id);
        model.addAttribute("user",user);
        List<Role> roles = roleService.findAllRoles();
        model.addAttribute("roles",roles);
        List<Customer> customers = customerService.findAllCustomer();
        model.addAttribute("customers",customers);
    }

    @RequestMapping("user_save")
    @ResponseBody
    public Result userSave(Long id,String loginName,String password,
                         String name,Long sex,String telephone,
                         String mobile,String email,Long role,
                         Long customer,String jobTitle,String remark){
        userService.saveUser(id, loginName, password, name, sex, telephone, mobile, email, role, customer, jobTitle, remark);
        Result result = new Result();
        result.setType("alert");
        result.setMessage("保存成功");
        return result;
    }

    @RequestMapping("user_del")
    @ResponseBody
    public Result userDel(Long[] ids){
        userService.delUser(ids);
        Result result = new Result();
        result.setType("alert");
        result.setMessage("删除成功");
        return result;
    }
}
