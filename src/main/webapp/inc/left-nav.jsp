<c:if test="${user_resource != null && not user_resource}">
	<%@include file="left-my-nav.jsp"%>
</c:if>
<c:if test="${user_resource != null && user_resource}">
	<%@include file="left-other-nav.jsp"%>
</c:if>
<c:if test="${user_resource == null}">
	<c:if test="${_owner == null }">
		<%@include file="left-my-nav.jsp"%>
	</c:if>
	<c:if test="${_owner != null }">
		<%@include file="left-other-nav.jsp"%>
	</c:if>
</c:if>