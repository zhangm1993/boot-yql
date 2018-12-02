package com.ideal.manage.dsp.test;

import com.ideal.manage.dsp.bean.system.User;

import java.lang.reflect.Field;

public class TestReflex {
    public static void main(String[] args) throws Exception {
        User user = new User();
        Class c = user.getClass();
        System.out.println(c.getName());
        Object object = Class.forName(c.getName()).newInstance();
        try {
            Field field = c.getDeclaredField("role");
            field.setAccessible(true);
//            field.set(object,field.getType().cast("test"));
            String classPath = field.getType().getName();
            if(classPath.contains("com.ideal.manage.dsp.bean")){
                Class c1 = Class.forName(classPath);
                Field fieldBean = c1.getDeclaredField("id");
                fieldBean.setAccessible(true);
                Object bean = Class.forName(classPath).newInstance();
                fieldBean.set(bean,fieldBean.getType().cast(10L));

                field.set(object,field.getType().cast(bean));
                fieldBean.setAccessible(false);
            }
            field.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        User user1 = (User)object;
//        System.out.println(user1.getRole().getId());
    }
}
