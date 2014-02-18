<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.renren.com/profile" prefix="profile"%>
<%@ taglib uri="http://www.renren.com/profile/functions" prefix="profilefn"%>

<c:set var="nav_menu" value="admin_role"></c:set>

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
					<h2><a href="/admin/roles">角色</a> &#187; 创建角色</h2>
			</div>
			<form node-type="ajax-form">
				<p>
					<label class="field-label"><span class="required">*</span>角色名称</label>
					<input type="text" class="field-input" name="name" tip="请输入角色的名称" required="required">
					<span node-type="field-message" message-for="name"></span>
				</p>
				<h3>权限</h3>
				<div id="permissions">
					<c:forEach var="actionCategory" items="${actionCategories }">
						<c:set var="actionList" value="${actionCategoryMap[actionCategory] }"></c:set>
						<c:if test="${not empty actionList}">
							<fieldset class="tabular">
								<legend>${actionCategory.name}</legend>
								<c:forEach var="action" items="${actionList }">
									<label class="floating">
										<input type="checkbox" name="actions" value="${action.id}">
										${action.name }
									</label>
								</c:forEach>
							</fieldset>
						</c:if>
					</c:forEach>
				</div> 
				<p>
					<input type="submit" value="创建">	<span node-type="form-message"></span>
				</p>
			</form>
		</div>
		<div class="fc"></div>
	</div>
	
	<%@include file="/inc/footer.jsp" %>
</body>
</html>