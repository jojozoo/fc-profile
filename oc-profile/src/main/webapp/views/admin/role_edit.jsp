<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tld/oc-profile.tld" prefix="profile"%>
<%@ taglib uri="/WEB-INF/tld/oc-profile-functions.tld" prefix="profilefn"%>

<c:set var="nav_menu" value="admin_role"></c:set>

<!DOCTYPE html>
<html>
<head>
	<%@include file="/inc/head.jsp" %>
	<script src="/static/js/jquery.jeditable.js" type="text/javascript"></script>
	<title>管理员 -角色管理- ${_siteName }</title>
	<style>
	</style>
	<script type="text/javascript">
	jQuery(function($){
		// editable
		$('#edit-name').each(function(){
			var self = $(this);
			self.editable("/admin/roles/editname",{
				id:'none',
				name:'name',
				submitdata:{id:${role.id+0},__ajax:'json'},
				callback:function(result){
					if(result.code === 0){
						$(self).html((result.data || {}).name);
					}else{
						PU.alertDialog('编辑失败:'+((result.errors||{})[""] ||[] ).join(","));
					}
				},
				ajaxoptions:{
					dataType:'json'
				},
			//onblur:'ignore',
				submit:'保存',
				cancel:'取消',
				cssclass:'single-line-form',
				placeholder: '<span class="empty-content">点击此处编辑</span>'
			});
		});
	});
	
	jQuery(function($){
		(function() {
			var cache = {}, lastXhr;
			var inputObj =$('#user-input');
			inputObj.suggest({
				from:'roleedit',
				focus: function( event, ui ) {
					inputObj.val(ui.item.email);
					return false;
				},
				select: function( event, ui ) {
					inputObj.val(ui.item.email);
					return false;
				}
			});
		})();
		
		$('#add-user-btn').click(function(){
			var email = $('#user-input').val();
			if(!email){
				PU.alertDialog('你需要输入用户的Email');
				return;
			}
			PU.ajaxPost('/admin/roles/users/add',{
				data:{id:${role.id+0},email:email},
				success:function(json){
					$('#user-input').val("");
					if(json.code !== 0){
						PU.alertDialog(((json.errors||{})[""] || {}).join(",") || '服务器忙，请稍候再试');
						return;
					}
					//
					var user = json.data.user;
					$('#user-list tbody').prepend(
							[
							 "<tr>",//
							 ' <td class="user-headpic">',//
							 '	<a href="/info/',user.id,'">',//
								'		<img width="50px" src="',user.tinyurl,'">',//
							'		</a>',//
								'</td>',//
								'<td>',//
								'	<p>',//
								'		<a href="/info/',user.id,'">',//
								user.name || "",//
								'		</a>',//
								'	</p>',//
								'	<p>',//
								user.email || "",//
								'	</p>',//
								'</td>',//
								'<td>',//
								user.job || "",//
								'</td>',//
								'<td>',//
								user.department || "",//
								'</td>',//
								'<td>',//
								' <a href="javascript:void(0);" onclick="javascript:deleteUser(this,',(user.id||0),')">删除</a>',//
							  '</td>',//
							 "</tr>",//
							 ""].join("")
							);
				}
			});
			return false;
		});
		
	});
	
	function deleteUser(aObj,userId){
		if(!userId || userId < 0){
			return;
		}
		aObj= jQuery(aObj);
		PU.confirmDialog('你确定删除此用户么?',function(){
			PU.ajaxPost("/admin/roles/users/delete",{
				data:{id:${role.id+0},user:userId},
				success:function(json){
					if(json.code !== 0){
						PU.alertDialog(((json.errors||{})[""] || {}).join(",") || '服务器忙，请稍候再试');
						return;
					}
					aObj.parents('tr').remove();
				}
			});
		});
	}
	</script>
</head>
<body>
	<%@include file="/inc/header.jsp" %>
	
	<div id="main">
		<%@include file="/inc/left-nav.jsp" %>
		<div id="content">
			<div class="content-title">
					<h2><a href="/admin/roles">角色</a> &#187; ${role.name}</h2>
			</div>
			<div class="messages-box">
				<%@include file="/inc/messages.jsp" %>
			</div>
			
			<div class="content-box">
				<p>
					<label>角色名称</label>
					<span id="edit-name" type="text" name="name" class="editable">${role.name}</span>
				</p>
		  </div>
		  
			<div class="sub-title">
				用户列表
			</div>
			<div class="content-box">
				<p>
					用户
					<input type="text" id="user-input" class="middle-input">
					<input type="button" id="add-user-btn" value="添加">
				</p>
				
				<table id="user-list" class="user-full-table">
					<tbody>
						<c:forEach var="user" items="${user_list }">
							<tr>
								<td class="user-headpic">
									<a href="/info/${user.id }">
										<img width="50px" src="${user.tinyUrl}">
									</a>
								</td>
								<td>
									<p>
										<a href="/info/${user.id }">
											${user.name}
										</a>
									</p>
									<p>
										${user.email}
									</p>
								</td>
								<td>
									${user.jobTitle}
								</td>
								<td>
									<c:if test="${not empty user.department}">
										${user.department.departmentName}
									</c:if>
								</td>
								<td>
									<a href="javascript:void(0);" onclick="javascript:deleteUser(this,${user.id+0})">删除</a>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<profile:page page="${curpage }" total="${total}" href="?curpage={page}"/>
			</div> 
		</div>
		<%@include file="/inc/right-nav.jsp" %>
		<div class="fc"></div>
	</div>
	
	<%@include file="/inc/footer.jsp" %>
</body>
</html>