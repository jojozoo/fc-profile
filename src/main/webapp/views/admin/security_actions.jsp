<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.renren.com/profile" prefix="profile"%>
<%@ taglib uri="http://www.renren.com/profile/functions" prefix="profilefn"%>

<c:set var="nav_menu" value="admin_security_actions"></c:set>

<!DOCTYPE html>
<html>
<head>
	<%@include file="/inc/head.jsp" %>
	<title></title>
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
					<h2>功能点</h2>
			</div>
			<c:if test="${empty actions}">
				没有数据。
			</c:if>
			<c:if test="${not empty actions}">
				<form method="post">
					<table class="data-list">
						<thead>
							<tr>
								<th>功能点</th>
							  <c:forEach var="role" items="${roles}">
									<th>${role.name}</th>
							  </c:forEach>
							  <c:forEach var="relation" items="${relations}">
									<th>${relation.name}</th>
							  </c:forEach>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="action" items="${actions}" varStatus="status">	
								<input type="hidden" name="roles" value="${action.id}:0"><%--保证能提交一个 --%>
								<input type="hidden" name="relations" value="${action.id}:0"><%--保证能提交一个 --%>
								<c:set var="roleSet" value="${action_roleset_map[action.id]}"></c:set>
								<c:set var="relationSet" value="${action_relationset_map[action.id]}"></c:set>
							 	<tr class="${status.index % 2 == 0 ? 'even' :'odd' }">
							 		<td class="caption">${action.name}</td>
								  <c:forEach var="role" items="${roles}">
								  	<td class="checkbox center">
								  		<input title="${action.name},${role.name}" type="checkbox" name="roles" value="${action.id}:${role.id }" ${roleSet!=null && roleSet.contains(role.id) ? 'checked="checked"' : ''}>
								  	</td>
								  </c:forEach>
								  <c:forEach var="relation" items="${relations}">
								  	<td class="checkbox center">
								  		<input title="${action.name},${relation.name}" type="checkbox" name="relations" value="${action.id}:${relation.id }" ${relationSet!=null && relationSet.contains(relation.id) ? 'checked="checked"' : ''}>
								  	</td>
								  </c:forEach>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<input type="submit" value="保存">
				</form>
			</c:if>
		</div>
		<div class="fc"></div>
	</div>
	
	<%@include file="/inc/footer.jsp" %>
</body>
</html>