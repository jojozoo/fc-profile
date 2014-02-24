package com.orientalcomics.profile.constants.email.type;
/**
 * 
 * 项目名称：renren-profile  
 * 类名称：PlaceHoderStatus  
 * 类描述：发送邮件模板替换的类型  
 * 创建人：Administrator  
 * 创建时间：2012-3-29 下午06:07:27  
 * 
 * @version
 */
public enum PlaceHolderType {
	
	XX_PLACE(1, "xx"), 							
    
    YY_PLACE(2, "yy"), 						
    
    ZZ_PLACE(3, "zz"), 				
    
    NN_PLACE(4, "nn"),    				
    
    MM_PLACE(5, "mm"),  
    
    TT_PLACE(5, "tt"), 
    
    ;
    
    private final int    id;
    private final String name;
    
    PlaceHolderType(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

}
