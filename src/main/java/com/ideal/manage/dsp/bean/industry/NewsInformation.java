package com.ideal.manage.dsp.bean.industry;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.ideal.manage.dsp.bean.system.User;
import com.ideal.manage.dsp.bean.system.Parameter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by liuhang on 2018/3/9.
 *
 *      新闻资讯 实体类
 */

@Entity
@Table(name="t_news_information")
public class NewsInformation {

    @Id
    @GeneratedValue
    private Long id;
    private String name;                    //新闻标题

    @ManyToOne
    @JoinColumn(name="create_user")
    private User createUser;                //创建人
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createDate;                //创建时间
    private String content;                 //新闻内容

    @ManyToOne
    @JoinColumn(name="type")
    private Parameter type;                    //新闻类别
    private Long status;                       //0.下线，1.上线
    private Long delFlag;

    @ManyToOne
    @JoinColumn(name="edit_user")
    private User editUser;                  //修改人
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date editDate;                  //修改时间

    @Transient
    private String createUserName;

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
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

    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Parameter getType() {
        return type;
    }

    public void setType(Parameter type) {
        this.type = type;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Long delFlag) {
        this.delFlag = delFlag;
    }

    public User getEditUser() {
        return editUser;
    }

    public void setEditUser(User editUser) {
        this.editUser = editUser;
    }

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }
}
