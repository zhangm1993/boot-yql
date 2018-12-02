package com.ideal.manage.dsp.config.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class MyShiroFilter {

    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(myShiroRealm());
        return defaultWebSecurityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){
        System.out.println("---shiro Filter---");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/main/index");
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        Map<String,String> mapFilter = new LinkedHashMap<>();
        mapFilter.put("/logout","logout");
        mapFilter.put("/html/**","anon");
        mapFilter.put("/portal/**","anon");
        mapFilter.put("/js/**","anon");
        mapFilter.put("/css/**","anon");
        mapFilter.put("/image/**","anon");
        mapFilter.put("/ace/**","anon");
        mapFilter.put("/industryPublic/**","anon");
        mapFilter.put("/test2","anon");
        mapFilter.put("/test3","anon");
        mapFilter.put("/test4","anon");
        mapFilter.put("/test5","anon");
        mapFilter.put("/favicon.ico","anon");

        //首页
        mapFilter.put("/","anon");
        mapFilter.put("/control_login","anon");
        mapFilter.put("/control_logout","anon");
        mapFilter.put("/index","anon");
        mapFilter.put("/**","authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(mapFilter);
        return shiroFilterFactoryBean;
    }

    @Bean
    public MyShiroRealm myShiroRealm(){
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        return myShiroRealm;
    }

    @Bean(name="shiroDialect")
    public ShiroDialect shiroDialect(){
        return new ShiroDialect();
    }
}
