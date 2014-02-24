package com.orientalcomics.profile.biz.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.biz.dao.UserDAO;
import com.orientalcomics.profile.biz.dao.UserPerfDAO;
import com.orientalcomics.profile.biz.model.User;
import com.orientalcomics.profile.biz.model.UserPerf;
import com.orientalcomics.profile.constants.status.UserPerfStatus;
import com.orientalcomics.profile.util.common.Collections0;
import com.orientalcomics.profile.util.excel.ReadExcelUtil;
import com.orientalcomics.profile.util.logging.ILogger;
import com.orientalcomics.profile.util.logging.ProfileLogger;


@Service
public class StatisticsService {

	@Autowired
	NotifyService notifyService;

	@Autowired
	DepartmentService departmentService;

	@Autowired
	UserDAO userDAO;

	@Autowired
	UserService userService;
	
	@Autowired
	UserPerfDAO userPerfDAO;
	
	private Map<String, String[]> userMap = new HashMap<String, String[]>(420);

	private final ILogger LOG = ProfileLogger.getLogger(UserService.class);

	public Map<String, String[]> getUserExcelMap() {

		if (userMap.size() == 0) {
			return getExcelUserInfo();
		} else {
			return userMap;
		}

	}

	// 得出数据库中没有的用户
	public Map<String, String[]> compareUserInfoFromExcelToDatabase() {

		userMap = getExcelUserInfo();

		List<User> userList1 = userService.getValidUsers(0, 300);
		for (User user : userList1) {
			if (userMap.containsKey(user.getNumber()))
				userMap.remove(user.getNumber());
		}

		List<User> userList2 = userService.getValidUsers(300, 300);
		if (Collections0.isNotEmpty(userList2)) {
			for (User user : userList2) {
				if (userMap.containsKey(user.getNumber()))
					userMap.remove(user.getNumber());
			}
		}

		return userMap;

	}

	private Map<String, String[]> getExcelUserInfo() {
		try {

			userMap = ReadExcelUtil.readALLUserExcel(OcProfileConstants.EXCEL_DATA_DIR_USER);

		} catch (BiffException e) {
			LOG.error(e, "读取全部员工用户信息Excel格式不对！");
		} catch (IOException e) {
			LOG.error(e, "读取全部员工用户信息Excel出错！");
		}
		return userMap;
	}

	/**
	 * 得到当前周没有写周报的人
	 * 
	 * @return
	 */
	public Map<String, String[]> getNoSummbitedWeeklyUsers() {

		List<User> userList = notifyService.getNoWeeklyReprUsersForLastWeek();
		if (Collections0.isEmpty(userList)) {
			return null;
		}

		Map<String, String[]> userExcel = getExcelUserInfo();
		Map<String, String[]> noWeekUser = new HashMap<String, String[]>(200);
		for (User user : userList) {
			if (StringUtils.isNotEmpty(user.getNumber()) && userExcel.containsKey(user.getNumber())) {
				noWeekUser.put(user.getNumber(), userExcel.get(user.getNumber()));
			}
		}

		return noWeekUser;
	}
	
	/**
	 * 得到当前周没有写周报的人
	 * 
	 * @return
	 */
	public List<User> getNoSummbitedWeeklyUserList() {

		List<User> userList = notifyService.getNoWeeklyReprUsersForLastWeek();
		if (Collections0.isEmpty(userList)) {
			return null;
		}

		Map<String, String[]> userExcel   = getExcelUserInfo();
		List<User>            noWeekUser  = new ArrayList<User>(30);
		for (User user : userList) {
			if (StringUtils.isNotEmpty(user.getNumber()) && userExcel.containsKey(user.getNumber())) {
				noWeekUser.add(user);
			}
		}

		return noWeekUser;
	}

	public Map<String, String[]> getNoUploadImageUsers() {

		List<User> userList = userDAO.queryNOUploadImage("");

		Map<String, String[]> userExcel = getExcelUserInfo();
		Map<String, String[]> noImageUser = new HashMap<String, String[]>(200);
		for (User user : userList) {
			if (StringUtils.isNotEmpty(user.getNumber()) && userExcel.containsKey(user.getNumber())) {
				noImageUser.put(user.getNumber(), userExcel.get(user.getNumber()));
			}
		}

		return noImageUser;
	}
	
	/**
	 * 
	 * @param columns  员工编号，等级，绩效成绩
	 * @param filePath 文件存放的路径
	 * @return
	 */
	public  Map<String, String> getPerfScoreExcelData(String filePath,int column) {
		Map<String,String> map = new HashMap<String,String>(320);
		try {
		    //创建一个list 用来存储读取的内容
		    Workbook rwb = null;
		    Cell cell = null;
		    //创建输入流
		    InputStream stream = new FileInputStream(filePath);
		   
		    //获取Excel文件对象
		    rwb = Workbook.getWorkbook(stream);
		   
		    //获取文件的指定工作表 默认的第一个
		    Sheet sheet = rwb.getSheet(0); 
		  
		    //行数(表头的目录不需要，从1开始)
		    for(int i=1; i<sheet.getRows(); i++){
		    
		     //创建一个数组 用来存储每一列的值
		     String[] str = new String[2];
		     int t = 0 ;
		     //列数
		     for(int j=0; j<sheet.getColumns(); j++){
		    
			      //获取第i行，第j列的值
			      cell = sheet.getCell(j,i);
			      String columnContent = StringUtils.trim(cell.getContents());
			      if (StringUtils.isNotEmpty(columnContent) && (j == 0 || j == column)) {
			    	  if (t < 2 && !StringUtils.endsWithIgnoreCase(columnContent, "#N/A")) {
			    		  str[t] = cell.getContents();
			    		  t++;
			    	  }
			      }
		     
		     }
		     
		     //把刚获取的列存入list
		     if(str.length != 0 && str[0] != null)
		    	 map.put(str[0], str[1]);
		    }
		   
		    //返回值集合
		    return map;
		    
		   
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	public  void genExcelData(Map<String, String[]> map, String fileName) throws Exception {

		File tempFile = new File(OcProfileConstants.DIR + fileName);
		WritableWorkbook workbook = Workbook.createWorkbook(tempFile);
		WritableSheet sheet = workbook.createSheet("TestCreateExcel", 0);

		Label l = null;
		WritableFont titleFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		WritableCellFormat titleFormat = new WritableCellFormat(titleFont);

		// add Title
		int column = 0;
		l = new Label(column++, 0, "员工编号", titleFormat);
		sheet.addCell(l);
		l = new Label(column++, 0, "二级部门", titleFormat);
		sheet.addCell(l);
		l = new Label(column++, 0, "三级部门", titleFormat);
		sheet.addCell(l);
		l = new Label(column++, 0, "四级部门", titleFormat);
		sheet.addCell(l);
		l = new Label(column++, 0, "姓名", titleFormat);
		sheet.addCell(l);
		l = new Label(column++, 0, "工作性质", titleFormat);
		sheet.addCell(l);
		l = new Label(column++, 0, "直接主管", titleFormat);
		sheet.addCell(l);
		int i = 0;
		Set<Entry<String, String[]>> set = map.entrySet();
		for (Entry<String, String[]> entry : set) {
			i++;
			column = 0;
			l = new Label(column++, i, entry.getValue()[0], titleFormat);
			sheet.addCell(l);
			l = new Label(column++, i, entry.getValue()[1], titleFormat);
			sheet.addCell(l);
			l = new Label(column++, i, entry.getValue()[2], titleFormat);
			sheet.addCell(l);
			l = new Label(column++, i, entry.getValue()[3], titleFormat);
			sheet.addCell(l);
			l = new Label(column++, i, entry.getValue()[4], titleFormat);
			sheet.addCell(l);
			l = new Label(column++, i, entry.getValue()[5], titleFormat);
			sheet.addCell(l);
			l = new Label(column++, i, entry.getValue()[6], titleFormat);
			sheet.addCell(l);
		}

		// 设置列的宽度
		column = 0;
		sheet.setColumnView(column++, 20);
		sheet.setColumnView(column++, 20);
		sheet.setColumnView(column++, 20);
		sheet.setColumnView(column++, 20);
		sheet.setColumnView(column++, 20);
		sheet.setColumnView(column++, 20);
		sheet.setColumnView(column++, 20);

		workbook.write();
		workbook.close();

	}
	
	/**
	 * 要替换的maps，将它所有的key：number替换成userId
	 * @param maps
	 * @return
	 */
	public Map<Integer,String> replaceKeyOfMap(Map<String,String> maps) {
		if (maps == null || maps.size() == 0) {
			return null;
		}
		Map<Integer,String> replaceKeyMaps = new HashMap<Integer,String>(300);
		for (String number : maps.keySet()) {
			User user = userService.queryByNumber(number);
			if (user != null) {
				replaceKeyMaps.put(user.getId(), maps.get(number));
			}
		}
		
		return replaceKeyMaps;
	}
	
	
	public void insertPerfData(Map<String,String> maps,int perfTimeId) {
		if (maps == null || maps.size() == 0) {
			return;
		}
		for (Entry<String,String> value : maps.entrySet()) {
			User user = userService.queryByNumber(value.getKey());
			if (user != null) {
				  UserPerf userPerf = new UserPerf();
                  userPerf.setUserId(user.getId());
                  userPerf.setPerfTimeId(perfTimeId);
                  userPerf.setUserName(user.getName());
                  userPerf.setAdvantage("");
                  userPerf.setDisadvantage("");
                  userPerf.setEditTime(new Timestamp(new Date().getTime()));
                  userPerf.status(UserPerfStatus.SUBMITTED);
                  userPerf.setPerfScore(value.getValue());
                  userPerf.setIsPromotion(false);
                  
                  userPerfDAO.save(userPerf);
			}
			
		}
	}
	
	public void updatePerfScore(Map<Integer,String> maps,int perfTimeId) {
		if (maps == null || maps.size() == 0) {
			return;
		}
		
		for (Entry<Integer, String> entry : maps.entrySet()) {
			userPerfDAO.updateByPerfScore(entry.getValue(), entry.getKey(),perfTimeId);
		}
	}
	
	
	public static Map<String,String[]> readSVNFile(String strFile1 , String chompStr) {

		Map<String,String[]> map = new HashMap<String,String[]>(320);
		try {
		    //创建一个list 用来存储读取的内容
		    Workbook rwb = null;
		    Cell cell = null;
		    //创建输入流
		    InputStream stream = new FileInputStream(strFile1);
		   
		    //获取Excel文件对象
		    rwb = Workbook.getWorkbook(stream);
		   
		    //获取文件的指定工作表 默认的第一个
		    Sheet sheet = rwb.getSheet(0); 
		  
		    //行数(表头的目录不需要，从1开始)
		    for(int i=1; i<sheet.getRows(); i++){
		    
		     //创建一个数组 用来存储每一列的值
		     String[] str = new String[sheet.getColumns()];
		     //列数
		     for(int j=0; j< sheet.getColumns(); j++){
		    
			      //获取第i行，第j列的值
			      cell = sheet.getCell(j,i);
			      String columnContent = StringUtils.trim(cell.getContents());
			      str[j] = (j == 0 ? StringUtils.chomp(columnContent,chompStr) : columnContent);
		     
		     }
		     
			     if(str.length != 0 && str[0] != null) {
			    	 if (map.containsKey(str[0])) {
			    		 String[] temp = map.get(str[0]);
			    		 temp[0] = String.valueOf(Integer.valueOf(temp[0]) + Integer.valueOf(str[1]));
			    		 temp[1] = String.valueOf(Integer.valueOf(temp[1]) + Integer.valueOf(str[2]));
			    		 map.put(str[0],temp);
			    	 }else {
			    		 String[] insertStr = {str[1],str[2]};
			    		 map.put(str[0], insertStr);
			    	 }
			     }
		    }
		   
		    //返回值集合
		    return map;
		    
		   
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static Map<String,String> readUserFile(String strFile1 , String chompStr) {

		Map<String,String> map = new HashMap<String,String>(320);
		try {
		    //创建一个list 用来存储读取的内容
		    Workbook rwb = null;
		    Cell cell = null;
		    //创建输入流
		    InputStream stream = new FileInputStream(strFile1);
		   
		    //获取Excel文件对象
		    rwb = Workbook.getWorkbook(stream);
		   
		    //获取文件的指定工作表 默认的第一个
		    Sheet sheet = rwb.getSheet(0); 
		  
		    //行数(表头的目录不需要，从1开始)
		    for(int i=1; i<sheet.getRows(); i++){
		    
		     //创建一个数组 用来存储每一列的值
		     String[] str = new String[sheet.getColumns()];
		     //列数
		     for(int j=0; j< sheet.getColumns(); j++){
		    
			      //获取第i行，第j列的值
			      cell = sheet.getCell(j,i);
			      String columnContent = StringUtils.trim(cell.getContents());
			      if (j == 0 && StringUtils.indexOf(columnContent, "?") != -1) {
			    	  str[j] = StringUtils.chomp(columnContent,chompStr+"?");
			      }else {
			    	  str[j] = (j == 0 ? StringUtils.chomp(columnContent,chompStr) : columnContent);
			      }
		     
		     }
		     
			     if(str.length != 0 && str[0] != null) {
			    	 map.put(str[0], str[1]);
			     }
		    }
		   
		    //返回值集合
		    return map;
		    
		   
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static Map<String,String> compareFromSVNToUser() {
		Map<String,String[]> svnMaps  = readSVNFile("D:/svn.xls","@XIAONEI.OPI.COM");
		Map<String,String>   userMaps = readUserFile("D:/user.xls","@renren-inc.com");
		
		Map<String,String> compareMaps = new HashMap<String,String>();
		for (Entry<String,String> userEntry : userMaps.entrySet()) {
			if (!svnMaps.containsKey(userEntry.getKey())) {
				compareMaps.put(userEntry.getKey()+"@renren-inc.com", userEntry.getValue());
			}
		}
		
		return compareMaps;
	}
	
	public static Map<String,String[]> compareToUser() {
		Map<String,String[]> svnMaps  = readSVNFile("D:/svn.xls","@XIAONEI.OPI.COM");
		Map<String,String>   userMaps = readUserFile("D:/user.xls","@renren-inc.com");
		
		Map<String,String[]> compareMaps = new HashMap<String,String[]>();
		for (Entry<String,String> userEntry : userMaps.entrySet()) {
			if (svnMaps.containsKey(userEntry.getKey())) {
				compareMaps.put(userEntry.getKey()+"@renren-inc.com", svnMaps.get(userEntry.getKey()));
			}
		}
		
		return compareMaps;
	}
	
	public  static void genExcelSummbitSvnData(Map<String, String[]> map, String fileName) throws Exception {

		File tempFile = new File("D:/" + fileName);
		WritableWorkbook workbook = Workbook.createWorkbook(tempFile);
		WritableSheet sheet = workbook.createSheet("TestCreateExcel", 0);

		Label l = null;
		WritableFont titleFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		WritableCellFormat titleFormat = new WritableCellFormat(titleFont);

		// add Title
		int column = 0;
		l = new Label(column++, 0, "邮箱", titleFormat);
		sheet.addCell(l);
		l = new Label(column++, 0, "提交次数", titleFormat);
		sheet.addCell(l);
		l = new Label(column++, 0, "注释字节数", titleFormat);
		sheet.addCell(l);
		int i = 0;
		Set<Entry<String, String[]>> set = map.entrySet();
		for (Entry<String, String[]> entry : set) {
			i++;
			column = 0;
			l = new Label(column++, i, entry.getKey(), titleFormat);
			sheet.addCell(l);
			l = new Label(column++, i, entry.getValue()[0], titleFormat);
			sheet.addCell(l);
			l = new Label(column++, i, entry.getValue()[1], titleFormat);
			sheet.addCell(l);
		}

		// 设置列的宽度
		column = 0;
		sheet.setColumnView(column++, 20);
		sheet.setColumnView(column++, 20);
		sheet.setColumnView(column++, 20);

		workbook.write();
		workbook.close();

	}
	
	public  static void genExcelNosummbitSvnData(Map<String, String> map, String fileName) throws Exception {

		File tempFile = new File("D:/" + fileName);
		WritableWorkbook workbook = Workbook.createWorkbook(tempFile);
		WritableSheet sheet = workbook.createSheet("TestCreateExcel", 0);
	
		Label l = null;
		WritableFont titleFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		WritableCellFormat titleFormat = new WritableCellFormat(titleFont);
		
		// add Title
		int column = 0;
		l = new Label(column++, 0, "邮箱", titleFormat);
		sheet.addCell(l);
		l = new Label(column++, 0, "部门", titleFormat);
		sheet.addCell(l);
		int i = 0;
		Set<Entry<String, String>> set = map.entrySet();
		for (Entry<String, String> entry : set) {
			i++;
			column = 0;
			l = new Label(column++, i, entry.getKey(), titleFormat);
			sheet.addCell(l);
			l = new Label(column++, i, entry.getValue(), titleFormat);
			sheet.addCell(l);
		}

		// 设置列的宽度
		column = 0;
		sheet.setColumnView(column++, 20);
		sheet.setColumnView(column++, 20);

		workbook.write();
		workbook.close();

	}
	
	

	public static void main(String[] args) throws Exception {
//		Map<String, String[]> map = new HashMap<String,String[]>();
//		map.put("213232", new String[]{"232","erer","efdew","wef","fewf","wefw","few"});
//		map.put("21323d2", new String[]{"232w","ewrer","efdew","wef","fewf","wefw","few"});
//		System.out.println(StringUtils.chomp("wen.he1@renren-inc.com", "@renren-inc.com"));
//		Map<String,String[]> maps = StatisticsService.readSVNFile("D:/svn.xls","@XIAONEI.OPI.COM");
//		for (Entry<String,String[]> entry : maps.entrySet()) {
//			System.out.println("姓名="+entry.getKey());
//		}
//		System.out.print("注释字节数");
		//System.out.print("                                                                  提交次数                   " + "   注释字节数");
//		System.out.println();
//		Map<String,String[]> maps = StatisticsService.compareToUser();
//		for (Entry<String,String[]> entry : maps.entrySet()) {
//			System.out.println(entry.getValue()[1]);
//		}
		
//		System.out.println(maps.size());
		
//		System.out.println(StatisticsService.readUserFile("D:/user.xls","@renren-inc.com").size());
//		StatisticsService.genExcelNosummbitSvnData(StatisticsService.compareFromSVNToUser(),"advertise_no_sumbit_svn.xls");
		StatisticsService s = new StatisticsService();
		System.out.println(s.getPerfScoreExcelData("D:/Q1.xls",6));
		
	}
}
