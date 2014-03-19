<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
	<title>download test</title>
	<style>
	</style>
	<script type="text/javascript">
	function redirect(){
		location.href = href;
		}

		var url_parts = document.URL.split('?');
		var query = url_parts.length == 2 ? ('?' + url_parts[1]) : '';
		var href = '/';
		var ua = navigator.userAgent.toLowerCase();

		if (/iphone|ipad|ipod/.test(ua)) {
		href = 'https://itunes.apple.com/cn/app/man-hua-kong/id504498659?mt=8';
		__ga('set', 'page', '/download/ios' + query);
		__ga('send', 'pageview');
		__ga('send', 'event', 'app', 'download-via-qrcode', 'ios', {'hitCallback': redirect});
		setTimeout(redirect, 2000);
		} else if (/android/.test(ua)) {
		href = 'http://apps.wandoujia.com/apps/net.comikon.reader/download';
		__ga('set', 'page', '/download/android' + query);
		__ga('send', 'pageview');
		__ga('send', 'event', 'app', 'download-via-qrcode', 'android-local', {'hitCallback': redirect});
		setTimeout(redirect, 2000);
		} else if (/windows phone|blackberry/.test(ua)) {
		__ga('set', 'page', '/download/other' + query);
		__ga('send', 'pageview');
		alert('抱歉，暂不支持您的系统');
		location.href = '/';
		} else {
		__ga('send', 'pageview');
		location.href = '/';
		}
		})()
	</script>
</head>
<body>
	
	<div id="main">
		    <p style="font-size: 20px;">
		       Just for test
		    </p>
	<div class="fc"></div>
	</div>
</body>
</html>