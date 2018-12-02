package com.ideal.manage.dsp.bean.industry;

import com.ideal.manage.dsp.bean.system.Parameter;
import com.ideal.manage.dsp.bean.system.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_carousel")
public class Carousel {

    @Id
    @GeneratedValue
    private Long id;

    private String title;                  //轮播标题
    private String pictureUrl;             //轮播图片地址
    private String pictureBackground;      //图片背景颜色
    private String description;            //轮播简介

    @ManyToOne
    @JoinColumn(name="category")
    private Parameter category;            //轮播类型
    private Long categoryId;               //类型关联的主键
    private String categoryUrl;            //轮播对象的url

    private String delFlag;                //删除标识 0:正常   1:已删除
    private String status;                 //轮播状态 0:未上线 1:已上线
    private String cateRank;               //类型排序
    private String homeRank;               //首页排序

    private Date createTime;               //创建时间
    @ManyToOne
    @JoinColumn(name="create_user")
    private User createUser;               //创建人
    private Date updateTime;               //修改时间
    @ManyToOne
    @JoinColumn(name="update_user")
    private User updateUser;               //修改人

    public String getCategoryUrl() {
        return categoryUrl;
    }

    public void setCategoryUrl(String categoryUrl) {
        this.categoryUrl = categoryUrl;
    }

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

    public String getPictureBackground() {
        return pictureBackground;
    }

    public void setPictureBackground(String pictureBackground) {
        this.pictureBackground = pictureBackground;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Parameter getCategory() {
        return category;
    }

    public void setCategory(Parameter category) {
        this.category = category;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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

    public String getCateRank() {
        return cateRank;
    }

    public void setCateRank(String cateRank) {
        this.cateRank = cateRank;
    }

    public String getHomeRank() {
        return homeRank;
    }

    public void setHomeRank(String homeRank) {
        this.homeRank = homeRank;
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
