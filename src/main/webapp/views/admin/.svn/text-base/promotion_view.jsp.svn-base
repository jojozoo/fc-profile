<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.renren.com/profile" prefix="profile"%>
<%@ taglib uri="http://www.renren.com/profile/functions" prefix="profilefn"%>

<c:set var="nav_menu" value="admin_promotion"></c:set>

<!DOCTYPE html>
<html>
<head>
	<%@include file="/inc/head.jsp" %>
	<title>升职评定结果-升职管理</title>
	<style>
	</style>
	<script type="text/javascript">
	function savePromtion(promotionManageId){
		if(!promotionManageId){
			return;
		}
		PU.confirmDialog('你确定提交本次反馈吗？','警告',function(){
			$('#savePromotionForm').attr('action','/admin/promotion/save/'+promotionManageId).submit();
		});
	}
	function cancel(){
		PU.confirmDialog('你确定取消该用户的评定吗？','警告',function(){
			$('#cancelForm').submit();
		});
	}
	</script>
</head>
<body>
	<%@include file="/inc/header.jsp" %>
	
	<div id="main">
		<%@include file="/inc/left-nav.jsp" %>
		<div id="content">
			<div class="content-title">
						<h2>升职评定结果反馈</h2>
			</div>
			<div>
					<strong>${userPerf.userName}的升职反馈</strong>
			</div>
			<form id="savePromotionForm" method="POST" node-type="ajax-form">
				<fieldset>
					<legend>评定结果：</legend>
					<c:if test="${promotion_end == 'false'}">
						<label><input name="ispromotion" type="radio" value="0"/>同意</label>
	         			<label><input name="ispromotion" type="radio" value="1" />不同意</label>
         			</c:if>
         			<c:if test="${promotion_end == 'true'}">
						<c:if test="${not empty promotionManage}">
							<c:if test="${promotionManage.isPromotion == '0'}">同意 </c:if>
							<c:if test="${promotionManage.isPromotion == '1'}">不同意 </c:if>
						</c:if>
						<c:if test="${empty promotionManage}">不同意</c:if>
         			</c:if>
				</fieldset>
				<br/>
				<fieldset>
					<legend>TC反馈：</legend>
					<textarea name="promotioncontent" >请填写TC的反馈意见</textarea><br/>
					<span node-type="field-message" message-for="promotioncontent"></span>
				</fieldset>
				<input type="hidden" name="perftimeid" value="${perfTime.id}"/>
				<input type="hidden" name="selfperfid" value="${userPerf.id}"/>
				<c:if test="${promotion_end == 'false'}">
					<input type="button" value="提交" onclick="savePromtion('${promotionManage.id+0}')"/>&nbsp;&nbsp;&nbsp;
				</c:if>
					<input type="button" value="取消" onclick="cancel()"/>
					<span node-type="form-message"></span>
			</form>
			<form method="GET" action="/admin/promotion" id="cancelForm"></form>
		</div>
		<div class="fc"></div>
	</div>
	
	<%@include file="/inc/footer.jsp" %>
</body>
</html>