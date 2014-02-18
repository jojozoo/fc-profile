<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.renren.com/profile" prefix="profile"%>
<%@ taglib uri="http://www.renren.com/profile/functions" prefix="profilefn"%>

<c:set var="nav_menu" value="kpi_my"></c:set>

<!DOCTYPE html>
<html>
<head>
<%@include file="/inc/head.jsp" %>
<%@include file="/inc/tiny_mce.jsp" %>
	<title>KPI</title>
	<style>
	</style>
	<script type="text/javascript">
	var ajax_kpiId = '0';
	function saveKpi(kpiId,action){
		if(!kpiId){
			return;
		}
		if(kpiId != '0'){
			ajax_kpiId = kpiId;
		}
		if(action=='submit'){
			PU.confirmDialog('你确定提交KPI吗？','警告',function(){
				$('#actionStatus').val('submit');
				$('#saveKpiForm').attr('action','/kpi/save/'+ajax_kpiId).submit();
			});
		}
		if(action=='save'){
			$('#actionStatus').val('save');			
			$('#saveKpiForm').attr('action','/kpi/save/'+ajax_kpiId).submit();
		}
	}

	jQuery(function($){
		$('#saveKpiForm').ajaxform('option',{
			success:function(data){
				ajax_kpiId = data ? data['savedKpiId'] : 0;
			}
		});
	});
	function cancel(){
		PU.confirmDialog('你确定取消编辑KPI吗？','警告',function(){
			$('#cancelForm').submit();
		});
	}
	var pageTinymceOptions = {
			// Name, URL, Description
			template_templates :[
			         				{
					         			title:"KPI模板", 
					         			src:"/static/template/kpi_templates.html",
					         			description: "kpi_template"
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
					<h2>我的KPI</h2>
			</div>
			<c:if test="${showEditer != 'false'}">
				<div class="content-box">
					<form method="POST" id="saveKpiForm" node-type="ajax-form">
						<c:choose>
							<c:when test="${empty currentQTitle}">
								<c:set var="kpiTitle" value="${kpi.title}"></c:set>
							</c:when>
							<c:otherwise>
								<c:set var="kpiTitle" value="${currentQTitle}"></c:set>
							</c:otherwise>
						</c:choose>
						<p>
							KPI标题:
							<input type="text" name="kpititle" value="${kpiTitle}"/>
						</p>
						<c:if test="${empty kpi.content}">
							<textarea class="tinymce" name="content">
								<p><strong>2012年 Q1</strong></p><p><span>1、实施双机房方案。（70%）（此处写你对于你参与的项目1所做的贡献，以及该项目权重）</span></p><p><span>2、优化XOA。（30%）（此处写你对于你参与的项目2所做的贡献，以及该项目权重）</span></p>
							</textarea><br>
						</c:if>
						<c:if test="${not empty kpi.content}">
							<textarea class="tinymce" name="content">${kpi.content}</textarea><br>
						</c:if>
						<span node-type="field-message" message-for="content"></span>
						<input type="hidden" name="status" id="actionStatus"/>
						<input type="hidden" name="quartertime" value="${quarterTime}"/>
						<input type="button" onclick="saveKpi('${kpi.id+0}','submit')" value="确认"/>
						<c:if test="${kpi.status == '0'}">
							<input type="button" value="保存" onclick="saveKpi('${kpi.id+0}','save')"/>
						</c:if>
						<input type="button"  onclick = "cancel()" value="取消"/>
							<span node-type="form-message"></span><br/>
					</form>
					<form method="GET" action="/kpi/my/view" id="cancelForm"></form>	
				</div>
			</c:if>			
			<div class="kpi-list">
				<c:forEach var="kpiItem" items="${kpiList}" varStatus="status">
					<div class="kpi-item">
						<div class="sub-title">
							<strong>${kpiItem.title}</strong>
							<c:if test="${status.index == 0}">
								<span class="title-action">
									<a href="/kpi/edit/${kpiItem.id+0}" class="action">编辑  </a>
								</span>
							</c:if>
						</div>
						<div class="content-box">
							<div class="mce-content">
								${kpiItem.content}
							</div>
						</div>
					</div>
				</c:forEach>
			</div>
			<profile:page page="${curpage}" total="${total}" href="?curpage={page}"/>
		</div>
		<div class="fc"></div>
	</div>
	
	<%@include file="/inc/footer.jsp" %>
</body>
</html>