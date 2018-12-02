package com.ideal.manage.dsp.config.servletFilter;

import com.ideal.manage.dsp.bean.system.Menu;
import com.ideal.manage.dsp.bean.system.Role;
import com.ideal.manage.dsp.bean.system.User;
import com.ideal.manage.dsp.config.shiro.MyShiroRealm;
import com.ideal.manage.dsp.repository.system.RoleMenuRepository;
import com.ideal.manage.dsp.service.system.UserService;
import com.ideal.manage.dsp.service.system.RoleMenuService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class MenuFilter implements HandlerInterceptor{

    @Autowired
    private UserService userService;
    @Autowired
    private RoleMenuRepository roleMenuRepository;
    @Autowired
    private RoleMenuService roleMenuService;

//    @Autowired
//    private CacheManager cacheManager;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        MyShiroRealm.ShiroUser shiroUser = (MyShiroRealm.ShiroUser)subject.getPrincipal();
        Long userId = shiroUser.getId();
        User user = userService.findById(userId);
        //目前一个用户只有一个角色
        Role role = user.getRole();
        List<Menu> menus = roleMenuService.getAllMenu(role);
//        Collection list = cacheManager.getCacheNames();
//        System.out.println(list.size());


        httpServletRequest.setAttribute("menus",menus);
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
