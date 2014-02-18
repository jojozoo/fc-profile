<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.renren.com/profile" prefix="profile"%>
<%@ taglib uri="http://www.renren.com/profile/functions" prefix="profilefn"%>

<c:set var="nav_menu" value="perf_index"></c:set>

<!DOCTYPE html>
<html>
<head>
<%@include file="/inc/head.jsp"%>
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
					<h2>绩效考核</h2>
			</div>
				
			<div class="content-box">
				<table class="common-table">
					<thead>
						<tr>
							<th>季度</th>
							<th>成绩</th>
							<th>状态</th>
							<th>考核人</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="perf_time" items="${perf_times }">
							<c:set var="perf" value="${perf_map[perf_time.id] }"></c:set>
							<tr>
								<td>
									<a href="/perf/summary/my/${perf_time.id}">
										${perf_time.perfTitle }
									</a>
								</td>
								<c:if test="${perf_time.status == 1 }">
										<td>
											待考核
										</td>
										<td>
											${perf_time.status().display }
										</td>
										<td>
											经理
										</td>
								</c:if>
								<c:if test="${perf_time.status != 1 }">
									<c:if test="${perf != null }">
										<td>
											${perf.perfScore }
										</td>
										<td>
											${perf_time.status().display }
										</td>
										<td>
											经理
										</td>
									</c:if>
									<c:if test="${perf == null }">
										<td colspan="3">
											未参与考核。
										</td>
									</c:if>
								</c:if>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<profile:page page="${curpage}" total="${total }" size="${pagesize }" hidewhen="single" href="?curpage={page}"/>
				
				<p style="text-align: center;">
					<a href="/perf/self" class="button">开始自评</a>
				</p>
			</div>
		</div>
		<div class="fc"></div>
	</div>

	<%@include file="/inc/footer.jsp"%>
</body>
</html>