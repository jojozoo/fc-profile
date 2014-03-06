<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/oc-profile.tld" prefix="profile"%>
<%@ taglib uri="/WEB-INF/tld/oc-profile-functions.tld" prefix="profilefn"%>

<c:set var="nav_menu" value="dailyReport_other"></c:set>
<c:set var="user_resource" value="true"></c:set>

<!DOCTYPE html>
<html>
<head>
<%@include file="/inc/head.jsp"%>
<%@include file="/inc/tiny_mce.jsp"%>
<%@include file="/inc/datepicker.jsp"%>
<title>${_owner.name}的日报 - ${_siteName }</title>
<style>
</style>
<script type="text/javascript">
function submitComment(id){
	$('#comment_form_'+id).attr('action','/dailyReport/comment').submit();
}
jQuery(function($){
	$("form.comment_form").ajaxform({
		success:function(data){
			var formNo = $(this).attr('form-no');
			if(formNo){
				$('#comment_'+formNo).find('.comment-content').text(data.content ||"").end().show();
				$('#comment_btn_'+formNo).hide();
				$('#comment_form_'+formNo).hide();
			}
		}
	});
	
});
</script>
</head>
<body>
	<%@include file="/inc/header.jsp"%>

	<div id="main">
		<%@include file="/inc/left-nav.jsp"%>
		<div id="content">
			<div class="content-title">
				<h2>${_owner.name }的日报</h2>
			</div>
			<div class="messages-box">
				<%@include file="/inc/messages.jsp" %>
			</div>
			<form class="timerangeselect datepicker-wrapper">
					<input name="startdate" value="${profilefn:escapeHtml(startdate)}" class="starttime allow-empty-date short-input"/>
					至 
					<input name="enddate" value="${profilefn:escapeHtml(enddate)}" class="endtime allow-empty-date short-input"/>
				<input type="submit" class="datepicker-btn" value="查询"> 
			</form>
			<c:forEach var="report" items="${reports}" varStatus="status">
				<div class="report-item">
					<div class="sub-title">
						<strong>
							<profile:formatDate value="${report.startDate}" type="date" />
							至
							<profile:formatDate value="${report.endDate}" type="date" />
						</strong>
						<c:if test="${report.supplementary}">
							(补充的)
						</c:if>
					</div>
					<div class="content-box">
						<c:if test="${report.status != c_dailyreport_status$submitted}"><%--是未提交的日报 --%>
							<div><i>未提交当天日报</i></div>
						</c:if>
						<c:if test="${report.status == c_dailyreport_status$submitted}"><%--是已提交的日报 --%>
							<fieldset class="report-plan">
								<legend>当天计划</legend>
								${report.contentPlan }
							</fieldset>
							<fieldset class="report-done">
								<legend>当天结果</legend>
								${report.contentDone }
							</fieldset>
						</c:if>
					</div>
				</div>
			</c:forEach>
			<c:url var="pageurl" value="">
					<c:param name="startdate" value="${profilefn:escapeHtml(startdate)}"></c:param>
					<c:param name="enddate" value="${profilefn:escapeHtml(enddate)}"></c:param>
				</c:url>
				<profile:page page="${curpage}" total="${total}" href="${pageurl}&curpage={page}"/>
		</div><%@include file="/inc/right-nav.jsp" %>
		<div class="fc"></div>
	</div>

	<%@include file="/inc/footer.jsp"%>
</body>
</html>