<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.renren.com/profile" prefix="profile"%>
<%@ taglib uri="http://www.renren.com/profile/functions" prefix="profilefn"%>

<c:set var="nav_menu" value="perf_summary_current"></c:set>

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
					<h2>对我的评价汇总</h2>
			</div>
			<div class="content-box">
				<c:if test="${not is_current_perf }">
					<p>	<strong>绩效考核成绩:</strong> ${user_perf.perfScore }</p>
				</c:if>
			</div>
			<div class="sub-title">
				项目描述
			</div>	
			<div class="content-box">
				<c:set var="projects" value="${user_perf.projects() }"></c:set>
				<c:forEach var="project" items="${projects }">
					<p>
						项目：${project.projectName }
					</p>
					<p>
						角色:${project.role }(${project.weight().display})
					</p>
					<p>
						${project.projectContent }
					</p>
					<c:forEach var="peer_project" items="${project.peerPerfProjects() }">
						<p>${peer_project.peerId}:(${peer_project.weight().display}) ${peer_project.content}</p>
					</c:forEach>
					
					<c:set var="manager_project" value="${project.managerPerfProject() }"></c:set>
					<c:if test="${manager_project!=null }">
						<p>[汇报人]${manager_project.peerId}:(${manager_project.weight().display}) ${manager_project.content}</p>
					</c:if>
				</c:forEach>
			</div>
			
			<div class="sub-title">
				优点
			</div>	
			<div class="content-box">
				<p>${user_perf.advantage}</p>
				<c:forEach var="peer_perf" items="${peer_perfs}">
					<p>${peer_perf.peerId}:${peer_perf.advantageComments}</p>
				</c:forEach>
				<c:if test="${manager_perf!=null }">
					<p>[汇报人]${manager_perf.peerId}:${manager_perf.advantageComments}</p>
				</c:if>
			</div>
			
			<div class="sub-title">
				缺点
			</div>	
			<div class="content-box">
				<p>${user_perf.disadvantage}</p>
				<c:forEach var="peer_perf" items="${peer_perfs}">
					<p>${peer_perf.peerId}:${peer_perf.disadvantageComments}</p>
				</c:forEach>
				<c:if test="${manager_perf!=null }">
					<p>[汇报人]${manager_perf.peerId}:${manager_perf.disadvantageComments}</p>
				</c:if>
			</div>
			
			<c:if test="${user_perf.isPromotion }">
				<div class="sub-title">
					是否支持评价对象升职
				</div>	
				<div class="content-box">
					<c:forEach var="peer_perf" items="${peer_perfs}">
						<p>
							${peer_perf.peerId}
							${peer_perf.isPromotion?'支持':'不支持'}
						</p>
					</c:forEach>
					<c:if test="${manager_perf!=null }">
						<p>
							${manager_perf.peerId}
							${manager_perf.isPromotion?'支持':'不支持'}
						</p>
					</c:if>
				</div>
				
				<div class="sub-title">
					升职理由
				</div>	
				<div class="content-box">
					${user_perf.promotionReason }
				</div>
				
				<div class="sub-title">
					经理考核
				</div>	
				<div class="content-box">
					${manager_perf.promotionComment }
				</div>
			</c:if>
		</div>
		<div class="fc"></div>
	</div>

	<%@include file="/inc/footer.jsp"%>
</body>
</html>