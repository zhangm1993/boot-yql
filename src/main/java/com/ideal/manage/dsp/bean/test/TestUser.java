package com.ideal.manage.dsp.bean.test;

import com.ideal.manage.dsp.test.Test2;

public class TestUser {
    private String name;
    private String parentName;
    private String level;
    private String desc;

    public  TestUser(){

    }

    public TestUser(String name, String parentName, String level, String desc) {
        this.name = name;
        this.parentName = parentName;
        this.level = level;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
