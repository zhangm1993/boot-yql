package com.ideal.manage.dsp.controller.defaultController;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("default")
public class DefaultController {
    @RequestMapping("layout")
    public void layout(Model model){
        model.addAttribute("name","xxxxxxxx");
        System.out.println("layout");
    }

    @RequestMapping("content")
    public void content(Model model){
        model.addAttribute("name","======");
        System.out.println("content");
    }

    @RequestMapping("sidebar")
    public void sidebar(){
        System.out.println("sidebar");
    }
}
