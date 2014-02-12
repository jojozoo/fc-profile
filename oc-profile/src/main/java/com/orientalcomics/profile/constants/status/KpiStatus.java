package com.orientalcomics.profile.constants.status;

import java.util.HashMap;
import java.util.Map;

import com.orientalcomics.profile.biz.base.Displayable;
import com.orientalcomics.profile.biz.base.Idable;


/**
 * @author 张浩 E-mail:hao.zhang@renren-inc.com
 * @date 2012-3-23 下午06:55:24
 *
 * KpiStatus 
 */

public enum KpiStatus implements Idable,Displayable{
	
		READY(0, "未开始KPI"), //
	    SAVED(1, "KPI已保存"), //
	    SUBMITTED(2, "KPI已提交"), //
	    ;
	    private final int    id;
	    private final String display;

	    KpiStatus(int id, String display) {
	        this.id = id;
	        this.display = display;
	    }

	    public int getId() {
	        return id;
	    }

	    public String getDisplay() {
	        return display;
	    }

	    private static final Map<Integer, KpiStatus> MAP = new HashMap<Integer, KpiStatus>();

	    static {
	        for (KpiStatus item : values()) {
	            if (MAP.put(item.getId(), item) != null) {
	                throw new IllegalStateException("PerfPromotionStatus|id conflict|" + item.getId());
	            }
	        }
	    }

	    public static KpiStatus findById(int id) {
	        return MAP.get(id);
	    }
}
