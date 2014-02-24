package com.orientalcomics.profile.util.excel;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;


import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/***
 * 
 * <p> 项目名称：renren-profile
 *
 * <p> 类名称：ReadExcelUtil     
 *
 * <p> 类描述：  获取excel的数据
 *
 * <p> 创建人：wen.he1@renren-inc.com
 *
 * <p> 创建时间：2012-5-16 下午02:50:16  
 * 
 * <p> @version    1.0
 */
public class ReadExcelUtil {
	
	/**
	 * 
	 * @param excelFileName Excel文件路径和名称
	 * @return
	 * @throws BiffException
	 * @throws IOException
	 */
	public static Map<String,String> readExcel(String excelFileName) throws BiffException, IOException{
		   
	    //创建一个list 用来存储读取的内容
		Map<String,String> map = new HashMap<String,String>(300);
	    Workbook rwb = null;
	    Cell cell = null;
	   
	    //创建输入流
	    InputStream stream = new FileInputStream(excelFileName);
	   
	    //获取Excel文件对象
	    rwb = Workbook.getWorkbook(stream);
	   
	    //获取文件的指定工作表 默认的第一个
	    Sheet sheet = rwb.getSheet(0); 
	  
	    //行数(表头的目录不需要，从1开始)
	    for(int i=1; i<sheet.getRows(); i++){
	    
	     //创建一个数组 用来存储每一列的值
	     String[] str = new String[sheet.getColumns()];
	    
	     //列数
	     for(int j=0; j<sheet.getColumns(); j++){
	    
	      //获取第i行，第j列的值
	      cell = sheet.getCell(j,i);
	      String columnContent = cell.getContents();
	      if(StringUtils.isNotEmpty(columnContent))
	          str[j] = StringUtils.trim(cell.getContents());
	     
	     }
	     
	     //把刚获取的列存入list
	     if(str.length != 0 && str[0] != null)
	    	 map.put(str[0], str[1]);
	    }
	   
	    //返回值集合
	    return map;
	   }
	
	
	/**
	 * 
	 * @param excelFileName Excel文件路径和名称
	 * @return
	 * @throws BiffException
	 * @throws IOException
	 */
	public static Map<String,String[]> readALLUserExcel(String excelFileName) throws BiffException, IOException{
		   
	    //创建一个list 用来存储读取的内容
		Map<String,String[]> map = new HashMap<String,String[]>(420);
	    Workbook rwb = null;
	    Cell cell = null;
	   
	    //创建输入流
	    InputStream stream = new FileInputStream(excelFileName);
	   
	    //获取Excel文件对象
	    rwb = Workbook.getWorkbook(stream);
	   
	    //获取文件的指定工作表 默认的第一个
	    Sheet sheet = rwb.getSheet(0); 
	  
	    //行数(表头的目录不需要，从1开始)
	    for(int i=1; i<sheet.getRows(); i++){
	    
	     //创建一个数组 用来存储每一列的值
	     String[] str = new String[sheet.getColumns()];
	    
	     //列数
	     for(int j=0; j<sheet.getColumns(); j++){
	    
		      //获取第i行，第j列的值
		      cell = sheet.getCell(j,i);
		      String columnContent = cell.getContents();
		      if (j == 0 && StringUtils.isNotEmpty(columnContent)) {
		          str[j] = StringUtils.trim(cell.getContents());
		      }else {
		    	  str[j] = StringUtils.trim(cell.getContents());
		      }
	     
	     }
	     
	     //把刚获取的列存入list
	     if(str.length != 0 && str[0] != null)
	    	 map.put(str[0], str);
	    }
	   
	    //返回值集合
	    return map;
	    
	   }
	

	 public static void main(String[] args) {
	  try {
		  Map<String,String[]> data = readALLUserExcel("D:/all-user.xls");
		  System.out.println(data.size());
		  Set<Entry<String,String[]>> set = data.entrySet();
		  for(Entry<String,String[]> entry : set)
			 System.out.println(entry.getKey()+"--"+entry.getValue());
		  
//		  for(String[] strs : data)
//			  System.out.println(strs[0]+"--"+strs[1]);
	  } catch (BiffException e) {
	   // TODO Auto-generated catch block
	   e.printStackTrace();
	  } catch (IOException e) {
	   // TODO Auto-generated catch block
	   e.printStackTrace();
	  }
	}
}
