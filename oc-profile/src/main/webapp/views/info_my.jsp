<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="nav_menu" value="info_my"></c:set>

<!DOCTYPE html>
<html>
<head>
	<%@include file="/inc/head.jsp" %>
	<script src="/static/js/jquery.jeditable.js" type="text/javascript"></script>
	<title>个人资料 - ${_user.name} - ${_siteName }</title>
	<script type="text/javascript">
    	var showLevel  = ${user.showLevel != 0 || false};
 		function bottom_hidden() {
			 $('#bottom_id').css('display','none');
		}
 		/**
 		function toggleShowSchool(showSchool){
 			if(showSchool==0){
				PU.ajaxPost('/userSelf/showSchool',{
					success:function(){
						$('#is_show_school').text('未公开');
						$('#toggle_is_show_school_btn').text('公开');
					}
				});
			}else{
				PU.ajaxPost('/userSelf/hideSchool',{
					success:function(){
						$('#is_show_school').text('已公开');
						$('#toggle_is_show_school_btn').text('不公开');
					}
				});
			}
 		}
 		**/
		function toggleShowLevel(){
			if(showLevel){
				PU.ajaxPost('/userSelf/hidelevel',{
					success:function(){
						showLevel = false;
						$('#tip_show_level').text('未公开');
						$('#toggle_show_level_btn').text('公开');
					}
				});
			}else{
				PU.ajaxPost('/userSelf/showlevel',{
					success:function(){
						showLevel = true;
						$('#tip_show_level').text('已公开');
						$('#toggle_show_level_btn').text('不公开');
					}
				});
			}
		}
		
		jQuery(function($){
			// editable
			$('.editable').each(function(){
				var self = $(this);
				var editId = self.attr('edit-id') || "unkown";
				var editName = self.attr('edit-name') || "value";
				var editSaveUrl = self.attr('edit-save-url') || "/userSelf/edit";
                var editInputType = self.attr('edit-type') || "text";
                var editHeight = self.attr('edit-height') || 'auto';
                var tipName = '点击此处编辑';
                if (editId.indexOf('birthday') == 0){
                	 tipName = "填写格式:月-日";
                }
				self.editable(editSaveUrl,{
					name:editName,
					submitdata:{name:editId,__ajax:'json'},
					callback:function(result){
						if(result.code === 0){
							$(self).html((result.data || {}).value);
						}else{
                            $(self).html("");
							PU.alertDialog('编辑失败:'+((result.errors||{})[""] ||[] ).join(","));
						}
					},
					ajaxoptions:{
						dataType:'json'
					},
				//onblur:'ignore',
					submit:'保存',
					cancel:'取消',
					cssclass:'edit-info-form',
					placeholder: '<span class="empty-content">'+tipName+'</span>',
                    type:editInputType,
                    height:editHeight
				});
			});

			$('.notify').each(function(){
				var self = $(this);
				var editId = self.attr('edit-id') || "unkown";
				var editName = self.attr('edit-name') || "value";
				var editSaveUrl = self.attr('edit-save-url') || "/userSelf/edit";
                var editInputType = self.attr('edit-type') || "text";
                var editHeight = self.attr('edit-height') || 'auto';
                $(self).html("");
				self.editable(editSaveUrl,{
					name:editName,
					submitdata:{name:editId,__ajax:'json'},
					callback:function(result){
						if(result.code === 0){
							$(self).html("提交成功!");
						}else{
                            $(self).html("");
							PU.alertDialog('提交失败:'+((result.errors||{})[""] ||[] ).join(","));
						}
					},
					ajaxoptions:{
						dataType:'json'
					},
				//onblur:'ignore',
					submit:'提交',
					cancel:'取消',
					cssclass:'edit-info-form',
					placeholder: '<span class="empty-content"><a href="javascript:;"> 输入此人邮箱通知我们。</a></span>',
                    type:editInputType,
                    height:editHeight
				});
			});

		});
	</script>
	<style>
		form.edit-info-form{
			display: inline;
		}
		
		form.edit-info-form input{
			border: 1px solid #84ACC6;
			height: 20px;
			line-height: 20px;
			width: 290px;
		}
		
		form.edit-summbit input{
			border: 1px solid #84ACC6;
			height: 20px;
			line-height: 20px;
			width: 200px;
		}
		
		form.edit-info-form button{
			border: none;
			background:none;
			margin-left: 5px;
			padding: 0 3px;
		}
		
		form.edit-info-form button[type=submit]{
			background: #015EAC;
			color: white;
			font-size: 14px;
		}
		
		form.edit-info-form button[type=cancel]{
		/**	border-bottom: dotted 1px blue;
			font-size: 12px;**/
			background: #015EAC;
			color: white;
			font-size: 14px;
		}
	</style>
</head>
<body>
	<%@include file="/inc/header.jsp" %>
	
	<div id="main" class="reset-list-css">
		<%@include file="/inc/left-nav.jsp" %>
		<div id="content">
			<div class="content-title">
					<h2>${user.name}</h2>
			</div>
			<div class="messages-box">
				<%@include file="/inc/messages.jsp" %>
			</div>
			<div class="user-info-box content-box">
				<div class="user-headpic">
					<p><img alt="${user.name}" width="200px" src="${user.mainUrl()}"></p>
					<a class="change-headpic button btn_b" href="javascript:void(0);">
						<span>
							修改头像
						</span>
						<form id="photo-form" action="/photos/upload" method="post" enctype="multipart/form-data">
							<input type="file" name="photo" value="修改头像" onchange="javascript:if(jQuery(this).val()){jQuery('#photo-form').submit();}" >
						</form>
					</a>
				</div>
				<div class="user-overview">
					<table>
						<tbody>
							<tr>
								<th>邮&nbsp &nbsp &nbsp &nbsp箱:</th>
								<td><font color="#888888">${user.email}</font></td>
							</tr>
							<tr>
								<th>部&nbsp &nbsp &nbsp &nbsp门:</th>
								<td><font color="#888888">${department.departmentName}</font></td>
							</tr>
							<tr>
								<th>岗&nbsp &nbsp &nbsp &nbsp位:</th>
								<td>
									<font color="#888888">${user.jobTitle}</font>
								</td>
							</tr>
							<tr>
								<th>工&nbsp &nbsp &nbsp &nbsp号:</th>
								<td>
									<font color="#888888">${user.number}</font>
								</td>
							</tr>
							<tr>
								<th>业务名称:</th>
								<td class="editable" edit-id="businesses">${businessTagNames}</td>
							</tr>
							<tr>
								<th>级&nbsp &nbsp &nbsp &nbsp别:</th>
								<td>
									<profile:default def="<i>未评级</i>">${user.level}</profile:default>
									(<span id="tip_show_level">${user.showLevel == 0?'未公开':'已公开' }</span>)
									<a id="toggle_show_level_btn" class="button" href="javascript:toggleShowLevel();">${user.showLevel == 0? '显示':'隐藏'}</a>
								</td>
							</tr>
							<tr>
								<th>经&nbsp &nbsp &nbsp &nbsp理:</th>
								<td>
									<a href="/info/${managerUser.id}">${managerUser.name} </a>
								</td>
							</tr>
							<tr>
								<th>毕业学校:</th>
								<td class="editable" edit-id="graduateSchool">${userProfile.graduateSchool != '' ? userProfile.graduateSchool : ''}</td>
							</tr>
							<tr>
								<th>生&nbsp &nbsp &nbsp &nbsp日:</th>
								<td class="editable" edit-id="birthday">${userProfile.birthday}</td>
							</tr>
							<tr>
								<th>手机号码:</th>
								<td class="editable" edit-id="mobile">${userProfile.mobile}</td>
							</tr>
							<tr>
								<th>分机号码:</th>
								<td class="editable" edit-id="extNumber">${userProfile.extNumber > 0 ? userProfile.extNumber : ''}</td>
							</tr>
							<tr>
								<th>QQ&nbsp&nbsp号码:</th>
								<td class="editable" edit-id="qq">${userProfile.qq > 0 ? userProfile.qq : ''}</td>
							</tr>
							<tr>
								<th>人人主页:</th>
								<td class="editable" edit-id="renrenLink">${userProfile.renrenLink != '' ? userProfile.renrenLink : ''}</td>
							</tr>
							<tr>
								<th>兴趣爱好:</th>
								<td class="editable" edit-id="hobby" edit-type="textarea" edit-height="100">${userProfile.hobby}</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="fc"></div>
				<div class="user-detail">
				</div>
			</div>
			
			<c:if test="${not empty myFollows }">
				<div class="sub-title">
					向我汇报的人
				</div>
				
				
				<div class="content-box">
					<div class="message-info">
						<p>只有下属登陆后，信息才能得到更新。<p>
						<br><span style=color:red>注意：您下属离职或转岗，请您点击此处:<span class="notify" edit-id="userName" style="cursor: pointer"></span></span>
					</div>
					<table class="user-full-table">
						<tbody>
							<c:forEach var="item" items="${myFollows}" varStatus="status">
								<tr>
								<td style="width: 22px;">
									<c:if test="${item.testManager}">
									      <a href="/info/${item.user.id }">
											<img src="/static/images/add.png" >
										  </a>
									</c:if>
								</td>
									
									<td class="user-headpic">
										<a href="/info/${item.user.id }">
											<img width="50px" src="${item.user.tinyUrl()}">
										</a>
									</td>
									<td>
										<p>
											<a href="/info/${item.user.id }">
												${item.user.name}
											</a>
										</p>
										<p>
												<c:if test="${item.user.userProfile.qq > 0}">
													<a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=${item.user.userProfile.qq }&site=qq&menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=2:${item.user.userProfile.qq }:44" alt="点击这里给我发消息" title="点击这里给我发消息"></a>
												</c:if>
										</p>
										<p>
											${item.user.email}
										</p>
									</td>
									<td>
										${item.user.jobTitle}
									</td>
									<td>
										<c:if test="${item.weeklyReportStatus == c_weeklyreport_status$ready}">
											周报未写
										</c:if>
										<c:if test="${item.weeklyReportStatus == c_weeklyreport_status$saved }">
											周报已保存
										</c:if>
										<c:if test="${item.weeklyReportStatus == c_weeklyreport_status$submitted }">
											<a href="/weeklyreport/${item.user.id }">
												周报已提交
											</a>
										</c:if>
									</td>
									
									<td>
										<c:if test="${item.userPerfStatus == c_self_perf_status$ready }">
											自评未开始
										</c:if>
										<c:if test="${item.userPerfStatus == c_self_perf_status$saved }">
											自评已保存
										</c:if>
										<c:if test="${item.userPerfStatus == c_self_perf_status$submitted }">
											自评已提交
										</c:if>
									</td>
									
									
									<td>
										<c:if test="${item.kpiStatus == c_kpi_status$ready }">
											KPI未开始
										</c:if>
										<c:if test="${item.kpiStatus == c_kpi_status$saved }">
											<a href="../kpi/history/view/${item.user.id}/0">KPI已保存</a>
										</c:if>
										<c:if test="${item.kpiStatus == c_kpi_status$submitted }">
											<a href="../kpi/history/view/${item.user.id}/0">KPI已提交</a>
										</c:if>
									</td>
									
									<!--  
									<td>
										${item.deparmentName}
									</td>
									-->
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</c:if>
		</div>
		<%@include file="/inc/right-nav.jsp" %>
		<div class="fc"></div>
	</div>
	<c:if test="${not empty is_notice && is_notice==1 }">
		<div id='bottom_id' class="guide guide-qq"  style="display: block; bottom: 0px;">
	                <div class="guide_boby">
	                        <p>
	                                &nbsp &nbsp &nbsp &nbsp  &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp who系统通告！<br>
	                                   1、个人页面填写自己的生日，技术部相关负责人会为亲的生日制作一份精美的礼物！<br>
	                                   2、个人svn提交记录，以周为单位进行统计</a>
	                        </p>
	                        <div id="J_CloseGuide" class="del">
	                                <a href="javascript:bottom_hidden();">关闭引导</a>
	                        </div>
	                </div>
	     </div>
     </c:if>
	<%@include file="/inc/footer.jsp" %>
</body>
</html>
