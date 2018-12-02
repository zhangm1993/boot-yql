package com.ideal.manage.dsp.controller.system;

import com.ideal.manage.dsp.bean.DTO.PageDto;
import com.ideal.manage.dsp.bean.DTO.Result;
import com.ideal.manage.dsp.bean.system.Menu;
import com.ideal.manage.dsp.service.system.MenuService;
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
public class MenuController {

    @Resource
    private MenuService menuService;

    @RequestMapping("menu_list")
    public void menuList(Model model){

    }

    @RequestMapping("menu_data")
    @ResponseBody
    public PageDto menuData(int pageNum,HttpServletRequest request){
        Page<Menu> page = menuService.findAll(pageNum, request);
        List<Menu> menus = page.getContent();
        long total = page.getTotalElements();
        PageDto pageDto = new PageDto();
        pageDto.setRows(menus);
        pageDto.setTotal(total);
        return pageDto;
    }

    @RequestMapping("menu_add")
    public void menuAdd(Model model){
        List<Menu> menusParents = menuService.findAllParentMenu();
        model.addAttribute("menuParents",menusParents);
        List<Menu> menusChilds = menuService.findAllChildMenu();
        model.addAttribute("menuChilds",menusChilds);
    }

    @RequestMapping("menu_edit")
    public void menuEdit(Long id,Model model){
        Menu menu = menuService.findOne(id);
        model.addAttribute("menu",menu);
        List<Menu> menusParents = menuService.findAllParentMenu();
        model.addAttribute("menuParents",menusParents);
        List<Menu> menusChilds = menuService.findAllChildMenu();
        model.addAttribute("menuChilds",menusChilds);
    }

    @RequestMapping("menu_save")
    @ResponseBody
    public Result menuSave(Long id,String title,String url,Long parentMenu,Long type,Long sort,String cssClass,String code){
        menuService.menuSave(id, title, url, parentMenu, type, sort, cssClass, code);
        Result result = new Result();
        result.setType("alert");
        result.setMessage("保存成功");
        return result;
    }

    @RequestMapping("menu_del")
    @ResponseBody
    public Result roleDel(Long[] ids){
        menuService.delMenu(ids);
        Result result = new Result();
        result.setType("alert");
        result.setMessage("删除成功");
        return result;
    }



}
