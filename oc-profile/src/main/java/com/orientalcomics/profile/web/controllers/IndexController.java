package com.orientalcomics.profile.web.controllers;


import java.util.Map;

import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Get;

import org.springframework.beans.factory.annotation.Autowired;

import com.orientalcomics.profile.biz.logic.StatisticsService;
import com.orientalcomics.profile.biz.logic.UserService;
import com.orientalcomics.profile.web.controllers.internal.BaseController;

@Path( { "", "index" })
public class IndexController extends BaseController {

	@Autowired
	StatisticsService statisticsService;

	@Autowired
	UserService userService;

	@Get("")
	public String index() {
		return "f:/info/my";
	}

	@Get("export")
	public String export() throws Exception {

		Map<String,String[]>  noWeeklyUser    = statisticsService.getNoSummbitedWeeklyUsers();
		Map<String,String[]>  noUploadUser    = statisticsService.getNoUploadImageUsers();
		Map<String, String[]> mapUser         = statisticsService.compareUserInfoFromExcelToDatabase();

		statisticsService.genExcelData(noWeeklyUser,"no-weekly.xls");
		statisticsService.genExcelData(noUploadUser,"no-image.xls");
		statisticsService.genExcelData(mapUser,"no-login.xls");

		return "f:/info/my";
	}
	
	
	@Get("perfScore/{id:\\d+}")
	public String updatePerfScore(@Param("id") int perfTimeId) throws Exception {
		statisticsService.insertPerfData(statisticsService.getPerfScoreExcelData("/data/profile-data/excel/Q1.xls",6),perfTimeId);
		return "f:/info/my";
	}
	
}
