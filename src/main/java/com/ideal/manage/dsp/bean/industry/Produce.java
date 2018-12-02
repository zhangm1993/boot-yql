package com.ideal.manage.dsp.bean.industry;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import com.ideal.manage.dsp.bean.system.Parameter;

@Entity
@Table(name = "t_produce")
public class Produce implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name="first_cate")
    private Parameter firstCate;           //一级分类

    @ManyToOne
    @JoinColumn(name="second_cate")
    private Parameter secondCate;          //二级分类

    private String name;                   //产品名称
    private String code;                   //产品编码
    private String title;                  //产品标题
    private String pictureUrl;             //产品图片地址
    private String pictureBackground;      //图片背景颜色
    private String description;            //产品简介

    private String overview;               //产品概述
    private String explanation;            //产品说明
    private String advantage;              //产品优势
    private String specification;          //产品规格

    private Date createTime;               //创建时间
    private String createName;             //创建人
    private Date updateTime;               //修改时间
    private String updateName;             //修改人

    private String delFlag;                //删除标识 0:正常 1:已删除
    private String status;                 //产品状态  0:未上线 1:已上线

    private String homeUrl;                //首页展示图片url
    private String homeRank;               //首页展示排序

    private String recommendStatus;        //推荐状态  0:未推荐 1:已推荐

    @Transient
    private Long firstCatel;
    @Transient
    private Long secondCatel;

    @Transient
    private String firstCateName;
    @Transient
    private String secondCateName;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFirstCateName() {
        return firstCateName;
    }

    public void setFirstCateName(String firstCateName) {
        this.firstCateName = firstCateName;
    }

    public String getSecondCateName() {
        return secondCateName;
    }

    public void setSecondCateName(String secondCateName) {
        this.secondCateName = secondCateName;
    }

    public Long getFirstCatel() {
        return firstCatel;
    }

    public void setFirstCatel(Long firstCatel) {
        this.firstCatel = firstCatel;
    }

    public Long getSecondCatel() {
        return secondCatel;
    }

    public void setSecondCatel(Long secondCatel) {
        this.secondCatel = secondCatel;
    }
    //    private String carouselStatus;         //轮播状态 0:未轮播 1:已轮播
//    private String carouselUrl;            //轮播图片url
//    private String carouselBackground;     //轮播背景颜色
//    private Date carouselTime;             //轮播时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Parameter getFirstCate() {
        return firstCate;
    }

    public void setFirstCate(Parameter firstCate) {
        this.firstCate = firstCate;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getAdvantage() {
        return advantage;
    }

    public void setAdvantage(String advantage) {
        this.advantage = advantage;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public Parameter getSecondCate() {
        return secondCate;
    }

    public void setSecondCate(Parameter secondCate) {
        this.secondCate = secondCate;
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

//    public String getCarouselStatus() {
//        return carouselStatus;
//    }
//
//    public void setCarouselStatus(String carouselStatus) {
//        this.carouselStatus = carouselStatus;
//    }
//
//    public String getCarouselUrl() {
//        return carouselUrl;
//    }
//
//    public void setCarouselUrl(String carouselUrl) {
//        this.carouselUrl = carouselUrl;
//    }
//
//    public String getCarouselBackground() {
//        return carouselBackground;
//    }
//
//    public void setCarouselBackground(String carouselBackground) {
//        this.carouselBackground = carouselBackground;
//    }
//
//    public Date getCarouselTime() {
//        return carouselTime;
//    }
//
//    public void setCarouselTime(Date carouselTime) {
//        this.carouselTime = carouselTime;
//    }

    public String getPictureBackground() {
        return pictureBackground;
    }

    public void setPictureBackground(String pictureBackground) {
        this.pictureBackground = pictureBackground;
    }

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

    public String getRecommendStatus() {
        return recommendStatus;
    }

    public void setRecommendStatus(String recommendStatus) {
        this.recommendStatus = recommendStatus;
    }
}
