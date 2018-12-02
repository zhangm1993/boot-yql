package com.ideal.manage.dsp.bean.industry;

import com.ideal.manage.dsp.bean.system.Parameter;
import com.ideal.manage.dsp.bean.system.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_case")
public class Case {

    @Id
    @GeneratedValue
    private Long id;

    private String name;                   //案例名称
    private String title;                  //案例标题
    private String pictureUrl;             //案例图片地址
    private String pictureBackground;      //图片背景颜色
    private String description;            //案例简介

    private String delFlag;                //删除标识 0:正常 1:已删除
    private String status;                 //案例状态  0:未上线 1:已上线

    private String companyInformation;     //公司信息
    private String caseQuestion;           //方案问题
    private String userScheme;             //使用方案
    private String solution;               //解决方法

    @ManyToOne
    @JoinColumn(name="industry_cate")
    private Parameter industryCate;        //行业分类

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

    @Transient
    private String industryCateName;       //分类名称

    public String getIndustryCateName() {
        return industryCateName;
    }

    public void setIndustryCateName(String industryCateName) {
        this.industryCateName = industryCateName;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getCompanyInformation() {
        return companyInformation;
    }

    public void setCompanyInformation(String companyInformation) {
        this.companyInformation = companyInformation;
    }

    public String getCaseQuestion() {
        return caseQuestion;
    }

    public void setCaseQuestion(String caseQuestion) {
        this.caseQuestion = caseQuestion;
    }

    public String getUserScheme() {
        return userScheme;
    }

    public void setUserScheme(String userScheme) {
        this.userScheme = userScheme;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
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

    public Parameter getIndustryCate() {
        return industryCate;
    }

    public void setIndustryCate(Parameter industryCate) {
        this.industryCate = industryCate;
    }
}
