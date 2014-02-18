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
	<title>历史申请列表-升职管理</title>
	<style>
	</style>
	<script type="text/javascript">
	function seePromotionList(perfTimeId){
		if(!perfTimeId){
			return;
		}
		$('#perfTimeIdForList').val(perfTimeId);			
		$('#promotionListForm').attr('action','/admin/promotion/list').submit();
	}
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
					<c:forEach var="year" items="${yearSet}" varStatus="status">
						<tr class="user-item">
							<c:forEach var="perfTimeItem" items="${perfMap[year]}" varStatus="perfTimeStatus">
								<td>
									<a href="javascript:;" onclick="seePromotionList('${perfTimeItem.id}')"><strong>${perfTimeItem.perfTitle }</strong></a>
								</td>
							</c:forEach>
						</tr>
					</c:forEach>
				</thead>
			</table>
			<form method="GET" id="promotionListForm">
				<input id="perfTimeIdForList" type="hidden" name="perftimeid"/>
			</form>
			<profile:page page="${curpage}" total="${total}" href="?curpage={page}"/>
		</div>
		<div class="fc"></div>
	</div>
	
	<%@include file="/inc/footer.jsp" %>
</body>
</html>