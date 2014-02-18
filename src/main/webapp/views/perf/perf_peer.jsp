<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<c:set var="nav_menu" value="perf_peer"></c:set>

<!DOCTYPE html>

<head>
<%@include file="/inc/head.jsp"%>
<title>绩效互评-perfs</title>
<style>
</style>
<script type="text/javascript">
	function saveForm() {
		$('#peerPerfStatus').val('save');
		$('#peerPerfForm').attr('action', '/perf/peer/save/save').submit();
	}
	function submitForm(promotionManageId) {
		PU.confirmDialog('你确定提交本次互评吗？', '警告', function() {
			$('#peerPerfStatus').val('submit');
			$('#peerPerfForm').attr('action', '/perf/peer/save/submit')
					.submit();
		});
	}
	function cancel() {
		PU.confirmDialog('你确定取消离开此页面？', '警告', function() {
			$('#cancelForm').submit();
		});
	};
	function addProject() {
		jQuery('#project-list').append(jQuery('#empty_project').html());
	}
</script>
</head>
<body>
	<%@include file="/inc/header.jsp"%>

	<div id="main">
		<%@include file="/inc/left-nav.jsp"%>
		<div id="content">
			<div class="content-title">
				<h2>对${selfPerfUser.name}-${perfTime.perfTitle}Perf的评价</h2>
				<p>请评价你的同事。</p>
			</div>
			<form name="peerPerfForm" id="peerPerfForm" node-type="ajax-form">
				<fieldset>
					<legend>项目描述：</legend>
					<div id="project-list">
						<c:set var="selfPerfProjects" value="${userSelfPerf.projects();}"></c:set>
						<c:set var="peerPerfProjects" value="${peerPerf.projects();}"></c:set>
						<c:if test="${not empty selfPerfProjects}">
							<c:forEach var="projectItem" items="${selfPerfProjects}" varStatus="status">
								<div class="project">
									<strong>项目${status.index+1}:</strong><span>${projectItem.projectName}</span><br>
									<p>项目内容:${projectItem.projectContent}</p>
									我在项目中的角色 ：${projectItem.role}<br> 我在项目中的影响力 ：${projectItem.weight().display() }<br> <input type="hidden" name="selfperfprojectids" value="${projectItem.id}" /> <input type="hidden" name="peerperfprojectids" value="<c:if test="${not empty peerPerfProjects}">${peerPerfProjects[status.index].id}</c:if><c:if test="${empty peerPerfProjects}">0</c:if> " />
									<textarea name="projectcomments">${peerPerfProjects[status.index].content}</textarea>
								</div>
							</c:forEach>
						</c:if>
					</div>
				</fieldset>
				<fieldset>
					<legend>Ta的优点：</legend>
					<p>${userSelfPerf.advantage}</p>
					<textarea name="advantagecomment">${peerPerf.advantageComments}</textarea>
					<br> <span node-type="field-message" message-for="advantagecomment"></span><br>
				</fieldset>
				<fieldset>
					<legend>Ta的不足点：</legend>
					<p>${userSelfPerf.disadvantage}</p>
					<textarea name="disadvantagecomment">${peerPerf.disadvantageComments}</textarea>
					<br> <span node-type="field-message" message-for="disadvantagecomment"></span><br>
				</fieldset>
				<c:if test="${perfTime.isPromotion == 'true'}">
					<c:if test="${userSelfPerf.isPromotion == 'true'}">
						<h3>是否支持Ta升职</h3>
						<label><input type="radio" name="ispromotion" value="true" checked="checked" />是，我支持Ta申请升职</label>
						<br>
						<label><input type="radio" name="ispromotion" value="false" <c:if test="${peerPerf.isPromotion == 'false'}"> checked="checked"</c:if> />不，Ta还需要锻炼锻炼</label>
						<br>
					</c:if>
				</c:if>
				<c:if test="${isManager == 'true'}">
					<h3>对TA的整体考核意见</h3>
					<textarea name="managercomment">${peerPerf.content}</textarea>
					<br>
					<p>
						绩效评级： <select name="perfscore">
							<option value="S" <c:if test="${peerPerf.perfScore == 'S'}">selected="selected"</c:if>>S</option>
							<option value="A" <c:if test="${peerPerf.perfScore == 'A'}">selected="selected"</c:if>>A</option>
							<option value="B" <c:if test="${peerPerf.perfScore == 'B'}">selected="selected"</c:if>>B</option>
							<option value="C" <c:if test="${peerPerf.perfScore == 'C'}">selected="selected"</c:if>>C</option>
						</select>
					</p>
				</c:if>
				<input type="hidden" name="perftimeid" value="${perfTime.id}" /> <input type="hidden" name="invitationid" value="${invitation.id}" /> <input type="hidden" name="userselfperfid" value="${userSelfPerf.id}" /> <input type="hidden" name="status" id="peerPerfStatus" /><br>
				<c:if test="${peerPerf.status != '1'}">
					<p>
						<input type="button" value="提交" onclick="submitForm();" /> <input type="button" value="保存" onclick="saveForm();" /><span node-type="form-message"></span>
					</p>
				</c:if>
				<input type="button" value="取消" onclick="cancel();" /><br>
			</form>
			<form id="cancelForm" method="GET" action="/perf"></form>
		</div>
		<div class="fc"></div>
	</div>

	<%@include file="/inc/footer.jsp"%>
</body>
</html>