<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.renren.com/profile" prefix="profile"%>
<%@ taglib uri="http://www.renren.com/profile/functions" prefix="profilefn"%>

<c:set var="nav_menu" value="weeklyreport_other"></c:set>
<c:set var="user_resource" value="true"></c:set>

<!DOCTYPE html>
<html>
<head>
<%@include file="/inc/head.jsp"%>
<%@include file="/inc/tiny_mce.jsp"%>
<%@include file="/inc/datepicker.jsp"%>
<title></title>
<style>
</style>
<script type="text/javascript">

</script>
</head>
<body>
	<%@include file="/inc/header.jsp"%>

	<div id="main">
		<%@include file="/inc/left-nav.jsp"%>
		<div id="content">
			<div class="content-title">
				<h2>${_owner.name }的周报</h2>
			</div>
			<form class="timerangeselect datepicker-wrapper">
					<input name="startdate" value="${profilefn:escapeHtml(startdate)}" class="starttime allow-empty-date short-input"/>
					至 
					<input name="enddate" value="${profilefn:escapeHtml(enddate)}" class="endtime allow-empty-date short-input"/>
				<input type="submit" class="datepicker-btn" value="查询"> 
			</form>
			<c:forEach var="report" items="${reports}">
				<c:if test="${report.status() == _weeklyreport_status$submitted}"><%--是已提交的周报 --%>
					<div class="report-item">
						<div class="sub-title">
							<strong>
								<profile:formatDate value="${report.startDate() }" type="date" />
								至
								<profile:formatDate value="${report.endDate() }" type="date" />
							</strong>
						</div>
						<div class="content-box">
							<div class="report-done">
								<h4>本周已做</h4>
								${report.contentDone }
							</div>
							<div class="report-plan">
								<h4>下周要做</h4>
								${report.contentPlan }
							</div>
						</div>
					</div>
				</c:if>
			</c:forEach>
			<c:url var="pageurl" value="">
					<c:param name="startdate" value="${profilefn:escapeHtml(startdate)}"></c:param>
					<c:param name="enddate" value="${profilefn:escapeHtml(enddate)}"></c:param>
				</c:url>
				<profile:page page="${curpage}" total="${total}" href="${pageurl}&curpage={page}"/>
		</div>
		<div class="fc"></div>
	</div>

	<%@include file="/inc/footer.jsp"%>
</body>
</html>