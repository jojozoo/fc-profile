<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/oc-profile.tld" prefix="profile"%>
<%@ taglib uri="/WEB-INF/tld/oc-profile-functions.tld" prefix="profilefn"%>
<div id="full_header">
	<div id="header" class="clearfix">
		<div class="logo">
		 <a href="/">
		 	<img id="profile-logo" alt="这是LOGO" src="/static/images/COMIKON.jpg"> 
		 </a>
		</div>
		<div class="right">
			<div class="search">
				<form id="header_search_form" action="/search">
					<input id="header_search_input" type="text" class="search-input" name="keyword" value="${profilefn:escapeHtml(_search_keyword)}" placeholder="请输入姓名、邮箱">
					<a id="header_search_button" class="search-btn button"></a> 
				</form>
			</div>
			<c:if test="${_user !=null }">
				<div class="menu">
					 <a class="menu-link" href="/info/${_user.id }">
					 	${_user.name}
					 </a>
				</div>
				<div class="menu">
					 <a class="menu-link" href="/logout">
					 	退出
					 </a>
				</div>
			</c:if>
			<c:if test="${_user ==null }">
				<div class="menu">
					 <a class="menu-link" href="/login">
					 	登录
					 </a>
				</div>
			</c:if>
		</div>
	</div>
</div>