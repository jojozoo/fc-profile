<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/oc-profile.tld" prefix="profile"%>
<%@ taglib uri="/WEB-INF/tld/oc-profile-functions.tld" prefix="profilefn"%>

<c:set var="nav_menu" value="weeklyreport_subordinates"></c:set>

<!DOCTYPE html>
<html>
<head>
<%@include file="/inc/head.jsp"%>
<%@include file="/inc/tiny_mce.jsp"%>
<%@include file="/inc/datepicker.jsp"%>
<title>下属周报列表 - ${_siteName }</title>
<style>
</style>
<script type="text/javascript">

	function submitComment(id){
		$('#comment_form_'+id).attr('action','/weeklyreport/comment').submit();
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
				<h2>
					<c:if test="${not empty week_date_desc }">${week_date_desc }(</c:if>
					<profile:formatDate value="${start_date }" type="date" />
								至
					<profile:formatDate value="${end_date}" type="date" />
					<c:if test="${not empty week_date_desc }">)</c:if>
					的周报
				</h2>
			</div>
			<div class="messages-box">
				<%@include file="/inc/messages.jsp" %>
			</div>
			<form class="timerangeselect datepicker-wrapper">
					<input name="date" value="${profilefn:escapeHtml(date)}" class="starttime allow-empty-date short-input"/>
				<input type="submit" class="datepicker-btn" value="查询"> 
			</form>
			
			<c:forEach var="user" items="${subordinates}" varStatus="status">
				<div class="sub-title">
							<strong>
								<a href="/info/${user.id }">
									${user.name }
								</a>
							</strong>
				</div>
				
				<div class="content-box">
					<p>${weeklyreport_map[user.id].emailTos} </p>
					<p>${c_weeklyreport_status$submitted.display}</p>
					<c:set var="report" value="${weeklyreport_map[user.id] }"></c:set>
					<c:set var="comment" value="${weeklyreport_comment_map[report.id] }"></c:set>
					
					<c:choose>
						<c:when test="${report == null}">
							<div><i>未填写周报</i></div>
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${report.status != c_weeklyreport_status$submitted.id}">
									<div><i>未提交周报</i></div>
								</c:when>
								<c:otherwise>
									<fieldset class="report-done">
										<legend>本周已做</legend>
										${report.contentDone }
									</fieldset>
									<fieldset class="report-plan">
										<legend>下周要做</legend>
										${report.contentPlan }
									</fieldset>
									<fieldset class="report-qa">
										<legend>遇到的问题及解决方案</legend>
										${report.qa }
									</fieldset>
								</c:otherwise>
							</c:choose>
							<c:forEach var="item" items="${comment}" varStatus="status">
								<fieldset id="comment_${status.index}" class="report-comment" (${item == null ?'style="display: none"':''} || isRoot == false ? 'style="display: none"':'')>
									<legend>上司${item.commentUserName }点评</legend>
									<p class="comment-content">
										${item.comment }
									</p>
								</fieldset>
							</c:forEach>
							<c:if test="${comment == null}">
								<%--显示点评按钮 --%>
								<input id="comment_btn_${status.index}" type="button" value="点评" onclick="javascript:jQuery('#comment_form_${status.index }').show();jQuery('#comment_btn_${status.index }').hide();">
								<form id="comment_form_${status.index}" form-no="${status.index}" class="comment_form" method="post" action="/weeklyreport/comment" node-type="ajax-form" style="display: none;">
									<input type="hidden" name="reporter_id" value="${user.id }">
									<input type="hidden" name="weekly_report_id" value="${report.id}">
									<textarea name="comment" placeholder="输入点评的内容"></textarea>
									<p>
										<span node-type="field-message" message-for="comment"></span>
									</p>
									<p>
									    <input  type="button" class="input-submit" onclick="javascript:return submitComment('${status.index}');" value="点评">
										<input type="button" class="input-submit" value="取消" onclick="javascript:jQuery('#comment_form_${status.index }').hide();jQuery('#comment_btn_${status.index }').show();">
										<span node-type="form-message"></span>
									</p>
								</form>
							</c:if>
						</c:otherwise>
					</c:choose>
				</div>
			</c:forEach>
			<c:url var="pageurl" value="">
					<c:param name="startdate" value="${profilefn:escapeHtml(startdate)}"></c:param>
					<c:param name="enddate" value="${profilefn:escapeHtml(enddate)}"></c:param>
				</c:url>
				<profile:page page="${curpage}" total="${total}" href="${pageurl}&curpage={page}" hidewhen="single"/>
		</div><%@include file="/inc/right-nav.jsp" %>
		<div class="fc"></div>
	</div>

	<%@include file="/inc/footer.jsp"%>
</body>
</html>