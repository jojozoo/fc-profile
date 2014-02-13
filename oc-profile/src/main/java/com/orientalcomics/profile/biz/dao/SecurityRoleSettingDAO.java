package com.orientalcomics.profile.biz.dao;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.biz.model.SecurityRoleSetting;

@DAO(catalog = OcProfileConstants.DB_CATALOG)
public interface SecurityRoleSettingDAO {
    String TABLE             = "security_role_setting";
    // -------- { Column Defines
    String ID                = "id";
    String ACTION_ID         = "action_id";
    String ROLE_ID           = "role_id";
    String EDIT_TIME         = "edit_time";
    String EDITOR_ID         = "editor_id";
    String EDITOR_NAME       = "editor_name";
    // -------- } Column Defines

    String FIELD_PK          = "id";
    String FIELDS_WITHOUT_PK = "action_id,role_id,edit_time,editor_id,editor_name";
    String FIELDS_ALL        = FIELD_PK + "," + FIELDS_WITHOUT_PK;

    @SQL("select " + FIELDS_ALL + " from " + TABLE)
    public List<SecurityRoleSetting> queryAll();

    @SQL("select action_id from " + TABLE + " where role_id = :1")
    public Set<Integer> queryActionIdSetByRoleId(int roleId);

    @SQL("select role_id from " + TABLE + " where action_id = :1")
    public Set<Integer> queryRoleIdSetByActionId(int actionId);

    @SQL("select COUNT(" + FIELD_PK + ") from " + TABLE)
    public int countAll();

    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `id` = :1")
    public SecurityRoleSetting query(int id);

    String SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK = "action_id=:model.actionId,role_id=:model.roleId,edit_time=:model.editTime,editor_id=:model.editorId,editor_name=:model.editorName";

    @SQL("update " + TABLE + " set " + SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK + " where id=:1.id")
    public int update(@SQLParam("model") SecurityRoleSetting model);

    @SQL("insert ignore into " + TABLE + " (" + FIELDS_WITHOUT_PK
            + ") VALUES (:model.actionId,:model.roleId,:model.editTime,:model.editorId,:model.editorName)")
    @ReturnGeneratedKeys
    public Integer save(@SQLParam("model") SecurityRoleSetting model);

    @SQL("delete from " + TABLE + " where `id` = :1")
    public void delete(int id);

    @SQL("delete from $TABLE where role_id = :1 and action_id in (:2)")
    public void removeActionIdsOfRoleId(int roleId, Collection<?> actionIds);

    @SQL("delete from $TABLE where action_id = :1 and role_id in (:2)")
    public void removeRoleIdsOfActionId(Integer actionId, Collection<?> roleIds);
}