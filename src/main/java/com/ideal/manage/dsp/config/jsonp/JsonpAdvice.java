package com.ideal.manage.dsp.config.jsonp;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

/**
 * Created by xbb on 17/8/11.
 * 配置跨域
 */
@ControllerAdvice(basePackages = "com.ideal.manage.dsp.controller")
public class JsonpAdvice extends AbstractJsonpResponseBodyAdvice {

    public JsonpAdvice() {

        super("callback","jsonp");
    }
}