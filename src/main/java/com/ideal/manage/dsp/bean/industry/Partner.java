package com.ideal.manage.dsp.bean.industry;

import com.ideal.manage.dsp.bean.system.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_partner")
public class Partner {

    @Id
    @GeneratedValue
    private Long id;

    private String name;                   //名称
    private String pictureUrl;             //logo图片地址

    private String delFlag;                //删除标识 0:正常 1:已删除
    private String status;                 //案例状态  0:未上线 1:已上线

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
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
}
