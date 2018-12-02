package com.ideal.manage.dsp.bean.industry;

import com.ideal.manage.dsp.bean.system.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_feature")
public class Feature {

    @Id
    @GeneratedValue
    private Long id;

    private String title;                  //标题
    private String pictureUrl;             //logo图片地址
    private String description;            //简介
    private String featureUrl;             //对象的url

    private String delFlag;                //删除标识 0:正常   1:已删除
    private String status;                 //状态 0:未上线 1:已上线

    private Date createTime;               //创建时间
    @ManyToOne
    @JoinColumn(name="create_user")
    private User createUser;               //创建人
    private Date updateTime;               //修改时间
    @ManyToOne
    @JoinColumn(name="update_user")
    private User updateUser;               //修改人

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFeatureUrl() {
        return featureUrl;
    }

    public void setFeatureUrl(String featureUrl) {
        this.featureUrl = featureUrl;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public User getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(User updateUser) {
        this.updateUser = updateUser;
    }
}
