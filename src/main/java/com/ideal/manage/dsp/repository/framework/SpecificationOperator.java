package com.ideal.manage.dsp.repository.framework;


public class SpecificationOperator {
    private String key;        //字段名
    private Object value;      //字段值
    private String oper;       //符号
    private String join;       //连接条件 and / or

    public SpecificationOperator(String key, Object value, String oper) {
        this.key = key;
        this.value = value;
        this.oper = oper;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getOper() {
        return oper;
    }

    public void setOper(String oper) {
        this.oper = oper;
    }

    public String getJoin() {
        return join;
    }

    public void setJoin(String join) {
        this.join = join;
    }
}
