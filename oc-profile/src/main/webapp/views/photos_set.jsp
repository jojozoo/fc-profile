<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/oc-profile.tld" prefix="profile"%>
<%@ taglib uri="/WEB-INF/tld/oc-profile-functions.tld" prefix="profilefn"%>

<c:set var="nav_menu" value="info"></c:set>

<!DOCTYPE html>
<html>
<head>
<%@include file="/inc/head.jsp"%>
<script src="/static/js/jquery.jcrop.js" type="text/javascript"></script>
<title>设置头像 - ${_user.name} - ${_siteName }</title>
<style>
	#c_ori{
		width: 300px;
		height: 300px;
		border: solid 1px #777;
		background: #eee;
	}
	
	#change_photo{
		table-layout: fixed;
	}
	
	td.img_ori{
		border: none;
		border-right: solid 1px #aaa; 
		width: 350px;
		text-align: left;
	}
	
	td.img_200{
		width: 200px;
		text-align: center;
		vertical-align: middle;
		padding-left:50px;
	}
	
	td.img_small{
		width:60px;
		text-align: center;
		vertical-align: middle;
	}
	
</style>
<script type="text/javascript">
	jQuery(function($) {
		var oriImageObj = $('#upload_photo');
		var oriImageWidth = oriImageObj.width();
		var oriImageHeight = oriImageObj.height();
		
		var showPreview = function(container,coords){
			var cw = container.width();
			var ch = container.height();
			var rx = cw / coords.w;
			var ry = cw / coords.h;
			
			container.find('img').css({
				width: Math.round(rx * oriImageWidth) + 'px',
				height: Math.round(ry * oriImageHeight) + 'px',
				marginLeft: '-' + Math.round(rx * coords.x) + 'px',
				marginTop: '-' + Math.round(ry * coords.y) + 'px'
			});
			
		}
		
		var showPreviews = function(coords){
			showPreview($('#c_200'),coords);
			showPreview($('#c_160'),coords);
			showPreview($('#c_50'),coords);
		};
		
		var updateCoords = function(x,y,w,h){
			jQuery('#x-input').val(x);
			jQuery('#y-input').val(y);
			jQuery('#w-input').val(w);
			jQuery('#h-input').val(h);
		}
		
		var sx,sy,sw,sh;
		sw = sh = Math.min(oriImageWidth,oriImageHeight);
		sx = (oriImageWidth - sw) / 2;
		sy = (oriImageHeight - sh) / 2;
		updateCoords(sx,sy,sw,sh);
		oriImageObj.Jcrop({
			onChange : function(coords){
				showPreviews(coords);
			},
			onSelect :  function(coords){
				showPreviews(coords);
				updateCoords(coords.x,coords.y,coords.w,coords.h);
			},
			aspectRatio : 1,
			setSelect: [sx,sy,sw,sh]
		});
		
	});
	
	function submitPhoto(){
		var path = jQuery('#upload_photo').attr('src');
		if(!path){
			return;
		}
	}
</script>
</head>
<body>
	<%@include file="/inc/header.jsp"%>

	<div id="main" class="reset-list-css">
		<%@include file="/inc/left-nav.jsp"%>
		<div id="content">
			<div class="content-title">
				<h2>修改头像</h2>
			</div>
			<div class="messages-box">
				<%@include file="/inc/messages.jsp" %>
			</div>
			
			<div class="content-box">
				<table id="change_photo">
					<tbody>
						<tr>
							<td class="img_ori">
								<div id="c_ori">
									<div style="margin-left:${(300-_width)/2 }px;margin-top:${(300-_height)/2 }px;">
										<img alt="上传的图片" src="${imageurl}" id="upload_photo" style="width:${_width}px;height:${_height}px;">
									</div> 
								</div>
							</td>
							<td class="img_200">
								<div id="c_200" style="width: 200px;height: 200px;overflow: hidden;">
									<img alt="200" src="${imageurl}">
								</div>
								大尺寸头像，200&times;200像素
							</td>
							<td class="img_small">
								<div id="c_50" style="width: 50px;height: 50px;overflow: hidden;">
									<img alt="50" src="${imageurl}">
								</div>
								小尺寸头像，50&times;50像素
							</td>
						</tr>
					</tbody>
				</table>
				<div class="clearfix" style="margin-top: 10px;">
					<div class="fl">
						<a class="change-headpic button btn_b" href="javascript:void(0);">
							<span>
								重新上传
							</span>
							<form id="photo-form" action="/photos/upload" method="post" enctype="multipart/form-data">
								<input type="file" name="photo" value="修改头像" onchange="javascript:if(jQuery(this).val()){jQuery('#photo-form').submit();}" >
							</form>
						</a>
					</div>
					<div class="fl" style="margin-left: 10px;">
						<a class="btn_gp" href="javascript:void(0);" onclick="javascript:jQuery('#set-form').submit();">
							<span>设置头像</span>
						</a>
					</div>
				</div>
					
				<form id="set-form" action="/photos/set" method="post">
					<input name="token" value="${token}" type="hidden">
					<input id="x-input" name="cx" value="0" type="hidden">
					<input id="y-input" name="cy" value="0" type="hidden">
					<input id="w-input" name="cw" value="0" type="hidden">
					<input id="h-input" name="ch" value="0" type="hidden">
				</form>
			</div>
		</div><%@include file="/inc/right-nav.jsp" %>
		<div class="fc"></div>
	</div>

	<%@include file="/inc/footer.jsp"%>
</body>
</html>