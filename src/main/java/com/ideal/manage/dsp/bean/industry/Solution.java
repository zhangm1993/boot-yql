package com.ideal.manage.dsp.bean.industry;

import com.ideal.manage.dsp.bean.system.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_solution")
public class Solution {

    @Id
    @GeneratedValue
    private Long id;

    private String name;                   //方案名称
    private String title;                  //方案标题
    private String pictureUrl;             //方案图片地址
    private String pictureBackground;      //图片背景颜色
    private String description;            //方案简介

    private String delFlag;                //删除标识 0:正常 1:已删除
    private String status;                 //方案状态  0:未上线 1:已上线

    private String solveProblem;           //解决问题
    private String provideService;         //提供服务
    private String application;            //应用实践

    private Date createTime;               //创建时间
    @ManyToOne
    @JoinColumn(name="create_user")
    private User createUser;               //创建人
    private Date updateTime;               //修改时间
    @ManyToOne
    @JoinColumn(name="update_user")
    private User updateUser;               //修改人

    private String homeUrl;                //首页展示图片url
    private String homeRank;               //首页展示排序

    public String getHomeUrl() {
        return homeUrl;
    }

    public void setHomeUrl(String homeUrl) {
        this.homeUrl = homeUrl;
    }

    public String getHomeRank() {
        return homeRank;
    }

    public void setHomeRank(String homeRank) {
        this.homeRank = homeRank;
    }

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

    public String getPictureBackground() {
        return pictureBackground;
    }

    public void setPictureBackground(String pictureBackground) {
        this.pictureBackground = pictureBackground;
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

    public String getSolveProblem() {
        return solveProblem;
    }

    public void setSolveProblem(String solveProblem) {
        this.solveProblem = solveProblem;
    }

    public String getProvideService() {
        return provideService;
    }

    public void setProvideService(String provideService) {
        this.provideService = provideService;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
