package com.renren.profile.biz.logic;


import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.renren.profile.biz.dao.InfiniteSkDAO;
import com.renren.profile.biz.model.access.InfiniteSk;



@Service
public class InfiniteSkHome {

	private static ApplicationContext applicationContext;
	
    private static InfiniteSkHome instance ;
    
    @Autowired
    private InfiniteSkDAO  infiniteDAO ;
    
	@Autowired
	public void setApplicationContext(ApplicationContext ac) {
		InfiniteSkHome.applicationContext = ac;
		}

		public static InfiniteSkHome getInstance() {
			if (instance == null) {
				instance =  (InfiniteSkHome) BeanFactoryUtils.beanOfType(applicationContext, InfiniteSkHome.class);
			}
			return instance;
		}
	
	public InfiniteSk get(long userId, int appId) {
		InfiniteSk infiniteSk = null;
		
         infiniteSk = infiniteDAO.get(userId, appId);
         return infiniteSk;
	}
	
	public void generateKey(InfiniteSk inSK) {
	    infiniteDAO.generateKeys(inSK);
	}
	
	public void delete(int appId,long userId){
			infiniteDAO.delete(userId, appId);
	}
}
