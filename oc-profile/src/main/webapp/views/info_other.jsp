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
	<title>个人资料 - ${_owner.name} - ${_siteName }</title>
</head>
<body>
	<%@include file="/inc/header.jsp" %>
	<div id="main">
		<%@include file="/inc/left-nav.jsp" %>
		<div id="content" class="reset-list-css">
			<div class="content-title">
					<h2>${user.name}</h2>
			</div>
			<div class="messages-box">
				<%@include file="/inc/messages.jsp" %>
			</div>
			<div class="user-info-box content-box">
				<div class="user-headpic">
					<a href="/info/${user.id}">
						<img alt="${user.name}" width="200px" src="${user.mainUrl}">
					</a>
				</div>
				<div class="user-overview">
					<table>
						<tbody>
							<tr>
								<th>邮&nbsp &nbsp &nbsp &nbsp箱:</th>
								<td>${user.email}</td>
							</tr>
							<tr>
								<th>部&nbsp &nbsp &nbsp &nbsp门:</th>
								<td>${department.departmentName}</td>
							</tr>
							<tr>
								<th>岗&nbsp &nbsp &nbsp &nbsp位:</th>
								<td>
									${user.jobTitle }
									${user.showLevel == 0? '' : user.level}
								</td>
							</tr>
							<tr>
								<th>工&nbsp &nbsp &nbsp &nbsp号:</th>
								<td>${user.number}</td>
							</tr>
							<tr>
								<th>业务名称:</th>
								<td>${businessTagNames}</td>
							</tr>
						
							<c:if test="${user.showLevel != 0 || profilefn:access('view_info_level')}">
								<tr>
									<th>级&nbsp &nbsp &nbsp &nbsp别:</th>
									<td>
										<profile:default def="<i>未评级</i>">${user.level}</profile:default>
									</td>
								</tr>
							</c:if>
							<tr>
								<th>经&nbsp &nbsp &nbsp &nbsp理:</th>
								<td>
									<a href="/info/${managerUser.id}">${managerUser.name} </a>
								</td>
							</tr>
							<tr>
								<th>毕业学校:</th>
								<td>${userProfile.graduateSchool}</td>
							</tr>
							<tr>
								<th>生&nbsp &nbsp &nbsp &nbsp日:</th>
								<td>${userProfile.birthday}</td>
							</tr>
							<tr>
								<th>QQ&nbsp&nbsp号码:</th>
								<td>
									 ${userProfile.qq} &nbsp
									<c:if test="${userProfile.qq > 0}">
										<a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=${userProfile.qq }&site=qq&menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=2:${userProfile.qq }:44" alt="点击这里给我发消息" title="点击这里给我发消息"></a>
									</c:if>
									
								</td>
							</tr>
							<tr>
								<th>手机号码:</th>
								<td>${userProfile.mobile}</td>
							</tr>
							<tr>
								<th>分机号码:</th>
								<td>${userProfile.extNumber}</td>
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
					向他汇报的人
				</div>
				<div class="content-box">
					<table class="user-full-table">
						<tbody>
							<c:forEach var="item" items="${myFollows}" varStatus="status">
								<tr>
									<td style="width: 22px;">
									<c:if test="${item.testManager }">
											<a href="/info/${item.user.id }">
												<img src="/static/images/add.png" >
										    </a>
									</c:if>
										</td>
									<td class="user-headpic">
										<a href="/info/${item.user.id }">
											<img width="50px" src="${item.user.tinyUrl}">
										</a>
									</td>
									<td>
										<p>
											<a href="/info/${item.user.id }">
												${item.user.name}
											</a>
										</p>
										<p>
												<c:if test="${item.user.userProfile.qq > 0}">
													<a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=${item.user.userProfile.qq }&site=qq&menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=2:${item.user.userProfile.qq }:44" alt="点击这里给我发消息" title="点击这里给我发消息"></a>
												</c:if>
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
  		<%@include file="/inc/right-nav.jsp" %>
		<div class="fc"></div>
	</div>
	
	<%@include file="/inc/footer.jsp" %>
</body>
</html>
