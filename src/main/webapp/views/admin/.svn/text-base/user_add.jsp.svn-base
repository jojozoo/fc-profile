<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.renren.com/profile" prefix="profile"%>
<%@ taglib uri="http://www.renren.com/profile/functions" prefix="profilefn"%>

<c:set var="nav_menu" value="admin_user"></c:set>

<!DOCTYPE html>
<html>
<head>
<%@include file="/inc/head.jsp" %>
	<title>添加员工-员工管理</title>
	<style>
	</style>
	<script type="text/javascript">
	</script>
</head>
<body>
	<%@include file="/inc/header.jsp" %>
	
	<div id="main">
		<%@include file="/inc/left-nav.jsp" %>
		<div id="content">
			<div class="content-title">
					<h2><a href="/admin/user">角色</a> &#187; 创建用户</h2>
			</div>
			<form action=""></form>
			
			
			<form name="addUser" node-type="ajax-form">
			<p>姓名:<input type="text" name="name" value="${user.name}"/><span node-type="field-message" message-for="name"></span></p>
			<p>工号:<input type="text" name="number" value="${user.number}"/><span node-type="field-message" message-for="number"></span></p>
			<p>邮箱:<input type="text" name="email" value="${user.email}"/><span node-type="field-message" message-for="email"></span></p>
			<p>职位:<input type="text" name="jobtitle" value="${user.jobTitle}"/><span node-type="field-message" message-for="jobtitle"></span></p>
			<p>部门:<input type="text" name="department" value="${user.departmentId}"/><span node-type="field-message" message-for="department"></span></p>
			<p>QQ:<input type="text" name="qq" value="${userProfile.qq}"/><span node-type="field-message" message-for="qq"></span></p>
			<p>兴趣:<input type="text" name="hobby" value="${userProfile.hobby}"/><span node-type="field-message" message-for="hobby"></span></p>
			<p>BOSS邮箱:<input type="text" name="bossemail" value="${boss.email}"/><span node-type="field-message" message-for="bossemail"></span></p>
			<p>角色:
				<c:forEach var="role" items="${all_roles}" varStatus="status">
					<label><input type="checkbox" name="roles" value="${role.id }">${role.name }</label>
				</c:forEach>
			</p>
			<p><input type="submit" name="button" value="创建"/><span node-type="form-message"></span></p>
			</form>
		</div>
		<div class="fc"></div>
	</div>
	
	<%@include file="/inc/footer.jsp" %>
	<script type="text/javascript">
	</script>
</body>
</html>