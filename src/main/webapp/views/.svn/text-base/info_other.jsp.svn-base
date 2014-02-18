<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="nav_menu" value="info_other"></c:set>
<c:set var="user_resource" value="true"></c:set>


<!DOCTYPE html>
<html>
<head>
	<%@include file="/inc/head.jsp" %>
	<title>个人信息 - ${_owner.name} - ${_siteName }</title>
</head>
<body>
	<%@include file="/inc/header.jsp" %>
	<div id="main">
		<%@include file="/inc/left-nav.jsp" %>
		<div id="content">
			<div class="content-title">
					<h2>${user.name}</h2>
			</div>
			<div class="user-info-box content-box">
				<div class="user-headpic">
					<img alt="${user.name}" width="200" src="${user.headPic()}">
				</div>
				<div class="user-overview">
					<table>
						<tbody>
							<tr>
								<th>邮箱:</th>
								<td>${user.email}</td>
							</tr>
							<tr>
								<th>部门:</th>
								<td>${department.departmentName}</td>
							</tr>
							<tr>
								<th>业务名:</th>
								<td>${businessTagNames}</td>
							</tr>
							<tr>
								<th>职称:</th>
								<td>
									${user.jobTitle }
									${user.showLevel == 0? '':user.level}
								</td>
							</tr>
							<tr>
								<th>经理:</th>
								<td>${managerUser.name}</td>
							</tr>
							<tr>
								<th>QQ:</th>
								<td>${userProfile.qq}</td>
							</tr>
							<tr>
								<th>兴趣爱好:</th>
								<td>${userProfile.hobby}</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="fc"></div>
				<div class="user-detail">
				</div>
			</div>
			
			<c:if test="${not empty myFollows }">
				<div class="sub-title">
					向我汇报的人
				</div>
				<div class="content-box">
					<table class="user-full-table">
						<tbody>
							<c:forEach var="item" items="${myFollows}" varStatus="status">
								<tr>
									<td class="user-headpic">
										<a href="/info/${item.user.id }">
											<img width="50px" src="${empty item.user.headPic ?'/static/images/man_headpic.gif':item.user.headPic }">
										</a>
									</td>
									<td>
										<p>
											<a href="/info/${item.user.id }">
												${item.user.name}
											</a>
										</p>
										<p>
											${item.user.email}
										</p>
									</td>
									<td>
										${item.user.jobTitle}
									</td>
									<td>
										${item.deparmentName}
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</c:if>
		</div>
		<div class="fc"></div>
	</div>
	
	<%@include file="/inc/footer.jsp" %>
</body>
</html>
