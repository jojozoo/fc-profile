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
	<title>申请升职员工列表-升职管理</title>
	<style>
	</style>
	<script type="text/javascript">
	</script>
</head>
<body>
	<%@include file="/inc/header.jsp" %>
	
	<div id="main">
		<%@include file="/inc/left-nav.jsp" %>
		<div id="content">
			<div class="content-title">
						<h2>升职管理</h2>
			</div>
			<table class="user-full-table">
				<thead>
					<c:forEach var="perf" items="${userPerfs}" varStatus="status">
						<tr class="user-item">
							<td class="user-headpic">
								<a href="/info/${perf.userId }" target="_blank">
									<img width="50" height="50" src="${empty userMap[perf.userId].headPic ? 'http://s.xnimg.cn/actimg/12pingji/img/img.png':userMap[perf.userId].headPic}">
								</a>
							</td>
							<td class="user-info">
								<a href="/info/${perf.userId }" target="_blank">
									${userMap[perf.userId].name }(${userMap[perf.userId].number})
								</a>
								<br/>
								${profilefn:escapeHtml(userMap[perf.userId].email)}
							</td>
							<td>
								${userMap[perf.userId].jobTitle}
							</td>
							<td>
								${userMap[perf.userId].departmentId }
							</td>
							<td class="actionlist">
								<c:choose>
									<c:when test="${isEnd == 'end'}"><a href="/admin/promotion/view/${perf.id+0}">查看</a></c:when>
									<c:otherwise>
										<c:if test="${empty promotionMap[perf.id]}"><a href="/promotion/edit/${perf.id+0}">反馈评定结果</a></c:if>
										<c:if test="${not empty promotionMap[perf.id]}"><a href="/promotion/edit/${perf.id+0}">查看/编辑</a></c:if>
									</c:otherwise>
								 </c:choose>
							</td>
						</tr>
					</c:forEach>
				</thead>
			</table>
			
			<profile:page page="${curpage}" total="${total}" href="?curpage={page}"/>
		</div>
		<div class="fc"></div>
	</div>
	
	<%@include file="/inc/footer.jsp" %>
</body>
</html>