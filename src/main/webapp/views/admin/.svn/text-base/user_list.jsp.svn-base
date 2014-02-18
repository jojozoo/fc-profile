<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.renren.com/profile" prefix="profile"%>
<%@ taglib uri="http://www.renren.com/profile/functions" prefix="profilefn"%>

<c:set var="nav_menu" value="admin_user"></c:set>

<!DOCTYPE html>
<html>
<head>
	<%@include file="/inc/head.jsp" %>
	<title>员工列表-员工管理</title>
	<style>
	</style>
	<script type="text/javascript">
	function deleteUser(userId,userName){
		if(!userId){
			return;
		}
		PU.confirmDialog('你确定删除用户"'+userName+'"吗？','警告',function(){
			$('#delete_form').attr('action','/admin/user/delete/'+userId).submit();
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
							<a class="button" href="/admin/user/add">创建用户</a>
						</div>
						<h2>用户</h2>
			</div>
			<form id="delete_form" action="" method="POST"></form>
			
			<table class="user-full-table">
				<thead>
					<c:forEach var="item" items="${userList}" varStatus="status">
						<tr class="user-item">
							<td class="user-headpic">
								<a href="/info/${item.id }" target="_blank">
									<img width="50" height="50" src="${empty user.headPic ? 'http://s.xnimg.cn/actimg/12pingji/img/img.png':user.headPic}">
								</a>
							</td>
							<td class="user-info">
								<a href="/info/${item.id }" target="_blank">
									${item.name }(${item.number})
								</a>
								<br/>
								${profilefn:escapeHtml(item.email)}
							</td>
							<td>
								${item.jobTitle}
							</td>
							<td>
								${item.departmentId }
							</td>
							<td class="actionlist">
								<a href="/admin/user/edit/${item.id+0}">编辑</a> <a href="javascript:;" onclick="deleteUser(${item.id+0},'${item.name}')">删除</a>
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