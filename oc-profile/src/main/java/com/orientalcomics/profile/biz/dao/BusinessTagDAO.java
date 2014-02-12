package com.orientalcomics.profile.biz.dao;

import java.util.Collection;
import java.util.List;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.biz.model.BusinessTag;

@DAO(catalog = OcProfileConstants.DB_CATALOG)
public interface BusinessTagDAO {
    String TABLE             = "business_tag";
    // -------- { Column Defines
    String ID                = "id";
    String TAG_NAME          = "tag_name";
    String EDIT_TIME         = "edit_time";
    String CHARGE_USER       = "charge_user";
    // -------- } Column Defines

    String FIELD_PK          = "id";
    String FIELDS_WITHOUT_PK = "tag_name,edit_time,charge_user";
    String FIELDS_ALL        = FIELD_PK + "," + FIELDS_WITHOUT_PK;

    @SQL("select " + FIELDS_ALL + " from " + TABLE + " limit :offset,:count")
    public List<BusinessTag> queryAll(@SQLParam("offset") int offset, @SQLParam("count") int count);

    @SQL("select " + FIELDS_ALL + " from " + TABLE)
    public List<BusinessTag> queryAll();

    @SQL("select COUNT(" + FIELD_PK + ") from " + TABLE)
    public int countAll();

    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `charge_user` = :1")
    public List<BusinessTag> query(int chargeUserId);

    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `charge_user` in (:1)")
    public List<BusinessTag> query(Collection<Integer> chargeUserIds);

    String SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK = "tag_name=:model.tagName,edit_time=:model.editTime,charge_user=:model.chargeUser";

    @SQL("update " + TABLE + " set " + SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK + " where id=:1.id")
    public int update(@SQLParam("model") BusinessTag model);

    @SQL("insert ignore into " + TABLE + " (" + FIELDS_WITHOUT_PK + ") VALUES (:model.tagName,:model.editTime,:model.chargeUser)")
    @ReturnGeneratedKeys
    public Integer save(@SQLParam("model") BusinessTag model);

    @SQL("delete from " + TABLE + " where `id` = :1")
    public void delete(int id);

    @SQL("delete from " + TABLE + " where `charge_user` = :1 and `tag_name` = :2")
    public void deleteByUserIdAndTagName(int userId, String tagName);
}