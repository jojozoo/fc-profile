<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.renren.com/profile" prefix="profile"%>
<%@ taglib uri="http://www.renren.com/profile/functions" prefix="profilefn"%>

<c:set var="nav_menu" value="weeklyreport_my"></c:set>

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
<c:if test="${editedReport != null }">
	function submitReport(){
		PU.confirmDialog('提交后，将不能再次编辑本周报！你确定要提交么？','警告',function(){
			$('#report_form').attr('action','/weeklyreport/my/submit').submit();
		});
	}
</c:if>

</script>
</head>
<body>
	<%@include file="/inc/header.jsp"%>

	<div id="main">
		<%@include file="/inc/left-nav.jsp"%>
		<div id="content">
			<div class="content-title">
				<h2>
				<c:if test="${editmode }">
					<a href="/weeklyreport/my">我的周报</a> &#187; 编辑周报
				</c:if>
				<c:if test="${not editmode }">
				我的周报
				</c:if>
				</h2>
			</div>
			<c:if test="${editedReport != null }">
				<div class="content-box">
					<form id="report_form" node-type="ajax-form">
						<input type="hidden" name="id" value="${editedReport.id }">
						<h3>
							<profile:formatDate value="${editedReport.startDate() }" type="date" />
							至 
							<profile:formatDate value="${editedReport.endDate() }" type="date" />
							的周报
						</h3>
						<h4>本周已做<span node-type="field-message" message-for="content_done"></span></h4>
						<textarea class="tinymce" style="width: 99%;" name="content_done">${editedReport.contentDone }</textarea>
						<h4>下周要做<span node-type="field-message" message-for="content_plan"></span></h4>
						<textarea class="tinymce" style="width: 99%;" name="content_plan">${editedReport.contentPlan }</textarea>
						<input type="submit" onclick="javascript:return submitReport();" value="提交"> 
						<c:if test="${not editmode }"><%--编辑模式下，只允许提交，不允许保存 --%>
						<input type="submit" node-type="submit" form-action="/weeklyreport/my/save" value="保存"> 
						</c:if>
						<span node-type="form-message"></span>
					</form>
				</div>
			</c:if>
			<c:if test="${not editmode }">
				<div class="report-list">
					<form class="timerangeselect datepicker-wrapper">
							<input name="startdate" value="${profilefn:escapeHtml(startdate)}" class="starttime allow-empty-date short-input"/>
							至
							<input name="enddate" value="${profilefn:escapeHtml(enddate)}" class="endtime allow-empty-date short-input"/>
						<input type="submit" class="datepicker-btn" value="查询"> 
					</form>
					<c:forEach var="report" items="${reports}">
						<c:if test="${report.id != editedReport.id}">
							<div class="report-item">
								<div class="sub-title">
									<strong>
										<profile:formatDate value="${report.startDate() }" type="date" />
										~
										<profile:formatDate value="${report.endDate() }" type="date" />
										<c:if test="${report.status() != _weeklyreport_status$submitted}"><%--是未提交的周报 --%>
											(未提交)
										</c:if>
									</strong>
									<c:if test="${editableIds.contains(report.id)  }">
										<span class="title-action">
											<a href="/weeklyreport/my/edit?id=${report.id}" class="action">编辑</a>
										</span>
									</c:if>
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
			</c:if>
		</div>
		<div class="fc"></div>
	</div>

	<%@include file="/inc/footer.jsp"%>
</body>
</html>