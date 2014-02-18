package com.renren.profile.biz.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.renren.profile.RenrenProfileConstants;
import com.renren.profile.biz.model.PromotionManage;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

/**
 * @author 张浩 E-mail:hao.zhang@renren-inc.com
 * @date 2012-3-13 上午10:09:03
 *
 * 升职管理DAO
 */

@DAO(catalog = RenrenProfileConstants.DB_CATALOG)
public interface PromotionManageDAO {
		String TABLE             = "promotion_manage";
	    // -------- { Column Defines
	    String ID                = "id";
	    String USER_PERF_ID      = "user_perf_id";
	    String PERF_TIME_ID      = "perf_time_id";
	    String IS_PROMOTION      = "is_promotion";
	    String PROMOTION_CONTENT = "promotion_content";
	    String OPERATOR          = "operator";
	    // -------- } Column Defines

	    String FIELD_PK          = "id";
	    String FIELDS_WITHOUT_PK = "user_perf_id,perf_time_id,is_promotion,promotion_content,operator";
	    String FIELDS_ALL        = FIELD_PK + "," + FIELDS_WITHOUT_PK;
	    
	   
	    @SQL("select "+FIELDS_ALL+" from "+TABLE+" limit :offset,:count")
	    public List<PromotionManage> queryAll(@SQLParam("offset")int offset,@SQLParam("count") int count);
	    
	    /**
	     * 按perf_time_id查询,加翻页
	     * @param perfTimeId
	     * @param offset
	     * @param count
	     * @return
	     */
	    @SQL("select "+FIELDS_ALL+" from "+TABLE+" limit :offset,:count;")
	    public List<PromotionManage> queryListByQ(int perfTimeId,@SQLParam("offset")int offset,@SQLParam("count") int count);
	    
	    /**
	     * 按perf_time_id查询,加翻页
	     * @param perfTimeId
	     * @param offset
	     * @param count
	     * @return
	     */
	    @SQL("select "+FIELDS_ALL+" from "+TABLE+" limit :offset,:count;")
	    public Map<Integer,PromotionManage> queryMapByQ(int perfTimeId,@SQLParam("offset")int offset,@SQLParam("count") int count);
	    
	    /**
	     * 按Collection查询用户反馈记录
	     * @param userPerfIds
	     * @return
	     */
	    @SQL("select user_perf_id, id from "+TABLE+" where user_perf_id in (:1);")
	    public Map<Integer,Integer> queryMapByCollection(Collection<Integer> userPerfIds);
	    
	    
	  	/**
	  	 * 查询某一perf时申请升职的记录数	
	  	 * @param perfTimeId
	  	 * @return
	  	 */
	    @SQL("select COUNT("+FIELD_PK+") from "+TABLE + " where perf_time_id=:1;")
	    public int countAllByQ(int perfTimeId);
	    
	    /**
	     * 按ID查询
	     * @param id
	     * @return
	     */
	    @SQL("select "+FIELDS_ALL+" from "+TABLE+" where `id` = :1")
	    public PromotionManage query(int id);
	    
	    /**
	     * 按用户绩效记录ID查询
	     * @param userPerfId
	     * @return
	     */
	    @SQL("select "+FIELDS_ALL+" from "+TABLE+" where `user_perf_id` = :1")
	    public PromotionManage queryByUserPerfId(int userPerfId);
	    
	    String SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK = "user_perf_id=:model.userPerfId,perf_time_id=:model.perfTimeId,is_promotion=:model.isPromotion,promotion_content=:model.promotionContent,operator=:model.operator";
	    
	    @SQL("update "+TABLE+" set "+SQL_UPDATE_MODEL_FILEDS_WITHOUT_PK+" where id=:1.id")
	    public int update(@SQLParam("model") PromotionManage model);
	    
	    @SQL("insert ignore into " + TABLE +" ("+FIELDS_WITHOUT_PK+") VALUES (:model.userPerfId,:model.perfTimeId,:model.isPromotion,:model.promotionContent,:model.operator)")
	  	@ReturnGeneratedKeys
	    public Integer save(@SQLParam("model") PromotionManage model);
	    
	    @SQL("delete from "+TABLE+" where `id` = :1")
	    public void delete(int id);
}
