<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="string"%>
<%@ taglib uri="/WEB-INF/tld/oc-profile.tld" prefix="profile"%>
<%@ taglib uri="/WEB-INF/tld/oc-profile-functions.tld" prefix="profilefn"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/inc/head.jsp"%>
<title>${department.departmentName}部门 - ${_user.name} - ${_siteName }</title>
<style>
</style>
<script type="text/javascript">

$(document).ready(function(){

	 var select1 = $("#select1");
	    select1.change(function(){//添加onchange事件        
	        $("#select2").CascadingDropDown("", 'department/45', { textfield: 'departmentName', valuefiled: 'departmentId' });
	    });
	
	 var select2 = $("#select2");
    select2.change(function(){//添加onchange事件        
    	select2.empty();//清空下级下拉框        
        $("#select3").CascadingDropDown("#select1", 'department/$("#select1").val()', { textfield: 'departmentName', valuefiled: 'departmentId' });
       
    });

    var select3 = $("#select3");
    select3.change(function(){//添加onchange事件        
    	select3.empty();//清空下级下拉框        
       $("#select4").CascadingDropDown("#select2", 'department/$("#select2").val()', { textfield: 'departmentName', valuefiled: 'departmentId' });
     
    });

  var select4 = $("#select4");
    select4.change(function(){//添加onchange事件        
    	//select4.empty();//清空下级下拉框        
        //$("#select4").CascadingDropDown("#select3", 'department/$("#select3").val()', { textfield: 'departmentName', valuefiled: 'departmentId' });
      
    });
})
</script>
</head>
<body>
	<%@include file="/inc/header.jsp"%>

	<div id="main" class="reset-list-css">
		<%@include file="/inc/left-nav.jsp"%>
		<div id="content" class="search-result">
			<div class="content-title">
					<h2>${department.departmentName}部门</h2>
			</div>
			<div class="messages-box">
				<%@include file="/inc/messages.jsp" %>
			</div>
			<div class="content-box">
				<c:if test="${not empty users }">
					<table class="user-full-table">
							<tbody>
								<c:forEach var="user" items="${users}" varStatus="status">
									<c:set var="businesses" value="${businessesMap[user.id] }"></c:set>
									<tr>
										<td style="width: 22px;">
											<c:if test="${user.subordinateCount > 0}">
												 <a href="/info/${user.id }">
													<img src="/static/images/add.png" >
												 </a>
											</c:if>
										</td>
										<td class="user-headpic">
											<a href="/info/${user.id }">
												<img width="50px" src="${user.tinyUrl }">
											</a>
										</td>
										<td>
											<p>
												<a href="/info/${user.id }">
													${user.name}
												</a>
											</p>
											<p>
												<c:if test="${user.userProfile.qq > 0}">
													<a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=${item.user.userProfile.qq }&site=qq&menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=2:${item.user.userProfile.qq }:44" alt="点击这里给我发消息" title="点击这里给我发消息"></a>
												</c:if>
											</p>
											<p>
												${user.shortEmail}
											</p>
										</td>
										<td>
											${user.jobTitle}
										</td>
										<td>
											<profile:join items="${businesses}" separator=","/>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<c:url var="page_url" value="">
							<c:param name="keyword" value="${profilefn:escapeHtml(keyword)}"></c:param>
						</c:url>
						<profile:page page="${curpage }" total="${total }" href="${page_url}&curpage={page}"/>
				</c:if>
				<c:if test="${empty users }">
					<i>没有任何员工!</i>
				</c:if>
			</div>
		</div>
		<%@include file="/inc/right-nav.jsp" %>
		<div class="fc"></div>
	</div>

	<%@include file="/inc/footer.jsp"%>
</body>
</html>