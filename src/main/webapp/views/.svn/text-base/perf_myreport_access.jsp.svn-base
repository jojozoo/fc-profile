<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.renren.com/profile" prefix="profile"%>
<%@ taglib uri="http://www.renren.com/profile/functions" prefix="profilefn"%>

<!DOCTYPE html>
<html>
<head>
<%@include file="/inc/head.jsp" %>
	<title>我的汇报人评价</title>
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
					<h2>我的汇报人评价</h2>
			</div>
			
		  <c:if test="${empty userList }">
				<div class="message-warning">对不起!您没有下属信息!</div>
			</c:if>
			
			<c:if test="${not empty userList }">
			<table class="user-full-table">
						<tbody>
							<c:forEach var="user" items="${userList}" varStatus="status">
								<tr>
									<td class="user-headpic">
										<a href="/info/${user.user.id }" target="_blank">
											<img width="50" height="50" src="${user.user.headPic()}">
										</a>
									</td>
									<td class="user-info">
										<a href="/info/${user.user.id }" target="_blank">
											${user.user.name }
										</a>
									</td>
									<td>
										${user.user.jobTitle}
									</td>
									<td>
										<c:if test="${user.status == 1 }">
											未开始
										</c:if>
										<c:if test="${user.status == 2 }">
											<a href="/perf/peer/${user.user.id}">开始</a>
										</c:if>
										<c:if test="${user.status == 3 }">
											<a href="/perf/peer/${user.user.id}">编辑</a>
										</c:if>
										<c:if test="${user.status == 4 }">
											<a href="/perf/peer/${user.user.id}">查看</a>
										</c:if>
									</td>				
							  </tr>
						   </c:forEach>
						</tbody>
					</table> 
		    </c:if>
		</div>
		<div class="fc"></div>
	</div>
	
	<%@include file="/inc/footer.jsp" %>
	<script type="text/javascript">

	</script>
</body>
</html>