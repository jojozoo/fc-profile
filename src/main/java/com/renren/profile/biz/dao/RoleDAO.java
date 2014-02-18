package com.renren.profile.biz.dao;

import java.util.List;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import com.renren.profile.RenrenProfileConstants;
import com.renren.profile.biz.model.Role;

@DAO(catalog = RenrenProfileConstants.DB_CATALOG)
public interface RoleDAO {
    String TABLE             = "role";
    // -------- { Column Defines
    String ID                = "id";
    String NAME              = "name";
    String EDIT_TIME         = "edit_time";
    String EDITOR_ID         = "editor_id";
    String EDITOR_NAME       = "editor_name";
    // -------- } Column Defines

    String FIELD_PK          = "id";
    String FIELDS_WITHOUT_PK = "name,edit_time,editor_id,editor_name";
    String FIELDS_ALL        = FIELD_PK + "," + FIELDS_WITHOUT_PK;

    @SQL("select " + FIELDS_ALL + " from $TABLE")
    public List<Role> queryAll();

    @SQL("select COUNT(" + FIELD_PK + ") from " + TABLE)
    public int countAll();

    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `id` = :1")
    public Role query(int id);

    String SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK = "name=:model.name,edit_time=:model.editTime,editor_id=:model.editorId,editor_name=:model.editorName";

    @SQL("update " + TABLE + " set " + SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK + " where id=:1.id")
    public int update(@SQLParam("model") Role model);

    @SQL("insert ignore into " + TABLE + " (" + FIELDS_WITHOUT_PK + ") VALUES (:model.name,:model.editTime,:model.editorId,:model.editorName)")
    @ReturnGeneratedKeys
    public Integer save(@SQLParam("model") Role model);

    @SQL("delete from " + TABLE + " where `id` = :1")
    public void delete(int id);
}