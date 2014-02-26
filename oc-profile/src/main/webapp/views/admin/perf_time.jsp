<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="nav_menu" value="admin_perf"></c:set>

<!DOCTYPE html>
<html>
<head>
	<%@include file="/inc/head.jsp" %>
	<title>绩效管理- ${_siteName }</title>
	<style>
	</style>
	<script type="text/javascript">
	
	function perfPublic(perfTimeId){
		if(!perfTimeId){
			return;
		}
		PU.confirmDialog('你确定公开考核分数吗？','警告',function(){
			$('#start_perf').attr('action','/admin/perf/display/'+perfTimeId).submit();
		});
	}
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
		<div id="content">
			<div class="content-title">
						<h2>绩效周期管理</h2>
			</div>
			<div class="messages-box">
				<%@include file="/inc/messages.jsp" %>
			</div>
			
			<div class="content-box">
				<table id="start" class="common-table">
					<thead>
						<tr>
							<th>绩效周期</th>
							<th>状态</th>
							<th>是否包含升级</th>
							<th>操作</th>
							<th>升级考核</th>
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
									<c:if test="${perf.status() == c_perf_time_status$end && perf.isPublic == 0}">
										<a href="javascript:;" onclick="perfPublic(${perf.id+0})">公开分数</a>
									</c:if>
									<c:if test="${perf.status() == c_perf_time_status$end && perf.isPublic == 1}">
										已完成
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
				<profile:page page="${curpage}" total="${total}" href="?curpage={page}"/>
			</div>
			
			<form id="start_perf" method="POST">
				<input id="perf_promotion" type="hidden" name="perf_promotion" value="">
			</form>
		</div>
    <%@include file="/inc/right-nav.jsp" %>
	<div class="fc"></div>
	</div>
	
	<%@include file="/inc/footer.jsp" %>
</body>
</html>