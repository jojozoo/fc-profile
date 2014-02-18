package com.renren.profile.biz.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renren.profile.RenrenProfileConstants;
import com.renren.profile.biz.dao.ShadowDAO;
import com.renren.profile.biz.dao.UserProfileDAO;
import com.renren.profile.biz.model.Shadow;
import com.renren.profile.biz.model.User;

@Service
public class UserLoginService implements RenrenProfileConstants {

    @Autowired
    private UserService        userService;

    @Autowired
    private DepartmentService  departmentService;

    @Autowired
    private UserProfileDAO userProfileDAO;
    
    
    @Autowired
    private ShadowDAO userShadowDAO;

    /**
     * 登录验证
     * @param name
     * @param passwd
     * @return
     */
    public User loginUser(String name,String passwd) {

        if (name != null && passwd != null) {
        	Shadow shadow = userShadowDAO.query(name, passwd);
        	if(shadow != null){
        		User user = userService.query(shadow.getUserId());
        		return user;
        	}
        }
        return null;
    }
}
