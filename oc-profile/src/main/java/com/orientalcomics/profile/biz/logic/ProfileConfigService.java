package com.orientalcomics.profile.biz.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orientalcomics.profile.biz.dao.ProfileConfigDAO;
import com.orientalcomics.profile.biz.model.ProfileConfig;
import com.orientalcomics.profile.util.common.Collections0;

@Service
public class ProfileConfigService {
	
	@Autowired
    ProfileConfigDAO    profileConfigDAO;
	
	@SuppressWarnings("unchecked")
	public Map<String,List<ProfileConfig>> getNavData() {
		
		List<ProfileConfig> configs = profileConfigDAO.queryNav();
		if (Collections0.isEmpty(configs)) {
			return Collections.EMPTY_MAP;
		}
		
		Map<String,List<ProfileConfig>> retMap = new HashMap<String,List<ProfileConfig>>(30);
		for (ProfileConfig config : configs) {
			String name = config.getName();
			if (retMap.containsKey(name)) {
				List tempList = retMap.get(name);
				tempList.add(config);
				retMap.put(name, tempList);
			} else {
				List<ProfileConfig> tempList = new ArrayList<ProfileConfig>(5);
				tempList.add(config);
				retMap.put(name, tempList);
			}
		}
		
		return retMap;
	}

}
