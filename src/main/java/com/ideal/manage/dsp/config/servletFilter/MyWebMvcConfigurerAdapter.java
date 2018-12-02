package com.ideal.manage.dsp.config.servletFilter;

import com.ideal.auth.bj.filter.PortalsAuthoFilter;
import com.ideal.manage.dsp.config.Component.AuthorConfig;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MyWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

    @Resource
    private AuthorConfig authorConfig;

    @Bean
    MenuFilter menuFilter(){
        return new MenuFilter();
    }

    @Bean
    HtmlFilter htmlFilter(){return new HtmlFilter();}

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(menuFilter()).addPathPatterns("/**")
                .excludePathPatterns("/js/**","/css/**","/login","/logout","/ace/**","/error","/test*",
                        "/industryPublic/**","/html/**","/index","/portal/**","/portalDefault/**","/","/control_login","/control_logout");
        registry.addInterceptor(htmlFilter()).addPathPatterns("/html/**","/index")
                .excludePathPatterns("/html/*ajax");

        super.addInterceptors(registry);
    }

    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(authoFilter());

        //加载控制台登录参数
        Map<String,String> filterConfigMap = new HashMap<String, String>();
        filterConfigMap.put("clientId", authorConfig.getClientId());
        filterConfigMap.put("clientSecret", authorConfig.getClientSecret());
        filterConfigMap.put("redirectUri", authorConfig.getRedirectUri());
        filterConfigMap.put("authPath", authorConfig.getAuthPath());
        filterConfigMap.put("loginPath", authorConfig.getLoginPath());
        filterConfigMap.put("isShiro", authorConfig.getIsShiro());
        filterConfigMap.put("exclude", authorConfig.getExclude());

        registration.setInitParameters(filterConfigMap);
        registration.addUrlPatterns("/control_login");
        return registration;
    }

    @Bean(name = "authoFilter")
    public Filter authoFilter() {
        return new PortalsAuthoFilter();
    }
}
