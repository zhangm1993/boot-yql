package com.ideal.manage.dsp.bean.system;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="t_menu")
public class Menu implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String url;
    private String cssClass;
    private String code;

    @ManyToOne( cascade = {CascadeType.REFRESH})
    @JsonIgnore
    private Menu parentMenu;
    private Long type;

//    @OneToMany(targetEntity = Menu.class,mappedBy = "parentMenu",cascade = {CascadeType.REFRESH})
    @Transient
    @JsonIgnore
    private List<Menu> subMenu;

    @Transient
    private String requestUri;
    private Long status;
    private Long sort;
    private Date createDate;
    private Date lastUpdateDate;
    private Long delFlag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="create_user")
    @JsonIgnore
    private User createUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="last_update_user")
    @JsonIgnore
    private User lastUpdateUser;

    @Transient
    private String isParentMenu;

    @Transient
    private String isOpen;

//    @ManyToMany(cascade = CascadeType.REFRESH,mappedBy = "menus")
//    private Set<Role> roles;

    public Menu(){

    }

    public Menu(Long id){
        this.id = id;
    }


    public String getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public Menu getParentMenu() {
        return parentMenu;
    }

    public void setParentMenu(Menu parentMenu) {
        this.parentMenu = parentMenu;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public List<Menu> getSubMenu() {
        return subMenu;
    }

    public void setSubMenu(List<Menu> subMenu) {
        this.subMenu = subMenu;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
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

    public User getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(User lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public String getIsParentMenu() {
        return isParentMenu;
    }

    public void setIsParentMenu(String isParentMenu) {
        this.isParentMenu = isParentMenu;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    //    public Set<Role> getRoles() {
//        return roles;
//    }
//
//    public void setRoles(Set<Role> roles) {
//        this.roles = roles;
//    }
}
