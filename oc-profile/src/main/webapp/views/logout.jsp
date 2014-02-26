<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/oc-profile.tld" prefix="profile"%>
<%@ taglib uri="/WEB-INF/tld/oc-profile-functions.tld" prefix="profilefn"%>

<!DOCTYPE html>
<html>
<head>
<%@include file="/inc/head.jsp"%>
<title>成功登录</title>
</head>
<body>
	<%@include file="/inc/header.jsp"%>

	<div id="container" class="reset-list-css" style="margin-top: 200px;margin-bottom:100px;text-align: center;">
		<p style="font-size: 20px;">你已经成功退出系统！</p>
        <a href="/login">再次登录</a>
	</div>

	<%@include file="/inc/footer.jsp"%>
</body>
</html>