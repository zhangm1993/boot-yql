package com.ideal.manage.dsp.bean.industry;

import java.util.Date;
import com.ideal.manage.dsp.bean.system.Parameter;

import javax.persistence.*;

@Entity
@Table(name = "t_consult")
public class Consult {

    @Id
    @GeneratedValue
    private Long id;

    private Long objectId;       //对象id
    private String type;         //类别 1:产品 2:解决方案 3:优秀案例

    @ManyToOne
    @JoinColumn(name="business")
    private Parameter business;  //所属行业
    private String personage;    //公司名称/个人
    private String scale;        //公司规模
    private String name;         //姓名
    private String phone;        //联系方式
    private String description;      //咨询说明
    private String accessoryUrl; //附件地址
    private String communicateTime;  //适合沟通时间段  1:9:00-10:30  2:10:30-12:00  3:14:00-16:00  4:16:00-18:00

    @Transient
    private String objectName;   //对象名称
    private Date createTime;     //创建时间

    public Parameter getBusiness() {
        return business;
    }

    public void setBusiness(Parameter business) {
        this.business = business;
    }

    public String getAccessoryUrl() {
        return accessoryUrl;
    }

    public void setAccessoryUrl(String accessoryUrl) {
        this.accessoryUrl = accessoryUrl;
    }

    public String getCommunicateTime() {
        return communicateTime;
    }

    public void setCommunicateTime(String communicateTime) {
        this.communicateTime = communicateTime;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getPersonage() {
        return personage;
    }

    public void setPersonage(String personage) {
        this.personage = personage;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
