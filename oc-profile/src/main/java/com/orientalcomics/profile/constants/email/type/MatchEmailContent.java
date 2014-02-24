package com.orientalcomics.profile.constants.email.type;

import java.util.ArrayList;
import java.util.List;


public enum MatchEmailContent {
	
    MATCH_XX(1, "xx"), //
    MATCH_YY(2, "yy"), //
    MATCH_ZZ(3, "zz"), //
    MATCH_NN(4, "nn"), //
    ;
    
    private final String  name;
    private final int     id  ;

	MatchEmailContent(int id,String name) {
		this.id   = id;
        this.name = name;
    }
	
	public String getName() {
	    return name;
	}
	
	public int getId(){
		return id ;
	}
	 
    private static final List<String> matchList = new ArrayList<String>();

    public static List<String> getMatchList() {
		return matchList;
	}

	static {
        for (MatchEmailContent item : values())
            matchList.add(item.name);
    }
    
    public static void main(String[] args){
    	System.out.println(MatchEmailContent.matchList);
    }
}
