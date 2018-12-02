package com.ideal.manage.dsp.bean.system;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "t_role")
public class Role implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String name ;
    private String cnName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="type")
    private Parameter type;

    private Long status;

    private Date createDate;
    private Long delFlag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="create_user")
    private User createUser;

    @ManyToMany(cascade = CascadeType.REFRESH, mappedBy = "roles")
    @JsonIgnore
    private Set<User> users;

//    @ManyToMany
//    @JoinTable(name = "t_role_menu",inverseJoinColumns = @JoinColumn(name = "user_id"),joinColumns = @JoinColumn(name = "role_id"))
//    @JsonIgnore
//    private Set<Menu> menus;


//    public Set<Menu> getMenus() {
//        return menus;
//    }
//
//    public void setMenus(Set<Menu> menus) {
//        this.menus = menus;
//    }

    public Role(){

    }

    public Role(Long id){
        this.id = id;
    }


    public Parameter getType() {
        return type;
    }

    public void setType(Parameter type) {
        this.type = type;
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

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Long delFlag) {
        this.delFlag = delFlag;
    }

    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
