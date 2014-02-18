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
<%@include file="/inc/tiny_mce.jsp"%>
	<title>编辑简历</title>
	<style>
	</style>
	<script type="text/javascript">
	function saveResume(){
			PU.confirmDialog('你确定提交简历吗？','警告',function(){
				$('#saveResumeForm').attr('action','/resume/save').submit();
			});
	}
	function cancel(){
		PU.confirmDialog('你确定取消编辑简历吗？','警告',function(){
			$('#cancelForm').submit();
		});
	}

	var pageTinymceOptions = {
			// Name, URL, Description
			template_templates :[
			         				{
										title:"简历模板", 
										src:"/static/template/resume_templates.html",
										description: "resume_template"
									}
					         	],
			//
			__end__:true
			
		};
		
	</script>
</head>
<body>
	<%@include file="/inc/header.jsp" %>
	
	<div id="main">
		<%@include file="/inc/left-nav.jsp" %>
		<div id="content">
			<div class="content-title">
					<h2>个人简历</h2>
			</div>
			<div class="content-box">
				<form method="POST" id="saveResumeForm" node-type="ajax-form">
					<span node-type="field-message" message-for="content"></span>
					<p>
						<c:if test="${empty resume.content}">
							<textarea class="tinymce" name="content">
								<div class="mormal-detail">
									<p><strong>个人概况</strong></p>
										<p><span>姓名，部门，司龄，所属部门，主要负责的业务员，等等</span></p>
									<p><strong>2012年 主要项目经历、业绩</strong></p>
										<p><span>项目1：相册改版，担任负责人，负责主站的架构，blabla</span></p>
									<p><strong>2011年 主要项目经历、业绩</strong></p>
										<p><span>项目1：新鲜事改版，担任负责人，负责主站的架构，blabla</span></p>
										<p><span>项目2：相册改版，担任负责人，负责主站的架构，blabla</span></p>
										<p><span>项目3：相册改版，担任负责人，负责主站的架构，blabla</span></p>
								</div>
							</textarea>
						</c:if>
						<c:if test="${not empty resume.content}">
							<textarea class="tinymce" name="content">${resume.content}</textarea>
						</c:if>
					</p>
					<p>
						<input type="button"  onclick="saveResume()" value="提交"/>
						<input type="button"  onclick = "cancel()" value="取消"/>
						<span node-type="form-message"></span>
					</p>
				</form>	
				<form method="GET" action="/resume/my/view" id="cancelForm"></form>		
			</div>
		</div>
		<div class="fc"></div>
	</div>
	<%@include file="/inc/footer.jsp" %>
</body>
</html>