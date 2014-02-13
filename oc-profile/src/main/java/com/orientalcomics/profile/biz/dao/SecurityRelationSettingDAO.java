package com.orientalcomics.profile.biz.dao;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.biz.model.SecurityRelationSetting;

@DAO
public interface SecurityRelationSettingDAO {
    String TABLE             = "security_relation_setting";
    // -------- { Column Defines
    String ID                = "id";
    String ACTION_ID         = "action_id";
    String RELATION_ID       = "relation_id";
    String EDIT_TIME         = "edit_time";
    String EDITOR_ID         = "editor_id";
    String EDITOR_NAME       = "editor_name";
    // -------- } Column Defines

    String FIELD_PK          = "id";
    String FIELDS_WITHOUT_PK = "action_id,relation_id,edit_time,editor_id,editor_name";
    String FIELDS_ALL        = FIELD_PK + "," + FIELDS_WITHOUT_PK;

    @SQL("select " + FIELDS_ALL + " from " + TABLE)
    public List<SecurityRelationSetting> queryAll();

    @SQL("select action_id from " + TABLE + " where relation_id = :1")
    public Set<Integer> queryActionIdSetByRelationId(int relationId);

    @SQL("select relation_id from " + TABLE + " where action_id = :1")
    public Set<Integer> queryRelationIdSetByActionId(int actionId);

    @SQL("select COUNT(" + FIELD_PK + ") from " + TABLE)
    public int countAll();

    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `id` = :1")
    public SecurityRelationSetting query(int id);

    String SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK = "action_id=:model.actionId,relation_id=:model.relationId,edit_time=:model.editTime,editor_id=:model.editorId,editor_name=:model.editorName";

    @SQL("update " + TABLE + " set " + SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK + " where id=:1.id")
    public int update(@SQLParam("model") SecurityRelationSetting model);

    @SQL("insert ignore into " + TABLE + " (" + FIELDS_WITHOUT_PK
            + ") VALUES (:model.actionId,:model.relationId,:model.editTime,:model.editorId,:model.editorName)")
    @ReturnGeneratedKeys
    public Integer save(@SQLParam("model") SecurityRelationSetting model);

    @SQL("delete from " + TABLE + " where `id` = :1")
    public void delete(int id);

    @SQL("delete from $TABLE where relation_id = :1 and action_id in (:2)")
    public void removeActionIdsOfRelationId(int relationId, Collection<?> actionIds);

    @SQL("delete from $TABLE where action_id = :1 and relation_id in (:2)")
    public void removeRelationIdsOfActionId(Integer actionId, Collection<?> relationIds);
}