package com.dysy.bysj.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author: Dai Junfeng
 * @date: 2021-01-25
 */
@TableName("tb_menu")
public class Menu {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String icon;
    private Long parentId;
    private String path;
    private String component;
    private Integer sortNo;

    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    @TableLogic
    private Boolean deleted;

    @TableField(value = "is_enable", fill = FieldFill.INSERT)
    private Boolean enable;

    @TableField(fill = FieldFill.INSERT)
    private Timestamp createTime;
    private String createName;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Timestamp modifyTime;

    private String modifyName;
    private String description;

    @TableField(exist = false)
    private Integer level;
    // 只是为了使用 menuMapper.xml 中的递归查询（如果不用 xml 中的递归查询, 可以删除该属性）
    @TableField(exist = false)
    private List<Menu> children;

    public Menu() {
    }

    public Menu(String name, String icon, Long parentId, String path, String component, Integer sortNo, Timestamp createTime, String createName, Timestamp modifyTime, String modifyName, String description) {
        this.name = name;
        this.icon = icon;
        this.parentId = parentId;
        this.path = path;
        this.component = component;
        this.sortNo = sortNo;
        this.createTime = createTime;
        this.createName = createName;
        this.modifyTime = modifyTime;
        this.modifyName = modifyName;
        this.description = description;
    }

    public Menu(String name, String icon, Long parentId, String path, String component, Integer sortNo, boolean deleted, boolean enable, Timestamp createTime, String createName, Timestamp modifyTime, String modifyName, String description) {
        this.name = name;
        this.icon = icon;
        this.parentId = parentId;
        this.path = path;
        this.component = component;
        this.sortNo = sortNo;
        this.deleted = deleted;
        this.enable = enable;
        this.createTime = createTime;
        this.createName = createName;
        this.modifyTime = modifyTime;
        this.modifyName = modifyName;
        this.description = description;
    }

    public Menu(Long id, String name, String icon, Long parentId, String path, String component, Integer sortNo, Boolean deleted, Boolean enable, Timestamp createTime, String createName, Timestamp modifyTime, String modifyName, String description) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.parentId = parentId;
        this.path = path;
        this.component = component;
        this.sortNo = sortNo;
        this.deleted = deleted;
        this.enable = enable;
        this.createTime = createTime;
        this.createName = createName;
        this.modifyTime = modifyTime;
        this.modifyName = modifyName;
        this.description = description;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyName() {
        return modifyName;
    }

    public void setModifyName(String modifyName) {
        this.modifyName = modifyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", parentId=" + parentId +
                ", path='" + path + '\'' +
                ", component='" + component + '\'' +
                ", sortNo=" + sortNo +
                ", deleted=" + deleted +
                ", enable=" + enable +
                ", createTime=" + createTime +
                ", createName='" + createName + '\'' +
                ", modifyTime=" + modifyTime +
                ", modifyName='" + modifyName + '\'' +
                ", description='" + description + '\'' +
                ", level=" + level +
//                ", children=" + children +
                '}';
    }
}
