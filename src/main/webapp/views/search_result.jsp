<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.renren.com/profile" prefix="profile"%>
<%@ taglib uri="http://www.renren.com/profile/functions" prefix="profilefn"%>
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
					<h2>${profilefn:escapeHtml(keyword)}的搜索结果</h2>
			</div>
			<div class="content-box">
				<c:if test="${not empty users }">
					<table class="user-full-table">
							<tbody>
								<c:forEach var="user" items="${users}" varStatus="status">
									<tr>
										<td class="user-headpic">
											<a href="/info/${user.id }">
												<img width="50px" src="${user.headPic() }">
											</a>
										</td>
										<td>
											<p>
												<a href="/info/${user.id }">
													${user.name}
												</a>
											</p>
											<p>
												${user.email}
											</p>
										</td>
										<td>
											${user.jobTitle}
										</td>
										<td>
											${user.deparmentName()}
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
				</c:if>
				<c:if test="${empty users }">
					对不起，没有搜到结果！
				</c:if>
			</div>
		</div>
		<div class="fc"></div>
	</div>

	<%@include file="/inc/footer.jsp"%>
</body>
</html>