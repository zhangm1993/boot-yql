package com.ideal.manage.dsp.config;

import com.ideal.manage.dsp.bean.system.Parameter;
import com.ideal.manage.dsp.service.system.ParameterService;
import com.ideal.manage.dsp.util.SftpConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class SftpConnUtil {

    @Resource
    private ParameterService parameterService;

    @Bean
    public SftpConfig sftpConn(){

        Parameter pName= parameterService.findByCode("1300005");
        Parameter pWord= parameterService.findByCode("1300002");
        Parameter pHost= parameterService.findByCode("1300003");
        Parameter pPort= parameterService.findByCode("1300004");
        Parameter pPath= parameterService.findByCode("1300001");
        String host=pHost.getRemark().trim();
        int port=Integer.valueOf(pPort.getRemark().trim());
        String user=pName.getRemark().trim();
        String password=pWord.getRemark().trim();
        String path=pPath.getRemark().trim();
        return new SftpConfig(host,port,user,password,path);
    }



}
