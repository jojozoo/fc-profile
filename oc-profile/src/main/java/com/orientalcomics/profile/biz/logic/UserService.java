package com.orientalcomics.profile.biz.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.orientalcomics.profile.biz.dao.UserDAO;
import com.orientalcomics.profile.biz.dao.UserProfileDAO;
import com.orientalcomics.profile.biz.model.Department;
import com.orientalcomics.profile.biz.model.PerfReponse;
import com.orientalcomics.profile.biz.model.User;
import com.orientalcomics.profile.util.common.JsonUtils;
import com.orientalcomics.profile.util.common.Strings0;
import com.orientalcomics.profile.web.dto.UserExtendInfoDTO;

@Service
public class UserService {
	
    @Autowired
    UserDAO            userDAO;

//    @Autowired
//    IndexService       indexService;

    @Autowired
    DepartmentService  departmentService;

    @Autowired
    StatusService      statusService;
    
    @Autowired
    KpiService          KpiService;
    
    @Autowired
    UserProfileDAO      userProfileDAO;
    

    public List<User> queryValidUsers(int offset, int count) {
        return userDAO.queryValidUsers(offset, count);
    }
    
    public void deleteUserBy(String email) {
    	userDAO.updateUserStatus(email);
    }
    
    /**
     * 查询全部主管的信息
     * 
     * @return
     */
    public List<User> queryAllManager() {
        return userDAO.queryAllManager();
    }

    public List<User> queryAllWithIndexableFields() {
        return userDAO.queryAllWithIndexableFields();
    }

    public List<User> queryAllList(Collection<Integer> ids) {
        return userDAO.queryAllList(ids);
    }

    public List<User> queryByDepartment(int departmentId) {
        return userDAO.queryByDepartment(departmentId);
    }

    public Map<Integer, User> queryAllMap(Collection<Integer> ids) {
        return userDAO.queryAllMap(ids);
    }

    public int countSubordinates(int userId) {
        return userDAO.countSubordinates(userId);
    }
    
    public List<User> getSubordinatesIds(int userId){
    	return userDAO.getSubordinatesId(userId);
    }

    public int countAll() {
        return userDAO.countAll();
    }


    public int countValidUsers() {
        return userDAO.countValidUsers();
    }
    
    public List<User> getValidUsers(int start , int count) {
    	return userDAO.queryValidUsers(start,count);
    }


    public int countAllInDepartment(int departmentId) {
        return userDAO.countAllInDepartment(departmentId);
    }
    
    public int countAllInDepartmentRecursive(Collection<Integer> departments) {
        return userDAO.countAllInDepartmentRecursive(departments);
    }

    public User queryByEmail(String email) {
        return userDAO.queryByEmail(email);
    }

    public List<User> queryByUserIds(List<Integer> userIdList) {
        return userDAO.queryByUserIds(userIdList);
    }

    public List<User> queryAllInDepartment(int departmentId, int offset, int count) {
        return userDAO.queryAllInDepartment(departmentId, offset, count);
    }
    
    public List<User> queryAllInDepartmentRecursive(Collection<Integer> departments, int offset, int count) {
	  List<User> listUser =  userDAO.queryAllInDepartmentRecursive(departments);
	  List<User> retUser  =  new ArrayList<User>();
	  
	  for (User user : listUser) {
		  int countSub = userDAO.countSubordinates(user.getId());
          user.subordinateCount(countSub);
          if (countSub > 0) {
        	  retUser.add(user);
          }
      }
	  
	  for (User user : listUser) {
          if (user.subordinateCount() == 0) {
        	  retUser.add(user);
          }
      }
	  
	  int splitCount = (offset + count) < listUser.size() ? count : (listUser.size() - offset);
	  
	  return retUser.subList(offset, offset+splitCount);
    }

    public User query(int id) {
        return userDAO.query(id);
    }
    
    public User queryByNumber(String number) {
        return userDAO.queryByNumber(number);
    }

    public Map<Integer, User> query(Collection<Integer> ids) {
        return userDAO.query(ids);
    }

    public List<User> queryAllMyFollow(int id) {
        return userDAO.queryAllMyFollow(id);
    }
    
    public List<User> queryAllMyFollowExcludeKpiOpen(int id,int kpiOpen) {
        return userDAO.queryAllMyFollowExcludeKpiOpen(id,kpiOpen);
    }

    public Integer queryLeader(int userId) {
        return userDAO.queryLeader(userId);
    }

    public Integer queryDepartment(int userId) {
        return userDAO.queryDepartment(userId);
    }

    public List<User> queryByCondition(String condition, int offset, int count) {
        return userDAO.queryByCondition(condition, offset, count);
    }

    public int update(User model) {
        int result = userDAO.update(model);
//        try {
//            indexService.indexUserId(model.getId());
//        } catch (IndexException e) {
//            e.printStackTrace();
//        }
        return result;
    }

    public void setPhotos(int userId, String tinyUrl, String mainUrl) {
        userDAO.updateUserPhotos(userId, tinyUrl, mainUrl);
    }

    public int updateUserShowLevel(int is_show, int userId) {
        return userDAO.updateUserShowLevel(is_show, userId);
    }
    
    public int updateUserSchool(int is_show, int userId) {
        return userProfileDAO.updateUserSchool(is_show, userId);
    }

    public Integer save(User model) {
        Integer result = userDAO.save(model);
//        try {
//            indexService.indexUserId(result);
//        } catch (IndexException e) {
//            e.printStackTrace();
//        }
        return result;
    }
    
    public Integer updateLevel(String leavel,int userId) {
        return userDAO.updateUserLevel(leavel,userId);
    }

    public void delete(int id) {
        userDAO.delete(id);
//        try {
//            indexService.deleteUserId(id);
//        } catch (IndexException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 获取所有用户的id集合
     * 
     * @return
     */
    public Collection<Integer> getUserIds() {
        return userDAO.queryAllIds();
    }

    public JSONObject toJSON(User user) {
        if (user == null) {
            return null;
        }
        return JsonUtils.builder()//
                .put("id", user.getId())//
                .put("name", user.getName())//
                .put("email", user.getEmail())//
                .put("tinyurl", user.tinyUrl())//
                .put("job", user.getJobTitle())//
                .put("department", user.department() == null ? null : user.department().getDepartmentName())
                .build();
    }

    @SuppressWarnings("unchecked")
	public List<JSONObject> toJSON(List<User> users) {
        if (CollectionUtils.isEmpty(users)) {
            return new JSONArray();
        }
        departmentService.wrap(users);
        List<JSONObject> jsonUsers = Lists.transform(users, new Function<User, JSONObject>() {

            @Override
            public JSONObject apply(User user) {
                return toJSON(user);
            }
        });
        return jsonUsers;
    }

    /**
     * 得到我的下属信息,bean要进行转换
     * 
     * @param rlist
     * @return
     */
    public List<UserExtendInfoDTO> getMyFollows(List<User> rlist) {

        List<UserExtendInfoDTO> baseUserInfos = new ArrayList<UserExtendInfoDTO>();

        for (User user : rlist) {
        	user.setUserProfile(userProfileDAO.query(user.getId()));
            UserExtendInfoDTO baseUserInfo = new UserExtendInfoDTO();
            user.setJobTitle(Strings0.subString(user.getJobTitle()));
            baseUserInfo.setUser(user);

            // 是否本人有下属
            int reportNum = userDAO.queryMyFollwerNumber(user.getId());
            if (reportNum > 0)
                baseUserInfo.setTestManager(true);

            Department deparment = departmentService.queryById(user.getDepartmentId());
            baseUserInfo.setDeparmentName(deparment != null ? deparment.getDepartmentName() : null);

            baseUserInfos.add(baseUserInfo);

        }

        return baseUserInfos;
    }

    /**
     * 得到我的下属信息,并且包含kpi、周报、自评的状态
     * 
     * @param rlist
     * @return
     */
    public List<UserExtendInfoDTO> getMyFollowsAndContainsStatus(List<User> rlist) {

        List<UserExtendInfoDTO> baseUserInfos = new ArrayList<UserExtendInfoDTO>();

        for (User user : rlist) {
        	user.setUserProfile(userProfileDAO.query(user.getId()));
        	
            UserExtendInfoDTO baseUserInfo = new UserExtendInfoDTO();
            user.setJobTitle(Strings0.subString(user.getJobTitle()));
            baseUserInfo.setUser(user);

            // 是否本人有下属
            int reportNum = userDAO.queryMyFollwerNumber(user.getId());
            if (reportNum > 0)
                baseUserInfo.setTestManager(true);

            Department deparment = departmentService.queryById(user.getDepartmentId());
            baseUserInfo.setDeparmentName(deparment != null ? deparment.getDepartmentName() : null);

            // 三个状态设置
            baseUserInfo.setKpiStatus(KpiService.getUserKpiStatus(user.getId()));
            baseUserInfo.setUserPerfStatus(statusService.getUserPerfStatus(user.getId()));
            baseUserInfo.setWeeklyReportStatus(statusService.getUserWeeklyReportStatus(user.getId()));
            
            baseUserInfos.add(baseUserInfo);

        }

        return baseUserInfos;
    }
    
    public List<UserExtendInfoDTO> getUserInfoBy(List<PerfReponse> rlist) {

        List<UserExtendInfoDTO> baseUserInfos = new ArrayList<UserExtendInfoDTO>(rlist.size());

        for (PerfReponse response : rlist) {

            UserExtendInfoDTO baseUserInfo = new UserExtendInfoDTO();
            User user = userDAO.query(response.getUserId());
            if (user == null)
            	continue;
            
            baseUserInfo.setUser(user);

            Department deparment = departmentService.queryById(user.getDepartmentId());
            baseUserInfo.setDeparmentName(deparment != null ? deparment.getDepartmentName() : null);
            baseUserInfo.setPerfResponse(response);
            baseUserInfos.add(baseUserInfo);
        }

        return baseUserInfos;
    }
    
    public List<UserExtendInfoDTO> getUserInfoByUsers(List<User> rlist) {

        List<UserExtendInfoDTO> baseUserInfos = new ArrayList<UserExtendInfoDTO>(rlist.size());

        for (User user : rlist) {

            UserExtendInfoDTO baseUserInfo = new UserExtendInfoDTO();
            baseUserInfo.setUser(user);

            Department deparment = departmentService.queryById(user.getDepartmentId());
            baseUserInfo.setDeparmentName(deparment != null ? deparment.getDepartmentName() : null);
            
            baseUserInfos.add(baseUserInfo);
        }

        return baseUserInfos;
    }

    public List<User> querySubordinates(int id, int offset, int count) {
        List<User> result = userDAO.querySubordinates(id, offset, count);
        if (result == null) {
            return Collections.emptyList();
        }
        return result;
    }

    public void wrapSubordinateCount(Collection<User> users) {
        if (CollectionUtils.isNotEmpty(users)) {
            for (User user : users) {
                user.subordinateCount(userDAO.countSubordinates(user.getId()));
            }
        }
    }
    
    public static void main(String[] args) {
    	User user1 = new User();
    	user1.subordinateCount(1);
    	user1.setEmail("hewe@126.com");
    	user1.setName("name1");
    	User user2 = new User();
    	user2.subordinateCount(1);
    	user2.setEmail("hewe@126.com");
    	user2.setName("name2");

    	User user3 = new User();
    	user3.setEmail("hewe@126.com");
    	user3.setName("name3");
    	User user4 = new User();
    	user4.setEmail("hewe@126.com");
    	user4.setName("name4");

    	List<User> lists = new ArrayList<User>();
    	lists.add(user4);
    	lists.add(user3);
    	lists.add(user1);
    	lists.add(user2);
    	
    	System.out.println(lists.subList(4, 5).size());
    	
//    	List<User> list = new ArrayList<User>();

//    	for (User user : lists) {
//    		if (user.subordinateCount() > 0) {
//    			list.add(user);
//    		}
//    	}
//    	
//    	for (User user : lists) {
//    		if (user.subordinateCount() == 0) {
//    			list.add(user);
//    		}
//    	}
//    	
//    	for (User user : list) {
//    		System.out.println(user.getName());
//    	}

    }
    
}
