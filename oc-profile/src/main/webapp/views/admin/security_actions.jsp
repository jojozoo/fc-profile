<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tld/oc-profile.tld" prefix="profile"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/tld/oc-profile-functions.tld" prefix="profilefn"%>

<c:set var="nav_menu" value="admin_security_actions"></c:set>

<!DOCTYPE html>
<html>
<head>
	<%@include file="/inc/head.jsp" %>
	<title>权限管理- ${_siteName }</title>
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
			<div class="messages-box">
				<%@include file="/inc/messages.jsp" %>
			</div>
			
			<div class="content-box">
				<c:if test="${empty category_action_map}">
					没有数据。
				</c:if>
				<c:if test="${not empty category_action_map}">
					<form method="post">
						<table class="data-list">
							<thead>
								<tr>
									<th>组</th>
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
								<c:set var="_row" value="0"></c:set>
								<c:forEach var="category" items="${categories}" varStatus="status">	
									<c:set var="_actions" value="${category_action_map[category]}"></c:set>
									<c:set var="_actions_length" value="${_actions.size()+0}"></c:set>
									<c:forEach var="action" items="${_actions}" varStatus="_status">
										<c:set var="_is_first" value="${_status.index == 0 }"></c:set>
										<c:set var="_row" value="${_row+1 }"></c:set>
										<c:set var="roleSet" value="${action_roleset_map[action.id]}"></c:set>
										<c:set var="relationSet" value="${action_relationset_map[action.id]}"></c:set>
										
										<input type="hidden" name="roles" value="${action.id}:0"><%--保证能提交一个 --%>
										<input type="hidden" name="relations" value="${action.id}:0"><%--保证能提交一个 --%>
										<tr class="${_row % 2 == 0 ? 'even' :'odd' }">
											<c:if test="${_is_first }"><%--需要显示组名 --%>
										 		<td rowspan="${_actions_length}">
										 			${category.name}
										 		</td>
									 		</c:if>
									 		<td class="caption">${action.name}</td>
										  <c:forEach var="role" items="${roles}">
										  	<td class="checkbox center">
										  		<input title="${action.name},${role.name}" type="checkbox" name="roles" value="${action.id}:${role.id }" ${roleSet!=null && profilefn:contains(roleSet,role.id) ? 'checked="checked"' : ''}>
										  	</td>
										  </c:forEach>
										  <c:forEach var="relation" items="${relations}">
										  	<td class="checkbox center">
										  		<input title="${action.name},${relation.name}" type="checkbox" name="relations" value="${action.id}:${relation.id }" ${relationSet!=null && profilefn:contains(relationSet,relation.id) ? 'checked="checked"' : ''}>
										  	</td>
										  </c:forEach>
										</tr>
									</c:forEach>
								</c:forEach>
							</tbody>
						</table>
						<input type="submit" value="保存">
					</form>
				</c:if>
			</div>
		</div>
		<%@include file="/inc/right-nav.jsp" %>
		<div class="fc"></div>
	</div>
	
	<%@include file="/inc/footer.jsp" %>
</body>
</html>