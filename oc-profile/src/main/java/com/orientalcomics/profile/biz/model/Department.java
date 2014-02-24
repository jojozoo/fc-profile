package com.orientalcomics.profile.biz.model;

import java.sql.Timestamp;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.orientalcomics.profile.util.time.TimeUtils;
import com.orientalcomics.profile.util.time.TimeValidator;


public class Department {
    // -------- { Property Defines

    // 自增id,department_id
    private int       id               = 0;

    // 部门名称
    private String    departmentName   = StringUtils.EMPTY;

    // 上级部门id
    private int       parentDepartment = 0;

    // 编辑时间
    private Timestamp editTime         = TimeUtils.Constant.TIME_1970;

    // 信息维护人
    private int       editorId         = 0;

    // 负责人id
    private int       managerId        = 0;

    // -------- } Property Defines

    // -------- { Property Getter/Setter

    /**
     * get id<br/>
     * 自增id,department_id
     */
    public int getId() {
        return this.id;
    }

    /**
     * set id<br/>
     * 自增id,department_id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * get departmentName<br/>
     * 部门名称
     */
    public String getDepartmentName() {
        return this.departmentName;
    }

    public String simpleName() {
        return StringUtils.substringAfterLast(departmentName, "/");
    }

    /**
     * set departmentName<br/>
     * 部门名称
     */
    public void setDepartmentName(String departmentName) {
        this.departmentName = StringUtils.trimToEmpty(departmentName);
    }

    /**
     * get parentDepartment<br/>
     * 上级部门id
     */
    public int getParentDepartment() {
        return this.parentDepartment;
    }

    /**
     * set parentDepartment<br/>
     * 上级部门id
     */
    public void setParentDepartment(int parentDepartment) {
        this.parentDepartment = parentDepartment;
    }

    /**
     * get editTime<br/>
     * 编辑时间
     */
    public Timestamp getEditTime() {
        return this.editTime;
    }

    /**
     * set editTime<br/>
     * 编辑时间
     */
    public void setEditTime(Timestamp editTime) {
        this.editTime = TimeValidator.defaultIfInvalid(editTime, TimeUtils.Constant.TIME_1970);
    }

    /**
     * get editorId<br/>
     * 信息维护人
     */
    public int getEditorId() {
        return this.editorId;
    }

    /**
     * set editorId<br/>
     * 信息维护人
     */
    public void setEditorId(int editorId) {
        this.editorId = editorId;
    }

    /**
     * get managerId<br/>
     * 负责人id
     */
    public int getManagerId() {
        return this.managerId;
    }

    /**
     * set managerId<br/>
     * 负责人id
     */
    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    private transient Map<Integer, Department> childDepartments = null;

    public void childDepartments(Map<Integer, Department> childDepartments) {
        this.childDepartments = childDepartments;
    }

    public Map<Integer, Department> childDepartments() {
        return this.childDepartments;
    }

    // -------- } Property Getter/Setter

}