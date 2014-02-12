package com.orientalcomics.profile.biz.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.biz.model.UserProfile;

@DAO(catalog = OcProfileConstants.DB_CATALOG)
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
    String RENREN_LINK       = "renren_link";
    String IS_DISPLAY        = "is_display";
    String GRADUATE_SCHOOL   = "graduate_school";
    String birthday          = "birthday";
    // -------- } Column Defines

    String FIELD_PK          = "user_id";
    String FIELDS_WITHOUT_PK = "gender,rr,qq,ext_number,hobby,mobile,renren_link,is_display,graduate_school,birthday";
    String FIELDS_ALL        = FIELD_PK + "," + FIELDS_WITHOUT_PK;

    /**
     * 批量查询
     * @param offset
     * @param count
     * @return map
     */
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where user_id in (:1) ")
    public Map<Integer,UserProfile> query(Collection<Integer> userIds);
    
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where user_id in (:1) ")
    public List<UserProfile> queryBatch(Collection<Integer> userIds);

    @SQL("select COUNT(" + FIELD_PK + ") from " + TABLE)
    public int countAll();

    /**
     * 查询id单个记录
     * @param userId
     * @return
     */
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `user_id` = :1")
    public UserProfile query(int userId);
    
   

    String SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK = "gender=:model.gender,rr=:model.rr,qq=:model.qq,ext_number=:model.extNumber,hobby=:model.hobby,mobile=:model.mobile,renren_link=:model.renrenLink,is_display=:model.isDisplay,graduate_school=:model.graduateSchool,birthday=:model:birthday";
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
    
    @SQL("update " + TABLE + " set is_display=:1 where user_id=:2")
    public int updateUserSchool(int isShowSchool,int userId);
    
    @SQL("update " + TABLE + " set graduate_school=:1 where user_id=:2")
    public int updateUserGraduateSchool(String graduateSchool,int userId);
    
    @SQL("update " + TABLE + " set birthday=:1 where user_id=:2")
    public int updateUserBirthday(String birthday,int userId);
    
    
    /**
     * 修改用户QQ
     * @param model
     * @return
     */
    @SQL("update " + TABLE + " set qq=:1 where user_id=:2")
    public int updateUserQQ(long qq, int userId);
    
    /**
     * 修改用户手机号码
     * @return
     */
    @SQL("update " + TABLE + " set mobile=:1 where user_id=:2")
    public int updateUserMobile(String qq, int userId);
    
    /**
     * 修改用户座机号码
     * @return
     */
    @SQL("update " + TABLE + " set ext_number=:1 where user_id=:2")
    public int updateUserExtNumber(String extNumber, int userId);

    /**
     * 修改用户人人主页链接
     * 
     * @return
     */
    @SQL("update " + TABLE + " set renren_link=:1 where user_id=:2")
    public int updateUserRenrenLink(String renrenLink, int userId);
    
    /**
     * 插入数据
     * @param model
     * @return
     */
    @SQL("insert ignore into " + TABLE + " (" + FIELDS_ALL
            + ") VALUES (:model.userId,:model.gender,:model.rr,:model.qq,:model.extNumber,:model.hobby,:model.mobile,:model.renrenLink,:model.is_display,:model.graduate_school,:model.birthday)")
    @ReturnGeneratedKeys
    public Integer save(@SQLParam("model") UserProfile model);

    @SQL("delete from " + TABLE + " where `user_id` = :1")
    public void delete(int userId);
}