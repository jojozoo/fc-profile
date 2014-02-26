 
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
	<%@include file="/inc/head.jsp" %>
	<title>ta的KPI- ${_siteName }</title>
	<style type="text/css">

    </style>
	<script type="text/javascript">


	
	
	</script>
</head>
<body>
	<%@include file="/inc/header.jsp" %>
	
	<div id="main">
		<%@include file="/inc/left-nav.jsp" %>
		<div id="content">
			<div class="content-title">
						<h2>ta的KPI</h2>
			</div>
			
			<div class="messages-box">
				<%@include file="/inc/messages.jsp" %>
			</div>
			
			<div class="content-box">
			<br><br>
			<div class='message-error'>  KPI功能还在测试中，即将开放！</div>
	
			</div>
			 
		</div>
		
		
	<div class="fc"></div>
	</div>
	
	<%@include file="/inc/footer.jsp" %>
</body>
</html>


		      
