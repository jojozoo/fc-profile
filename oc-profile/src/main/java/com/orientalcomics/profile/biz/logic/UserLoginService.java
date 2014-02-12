package com.orientalcomics.profile.biz.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.biz.dao.UserProfileDAO;
import com.orientalcomics.profile.biz.model.User;

@Service
public class UserLoginService implements OcProfileConstants {

    @Autowired
    private UserService       userService;

    @Autowired
    private UserProfileDAO    userProfileDAO;


    public User saveOAUserInfo() {
    	
//    	updateDeparmentInfo.updateUserInfo(loginInfo);
//    
//    	updateUserInfo.updateUserInfo(loginInfo);
//    	
//    	updateLevelUserInfo.updateUserInfo(loginInfo);
//    	
//        HumresBean hum   = loginInfo.getHumres();
//        if (hum != null) {
//        	
//        	User user  = userService.query(hum.getId());
//        	if(user == null){
//	        	// 保存用户信息
//	        	user = new User();
//	            user.setOaId(hum.getId());
//	            user.setName(hum.getObjname());
//	            user.setEmail(hum.getEmail());
//	            user.setStatus(hum.getHrstatus() != null ? 0 : 1);
//	            user.setTinyUrl("");
//	            user.setMainUrl("");
//	            user.setJobTitle(hum.getMainstationname());
//	            user.setNumber(hum.getExttextfield11());
//	            Integer userId = userService.save(user);
//	            if (userId == null || userId == 0) {
//	                return null;
//	            }
//        	
//            
//	            // 保存用户详细信息
//	            UserProfile userProfile = new UserProfile();
//	            userProfile.setMobile(hum.getTel2());
//	            userProfile.setUserId(userId);
//	            userProfile.setIsDisplay(0);
//	            userProfileDAO.save(userProfile);
//        	}
//            
//            return user;
//        }
        return null;
    }

}
