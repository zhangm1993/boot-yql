package com.ideal.manage.dsp.service.system;

import com.ideal.manage.dsp.bean.system.Menu;
import com.ideal.manage.dsp.bean.system.Role;
import com.ideal.manage.dsp.bean.system.User;
import com.ideal.manage.dsp.config.shiro.MyShiroRealm;
import com.ideal.manage.dsp.repository.framework.MySpecification;
import com.ideal.manage.dsp.repository.framework.SpecificationOperator;
import com.ideal.manage.dsp.repository.system.MenuRepository;
import com.ideal.manage.dsp.util.HttpRequests;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;


    public Page<Menu> findAll(int pageNum, HttpServletRequest request){
        List<SpecificationOperator> operators = HttpRequests.getParametersStartingWith(request,"Q_");
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        SpecificationOperator operator = new SpecificationOperator("delFlag","0","EQ");
        operators.add(operator);
        MySpecification<Menu> mySpecifications = new MySpecification<>(operators);
        Pageable pageable = new PageRequest(pageNum,15,sort);
        Page<Menu> page = menuRepository.findAll(mySpecifications,pageable);
        return page;
    }

    public List<Menu> findAllMenu(){
        List<Menu> menus = menuRepository.findAllByDelFlag(0L);
        return menus;
    }

    /**
     * 获取所有的父菜单
     * @return
     */
    public List<Menu> findAllParentMenu(){
        List<Menu> menus = menuRepository.findAllParentMenu();
        return menus;
    }

    public List<Menu> findAllChildMenu(){
        List<Menu> menus = menuRepository.findAllChildMenu();
        return menus;
    }

    public Menu findOne(Long id){
        Menu menu = menuRepository.findOne(id);
        return menu;
    }

    public void menuSave(Long id,String title,String url,Long parentMenu,Long type,Long sort,String cssClass,String code){
        MyShiroRealm.ShiroUser shiroUser = (MyShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        Menu menu = new Menu();
        if(id != null){
            menu = menuRepository.findOne(id);
            menu.setLastUpdateUser(new User(shiroUser.getId()));
            menu.setLastUpdateDate(new Date());
        }else{
            menu.setDelFlag(0L);
            menu.setCreateDate(new Date());
            menu.setCreateUser(new User(shiroUser.getId()));
        }
        menu.setTitle(title);
        menu.setUrl(url);
        if(parentMenu != null){
            menu.setParentMenu(new Menu(parentMenu));
        }
        menu.setType(type);
        menu.setSort(sort);
        menu.setCssClass(cssClass);
        menu.setCode(code);
        menuRepository.save(menu);
    }

    public void delMenu(Long [] ids){
        List<Menu> menus = menuRepository.findAll(Arrays.asList(ids));
        for(Menu menu : menus){
            menu.setDelFlag(1L);
        }
        menuRepository.save(menus);
    }


}
