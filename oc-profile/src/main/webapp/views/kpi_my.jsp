<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
	<%@include file="/inc/head.jsp" %>
	<title>我的KPI- ${_siteName }</title>
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
		PU.confirmDialog('你确定结束考核吗？','警告',function(){
			$('#start_perf').attr('action','/admin/perf/end/'+perfTimeId).submit();
		});
	}
	function promotionStart(perfTimeId){
		if(!perfTimeId){
			return;
		}
		PU.confirmDialog('你确定结束升级考核吗？','警告',function(){
			$('#start_perf').attr('action','/admin/perf/endPromotion/'+perfTimeId).submit();
		});
	}
	</script>
</head>
<body>
	<%@include file="/inc/header.jsp" %>
	
	<div id="main">
		<%@include file="/inc/left-nav.jsp" %>
		<!--
		<div id="content">
			<div class="content-title">
						<h2>我的KPI</h2>
			</div>
			<div class="messages-box">
				<%@include file="/inc/messages.jsp" %>
			</div>
			
			<div class="content-box">
				<table id="start" class="common-table">
					<thead>
						<tr>
							<th>目标</th>
							<th>衡量标准和行动计划</th>
							<th>权重</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="perf" items="${perfList}" varStatus="status">
							<tr>
								<td>${perf.perfTitle}</td>
								<td>
									${perf.status().display }
								</td>
								<td>
									<c:if test="${perf.status() == c_perf_time_status$ready}">
										<label><input id="pref_promotion_cb" type="checkBox">升级</label>
									</c:if>
									<c:if test="${perf.status() != c_perf_time_status$ready}">
										${perf.promotionStatus() == c_promotion_status$ready? '不包含升级':'包含升级'}
									</c:if>
								</td>
								<td>
									<c:if test="${perf.status() == c_perf_time_status$ready && !showStart}">
										<a href="javascript:;" onclick="perfStart(${perf.id+0})">开始考核</a>
									</c:if>
									<c:if test="${perf.status() == c_perf_time_status$started}">
										<a href="javascript:;" onclick="perfEnd(${perf.id+0})">结束考核</a>
									</c:if>
									<c:if test="${perf.status() == c_perf_time_status$will_end}">
										即将结束
									</c:if>
								</td>
								<td>
									<c:if test="${perf.promotionStatus() == c_promotion_status$started}">
										<a href="javascript:;" onclick="promotionStart('${perf.id+0}')">结束升级评定</a>
									</c:if>
									<c:if test="${perf.promotionStatus() == c_promotion_status$end}">
										升级评定已结束
									</c:if>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			
			<form id="start_perf" method="POST">
				<input id="perf_promotion" type="hidden" name="perf_promotion" value="">
			</form>
		</div>
		  -->
		
		<div id="container" class="reset-list-css" style="margin-top: 200px;margin-bottom:100px;text-align: center;">
		    <p style="font-size: 20px;">
		        KPI还在开发当中，请您期待下一个版本！
		    </p>
		</div>
	<div class="fc"></div>
	</div>
	
	<%@include file="/inc/footer.jsp" %>
</body>
</html>