<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="nav_menu" value="admin_perf"></c:set>

<!DOCTYPE html>
<html>
<head>
	<%@include file="/inc/head.jsp" %>
	<title>绩效周期管理</title>
	<style>
	</style>
	<script type="text/javascript">
	function perfStart(perfTimeId){
		if(!perfTimeId){
			return;
		}
		PU.confirmDialog('你确定开始考核吗？','警告',function(){
			$('#perf_promotion').val($('#pref_promotion_cb').prop('checked'));
			$('#start_perf').attr('action','/admin/perf/start/'+perfTimeId).submit();
		});
	}
	function perfEnd(perfTimeId){
		if(!perfTimeId){
			return;
		}
		PU.confirmDialog('你确定开始考核吗？','警告',function(){
			$('#start_perf').attr('action','/admin/perf/end/'+perfTimeId).submit();
		});
	}
	</script>
</head>
<body>
	<%@include file="/inc/header.jsp" %>
	
	<div id="main">
		<%@include file="/inc/left-nav.jsp" %>
		<div id="content">
			<form id="start_perf" method="POST">
				<input id="perf_promotion" type="hidden" name="perf_promotion" value="">
			</form>
			<table id="start" border="1">
				<tr>
					<td>绩效周期</td>
					<td>状态</td>
					<td>是否包含升职</td>
					<td>操作</td>
				</tr>
			<c:forEach var="perf" items="${perfList}" varStatus="status">
				<tr>
					<td>${perf.perfTitle}</td>
					<c:if test="${perf.status=='0'}">
						<td>未开始</td>
						<td><label><input id="pref_promotion_cb" type="checkBox">升职</label></td>
						<td><a href="javascript:;" onclick="perfStart(${perf.id+0})">开始考核</a></td>
					</c:if>
					<c:if test="${perf.status=='1'}">
						<td>已开始</td>
						<td><c:if test="${perf.isPromotion == 'true'}">是</c:if><c:if test="${perf.isPromotion == 'false'}">否</c:if></td>
						<td><a href="javascript:;" onclick="perfEnd(${perf.id+0})">结束考核</a></td>
					</c:if>
					<c:if test="${perf.status=='2'}">
						<td>已结束</td>
						<td><c:if test="${perf.isPromotion == 'true'}">是</c:if><c:if test="${perf.isPromotion == 'false'}">否</c:if></td>
						<td>&nbsp;</td>
					</c:if>
					
				</tr>
			</c:forEach>
			</table>
		</div>
	<div class="fc"></div>
	</div>
	
	<%@include file="/inc/footer.jsp" %>
</body>
</html>