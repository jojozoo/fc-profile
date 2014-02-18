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
	<title>个人信息 - ${_user.name} - ${_siteName }</title>
	<script type="text/javascript">
	
		function modifyHeadPic(){
				var tem = $("#headPic_id").val();
				if(!tem){
					alert("图像url不能空!");
					return;
				}
					
				PU.confirmDialog('你确定修改图像地址吗？','提示',function(){
					$('#modify_headpic_form').attr('action','/userSelf/modifyHeadPic').submit();
			    });
		
		}
		
		function modifyBusName(){
				var tem = $("#busName_id").val();
				if(!tem){
					alert("业务名为空!");
					return;
				}
					
				PU.confirmDialog('你确定修改业务的名称吗？','提示',function(){
					$('#modify_busName_form').attr('action','/userSelf/modifyTag').submit();
			    });
		
		}
		
		function modifyTitle(){
				
				PU.confirmDialog('你确定隐藏职称吗？','提示',function(){
					$('#modify_hiddenTitle_form').attr('action','/userSelf/isShow').submit();
			    });
		
		}
		
		function modifyQQ(){
				var tem = $("#qq_id").val();
				if(!tem){
					alert("QQ为空!");
					return;
				}
					
				PU.confirmDialog('你确定修改QQ吗？','提示',function(){
					$('#modify_qq_form').attr('action','/userSelf/modifyQQ').submit();
			    });
		
		}
		
		function modifyHobby(){
				var tem = $("#hobby_id").val();
				if(!tem){
					alert("业余爱好为空!");
					return;
				}
					
				PU.confirmDialog('你确定修改业余爱好吗？','提示',function(){
					$('#modify_hobby_form').attr('action','/userSelf/modifyHobby').submit();
			    });
		}
		
		var showLevel = ${user.showLevel != 0 || false};
		function toggleShowLevel(){
			if(showLevel){
				PU.ajaxPost('/userSelf/hidelevel',{
					success:function(){
						showLevel = false;
						$('#tip_show_level').text('已隐藏');
						$('#toggle_show_level_btn').text('显示');
					}
				});
			}else{
				PU.ajaxPost('/userSelf/showlevel',{
					success:function(){
						showLevel = true;
						$('#tip_show_level').text('未隐藏');
						$('#toggle_show_level_btn').text('隐藏');
					}
				});
			}
		}
		
		jQuery(function($){
			// editable
			$('.editable').each(function(){
				var self = $(this);
				var editId = self.attr('edit-id') || "id";
				var editName = self.attr('edit-name') || "value";
				var editSaveUrl = self.attr('edit-save-url') || "";
				self.editable(editSaveUrl,{
					id:editId,
					name:editName,
					submitdata:{__ajax:'html'},
				//onblur:'ignore',
					submit:'保存',
					cancel:'取消',
					cssclass:'edit-info-form',
					placeholder: '<span class="empty-content">点击此处编辑</span>'
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
		
		form.edit-info-form button{
			border: none;
			background:none;
			margin-left: 5px;
			padding: 0 3px;
		}
		
		form.edit-info-form button[type=submit]{
			background: blue;
			color: white;
			font-size: 14px;
		}
		
		form.edit-info-form button[type=cancel]{
			border-bottom: dotted 1px blue;
			font-size: 12px;
		}
	</style>
</head>
<body>
	<%@include file="/inc/header.jsp" %>
	
	<div id="main">
		<%@include file="/inc/left-nav.jsp" %>
		<div id="content">
			<div class="content-title">
					<h2>${user.name}</h2>
			</div>
			<div class="user-info-box content-box">
				<div class="user-headpic">
					<img alt="${user.name}" width="200" src="${user.headPic()}">
				</div>
				<div class="user-overview">
					<table>
						<tbody>
							<tr>
								<th>邮箱:</th>
								<td>${user.email}</td>
							</tr>
							<tr>
								<th>部门:</th>
								<td>${department.departmentName}</td>
							</tr>
							<tr>
								<th>头像地址:</th>
								<td class="editable" edit-name="headUrl" edit-save-url="/userSelf/modifyHeadPic">${user.headPic}</td>
							</tr>
							<tr>
								<th>业务名:</th>
								<td class="editable" edit-name="busName" edit-save-url="/userSelf/modifyTag">${businessTagNames}</td>
							</tr>
							<tr>
								<th>职称:</th>
								<td>
									${user.jobTitle} ${user.level }(<span id="tip_show_level">${user.showLevel == 0?'已隐藏':'未隐藏' }</span>)
									<a id="toggle_show_level_btn" class="button" href="javascript:toggleShowLevel();">${user.showLevel == 0? '显示':'隐藏'}</a>
								</td>
							</tr>
							<tr>
								<th>经理:</th>
								<td>${managerUser.name}</td>
							</tr>
							<tr>
								<th>QQ:</th>
								<td class="editable" edit-name="qq" edit-save-url="/userSelf/modifyQQ">${userProfile.qq}</td>
							</tr>
							<tr>
								<th>兴趣爱好:</th>
								<td class="editable" edit-name="hobby" edit-save-url="/userSelf/modifyHobby">${userProfile.hobby}</td>
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
					<table class="user-full-table">
						<tbody>
							<c:forEach var="item" items="${myFollows}" varStatus="status">
								<tr>
									<td class="user-headpic">
										<a href="/info/${item.user.id }">
											<img width="50px" src="${empty item.user.headPic ?'/static/images/man_headpic.gif':item.user.headPic }">
										</a>
									</td>
									<td>
										<p>
											<a href="/info/${item.user.id }">
												${item.user.name}
											</a>
										</p>
										<p>
											${item.user.email}
										</p>
									</td>
									<td>
										${item.user.jobTitle}
									</td>
									<td>
										${item.deparmentName}
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</c:if>
		</div>
		<div class="fc"></div>
	</div>
	
	<%@include file="/inc/footer.jsp" %>
</body>
</html>