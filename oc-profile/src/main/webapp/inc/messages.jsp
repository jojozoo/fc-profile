<%@taglib uri="http://rose.paoding.net/tags-core" prefix="rose"%>
<rose:flash key="pf.errors" prefix="<div class='message-error'>" suffix="</div>" />
<rose:flash key="pf.warnings" prefix="<div class='message-warning'>" suffix="</div>" />
<rose:flash key="pf.infos" prefix="<div class='message-info'>" suffix="</div>" />

<c:forEach items="${pageMessages.messages}" var="pageMessage">
	<c:if test="${pageMessage.type=='message'}">
		<div class="message-info">${pageMessage}</div>
	</c:if>
	<c:if test="${pageMessage.type=='warning'}">
		<div class="message-warning">${pageMessage}</div>
	</c:if>
	<c:if test="${pageMessage.type=='error'}">
		<div class="message-error">${pageMessage}</div>
	</c:if>
</c:forEach> 

<c:forEach items="${p_global_errors}" var="_error">
     $('div.message-error').remove();
	<div class="message-error">${_error}</div>
</c:forEach> 

<c:forEach items="${p_global_warnings}" var="_warning">
	<div class="message-warning">${_warning}</div>
</c:forEach> 

<c:forEach items="${p_global_infos}" var="_info">
	<div class="message-info">${_info}</div>
</c:forEach> 

