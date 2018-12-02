package com.ideal.manage.dsp.bean.DTO;

/**
 * 前端ztree插件对象
 */
public class ZtreeDto {
    private Long id;
    private Long pId;
    private String name;
    private Boolean open;
    private Boolean checked;

    public ZtreeDto(){

    }

    public ZtreeDto(Long id, Long pId, String name, Boolean open, Boolean checked) {
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.open = open;
        this.checked = checked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getpId() {
        return pId;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
