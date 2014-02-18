package com.renren.profile.biz.model;

import org.apache.commons.lang.StringUtils;

/**
 * @author 张浩 E-mail:hao.zhang@renren-inc.com
 * @date 2012-3-13 上午10:13:21
 *
 * 升职管理Model
 */

public class PromotionManage {
		
	//自增主键
	private int id = 0;
	
	//申请升职的用户绩效Id
	private int userPerfId = 0;
	
	//绩效时间记录ID
	private int perfTimeId = 0;
	
	//是否同意升职
	private int isPromotion = 0;
	
	//升职反馈意见
	private String promotionContent = StringUtils.EMPTY;
	
	//操作人
	private int operator = 0;

	public static final int PROMOTION_YES = 0;
	public static final int PROMOTION_NO  = 1;
	
	/**
	 * 自增主键
	 * @return
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * 自增主键
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * 申请升职的用户绩效记录Id
	 * @return
	 */
	public int getUserPerfId() {
		return userPerfId;
	}
	/**
	 * 申请升职的用户绩效记录Id
	 * @param userId
	 */
	public void setUserPerfId(int userPerfId) {
		this.userPerfId = userPerfId;
	}
	/**
	 * 绩效时间记录ID
	 * @return
	 */
	public int getPerfTimeId() {
		return perfTimeId;
	}
	/**
	 * 绩效时间记录ID
	 * @param perfTimeId
	 */
	public void setPerfTimeId(int perfTimeId) {
		this.perfTimeId = perfTimeId;
	}
	/**
	 * 是否同意升职
	 * @param isPromotion
	 */
	public int getIsPromotion() {
		return isPromotion;
	}
	/**
	 * 是否同意升职
	 * @param isPromotion
	 */
	public void setIsPromotion(int isPromotion) {
		this.isPromotion = isPromotion;
	}
	/**
	 * 升职反馈意见
	 * @return
	 */
	public String getPromotionContent() {
		return promotionContent;
	}
	/**
	 * 升职反馈意见
	 * @param promotionContent
	 */
	public void setPromotionContent(String promotionContent) {
		this.promotionContent = promotionContent;
	}
	/**
	 * 操作人
	 * @return
	 */
	public int getOperator() {
		return operator;
	}
	/**
	 * 操作人
	 * @param operator
	 */
	public void setOperator(int operator) {
		this.operator = operator;
	}
	
	
}
