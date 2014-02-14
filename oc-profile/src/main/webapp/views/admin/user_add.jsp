<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tld/oc-profile.tld" prefix="profile"%>
<%@ taglib uri="/WEB-INF/tld/oc-profile-functions.tld" prefix="profilefn"%>

<c:set var="nav_menu" value="admin_user"></c:set>

<!DOCTYPE html>
<html>
<head>
<%@include file="/inc/head.jsp" %>
	<title>添加员工-员工管理-${_siteName }</title>
	<style type="text/css">
		table input{
			width:200px;
		}
		.check_box{
			position: relative;
    		top: 5px;
		}
		.user_button{
			position:relative;
			margin-top:15px;
		}
	</style>
	<script type="text/javascript">
	jQuery(function($){
		(function() {
			var inputObj =$('#boss-email-input');
			inputObj.suggest({
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
	});
	</script>
</head>
<body>
	<%@include file="/inc/header.jsp" %>
	
	<div id="main">
		<%-- <%@include file="/inc/left-nav.jsp" %> --%>
		<div id="content">
			<div class="content-title">
					<h2><a href="/admin/user">角色</a> &#187; 创建用户</h2>
			</div>
			<div class="messages-box">
				<%@include file="/inc/messages.jsp" %>
			</div>
			<form action=""></form>
			
			
			<form name="addUser" node-type="ajax-form">
				<table>
					<tbody>
						<tr>
							<th><span class="required">*</span>姓名:</th>
							<td><input type="text" name="name" value="${user.name}" required="required"/></td>
							<td><span node-type="field-message" message-for="name"></span></td>
						</tr>
						<tr>
							<th>工号:</th>
							<td><input type="text" name="number" value="${user.number}"/></td>
							<td><span node-type="field-message" message-for="number"></span></td>
						</tr>
						<tr>
							<th><span class="required">*</span>邮箱:</th>
							<td><input type="text" name="email" value="${user.email}" required="required"/></td>
							<td><span node-type="field-message" message-for="email"></span></td>
						</tr>
						<tr>
							<th><span class="required">*</span>岗位:</th>
							<td><input type="text" name="jobtitle" value="${user.jobTitle}" required="required"/></td>
							<td><span node-type="field-message" message-for="jobtitle"></span></td>
						</tr>
						<tr>
							<th><span class="required">*</span>级别:</th>
							<td><input type="text" name="level" value="${user.level}" required="required"/></td>
							<td><span node-type="field-message" message-for="level"></span></td>
						</tr>
						<tr>
							<th>部门:</th>
							<td><input type="text" name="department" value="${user.departmentId > 0 ? user.departmentId:''}"/></td>
							<td><span node-type="field-message" message-for="department"></span></td>
						</tr>
						<tr>
							<th>QQ:</th>
							<td><input type="text" name="qq" value="${userProfile.qq > 0 ?userProfile.qq:'' }"/></td>
							<td><span node-type="field-message" message-for="qq"></span></td>
						</tr>
						<tr>
							<th>兴趣:</th>
							<td><input type="text" name="hobby" value="${userProfile.hobby}"/></td>
							<td><span node-type="field-message" message-for="hobby"></span></td>
						</tr>
						<tr>
							<th><span class="required">*</span>BOSS邮箱:</th>
							<td><input type="text" id="boss-email-input" name="bossemail" value="${boss.email}" required="required"/></td>
							<td><span node-type="field-message" message-for="bossemail"></span></td>
						</tr>
					</tbody>
				</table>	
					<profile:security actions="root">
						<p>角色:
							<c:forEach var="role" items="${unchecked}" varStatus="status">
								<label><input class="check_box" type="checkbox" name="roles" value="${role.id }">${role.name }</label>
							</c:forEach>
						</p>
					</profile:security>					
			<p><input class="user_button" type="submit" name="button" value="创建"/><span node-type="form-message"></span></p>
			</form>
		</div>
		<%@include file="/inc/right-nav.jsp" %>
		<div class="fc"></div>
	</div>
	
	<%@include file="/inc/footer.jsp" %>
	<script type="text/javascript">
	</script>
</body>
</html>