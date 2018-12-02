package com.ideal.manage.dsp.bean.system;

import java.io.Serializable;
import java.util.List;

public class ParameterExtend extends Parameter implements Serializable {

    private Long id;
    private String name;
    private List<ParameterExtend> childList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ParameterExtend> getChildList() {
        return childList;
    }

    public void setChildList(List<ParameterExtend> childList) {
        this.childList = childList;
    }
}
