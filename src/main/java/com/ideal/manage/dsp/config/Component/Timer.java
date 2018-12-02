package com.ideal.manage.dsp.config.Component;

import com.ideal.manage.dsp.bean.system.Parameter;
import com.ideal.manage.dsp.controller.industryPublic.IndexController;
import com.ideal.manage.dsp.service.system.ParameterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Random;

@Component
@EnableScheduling
public class Timer {

    @Resource
    private ParameterService parameterService;

    private static final Logger logger = LoggerFactory.getLogger(Timer.class);

    /**
     * 每天00:00 12:00 修改数据用户数
     */
    @Scheduled(cron = "0 0 0,12 * * ?")
    public void updateUserNumber() {

        //获取当前用户数
        Parameter parameter = parameterService.findByCode("110000001");
        int userNumber = Integer.parseInt(parameter.getName());

        //随机取值
        Random rand = new Random();
        int randNum = rand.nextInt(10);

        //保存数据
        parameter.setName((userNumber + randNum) + "");
        parameterService.saveParameter(parameter);
        logger.info("用户数已修改为:" + (userNumber + randNum));
    }

    /**
     * 每天 每小时:01 修改设备连接数
     */
    @Scheduled(cron = "0 1 0/1 * * ?")
    public void updateEquipmentJoinNumber() {

        //获取当前设备连接数
        Parameter parameter = parameterService.findByCode("110000004");
        int userNumber = Integer.parseInt(parameter.getName());

        //随机取值
        Random rand = new Random();
        int randNum = rand.nextInt(20)*(rand.nextInt(2) == 0 ? -1 : 1);

        //保存数据
        parameter.setName((userNumber + randNum) + "");
        parameterService.saveParameter(parameter);
        logger.info("设备连接数已修改为:" + (userNumber + randNum));
    }


    /**
     * 每隔一天 00:02 修改企业用户数
     */
    @Scheduled(cron = "0 2 0 0/2 * ?")
    public void updateCompanyNumber() {

        //获取当前企业用户数
        Parameter parameter = parameterService.findByCode("110000002");
        int userNumber = Integer.parseInt(parameter.getName());

        //随机取值
        Random rand = new Random();
        int randNum = rand.nextInt(3);

        //保存数据
        parameter.setName((userNumber + randNum) + "");
        parameterService.saveParameter(parameter);
        logger.info("企业用户数已修改为:" + (userNumber + randNum));
    }

    /**
     * 每隔一天 00:03  修改平台工业数据量
     */
    @Scheduled(cron = "0 3 0 0/2 * ?")
    public void updateIndustryNumber() {

        //获取当前企业用户数
        Parameter parameter = parameterService.findByCode("110000003");
        int userNumber = Integer.parseInt(parameter.getName());

        //随机取值
        Random rand = new Random();
        int randNum = rand.nextInt(3);

        //保存数据
        parameter.setName((userNumber + randNum) + "");
        parameterService.saveParameter(parameter);
        logger.info("平台工业数据量已修改为:" + (userNumber + randNum));
    }
}
