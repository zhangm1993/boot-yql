package com.ideal.manage.dsp.bean.industry;

import com.ideal.manage.dsp.bean.system.Parameter;
import com.ideal.manage.dsp.bean.system.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_company")
public class Company {

    @Id
    @GeneratedValue
    private Long id;

    private String title;                  //标题
    private String pictureUrl;             //图片地址
    private String pictureBackground;      //背景颜色

    private String delFlag;                //删除标识 0:正常 1:已删除
    private String status;                 //案例状态  0:未上线 1:已上线

    private String content;                //内容信息

    @ManyToOne
    @JoinColumn(name="category")
    private Parameter category;            //分类  公司简介 用户手册

    @ManyToOne
    @JoinColumn(name="create_user")
    private User createUser;               //创建人
    private Date createTime;               //创建时间

    @ManyToOne
    @JoinColumn(name="update_user")
    private User updateUser;               //修改人
    private Date updateTime;               //修改时间

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public User getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(User updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Parameter getCategory() {
        return category;
    }

    public void setCategory(Parameter category) {
        this.category = category;
    }
}
