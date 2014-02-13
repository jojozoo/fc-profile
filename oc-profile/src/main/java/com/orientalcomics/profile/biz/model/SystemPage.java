package com.orientalcomics.profile.biz.model;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

public class SystemPage implements Comparator<Object>{

	private int id = 0;
	
	private String systemKey   = StringUtils.EMPTY;
	
	private String systemValue = StringUtils.EMPTY;
	
	private String descript = StringUtils.EMPTY;

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public String getSystemKey() {
		return systemKey;
	}

	public void setSystemKey(String systemKey) {
		this.systemKey = systemKey;
	}

	public String getSystemValue() {
		return systemValue;
	}

	public void setSystemValue(String systemValue) {
		this.systemValue = systemValue;
	}

	// 对应key/value中标记即说明用途
	private int mark = 0;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		SystemPage s1 = (SystemPage)o1;
		SystemPage s2 = (SystemPage)o2;
		if(Integer.parseInt(s1.getSystemValue()) > Integer.parseInt(s2.getSystemValue())){
			return 1;
		}else{
			return -1;
		}
	}
}
