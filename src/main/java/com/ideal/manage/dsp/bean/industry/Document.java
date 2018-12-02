package com.ideal.manage.dsp.bean.industry;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ideal.manage.dsp.bean.system.User;

import javax.persistence.*;
import java.util.Date;
import com.ideal.manage.dsp.bean.system.Parameter;
/**
 * 2018-03-08 liuwei
 */
@Entity
@Table(name="t_document")
public class Document {
    @Id
    @GeneratedValue
    private Long id;

    private Long produceId; //关联产品id

    private String name;

    private String url;
    @ManyToOne
    @JoinColumn(name="type")
    private Parameter type; //文档类型

    @ManyToOne
    @JoinColumn(name="create_user")
    private User createUser;

    @ManyToOne
    @JoinColumn(name="update_user")
    private User updateUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updateTime;

    private Long status;

    private Long delFlag=0l; //0 可用  1可用  2删除

    private String remark;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Parameter getType() {
        return type;
    }

    public void setType(Parameter type) {
        this.type = type;
    }

    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }

    public User getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(User updateUser) {
        this.updateUser = updateUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getProduceId() {
        return produceId;
    }

    public void setProduceId(Long produceId) {
        this.produceId = produceId;
    }

    public Long getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Long status) {
        this.delFlag = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }
}
