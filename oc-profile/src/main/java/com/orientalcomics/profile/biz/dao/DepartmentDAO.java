package com.orientalcomics.profile.biz.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import com.orientalcomics.profile.biz.model.Department;

@DAO
public interface DepartmentDAO {
    String TABLE             = "department";
    // -------- { Column Defines
    String ID                = "id";
    String DEPARTMENT_NAME   = "department_name";
    String PARENT_DEPARTMENT = "parent_department";
    String EDIT_TIME         = "edit_time";
    String EDITOR_ID         = "editor_id";
    String MANAGER_ID        = "manager_id";
    // -------- } Column Defines

    String FIELD_PK          = "id";
    String FIELDS_WITHOUT_PK = "department_name,parent_department,edit_time,editor_id,manager_id";
    String FIELDS_ALL        = FIELD_PK + "," + FIELDS_WITHOUT_PK;

    @SQL("select " + FIELDS_ALL + " from " + TABLE + " limit :offset,:count")
    public List<Department> queryAll(@SQLParam("offset") int offset, @SQLParam("count") int count);
    
    @SQL("select " + FIELDS_ALL + " from " + TABLE)
    public Map<Integer,Department> queryAll();
    
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where department_name like '%动漫%' and id not in (select parent_department  from department)")
    public List<Department> getBaseDepartment();
    

    @SQL("select COUNT(" + FIELD_PK + ") from " + TABLE)
    public int countAll();

    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `id` = :1")
    public Department query(int id);
    
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `department_name` = :1")
    public Department queryByName(String name);
    

    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `id` in (:1)")
    public List<Department> queryByIds(Collection<Integer> ids);

    String SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK = "department_name=:model.departmentName,parent_department=:model.parentDepartment,edit_time=:model.editTime,editor_id=:model.editorId,manager_id=:model.managerId";

    @SQL("update " + TABLE + " set " + SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK + " where id=:1.id")
    public int update(@SQLParam("model") Department model);

    @SQL("insert ignore into " + TABLE + " (" + FIELDS_WITHOUT_PK
            + ") VALUES (:model.departmentName,:model.parentDepartment,:model.editTime,:model.editorId,:model.managerId)")
    @ReturnGeneratedKeys
    public Integer save(@SQLParam("model") Department model);

    @SQL("delete from " + TABLE + " where `id` = :1")
    public void delete(int id);
}