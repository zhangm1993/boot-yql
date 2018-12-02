package com.ideal.manage.dsp.controller.system;

import com.ideal.manage.dsp.bean.DTO.PageDto;
import com.ideal.manage.dsp.bean.DTO.Result;
import com.ideal.manage.dsp.bean.system.Customer;
import com.ideal.manage.dsp.bean.system.Parameter;
import com.ideal.manage.dsp.bean.system.User;
import com.ideal.manage.dsp.service.system.CustomerService;
import com.ideal.manage.dsp.service.system.ParameterService;
import com.ideal.manage.dsp.service.system.UserService;
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
public class CustomerController {

    @Resource
    private CustomerService customerService;
    @Resource
    private ParameterService parameterService;
    @Resource
    private UserService userService;

    @RequestMapping("customer_list")
    public void customerPage(Model model){
        //公司类型
        List<Parameter> parameters = parameterService.findChildByParentCode("11000");
        model.addAttribute("parameters",parameters);
        //获取所有客户经理
        List<User> users = userService.findByParameterName("客户经理");
        model.addAttribute("users",users);
    }

    @RequestMapping("customer_data")
    @ResponseBody
    public PageDto customerData(int pageNum,HttpServletRequest request){
        Page<Customer> page = customerService.findAll(pageNum,request);
        List<Customer> customers = page.getContent();
        long total = page.getTotalElements();
        PageDto pageDto = new PageDto();
        pageDto.setRows(customers);
        pageDto.setTotal(total);
        return pageDto;
    }

    @RequestMapping("customer_add")
    public void userAdd(Model model){
        //公司类型
        List<Parameter> parameters = parameterService.findChildByParentCode("11000");
        model.addAttribute("parameters",parameters);
        //获取所有客户经理
        List<User> users = userService.findByParameterName("客户经理");
        model.addAttribute("users",users);
    }

    @RequestMapping("customer_edit")
    public void customerEdit(Long id,Model model){
        Customer customer = customerService.findById(id);
        model.addAttribute("customer",customer);
        //公司类型
        List<Parameter> parameters = parameterService.findChildByParentCode("11000");
        model.addAttribute("parameters",parameters);
        //获取所有客户经理
        List<User> users = userService.findByParameterName("客户经理");
        model.addAttribute("users",users);
    }

    @RequestMapping("customer_save")
    @ResponseBody
    public Result customerSave(Long id,String name,Long manager,Long companyType,String companyTelephone,
                             String contactName,String contactMobile,String email,String address,
                             String legalPerson,String businessNumber,String bankAccount,String remark ){
        customerService.saveCustomer(id, name, manager, companyType, companyTelephone, contactName, contactMobile, email, address, legalPerson, businessNumber, bankAccount, remark);
        Result result = new Result();
        result.setType("alert");
        result.setMessage("保存成功");
        return result;
    }

    @RequestMapping("customer_del")
    @ResponseBody
    public Result customerDel(Long[] ids){
        customerService.delCustomer(ids);
        Result result = new Result();
        result.setType("alert");
        result.setMessage("删除成功");
        return result;
    }

}
