<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="nav_menu" value="perf_self"></c:set>

<!DOCTYPE html>

<head>
<%@include file="/inc/head.jsp" %>
	<title>我的自评-Perf</title>
	<style>
	</style>
	<script type="text/javascript">
	function saveForm(){
		$('#selfPerfStatus').val('save');
		$('#perfSelfForm').attr('action','/perf/self/save/save').submit();
	}
	function submitForm(promotionManageId){
		PU.confirmDialog('你确定提交本次自评吗？','警告',function(){
			$('#selfPerfStatus').val('submit');
			$('#perfSelfForm').attr('action','/perf/self/save/submit').submit();
		});
	}
	function cancel(){
		PU.confirmDialog('你确定离开页面吗？','警告',function(){
			$('#cancelForm').submit();
		});
	};
	function addProject(){
		jQuery('#project-list').append("<div class='content-spliter'></div>"+jQuery('#empty_project').html().replace(/`no`/g,$('#project-list .project').size() ));
	}
	</script>
</head>
<body>
	<%@include file="/inc/header.jsp" %>
	
	<div id="main">
		<%@include file="/inc/left-nav.jsp" %>
		<div id="content">
			<div class="content-title">
					<h2>${user.name}的自评-${perfTime.perfTitle}</h2>
			</div>
			
			<form name="perfSelfForm"  id="perfSelfForm" node-type="ajax-form">
				<div class="sub-title">
					项目描述
					<span class="title-action">
						<a class="action" href="javascript:void(0);" onclick="addProject();">增加项目</a>
					</span>
				</div>
				
				<c:set var="_empty_project" scope="request">
					<div class="project">
						<p>
							<label class="field-label">项目名称</label><input type="text" name="projecttitles" class="middle-input"/>
							<span node-type="field-message" message-for="projecttitles`no`"></span>
						</p>
						<p>
							<label class="field-label field-top">项目内容</label><textarea name="projectcontents"  class="middle-input"></textarea>
							<span node-type="field-message" message-for="projectcontents`no`"></span>
						</p>
						<p>
							<label class="field-label">角色</label><input type="text" name="projectroles"  class="middle-input"/>
							<span node-type="field-message" message-for="projectroles`no`"></span>
						</p>
						<p>
						<label class="field-label">权重</label>
							<select name="projectweights" >
								<c:forEach var="_weight" items="${_project_weight_array}" varStatus="_weigthStatus">
									<option value="${_weight.id}" <c:if test="${_weigthStatus.index==0}">selected="selected"</c:if>>${_weight.display}</option>
								</c:forEach>
							</select>
						</p>
						<input type="hidden" name="projectids" value="0"/>
					</div>
				</c:set>
				
				<div id="project-list" class="content-box">
					<c:if test="${empty userProjects}">
						<% out.append(((String)request.getAttribute("_empty_project")).replace("`no`","0"));  %>
					</c:if>
					<c:if test="${not empty userProjects}">
						<c:forEach var="projectItem" items="${userProjects}" varStatus="status">
							<c:if test="${status.index != 0 }">
								<div class="content-spliter"></div>
							</c:if>
							<div class="project">
								<p>
									<label class="field-label">项目名称</label>
									<input type="text" name="projecttitles" class="middle-input" value="${projectItem.projectName}"/>
									<span node-type="field-message" message-for="projecttitles${status.index}"></span>
								</p>
								<p>
									<label class="field-label field-top">项目内容</label>
									<textarea name="projectcontents"  class="middle-input">${projectItem.projectContent}</textarea>
									<span node-type="field-message" message-for="projectcontents${status.index}"></span>
								</p>
								<p>
									<label class="field-label">角色</label><input type="text" name="projectroles"  class="middle-input" value="${projectItem.role}" />
									<span node-type="field-message" message-for="projectroles${status.index}"></span>
								</p>
								<p>
									<label class="field-label">权重</label>
									<select name="projectweights">
										<c:forEach var="_weight" items="${_project_weight_array}" varStatus="_weigthStatus">
											<option value="${_weight.id}" <c:if test="${_weigthStatus.index==projectItem.weight}">selected="selected"</c:if>>${_weight.display}</option>
										</c:forEach>
									</select>
								</p>
								<input type="hidden" name="projectids" value="${projectItem.id}"/>
							</div>
						</c:forEach>
					</c:if>
				</div>
				
				<div class="sub-title">
					<h2>你的优点<span node-type="field-message" message-for="advantage"></span></h2>
				</div>
				<div class="content-box">
					<textarea name="advantage"  class="full-input">${userSelfPerf.advantage}</textarea>
				</div>
				
				
				<div class="sub-title">
					<h2>你的缺点<span node-type="field-message" message-for="disadvantage"></span></h2>
				</div>
				<div class="content-box">
					<textarea name="disadvantage"  class="full-input">${userSelfPerf.disadvantage}</textarea>
				</div>
				
				
				<c:if test="${perfTime.isPromotion }">
					<div class="sub-title">
						<h2>升职<span node-type="field-message" message-for="promotionreason"></span></h2>
					</div>
					<div class="content-box">
						<p><label><input type="radio" name="ispromotion" value="true" <c:if test="${userSelfPerf.isPromotion == 'true'}"> checked="checked"</c:if>/>是，我要申请升职</label></p>
						<p><label><input type="radio" name="ispromotion" value="false" <c:if test="${userSelfPerf.isPromotion == 'false'}"> checked="checked"</c:if> />不，我还要继续准备一下</label></p>
					</div>
					
					<div class="sub-title">
						<h2>升职理由</h2>
					</div>
					
					<div class="content-box">
						<textarea name="promotionreason">${userSelfPerf.promotionReason}</textarea>
					</div>
				</c:if>
				
				<div class="content-box">
					<input type="hidden" name="perftimeid" value="${perfTime.id}"/>
					<input type="hidden" name="status" id="selfPerfStatus"/>
					<p>
						<c:if test="${userSelfPerf.status != '1'}">
							<input type="button" value="提交" onclick="submitForm();"/>
							<input type="button"  value="保存" onclick="saveForm();"/>
						</c:if>
						<input type="button" value="取消" onclick="cancel();"/>
						<span node-type="form-message"></span>
					</p>
				</div>
			</form>
			<form id="cancelForm" method="GET" action="/perf"></form>
			<div id="empty_project" style="display: none" >
						${_empty_project }
			</div>
		</div>
		<div class="fc"></div>
	</div>
	
	<%@include file="/inc/footer.jsp" %>
</body>
</html>