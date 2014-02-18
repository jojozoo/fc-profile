<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.renren.com/profile" prefix="profile"%>
<%@ taglib uri="http://www.renren.com/profile/functions" prefix="profilefn"%>

<c:set var="nav_menu" value="kpi_other"></c:set>
<c:set var="user_resource" value="true"></c:set>

<!DOCTYPE html>
<html>
<head>
<%@include file="/inc/head.jsp" %>
	<title>KPI</title>
	<style>
	</style>
	<script type="text/javascript">
	</script>
</head>
<body onload="check();">
	<%@include file="/inc/header.jsp" %>
	
	<div id="main">
		<%@include file="/inc/left-nav.jsp" %>
		<div id="content">
			<div class="content-title">
					<h2>${_owner.name }的Kpi</h2>
			</div>
			<form action=""></form>
			<div class="kpi-list">
				<c:forEach var="kpiItem" items="${kpiList}" varStatus="status">
					<div class="kpi-item">
						<div class="sub-title">
							<strong>${kpiItem.title}</strong>
						</div>
						<div class="content-box">
							<div class="mce-content">
								${kpiItem.content}
							</div>
						</div>
					</div>
				</c:forEach>
			</div>
			<profile:page page="${curpage}" total="${total}" href="?curpage={page}"/>
		</div>
		<div class="fc"></div>
	</div>
	
	<%@include file="/inc/footer.jsp" %>
	<script type="text/javascript">
	function check(){
		var errorCode = '${errorCode}';
		var msg = '${msg}';
		if(errorCode == '0'){
			var inputs = document.getElementsByTagName('input');
			for(var i=0;i<inputs.length;i++){
					inputs[i].value='';
			}
		}
		if(msg.length > 1)
			alert(msg);
		}
	</script>
</body>
</html>