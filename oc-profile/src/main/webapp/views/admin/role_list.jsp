<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tld/oc-profile.tld" prefix="profile"%>
<%@ taglib uri="/WEB-INF/tld/oc-profile-functions.tld" prefix="profilefn"%>

<c:set var="nav_menu" value="admin_role"></c:set>

<!DOCTYPE html>
<html>
<head>
	<%@include file="/inc/head.jsp" %>
	<title>角色管理- ${_siteName }</title>
	<style>
	</style>
	<script type="text/javascript">
		function deleteRole(roleId,roleName){
			if(!roleId){
				return;
			}
			PU.confirmDialog('你确定删除角色"'+roleName+'"吗？','警告',function(){
				$('#delete_form').attr('action','/admin/roles/delete/'+roleId).submit();
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
					<div class="content-action">
						<a class="button" href="/admin/roles/create">创建角色</a>
					</div>
					<h2>角色</h2>
			</div>
			<div class="messages-box">
				<%@include file="/inc/messages.jsp" %>
			</div>
			
			<div class="content-box">
				<c:if test="${empty roles}">
					没有数据。
				</c:if>
				<c:if test="${not empty roles}">
					<table class="common-table">
					  <tbody>
					    <c:forEach var="role" items="${roles}">
							<tr>
								<td><a href="/admin/roles/edit/${role.id+0}">${role.name}</a></td>
								<td><a href="javascript:void(0);" onclick="javascript:deleteRole(${role.id+0},'${role.name}');">删除</a></td>
							</tr>
						</c:forEach>
					  </tbody>
					</table>
				</c:if>
			</div>
			
			<form id="delete_form" action="" method="post">	</form>
		</div>
		<%@include file="/inc/right-nav.jsp" %>
		<div class="fc"></div>
	</div>
	
	<%@include file="/inc/footer.jsp" %>
</body>
</html>