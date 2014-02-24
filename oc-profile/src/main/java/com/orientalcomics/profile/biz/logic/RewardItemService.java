package com.orientalcomics.profile.biz.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orientalcomics.profile.biz.dao.RewardItemDAO;
import com.orientalcomics.profile.biz.model.RewardItem;

/**
 * @author 张浩 E-mail:hao.zhang@renren-inc.com
 * @date 2012-4-17 下午06:14:42
 *
 * rewardItemService
 */
@Service
public class RewardItemService {
	
	@Autowired
	private RewardItemDAO dao;
	
	public RewardItem query(int id){
		return dao.query(id);
	}
}
