package com.renren.profile.biz.dao;

import java.util.Collection;
import java.util.Map;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import com.renren.profile.RenrenProfileConstants;
import com.renren.profile.biz.model.UserProfile;

@DAO(catalog = RenrenProfileConstants.DB_CATALOG)
public interface UserProfileDAO {
    String TABLE             = "user_profile";
    // -------- { Column Defines
    String USER_ID           = "user_id";
    String GENDER            = "gender";
    String RR                = "rr";
    String QQ                = "qq";
    String EXT_NUMBER        = "ext_number";
    String HOBBY             = "hobby";
    String MOBILE            = "mobile";
    // -------- } Column Defines

    String FIELD_PK          = "user_id";
    String FIELDS_WITHOUT_PK = "gender,rr,qq,ext_number,hobby,mobile";
    String FIELDS_ALL        = FIELD_PK + "," + FIELDS_WITHOUT_PK;

    /**
     * 批量查询
     * @param offset
     * @param count
     * @return map
     */
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where user_id in (:1) ")
    public Map<Integer,UserProfile> query(Collection<Integer> userIds);

    @SQL("select COUNT(" + FIELD_PK + ") from " + TABLE)
    public int countAll();

    /**
     * 查询id单个记录
     * @param userId
     * @return
     */
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `user_id` = :1")
    public UserProfile query(int userId);
    
   

    String SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK = "gender=:model.gender,rr=:model.rr,qq=:model.qq,ext_number=:model.extNumber,hobby=:model.hobby,mobile=:model.mobile";
    /**
     * 更新记录
     * @param model
     * @return
     */
    @SQL("update " + TABLE + " set " + SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK + " where user_id=:1.userId")
    public int update(@SQLParam("model") UserProfile model);
    
    /**
     * 修改用户业余爱好
     * @param model
     * @return
     */
    @SQL("update " + TABLE + " set hobby=:1 where user_id=:2")
    public int updateUserHobby(String hobby, int userId);
    
    /**
     * 修改用户QQ
     * @param model
     * @return
     */
    @SQL("update " + TABLE + " set qq=:1 where user_id=:2")
    public int updateUserQQ(long qq, int userId);

    /**
     * 插入数据
     * @param model
     * @return
     */
    @SQL("insert ignore into " + TABLE + " (" + FIELDS_ALL
            + ") VALUES (:model.userId,:model.gender,:model.rr,:model.qq,:model.extNumber,:model.hobby,:model.mobile)")
    @ReturnGeneratedKeys
    public Integer save(@SQLParam("model") UserProfile model);

    @SQL("delete from " + TABLE + " where `user_id` = :1")
    public void delete(int userId);
}