package com.orientalcomics.profile.biz.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.object.SqlOperation;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.biz.model.User;

@DAO(catalog = OcProfileConstants.DB_CATALOG)
public interface UserDAO {
    String TABLE                  = "user";
    // -------- { Column Defines
    String ID                     = "id";
    String NAME                   = "name";
    String NUMBER                 = "number";
    String STATUS                 = "status";
    String PEMISSION_LEVEL        = "pemission_level";
    String JOB_TITLE              = "job_title";
    String LEVEL                  = "level";
    String SHOW_LEVEL             = "show_level";
    String DEPARTMENT_ID          = "department_id";
    String MANAGER_ID             = "manager_id";
    String EMAIL                  = "email";
    String TINY_URL               = "tiny_url";
    String MAIN_URL               = "main_url";
    String VIRTUAL_REWARD_ITEM_ID = "virtual_reward_item_id";
    String KPI_OPEN               = "kpi_open";
    // -------- } Column Defines

    String FIELD_PK               = "id";
    String FIELDS_WITHOUT_PK      = "name,number,email,tiny_url,main_url,status,pemission_level,job_title,level,show_level,department_id,manager_id,virtual_reward_item_id,kpi_open";
    String FIELDS_ALL             = FIELD_PK + "," + FIELDS_WITHOUT_PK;
    String INDEX_FIELDS_ALL       = "id,name,email";

    /**
     * 按偏移量查询员工列表,stauts=0,正常状态员工
     * 
     * @param offset
     * @param count
     * @return
     */
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where status=0 limit :offset,:count")
    public List<User> queryValidUsers(@SQLParam("offset") int offset, @SQLParam("count") int count);

    /**
     * 查询整个主管信息
     * 
     * @param offset
     * @param count
     * @return
     */
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where status=0 and id in (select " + MANAGER_ID + " from " + TABLE + " )")
    public List<User> queryAllManager();

    // 得到自己的下属数目
    @SQL("select count(id) from " + TABLE + " where status=0 and manager_id=:1")
    public Integer queryMyFollwerNumber(int userId);
    
    // 得到没有上传图片
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where status=0 and tiny_url=:1")
    public List<User> queryNOUploadImage(String url);

    /**
     * 获取一系列User
     * 
     * @param ids
     * @return
     */
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where status=0 and id in (:1)")
    public List<User> queryAllList(Collection<Integer> ids);

    /**
     * 获取一系列User
     * 
     * @param ids
     * @return
     */
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where status=0 and department_id = (:1)")
    public List<User> queryByDepartment(int department_id);

    /**
     * 获取一系列User
     * 
     * @param ids
     * @return
     */
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where status=0 and id in (:1)")
    public Map<Integer, User> queryAllMap(Collection<Integer> ids);

    @SQL("select COUNT(" + FIELD_PK + ") from " + TABLE + " where status=0")
    public int countAll();

    @SQL("select COUNT(" + FIELD_PK + ") from " + TABLE+" where status=0")
    public int countValidUsers();
    
    @SQL("select COUNT(" + FIELD_PK + ") from " + TABLE + " where manager_id=:1 and status=0")
    public int countSubordinates(int userId);
    
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where manager_id=:1 and status=0")
    public List<User> getSubordinatesId(int userId);

    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where manager_id=:1 and status=0 order by id asc limit :offset,:count")
    public List<User> querySubordinates(int leaderId, @SQLParam("offset") int offset, @SQLParam("count") int count);

    @SQL("select COUNT(" + FIELD_PK + ") from " + TABLE + " where department_id=:1 and status=0")
    public int countAllInDepartment(int departmentId);
    
    @SQL("select COUNT(" + FIELD_PK + ") from " + TABLE + " where department_id in (:1) and status=0")
    public int countAllInDepartmentRecursive(Collection<Integer> departIds);
    
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where department_id=:1 and status=0 order by id asc limit :offset,:count")
    public List<User> queryAllInDepartment(int departmentId, @SQLParam("offset") int offset, @SQLParam("count") int count);
    
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where department_id in (:1) and status=0 order by email")
    public List<User> queryAllInDepartmentRecursive(Collection<Integer> departIds);


    
    /**
     * 按email或者名字查询单个查询
     * 
     * @param email或者名字
     * @return
     */
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where status=0 and `email` = :1 or `name` = :1 limit 1")
    public User queryByEmail(String email);

    /**
     * 通过集合用户id，批量的查询出用户信息
     * 
     * @param userIdList
     * @return
     */
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where status=0 and `id` in (:1)")
    public List<User> queryByUserIds(List<Integer> userIdList);

    /**
     * 按UserID单个查询，正常用户才可以查询
     * 
     * @param id
     * @return
     */
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where status=0 and `id` = :1")
    public User query(int id);
    
    /**
     * 按照工号查询单个用户信息
     * 
     * @param number
     */
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where status=0 and `number` = :1 limit 1")
    public User queryByNumber(String number);

    /**
     * 按collection查询
     * 
     * @param id
     * @return
     */
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `id` in (:1)")
    public Map<Integer, User> query(Collection<Integer> ids);

    /**
     * 按UserID单个查询,我的下属
     * 
     * @param id
     * @return
     */
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `manager_id` = :1 and `status` = 0")
    public List<User> queryAllMyFollow(int id);
    
    /**
     * 按UserID单个查询,我的下属
     * 
     * @param id
     * @return
     */
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where `manager_id` = :1 and `kpi_open` = :2 and `status` = 0")
    public List<User> queryAllMyFollowExcludeKpiOpen(int id,int kpi_open);
    
    


    @SQL("select " + INDEX_FIELDS_ALL + " from " + TABLE +" where status=0")
    public List<User> queryAllWithIndexableFields();

    @SQL("select " + MANAGER_ID + " from " + TABLE + " where status=0 and id = :1")
    public Integer queryLeader(int userId);

    @SQL("select " + DEPARTMENT_ID + " from " + TABLE + " where id = :1")
    public Integer queryDepartment(int userId);

    /**
     * 获取所有用户的id集合
     * 
     * @return
     */
    @SQL("select " + ID + " from " + TABLE + " where status=0")
    public Collection<Integer> queryAllIds();

    /**
     * 按指定的条件搜索，注意：防止SQL注入
     * 
     * @param condition
     * @return
     */
    @SQL("select " + FIELDS_ALL + " from " + TABLE + " where status=0 and ##(:1) limit :offset,:count")
    public List<User> queryByCondition(String condition, @SQLParam("offset") int offset, @SQLParam("count") int count);

    String SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK = "name=:model.name,number=:model.number,email=:model.email,tiny_url=:model.tinyUrl,main_url=:model.mainUrl,status=:model.status,pemission_level=:model.pemissionLevel,job_title=:model.jobTitle,level=:model.level,show_level=:model.showLevel,department_id=:model.departmentId,manager_id=:model.managerId,virtual_reward_item_id=:model.virtualRewardItemId,kpi_open=:model.kpiOpen";

    /**
     * 更新员工信息
     * 
     * @param model
     * @return
     */
    @SQL("update " + TABLE + " set " + SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK + " where id=:model.id")
    public int update(@SQLParam("model") User model);

    /**
     * 修改用户头像
     * 
     * @param model
     * @return
     */
    @SQL("update " + TABLE + " set tiny_url=:2,main_url=:3 where id=:1")
    public int updateUserPhotos(int userId, String tinyUrl, String mainUrl);

    /**
     * 更新员工职称是否显示
     * 
     * @param model
     * @return
     */
    @SQL("update " + TABLE + " set show_level=:1 where id=:2")
    public int updateUserShowLevel(int is_show, int userId);
    
    /**
     * 更新员工级别
     * 
     * @param model
     * @return
     */
    @SQL("update " + TABLE + " set level=:1 where id=:2")
    public int updateUserLevel(String level, int userId);
    
    // 更新用户的是否离职或离岗
    @SQL("update " + TABLE + " set status=1 where email=:1")
    public int updateUserStatus(String email);

    /**
     * 插入员工信息
     * 
     * @param model
     * @return
     */
    @SQL("insert ignore into "
            + TABLE
            + " ("
            + FIELDS_ALL
            + ") VALUES (:model.id,:model.name,:model.number,:model.email,:model.tinyUrl,:model.mainUrl,:model.status,:model.pemissionLevel,:model.jobTitle,:model.level,:model.showLevel,:model.departmentId,:model.managerId,:model.virtualRewardItemId,:model.kpiOpen)")
    @ReturnGeneratedKeys
    public Integer save(@SQLParam("model") User model);

    /**
     * 删除员工信息
     * 
     * @param id
     */
    @SQL("delete from " + TABLE + " where `id` = :1")
    public void delete(int id);
    

    /**
     * 删除员工信息
     * 
     * @param id
     */
    @SQL("delete from " + TABLE + " where `email` = :1")
    public void deleteBy(String email);

}