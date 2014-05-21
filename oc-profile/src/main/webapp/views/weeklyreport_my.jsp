<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/oc-profile.tld" prefix="profile"%>
<%@ taglib uri="/WEB-INF/tld/oc-profile-functions.tld" prefix="profilefn"%>

<c:set var="nav_menu" value="weeklyreport_my"></c:set>

<!DOCTYPE html>
<html>
<head>
<%@include file="/inc/head.jsp"%>
<%@include file="/inc/tiny_mce.jsp"%>
<%@include file="/inc/datepicker.jsp"%>
<title>我的周报 - ${_owner.name} - ${_siteName }</title>
<style>
</style>
<script type="text/javascript">
<c:if test="${editedReport != null }">
	function submitReport(){
		PU.confirmDialog('提交后，将不能再次编辑本周报！你确定要提交么？','警告',function(){
			$('#report_form').attr('action','/weeklyreport/my/submit').submit();
		});
		return false;
	}
	function previewReport(){
		$('#report_form').attr('action','/weeklyreport/my/preview').submit();
		return false;
	}
	
	<c:if test="${empty editedReport.qa}">
	window.pageTinymceOptions = {
		setup:function(ed){
			if(ed.id == 'qa_textarea'){
				var _change = function(ed, evt) {
					$('#qa_changed').val('true');
					ed.onChange.remove(_change);
			  };
				ed.onChange.add(_change);
			}
		}	
	};
	</c:if>
	
	jQuery(function($){
		$('#report_form').ajaxform({
			success:function(data){
				var self = $(this);
				if(data && data.action == 'preview'){
					// preview
					$('#preview-report-done').html(data.content_done);
					$('#preview-report-plan').html(data.content_plan);
					$('#preview-report-qa').html(data.qa);

					$('#preview-report').show();
				}
			}
		});
	});
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
			<div class="messages-box">
				<%@include file="/inc/messages.jsp" %>
			</div>
			<c:if test="${editedReport != null }">
				<div class="content-box">
					<form id="report_form" node-type="ajax-form">
						<input type="hidden" name="id" value="${editedReport.id }">
						<h3>
							<profile:formatDate value="${editedReport.startDate}" type="date" />
							至 
							<profile:formatDate value="${editedReport.endDate}" type="date" />
							的周报
						</h3>
						<br>
						<div class="content-span">
						<h4>本周已做<span node-type="field-message" message-for="content_done"></span></h4>
						<textarea class="tinymce" style="width: 99%;" name="content_done">${editedReport.contentDone }</textarea>
						</div>
						<br>
						<div class="content-span">
						<h4>下周要做<span node-type="field-message" message-for="content_plan"></span></h4>
						<textarea class="tinymce" style="width: 99%;" name="content_plan">${editedReport.contentPlan }</textarea>
						</div>
						<div class="content-span">
							<h4>遇到的问题及解决方案<span node-type="field-message" message-for="q_a"></span></h4>
							<c:if test="${empty editedReport.qa}">
								<input type="hidden" id="qa_changed" name="qa_changed" value="false">
								<textarea id="qa_textarea" class="tinymce" style="width: 99%;" name="q_a">
										${_weeklyreport_qa_default }
								</textarea>
							</c:if>
							<c:if test="${not empty editedReport.qa}">
								<input type="hidden" id="qa_changed" name="qa_changed" value="true">
								<textarea id="qa_textarea" class="tinymce" style="width: 99%;" name="q_a">${editedReport.qa}</textarea>
							</c:if>
						</div>
						<div class="content-span">
							<h4>需要发送的邮件组（可选,用分号分隔）<span node-type="field-message" message-for="emailtos"></span></h4>
							<input type="text" maxlength="200" class="full-input" name="emailtos" value="${editedReport.emailTos}">
						</div>
						<br>
						<input type="button" class="input-submit" onclick="javascript:return submitReport();" value="提交"> 
						<input type="button" class="input-submit" node-type="submit" form-action="/weeklyreport/my/save" value="保存"> 
						<input type="button" class="input-submit" onclick="javascript:return previewReport();" value="预览"> 
						<span node-type="form-message"></span>
					</form>
					<div id="preview-report" class="preview-report" style="display: none;">
						<div class="report-item">
							<div class="content-box">
								<fieldset class="report-done">
									<legend>本周已做</legend>
									<div id="preview-report-done" class="mce-content">
									</div>
								</fieldset>
								<fieldset class="report-plan">
									<legend>下周要做</legend>
									<div id="preview-report-plan" class="mce-content">
									</div>
								</fieldset>
								<fieldset class="report-question">
									<legend>遇到的问题及解决方案</legend>
									<div id="preview-report-qa" class="mce-content">
									</div>
								</fieldset>
							</div>
						</div>
					</div>
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
										<profile:formatDate value="${report.startDate}" type="date" />
										~
										<profile:formatDate value="${report.endDate}" type="date" />
										(
										<c:choose>
											<c:when test="${report.status == c_weeklyreport_status$submitted.id }">
											已提交
											</c:when>
											<c:otherwise>
											未提交
											</c:otherwise>
										</c:choose>
										<c:if test="${report.supplementary}">
											，补充的
										</c:if>
										)
									</strong>
									<c:if test="${profilefn:contains(editableIds, report.id)}">
										<span class="title-action">
											<a href="/weeklyreport/my/edit?id=${report.id}" class="action">编辑</a>
										</span>
									</c:if>
								</div>
								
								<div class="content-box">
									<fieldset class="report-done">
										<legend>本周已做</legend>
										<profile:default def="<i>未填写内容</i>">${report.contentDone}</profile:default>
									</fieldset>
									<fieldset class="report-plan">
										<legend>下周要做</legend>
										<profile:default def="<i>未填写内容</i>">${report.contentPlan}</profile:default>
									</fieldset>
									<fieldset class="report-question">
										<legend>遇到的问题及解决方案</legend>
										<profile:default def="<i>未填写内容</i>">${report.qa}</profile:default>
									</fieldset>
									<c:set var="comments" value="${reporsComments[report.id]}"></c:set>
									<c:if test="${comments!=null }">
										<c:forEach var="item" items="${comments}" varStatus="status">
											<fieldset class="report-comments">
												<legend>${item.commentUserName }点评:</legend>
												<profile:default def="<i>未填写内容</i>">${item.comment }</profile:default>
											</fieldset>
										</c:forEach>
									</c:if>
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
		</div><%@include file="/inc/right-nav.jsp" %>
		<div class="fc"></div>
	</div>

	<%@include file="/inc/footer.jsp"%>
</body>
</html>