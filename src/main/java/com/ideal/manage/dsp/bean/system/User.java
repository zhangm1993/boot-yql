package com.ideal.manage.dsp.bean.system;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "t_user")
public class User implements Serializable{
    @Id
    @GeneratedValue
    private Long id;
    private String loginName;
    private String password;
    private String name;

    private Long sex;
    private String telephone;
    private String mobile;
    private String email;

    @OneToOne
    @JoinColumn(name="role_id")
    @JsonIgnore
    private Role role;

    @ManyToOne
    @JoinColumn(name="customer_id")
    @JsonIgnore
    private Customer customer;

    private String jobTitle;
    private String Remark;
    private Long status;
    private Long delFlag;

    private Date createDate;

    @ManyToOne
    @JoinColumn(name="create_user")
    private User createUser;

    private Date dateLastUpdated;

    @ManyToOne
    @JoinColumn(name="person_last_updated")
    @JsonIgnore
    private User updateUser;

    @Transient
    @JsonIgnore
    private List<String> roleNames = new ArrayList<String>();
    @Transient
    @JsonIgnore
    private List<Long> roleIds = new ArrayList<Long>();

    @ManyToMany
    @JoinTable(name = "t_user_role",inverseJoinColumns = @JoinColumn(name="role_id"),joinColumns = @JoinColumn(name = "user_id"))
    @JsonIgnore
    private Set<Role> roles;

//    @OneToMany(mappedBy = "user")
//    @JsonIgnore
//    private Set<UserRole> roleSet = new HashSet<UserRole>();

    @ManyToOne
    @JoinColumn(name="check_user")
    @JsonIgnore
    private User checkUser;
    private Date checkTime;
    private String checkMessage;

    // 用于前台页面展示
    @Transient
    private String roleName;
    @Transient
    private String customerName;

    public String getRoleName() {
        if(role != null){
            roleName = role.getName();
        }
        return roleName;
    }

    public String getCustomerName() {
        if(customer != null){
            customerName = customer.getName();
        }
        return customerName;
    }

    public User(){

    }

    public User(Long id){
        this.id = id;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSex() {
        return sex;
    }

    public void setSex(Long sex) {
        this.sex = sex;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }

    public Date getDateLastUpdated() {
        return dateLastUpdated;
    }

    public void setDateLastUpdated(Date dateLastUpdated) {
        this.dateLastUpdated = dateLastUpdated;
    }

    public User getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(User updateUser) {
        this.updateUser = updateUser;
    }

    public List<String> getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(List<String> roleNames) {
        this.roleNames = roleNames;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public User getCheckUser() {
        return checkUser;
    }

    public void setCheckUser(User checkUser) {
        this.checkUser = checkUser;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getCheckMessage() {
        return checkMessage;
    }

    public void setCheckMessage(String checkMessage) {
        this.checkMessage = checkMessage;
    }
}
