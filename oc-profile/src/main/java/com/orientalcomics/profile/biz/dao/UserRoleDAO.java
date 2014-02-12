package com.orientalcomics.profile.biz.dao;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.biz.model.UserRole;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;


@DAO(catalog = OcProfileConstants.DB_CATALOG)
public interface UserRoleDAO {
    String TABLE             = "user_role";
    // -------- { Column Defines
    String ID                = "id";
    String USER_ID           = "user_id";
    String ROLE_ID           = "role_id";
    String EDIT_TIME         = "edit_time";
    String EDITOR_ID         = "editor_id";
    String EDITOR_NAME       = "editor_name";
    // -------- } Column Defines

    String FIELD_PK          = "id";
    String FIELDS_WITHOUT_PK = "user_id,role_id,edit_time,editor_id,editor_name";
    String FIELDS_ALL        = FIELD_PK + "," + FIELDS_WITHOUT_PK;

    @SQL("select " + FIELDS_ALL + " from " + TABLE + " limit :offset,:count")
    public List<UserRole> queryAll(@SQLParam("offset") int offset, @SQLParam("count") int count);

    @SQL("select count(1) from " + TABLE + " where user_id = :1 and role_id = :2")
    public boolean existUserAndRole(int userId,int roleId);
    
    @SQL("select role_id from " + TABLE + " where user_id = :1")
    public Set<Integer> queryRoleIdSetByUser(int userId);

    @SQL("select user_id from " + TABLE + " where role_id = :1 limit :offset,:count")
    public List<Integer> queryUserIdListByRole(int roleId,@SQLParam("offset") int offset, @SQLParam("count") int count);

    @SQL("select COUNT(" + FIELD_PK + ") from " + TABLE)
    public int countAll();

    @SQL("select COUNT(" + FIELD_PK + ") from " + TABLE + " where role_id = :1")
    public int countByRole(int roleId);

    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `id` = :1")
    public UserRole query(int id);

    String SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK = "user_id=:model.userId,role_id=:model.roleId,edit_time=:model.editTime,editor_id=:model.editorId,editor_name=:model.editorName";

    @SQL("update " + TABLE + " set " + SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK + " where id=:1.id")
    public int update(@SQLParam("model") UserRole model);

    @SQL("insert ignore into " + TABLE + " (" + FIELDS_WITHOUT_PK + ") VALUES (:model.userId,:model.roleId,:model.editTime,:model.editorId,:model.editorName)")
    @ReturnGeneratedKeys
    public Integer save(@SQLParam("model") UserRole model);

    @SQL("delete from " + TABLE + " where `id` = :1")
    public void delete(int id);
    
    @SQL("delete from " + TABLE + " where `user_id` = :1 and role_id = :2")
    public void deleteByUserAndRole(int userId,int roleId);

    @SQL("delete from $TABLE where user_id = :1 and role_id in (:2)")
    public void removeRoleIdsOfUser(int userId, Collection<Integer> roleIds);
}