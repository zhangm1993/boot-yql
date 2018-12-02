package com.ideal.manage.dsp.controller;

import com.ideal.manage.dsp.bean.DTO.PageDto;
import com.ideal.manage.dsp.bean.system.Role;
import com.ideal.manage.dsp.bean.system.User;
import com.ideal.manage.dsp.bean.test.Result;
//import com.ideal.manage.dsp.config.AsyncHelper;
import com.ideal.manage.dsp.bean.test.TestUser;
import com.ideal.manage.dsp.config.shiro.MyShiroRealm;
import com.ideal.manage.dsp.service.system.RoleMenuService;
import com.ideal.manage.dsp.service.system.RoleService;
import com.ideal.manage.dsp.service.system.UserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class HelloController implements ErrorController{

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleMenuService roleMenuService;

    @RequestMapping({"/login"})
    public String login(HttpServletRequest request, Model model){
        String exception = (String) request.getAttribute("shiroLoginFailure");
        System.out.println(exception);
        if(exception != null && exception.equals("org.apache.shiro.authc.IncorrectCredentialsException")){
            model.addAttribute("error_msg","账号或密码错误");
        }

        MyShiroRealm.ShiroUser user = (MyShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        if (user != null) {
            return "main/index";
        } else {
            return "login";
        }
    }

    @RequestMapping("/")
    public String index(HttpServletRequest request, Model model){

//        if (request.getSession().getAttribute("PSID") == null) {
//            request.getSession().setAttribute("PSID", UUID.randomUUID().toString());
//        }
        return "redirect:html/index";
    }

    @RequestMapping("test")
    @ResponseBody
    public Result test(){
        Result result = new Result();
        result.setResult(200L);
        return result;
    }

    @RequestMapping("test2")
    @ResponseBody
    public List<User> testUser(){
//        List<User> userList = userService.findAll();
        List<User> userList = null;
        return userList;
    }

    @RequestMapping("test3")
    @ResponseBody
    @CrossOrigin(origins = "*")
    public String test3(HttpServletRequest request){
        Map<String,String> map = new HashMap<>();
        String a = request.getHeader("token");
        System.out.println(a);
        return "success";
    }


    @RequestMapping("test4")
    @ResponseBody
    public String test4(String test){
        Role role = new Role();
        role.setId(1L);
        roleMenuService.getAllMenu(role);
        return "success";
    }

    @RequestMapping("test5")
    @CrossOrigin("*")
    @ResponseBody
    public PageDto test5(String test){
//        userService.findAllMenu();
//        userService.testFind();
        TestUser t1 = new TestUser("test1","test1","test1","test1");
        TestUser t2 = new TestUser("test2","test2","test2","test2");
        TestUser t3 = new TestUser("test3","test3","test3","test3");
//        TestUser t4 = new TestUser("test4","test4","test4","test4");
        List<TestUser> list = new ArrayList<>();
        list.add(t1);
        list.add(t2);
        list.add(t3);
//        list.add(t4);
        PageDto page = new PageDto();
        page.setTotal(10L);
        page.setRows(list);
        return page;
    }

    /**
     * 404异常
     * @param request
     * @param e
     * @param map
     */
    @RequestMapping("error")
    public void error(HttpServletRequest request,Exception e,Map<String,Object> map){
        map.put("exception", e);
        map.put("url", request.getRequestURL());
    }

    @Override
    public String getErrorPath() {
        return "error";
    }

//    @Autowired
//    AsyncHelper asyncHelper;
//
//    @RequestMapping("test4")
//    @CrossOrigin(origins = "*")
//    public SseEmitter test4(HttpServletRequest request) throws Exception{
//        System.out.println("=====begin========");
//        SseEmitter sseEmitter = new SseEmitter();
//        asyncHelper.sse(sseEmitter, 1, 2000);
//        System.out.println("=====end========");
//        return sseEmitter;
//    }


}
