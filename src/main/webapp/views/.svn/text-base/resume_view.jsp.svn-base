<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.renren.com/profile" prefix="profile"%>
<%@ taglib uri="http://www.renren.com/profile/functions" prefix="profilefn"%>

<c:set var="nav_menu" value="resume_view"></c:set>

<!DOCTYPE html>
<html>
<head>
<%@include file="/inc/head.jsp" %>
	<title>KPI</title>
	<style>
	</style>
	<script type="text/javascript">
	function editResume(){
		$('#editResumeForm').attr('action','/resume/edit').submit();
	}
	</script>
</head>
<body onload="check();">
	<%@include file="/inc/header.jsp" %>
	
	<div id="main">
		<%@include file="/inc/left-nav.jsp" %>
		<div id="content">
			<div class="content-title">
				<div class="content-action">
					<a href="javascript:;" class="button" onclick = "editResume()">编辑</a>
				</div>
				<h2>个人简历</h2>
			</div>
			<div class="content-box">
					<div class="mce-content">
						${resume.content}
					</div>
					<c:if test="${empty resume.content }">
						<a href="javascript:;" class="button" onclick = "editResume()">点击此处</a>创建简历。
					</c:if>
				</p>
				<form method="POST" id="editResumeForm"></form>
			</div>
		</div>
		<div class="fc"></div>
	</div>
	
	<%@include file="/inc/footer.jsp" %>
	<script type="text/javascript">
	function check(){
		var errorCode = '${errorCode}';
		var msg = '${msg}';
		if(errorCode == '0'){
			var inputs = document.getElementsByTagName('input');
			for(var i=0;i<inputs.length;i++){
					inputs[i].value='';
			}
		}
		if(msg.length > 1)
			alert(msg);
		}
	</script>
</body>
</html>