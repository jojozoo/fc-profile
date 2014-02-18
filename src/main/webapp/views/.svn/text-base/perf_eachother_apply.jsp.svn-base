<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.renren.com/profile" prefix="profile"%>
<%@ taglib uri="http://www.renren.com/profile/functions" prefix="profilefn"%>


<c:set var="nav_menu" value="perf_eachother"></c:set>

<!DOCTYPE html>
<html>
<head>
<%@include file="/inc/head.jsp" %>
<link href="/static/css/jquery.autocomplete.css" rel="stylesheet" type="text/css" />
<script src="/static/js/jquery.autocomplete.js" type="text/javascript"></script>
	<title>我的互评</title>
	<style>
	</style>
	<script type="text/javascript">
	
	function delInviteUser(userId){
		PU.confirmDialog('你确定撤销当前邀请人吗？','提示',function(){
			$('#delInvite').attr('action','/perfApply/del/'+userId).submit();
	    });
	}

	function addInviteUser(){
		$('#addInvite').attr('action','/perfApply/add').submit();
	}


	$(function() {
		$('#keyId').keyup(function(){
			var keywords = $('#keyId').val();
			var data = 'keywords=' + keywords;
			PU.ajaxPost({
				url:"/perfApply/search",
				data:data,
				success:function(html) {
					if(!html.isSuccess)
						alert(html.error);
					else{
						$("#keyId").autocomplete(html.emailList); 
					}
					
				}
			});
			return false;
		});
	});
	</script>
	<style type="text/css">
		.tips {
		font-size:12px;}
   </style>
</head>
<body>
	<%@include file="/inc/header.jsp" %>
	
	<div id="main">
		<%@include file="/inc/left-nav.jsp" %>
		<div id="content">
			<div class="content-title">
					<h2>我的互评</h2>
			</div>
			
			<c:if test="${addStatus ==1}">
		     <div class="message-error"> 您输入的邮件名称为空值!</div>
	        </c:if>
	        <c:if test="${addStatus ==2}">
		     <div class="message-error"> 您邀请的好友不存在或者您的直接主管是不能邀请!</div>
	        </c:if>
	        <c:if test="${addStatus ==3}">
		     <div class="message-error"> 您不能重复邀请好友!</div>
	        </c:if>
	        <c:if test="${addStatus ==4}">
		     <div class="message-error"> 您不能邀请自己!</div>
	        </c:if>
	        <c:if test="${addStatus ==5}">
		     <div class="message-error"> 您邀请的好友数目已经超出最大邀请数!</div>
	        </c:if>
			<form id="delInvite" action="" method="post"></form>
			
			<div class="sub-title">
				我邀请的人
			</div>
			<div class="content-box">
				<c:if test="${not isSelfAccessSumbit}">
			    <div class="message-error">对不起!请您提交自己的自评信息才能邀请你的好友评价!</div>
		    </c:if>
		    <c:if test="${isSelfAccessSumbit}">
			    <form id="addInvite" action="" method="post">	
					<span class="tips">输入邮箱</span>
					<input type="text" id="keyId" class="middle-input" name="keywords" />
					<input type="button" class="confirm-btn" value="添加" onclick="javascript:addInviteUser();"/>
			   		<%--  <input class="textarea-style05" placeholder="邮箱/姓名" name="email"/><input type="button" class="confirm-btn" value="添加" onclick="javascript:addInviteUser();"> --%>
			   	</form>
					<table class="user-full-table">
						<thead>
							<tr>
								<th colspan="2">被邀请人</th>
								<th>职位</th>
								<th></th>
							</tr>
						</thead>
						<tbody>
							<c:if test="${empty  inviteUserDtoList}">
								<tr>
									<td colspan="4">
										你还没有邀请别人
									</td>
								</tr>
							</c:if>
							<c:forEach var="invite" items="${inviteUserDtoList}" varStatus="status">
								<tr>
									<td class="user-headpic">
										<a href="/info/${invite.user.id }" target="_blank">
											<img width="50" height="50" src="${empty invite.user.headPic ? _empty_headpic_url : invite.user.headPic}">
										</a>
									</td>
									<td class="user-info">
										<a href="/info/${invite.user.id }" target="_blank">
											${invite.user.name }
										</a>
									</td>
									<td>
										${invite.user.jobTitle}
									</td>
									<td>
										<c:if test="${invite.status == 1}">
											<span>未接收</span>
											<a href="javascript:void(0);" class="button" onclick="javascript:delInviteUser(${invite.user.id});">撤销</a>
										</c:if>	
										<c:if test="${invite.status == 2}">
											<span>未提交</span>
										</c:if>			
										<c:if test="${invite.status == 3}">
											<span>已提交</span>
										</c:if>
									</td>				
							  </tr>
						   </c:forEach>
						</tbody>
					</table> 
				</c:if>
			</div>
			
			
			<div class="sub-title">
				我的互评任务
			</div>
			<div class="content-box">
				<table class="user-full-table">
						<thead>
							<tr>
								<th colspan="2">被邀请人</th>
								<th>职位</th>
								<th></th>
							</tr>
						</thead>
						<tbody>
							<c:if test="${empty  assessUserDtoList}">
								<tr>
									<td colspan="4">
										你没有互评任务
									</td>
								</tr>
							</c:if>
							<c:forEach var="access" items="${assessUserDtoList}" varStatus="status">
								<tr>
									<td class="user-headpic">
										<a href="/info/${access.user.id }" target="_blank">
											<img width="50" height="50" src="${empty access.user.headPic ? _empty_headpic_url : access.user.headPic}">
										</a>
									</td>
									<td class="user-info">
										<a href="/info/${access.user.id }" target="_blank">
											${access.user.name }
										</a>
									</td>
									<td>
										${access.user.jobTitle}
									</td>
									<td>
										<c:if test="${access.status == 1}">
											<a class="button" href="/perf/peer/${access.user.id }">开始</a>
										</c:if>
										<c:if test="${access.status == 2}">
											<a class="button" href="/perf/peer/${access.user.id}">查看</a>
											<a class="button" href="/perf/peer/${access.user.id}">编辑</a>
										</c:if>
										<c:if test="${access.status == 3}">
											已提交
										</c:if>
									</td>				
							  </tr>
						   </c:forEach>
						</tbody>
					</table> 
			</div>
		</div>
		<div class="fc"></div>
	</div>
	
	<%@include file="/inc/footer.jsp" %>
	<script type="text/javascript">

	</script>
</body>
</html>