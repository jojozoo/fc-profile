package com.orientalcomics.profile.biz.model;

/**
 * @author 张浩 E-mail:hao.zhang@renren-inc.com
 * @date 2012-4-16 下午12:26:43
 *
 * 可以兑换的奖励列表
 */

public class RewardItem {
	
	//奖励ID
	private int id;
	
	//奖励名称
	private String name;
	
	//奖励类型
	private int type;
	
	//需要的小红花数量
	private int 	needNum;
	
	//图片
	private String img;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getNeedNum() {
		return needNum;
	}
	public void setNeedNum(int needNum) {
		this.needNum = needNum;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	
}
