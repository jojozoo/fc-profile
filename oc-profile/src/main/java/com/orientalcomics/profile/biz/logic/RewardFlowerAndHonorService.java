package com.orientalcomics.profile.biz.logic;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.biz.dao.HonorDAO;
import com.orientalcomics.profile.biz.dao.RewardFlowerDAO;
import com.orientalcomics.profile.biz.dao.RewardItemDAO;
import com.orientalcomics.profile.biz.model.Honor;
import com.orientalcomics.profile.biz.model.PerfTime;
import com.orientalcomics.profile.biz.model.RewardFlower;
import com.orientalcomics.profile.biz.model.RewardItem;
import com.orientalcomics.profile.biz.model.User;
import com.orientalcomics.profile.constants.RewardItemType;
import com.orientalcomics.profile.constants.status.RewardStatus;
import com.orientalcomics.profile.util.common.Collections0;

@Service
public class RewardFlowerAndHonorService {

	@Autowired
	RewardFlowerDAO rewardFlowerDAO;
	
	@Autowired
	private HonorDAO honorDAO;
	
	@Autowired
	PerfTimeService perfTimeService;
	
	@Autowired
	UserService     userService;
	
	@Autowired
	private RewardItemDAO rewardItemDAO;
	
	/**
	 * <p> 获取现在年度所有季度别人给userId赠送的小红花
	 * 
	 * <p> 注意：赠送的小红花只有主管同意才有效即status为2，已经兑换成别的东西小红花不显示
	 * 
	 * @param userId
	 * @return
	 */
	public List<RewardFlower> queryRewardFlowerByYear(int userId,int year){
		
		List<PerfTime> perfTimes = perfTimeService.queryByYear(year);
		if(Collections0.isEmpty(perfTimes))
			return null;
		
		Collection<Integer> perfTimeIds = Collections2.transform(perfTimes, new Function<PerfTime,Integer>(){

			@Override
			public Integer apply(PerfTime perfTime) {
				return perfTime !=null ? Integer.valueOf(perfTime.getId()) : null;
			}
		});
		
		return rewardFlowerDAO.queryByPerfTimeAndUserIdAndStatus(userId ,RewardStatus.LEADER_AGREE.getId(), perfTimeIds);
		
	}
	
	public RewardFlower queryRewardFlower(int fromId,int toId){
		PerfTime perfTime = perfTimeService.getLastestValid();
		return rewardFlowerDAO.querySendFlowerFromby(fromId, toId, RewardStatus.SEND_REWARD.getId(), perfTime.getId());
	}
	
	/**
	 * 得到我的下属需要确认的小红花
	 * 
	 * @return
	 */
	public List<RewardFlower> getMyFollowNeedConfirmFlower(int userId){
		
		int                perfTimeId = perfTimeService.getCurrentId();
		List<RewardFlower> list       = rewardFlowerDAO.queryByPerfTime(perfTimeId);
		
		List<User> userList = userService.getSubordinatesIds(userId);
		if(Collections0.isEmpty(list))
			return null;
		
		if(Collections0.isEmpty(userList))
			return null;
		
		List<RewardFlower> confirmList = new ArrayList<RewardFlower>();
		for(RewardFlower flower : list)
			for(User user : userList)
				if(flower.getToId() == user.getId()){
				    flower.setReceiveName(user.getName());
					confirmList.add(flower);
				}
		
		return confirmList;
		
	}
	
	/**
	 * <p> 按年度中季度quartor，获取别人给userId赠送的小红花
	 * 
	 * @param userId
	 * @param quartor
	 * @return
	 */
	public List<RewardFlower> queryRewardFlowerByQuartor(int userId,String quartor){
		
		PerfTime perfTime = perfTimeService.queryByQuartor(quartor);
		if(perfTime == null)
			return null;
		
		return rewardFlowerDAO.queryByPerfTimeOfQuartorAndUserIdAndStatus(userId, RewardStatus.LEADER_AGREE.getId(), perfTime.getId());
	}
	
	/**
	 * <p> 按年度中季度quartor，获取自己给别人赠送的小红花的用户列表
	 * 
	 * @param userId
	 * @param quartor
	 * @return
	 */
	public Map<Integer,User> querySendRewardFlowerByQuartor(int userId,String quartor){
		
		PerfTime perfTime = perfTimeService.queryByQuartor(quartor);
		if(perfTime == null)
			return null;
		
		List<Integer> userIds = rewardFlowerDAO.queryByPerfTimeOfQuartorAndFromIdAndStatus(userId, RewardStatus.LEADER_AGREE.getId(), perfTime.getId());
		if(Collections0.isEmpty(userIds))
			return null;
		
		Map<Integer,User> map = userService.queryAllMap(userIds);
		for(User user : map.values()){
			user.setRewardFlower(rewardFlowerDAO.queryByPerfTimeOfQuartor(userId,user.getId(), RewardStatus.LEADER_AGREE.getId(), perfTime.getId()));
		}
		
		return map;
		
	}
	
	/**
	 * <p> 按年获取自己给别人赠送的小红花的用户列表
	 * 
	 * @param userId
	 * @param quartor
	 * @return
	 */
	public Map<Integer,User> querySendRewardFlowerByYear(int userId,int year){
		
		List<PerfTime> perfTimes = perfTimeService.queryByYear(year);
		if(Collections0.isEmpty(perfTimes))
			return null;
		
		Collection<Integer> perfTimeIds = Collections2.transform(perfTimes, new Function<PerfTime,Integer>(){

			@Override
			public Integer apply(PerfTime perfTime) {
				return perfTime !=null ? Integer.valueOf(perfTime.getId()) : null;
			}
		});
		
		List<Integer> userIds = rewardFlowerDAO.queryByPerfTimeOfYearAndFromIdAndStatus(userId, RewardStatus.LEADER_AGREE.getId(), perfTimeIds);
		if(Collections0.isEmpty(userIds))
			return null;
		
		Map<Integer,User> map = userService.queryAllMap(userIds);
		for(User user : map.values()){
			user.setRewardFlower(rewardFlowerDAO.queryByPerfTimeOfYear(userId,user.getId(), RewardStatus.LEADER_AGREE.getId(), perfTimeIds));
		}
		
		return map;
		
	}
	
	/**
	 * 统计自己获得的小红花并且小红花没有进行兑换
	 * 
	 * @param userId
	 * @return
	 */
	public int countSelfObtainRewardFlowerWithoutExchange(int userId){
		
		return rewardFlowerDAO.countReward(userId, RewardStatus.LEADER_AGREE.getId());
		
	}
	
	/**
	 * 统计当前用户在本季度发送给好友的小红花数目
	 * 
	 * @param userId
	 * @return
	 */
	public int countFlowerNumberByCurrentPerfTime(int userId){
		
		PerfTime perfTime = perfTimeService.getCurrent();
		if(perfTime == null)
			return Integer.valueOf(0);
		
		return rewardFlowerDAO.countFlowerByPerfTimeId(userId, RewardStatus.SEND_REWARD.getId(), perfTime.getId());
	}
	
	/**
	 * 统计当前用户在本季度发送给好友的小红花数目不包含领导不同意的小红花
	 * 
	 * @param userId
	 * @return
	 */
	public int countFlowerNumberByCurrentPerfTimeNotContainLeaderDisagree(int userId){
		
		PerfTime perfTime = perfTimeService.getCurrent();
		if(perfTime == null)
			return Integer.valueOf(0);
		
		return rewardFlowerDAO.countFlowerByPerfTimeIdNotContainLeaderDisagree(userId, RewardStatus.LEADER_DISAGREE.getId(), perfTime.getId());
	}
	
	/***
	 * <p> 核实fromUserId用户是否在本季度已经发送小红花给toUserId，
	 * 
	 * <p> 如果存在就返回true，否则返回false
	 * 
	 * @param fromUserId
	 * @param toUserId
	 * @return
	 */
	public RewardFlower getUserSendRewardFlower(int fromUserId,int toUserId){
		
		PerfTime perfTime = perfTimeService.getCurrent();
		if(perfTime == null)
			return null;
		
		return rewardFlowerDAO.querySendFlowerFromby(fromUserId, toUserId, RewardStatus.SEND_REWARD.getId(), perfTime.getId()) ;
	}
	
	/**
	 * 保存用户发送的小红花
	 * 
	 * @param user 当前用户要送小红花
	 * @param toId  接收小红花的人
	 * @param reason  送小红花的理由
	 */
	public Integer saveRewardFlower(User user,int toId,String reason){
		
		RewardFlower rewardFlower = new RewardFlower();
		rewardFlower.setFromId(user.getId());
		rewardFlower.setUserName(user.getName());
		rewardFlower.setToId(toId);
		rewardFlower.setReason(reason);
		rewardFlower.setEditTime(new Timestamp(new Date().getTime()));
		rewardFlower.setImageUrl(OcProfileConstants.EMPTY_MAIN_URL);
		rewardFlower.setStatus(RewardStatus.SEND_REWARD.getId());
		rewardFlower.setPerfTimeId(perfTimeService.getCurrentId());
		
		return rewardFlowerDAO.save(rewardFlower);
		
	}
	
	//:to do perfTimeId结束呢？
	public boolean updateRewardFlower(int status,int fromId,int toId){
		
		int perfTimeId = perfTimeService.getCurrentId();
		return rewardFlowerDAO.updateStatus(status, fromId, toId,perfTimeId) > 0 ? true : false;
		
	}
	/**
	 * 查询一个{@link RewardItem}记录
	 * @param id
	 * @return
	 */
	public RewardItem queryRewardItem(int id){
		return  rewardItemDAO.query(id);
	}
	/**
	 * 获取当前用户可以获得奖励的列表
	 * @param user
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RewardItem> userCanGetRewards(User user,RewardItemType type){
		try{
			int flowers = countSelfObtainRewardFlowerWithoutExchange(user.getId());
			List<RewardItem> list =  rewardItemDAO.queryByNeedNum(flowers,type.getId());
			if(list != null){
				return list;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ListUtils.EMPTY_LIST;
	}
	/**
	 * 用户是否可以获得这个
	 * @param item
	 * @return
	 */
	public boolean isUserCanGetReward(RewardItem item,User user){
		int flowers = countSelfObtainRewardFlowerWithoutExchange(user.getId());
		return flowers >= item.getNeedNum();
	}
	
	/**
	 * 保存兑换的honor记录
	 * @param user
	 * @param honor
	 * @return
	 */
	public Integer saveHonor(User user,Honor honor){
		
		return honorDAO.save(honor);
	}
	
	/**
	 * 更改用户的小红花状态
	 * @param num
	 * @param user
	 */
	public void changeFlowerStateForNum(int num,User user){
		try{
			rewardFlowerDAO.changeStateForNum(user.getId(),num);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
