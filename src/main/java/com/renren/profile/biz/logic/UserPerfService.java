package com.renren.profile.biz.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.renren.profile.RenrenProfileConstants;
import com.renren.profile.biz.dao.PeerPerfDAO;
import com.renren.profile.biz.dao.PeerPerfProjectDAO;
import com.renren.profile.biz.dao.PerfTimeDAO;
import com.renren.profile.biz.dao.UserDAO;
import com.renren.profile.biz.dao.UserInvitationDAO;
import com.renren.profile.biz.dao.UserPerfDAO;
import com.renren.profile.biz.dao.UserPerfProjectDAO;
import com.renren.profile.biz.model.Department;
import com.renren.profile.biz.model.PeerPerf;
import com.renren.profile.biz.model.PeerPerfProject;
import com.renren.profile.biz.model.PerfTime;
import com.renren.profile.biz.model.User;
import com.renren.profile.biz.model.UserInvitation;
import com.renren.profile.biz.model.UserPerf;
import com.renren.profile.biz.model.UserPerfProject;
import com.renren.profile.util.Collections0;
import com.renren.profile.util.logging.ILogger;
import com.renren.profile.util.logging.ProfileLogger;
import com.renren.profile.util.sys.status.ReportStatus;
import com.renren.profile.util.time.TimeUtils;
import com.renren.profile.web.dto.UserAccessInfoDTO;
import com.renren.profile.web.dto.UserInviteInfoDTO;

/**
 * 
 * 项目名称：renren-profile 类名称：UserPerfService
 * 
 * 类描述： 用户邀请信息和被邀请信息
 * 
 * 创建人：wen.he1 创建时间：2012-3-12 上午11:12:13
 * 
 * @version 1.0
 */

@Service
public class UserPerfService implements RenrenProfileConstants {

    @Autowired
    PerfTimeDAO        perfTimeDAO;

    @Autowired
    UserInvitationDAO  userInvitationDAO;

    @Autowired
    UserService        userService;

    @Autowired
    DepartmentService  departmentService;

    @Autowired
    UserPerfDAO        userPerfDAO;

    @Autowired
    PeerPerfDAO        peerPerfDAO;

    @Autowired
    UserPerfProjectDAO userPerfProjectDAO;

    @Autowired
    PeerPerfProjectDAO peerPerfProjectDAO;

    private ILogger    LOG = ProfileLogger.getLogger(this.getClass());

    /***
     * 根据条件：人名或者邮件名称模糊查询用户信息
     * 
     * @param matchStr
     * @return
     */
    public List<User> getMatchPatternInfo(String matchStr) {

        List<User> users = userService.queryByCondition(UserDAO.NAME + " like '%" + matchStr + "%' " + " OR " + UserDAO.EMAIL + " like '%" + matchStr + "%' ",
                0,
                10000);

        return users;
    }

    /**
     * 通过用户Id找到用户考核的季度时间
     * 
     * @param userId
     * @return
     */
    public PerfTime getUserCurrentPerf() {

        return perfTimeDAO.queryNewestPerfItem();

    }

    /**
     * 得到当前季度考核向我所有汇报人的信息
     * 
     * @param userId
     * @param perfTimeId
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<UserAccessInfoDTO> getMyReportUserInfo(int userId , int perfTimeId){

        List<User> userList = userService.queryAllMyFollow(userId);
        if (!Collections0.isEmpty(userList)) {

			List<UserAccessInfoDTO> userDTOList   = convertDTOFrom(userList);
            Collection<Integer> myreportUserIds = Collections2.transform(userList, new Function<User, Integer>() {

                @Override
                public Integer apply(User item) {
                    return item == null ? null : item.getId();
                }
            });

            // 查询得到我的下属自己自评已经提交的集合用户selfPerfSumbitUserIds以及用户没有提交自评或者
			// 还未写自评信息用户selfPerfNoSumbIds;
            Collection<UserPerf> userPerfList = userPerfDAO.batchQueryByUserIdAndPerfTimeId(perfTimeId, myreportUserIds);

            Collection<Integer> selfPerfSumbitUserIds = Collections2.transform(userPerfList, new Function<UserPerf, Integer>() {

                @Override
                public Integer apply(UserPerf item) {
                    return item == null ? null : item.getUserId();
                }
            });
            Collection<Integer> selfPerfNoSumbIds = CollectionUtils.subtract(myreportUserIds, selfPerfSumbitUserIds);

            // 更新下属没有写自评的DTO状态
            updateDTOStatus(userDTOList, selfPerfNoSumbIds, ReportStatus.ON_START.getName());

            // 得到所有自评提交id的集合，并且查询出领导对评价人自评信息的评价
            Collection<Integer> selfPerfSumbitIds = Collections2.transform(userPerfList, new Function<UserPerf, Integer>() {

                @Override
                public Integer apply(UserPerf item) {
                    return item == null ? null : item.getId();
                }
            });
            Collection<PeerPerf> accessPerfList = peerPerfDAO.batchQueryPeerPerfInfo(userId, perfTimeId, 1, selfPerfSumbitIds);

            // accessSelfPerfIds对下属进行了评价（保存或者提交）；noAccessPerfIds主管没有对下属的评价
            Collection<Integer> accessSelfPerfIds = Collections2.transform(accessPerfList, new Function<PeerPerf, Integer>() {

                @Override
                public Integer apply(PeerPerf item) {
                    return item == null ? null : item.getUserPerfId();
                }
            });

            // 更新主管对下属已经评价的状态,主管的对员工评价是否提交
            for (UserPerf userPerf : userPerfList)
                for (PeerPerf peerPerf : accessPerfList)
                    if (peerPerf.getUserPerfId() == userPerf.getId()) {

						for(UserAccessInfoDTO dto : userDTOList)
							if(dto.getUser().getId() == userPerf.getUserId()){

                                dto.setStatus(peerPerf.getStatus() == 0 ? ReportStatus.EDIT.getName() : ReportStatus.VIEW.getName());
                                break;

                            }

                        break;
                    }

            // 更新了主管没有对下属进行评价的状态
            Collection<Integer> noAccessPerfIds = CollectionUtils.subtract(selfPerfSumbitIds, accessSelfPerfIds);
            for (UserPerf userPerf : userPerfList)
                for (int id : noAccessPerfIds) {
                    if (userPerf.getId() == id) {
						for(UserAccessInfoDTO dto : userDTOList)
							if(dto.getUser().getId() == userPerf.getUserId()){
                                dto.setStatus(ReportStatus.START.getName());
                                break;
                            }

                        break;
                    }
                }

            return userDTOList;

        }

        return null;

    }

    /**
     * 对DTO的状态进行更新
     * 
     * @param userDTOList
     * @param ids
     * @param status
     */
	private void updateDTOStatus(List<UserAccessInfoDTO> userDTOList,Collection<Integer> ids,int status) {

		for(UserAccessInfoDTO dto : userDTOList)
            for (int id : ids) {

				if(dto.getUser().getId() == id){
                    dto.setStatus(status);
                    break;
                }

            }
    }

	private List<UserAccessInfoDTO> convertDTOFrom(List<User> userList) {

		List<UserAccessInfoDTO> userDTOList = new ArrayList<UserAccessInfoDTO>();
        for (User user : userList) {

			UserAccessInfoDTO userDTO = new UserAccessInfoDTO();
            Department deparment = departmentService.queryById(user.getDepartmentId());

			userDTO.setUser(user);
            userDTO.setDeparmentName(deparment == null ? "" : deparment.getDepartmentName());

			
            userDTOList.add(userDTO);

        }

        return userDTOList;
    }

    /**
     * 添加用户邀请的表信息，如果返回0表示邀请失败，否则邀请成功
     * 
     * @param from_id
     * @param invite_id
     * @return
     */
    public Integer addUserInvitation(int from_id, int invite_id) {

        UserInvitation userInvite = new UserInvitation();
        userInvite.setFromId(from_id);
        userInvite.setInviteId(invite_id);
        userInvite.setStatus(1);
        userInvite.setEditTime(TimeUtils.FetchTime.now());

        PerfTime perfTime = perfTimeDAO.queryNewestPerfItem();
        if (perfTime == null)
            return 0;

        userInvite.setPerfTimeId(perfTime.getId());

        return userInvitationDAO.save(userInvite);

    }
    
    public Integer countUserInvitedFriends(int fromId , int inviteId){
    	
    	 PerfTime perfTime = perfTimeDAO.queryNewestPerfItem();
         if (perfTime == null) {
             return null;
         }
         
         return userInvitationDAO.countInviteFriendsNumber(fromId, inviteId, perfTime.getId());
    	
    }

    /**
     * 按邀请和被邀请的ID，查看当前Q的邀请记录
     * 
     * @return {@link UserInvitation}
     * @author hao.zhang
     */
    public UserInvitation testIsUserInvitedForPeerPerf(int userId, int peerId) {
        PerfTime perfTime = perfTimeDAO.queryNewestPerfItem();
        if (perfTime == null) {
            return null;
        }
        UserInvitation invitation = userInvitationDAO.queryInvitationItem(
                userId, peerId, perfTime.getId());
        if (invitation == null) {
            return null;
        }
        return invitation;
    }

    /**
     * 根据当前用户ID和邀请人的Id已经当前绩效考核的Id，删除邀请表对应邀请人的信息
     * 
     * @param currentUserId
     * @param inviteId
     * @return
     */
    public Integer delUserInvitation(int currentUserId, int inviteId) {

        PerfTime perfTime = perfTimeDAO.queryNewestPerfItem();
        if (perfTime == null)
            return 0;

        return userInvitationDAO.deleteUserInviteInfo(currentUserId, inviteId, perfTime.getId());
    }

    /**
     * 根据email或者名字条件查询用户信息
     * 
     * @param email
     * @return
     */
    public User getUserInfo(String keywords) {

        return userService.queryByEmail(keywords);
    }

    /**
     * 根据用户Id，查询得到用户被邀请的信息
     * 
     * @param userId
     * @param perfId
     * @return
     */
    public List<UserInviteInfoDTO> getUserInvitationInfo(int userId, int perfId) {

        return getUserInfoDTOList(userInvitationDAO.queryInvitationInfo(userId, perfId), false, perfId);

    }

    /**
     * 根据用户id和当前考核id查询用户自评是否提交
     * 
     * @param userId
     * @param perfId
     * @return
     */
    public boolean isSelfAccessSumbit(int userId, int perfId) {

        int status = 0;
        try {

            status = userPerfDAO.queryPeerPerfStatus(userId, perfId);

        } catch (Exception e) {

            LOG.info("UserPerfService->isSelfAccessSumbit->userPerfDAO.queryPeerPerfStatus查询信息出错", e);
            return false;

        }

        return status == 0 ? false : true;

    }

    /**
     * 
     * @param invList
     * @param isAccess
     *            是否是给别人评价的信息，false表示不是，true表示是
     * @return
     */
    private List<UserInviteInfoDTO> getUserInfoDTOList(List<UserInvitation> invList, boolean isAccess, int perfId) {

        if (Collections0.isEmpty(invList))
            return null;

        List<Integer> inviUserIdList = convertListContainInteger(invList, isAccess);
        List<User> userList = userService.queryByUserIds(inviUserIdList);

        return convertElementsOfList(invList, userList);
    }

    /**
     * 根据用户id查询用户要评价的信息
     * 
     * @param userId
     * @return
     */
    public List<UserInviteInfoDTO> getAccessUserInfo(int userId, int perfId) {

        return getUserInfoDTOList(userInvitationDAO.queryAccessInfo(userId, perfId), true, perfId);

    }

    /**
	 * 将invList和userList元素合并成UserInviteInfoDTO元素，返回List<UserInviteInfoDTO>
     * 
     * @param invList
     * @param userList
     * @return
     */
    private List<UserInviteInfoDTO> convertElementsOfList(List<UserInvitation> invList, List<User> userList) {

        List<UserInviteInfoDTO> dtoList = new ArrayList<UserInviteInfoDTO>();
        for (UserInvitation inv : invList)
            for (User user : userList) {
                if (user.getId() == inv.getInviteId() || user.getId() == inv.getFromId()) {

					UserInviteInfoDTO dto = new UserInviteInfoDTO();
                    dto.setUser(user);
                    dto.setStatus(inv.getStatus());

                    dtoList.add(dto);

                    break;
                }

            }

        return dtoList;
    }

    /**
     * 将List元素中的邀请人Id转换成List，元素是userId
     * 
     * @param invList
     * @param isAccess
     * @return
     */
    private List<Integer> convertListContainInteger(List<UserInvitation> invList, boolean isAccess) {

        List<Integer> userIdList = new ArrayList<Integer>();
        for (UserInvitation inv : invList) {

            if (isAccess)
                userIdList.add(inv.getFromId());
            else
                userIdList.add(inv.getInviteId());

        }

        return userIdList;

    }

    /**
     * 返回用户的绩效记录，偏移量
     * 
     * @param userId
     * @param offset
     * @param count
     * @return
     */
    public List<UserPerf> getUserSelfPerfList(int userId, int offset, int count) {
        return userPerfDAO.queryAllByUser(userId, offset, count);
    }

    public List<UserPerf> getUserSelfPerfListByPerfTimes(int userId, Collection<Integer> perfTimeIds) {
        return userPerfDAO.queryAllByPerfTimes(userId,perfTimeIds);
    }

    /**
     * 返回用户对应某一个季度的自评记录，填充项目自评
     * 
     * @param userId
     * @param perfTimeId
     * @return
     * @author hao.zhang
     */
    public UserPerf getUserSelfPerf(int userId, int perfTimeId) {

        UserPerf userSelfPerf = userPerfDAO.queryByUserIdAndPerfTimeID(userId, perfTimeId);
        if (userSelfPerf == null) {
            return null;
        }
        wrapPerfProjects(userSelfPerf);
        return userSelfPerf;
    }

    /**
     * 返回对应用户自评的非上级互评
     * 
     * 因为项目回评和评价人是多对多关系，这里使用项目自评ID和项目互评对应
     * 
     * @param userSelfPerfId
     * @return
     * @author hao.zhang
     */
    public List<PeerPerf> getPeerPerfListByUserSelfPerfId(int userSelfPerfId) {

        // 查询互评列表
        return peerPerfDAO.queryNoManagerByUserPerfId(userSelfPerfId);
    }

    /**
     * 对应用户自评的上级评价
     * 
     * @param userSelfPerfId
     * @return
     */
    public PeerPerf getManagerPerfByUserSelfPerfId(int userSelfPerfId) {
        PeerPerf managerPerf = peerPerfDAO.queryManagerPerfByUserPerfId(userSelfPerfId);
        if (managerPerf == null) {
            return null;
        }
        wrapPeerPerfProjects(managerPerf);
        return managerPerf;
    }

    /**
     * 包装UserPerfProject 到Collection <> UserPerfs中
     * 
     * @param userPerfs
     */
    public void wrapPerfProjects(Collection<? extends UserPerf> userPerfs) {
        if (CollectionUtils.isEmpty(userPerfs)) {
            return;
        }
        for (UserPerf userPerf : userPerfs) {
            wrapPerfProjects(userPerf);
        }
    }

    /**
     * 包装UserPerfProject 到UserPerf中
     * 
     * @param userPerf
     */
    public void wrapPerfProjects(UserPerf userPerf) {
        if (userPerf == null) {
            return;
        }
        // 填充项目评价
        List<UserPerfProject> projects = userPerfProjectDAO.queryByUserIdAndPerfTime(userPerf.getUserId(), userPerf.getPerfTimeId());
        userPerf.projects(projects);
    }

    /**
     * 包装peerPerfProjects 到Collection<PeerPerf> PeerPerfs中
     * 
     * @param peerPerfs
     */
    public void wrapPeerPerfProjects(Collection<? extends PeerPerf> peerPerfs) {
        if (CollectionUtils.isEmpty(peerPerfs)) {
            return;
        }
        for (PeerPerf peerPerf : peerPerfs) {
            wrapPeerPerfProjects(peerPerf);
        }
    }

    /**
     * 包装peerPerfProjects到PeerPerf中
     * 
     * @param peerPerf
     */
    public void wrapPeerPerfProjects(PeerPerf peerPerf) {
        if (peerPerf == null) {
            return;
        }
        List<PeerPerfProject> projects = peerPerfProjectDAO.queryListByPeerPerfId(peerPerf.getId());
        peerPerf.projects(projects);
    }

    /**
     * 包装peerPerfProjects 和 managerPerfProjects 到UserPerfProject中
     * 
     * 绩效自评互评汇总页
     * 
     * @param userPerfProject
     */
    public void wrapPeerPerfProjects(UserPerfProject userPerfProject) {
        if (userPerfProject == null) {
            return;
        }
        // 填充项目评价
        List<PeerPerfProject> projects = peerPerfProjectDAO.queryListByPerfProjectId(userPerfProject.getId());
        if (CollectionUtils.isEmpty(projects)) {
            return;
        }
        List<PeerPerfProject> peerPerfProjects = new ArrayList<PeerPerfProject>(projects.size());
        PeerPerfProject managerPerfProject = null;
        for (PeerPerfProject project : projects) {
            if (project.getIsManager()) {
                managerPerfProject = project;
            } else {
                peerPerfProjects.add(project);
            }
        }

        userPerfProject.peerPerfProjects(peerPerfProjects);
        userPerfProject.managerPerfProject(managerPerfProject);
    }

    /**
     * 查找用户的项目ID列表
     * 
     * @param userSelfPerfId
     * @return
     * @author hao.zhang
     */
    @SuppressWarnings("unused")
	private Collection<Integer> getUserProjectPerfIds(int userSelfPerfId) {

        UserPerf userPerf = userPerfDAO.query(userSelfPerfId);
        List<UserPerfProject> userProjects = userPerfProjectDAO.queryByUserIdAndPerfTime(userPerf.getUserId(), userPerf.getPerfTimeId());

        Collection<Integer> userPerfProjectIds = Collections2.transform(userProjects, new Function<UserPerfProject, Integer>() {

            @Override
            public Integer apply(UserPerfProject item) {
                return item == null ? null : item.getId();
            }
        });
        return userPerfProjectIds;
    }

    /**
     * 更新邀请状态
     * 
     * @param invitationId
     * @author hao.zhang
     */
    public void updateInvitationStatus(int invitationId, ReportStatus status) {
        UserInvitation invitation = userInvitationDAO.query(invitationId);
        if (invitation == null) {
            return;
        }
        invitation.setStatus(status.getName());
        try {
            userInvitationDAO.update(invitation);
        } catch (Exception e) {
            LOG.error(e, e.getMessage());
        }

    }

}
