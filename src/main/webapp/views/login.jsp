<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.renren.com/profile" prefix="profile"%>
<%@ taglib uri="http://www.renren.com/profile/functions" prefix="profilefn"%>

<c:set var="nav_menu" value="resume_view"></c:set>

<!DOCTYPE html>
<html>
<head>
<%@include file="/inc/head.jsp"%>
<title></title>
<style>
</style>
<script type="text/javascript">
	
</script>
</head>
<body>
	<%@include file="/inc/header.jsp"%>

	<div id="container">
	<form method="POST" action="/do">
		<input type="text" name="name"> </input></br>
		<input type="password" name="passwd"> </input></br>
		<input type="hidden"></input></br>
		<input type="submit">登录</input>
	</form>
	</div>

	<%@include file="/inc/footer.jsp"%>
</body>
</html>