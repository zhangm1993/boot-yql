package com.ideal.manage.dsp.controller.industryPublic;

import com.ideal.manage.dsp.bean.system.ParameterExtend;
import com.ideal.manage.dsp.service.system.ParameterService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("industryPublic")
public class ParameterHttpController {

    @Resource
    private ParameterService parameterService;

    @RequestMapping("classify")
    public Object test(String code){

        List<ParameterExtend> parameter=parameterService.findByAllChildListByCode(code);

        return parameter;
    }

}
