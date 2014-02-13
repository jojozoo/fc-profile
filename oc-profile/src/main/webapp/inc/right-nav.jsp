<!--  
<div id="right-nav" class="md_border">
#right-nav {
	width: 180px;
	float: left;
	overflow: hidden;
}

#right-nav  .message dt {
	margin: 0 10px 0 0;
	width: 80px;
}
			<dl class="message">
				<h3>各业务发布窗口时间</h3><br>
				<p style="text-align:center;color:blue;font-size:20px;" > 周一 </p>
				<ul>
				    <p style="text-align:center;color:red;font-size:17px;" >暂无 </p>
					<c:forEach var="window1" items="${windowsList1}" varStatus="status">
						<li class=""><a title="${window1.descript }" href="#">${ window1.value}(${window1.descript })</a></li>
					</c:forEach>
				</ul><br>
				<p style="text-align:center;color:blue;font-size:20px;" > 周二 </p>
				<ul>
				    <p style="text-align:center;color:red;font-size:15px;" > 凌晨 </p>
				    <li class="">引擎部</li>
				    <li class="">AdEngine引擎</li>
   				    <li class="">search引擎</li>
   				    <li class="">social graph业务</li>
   				    <li class="">antispam引擎</li><br>
				
				    <p style="text-align:center;color:red;font-size:15px;"> 15:00-17:00 </p>
				    <li class="">sns-login</li>
				    <li class="">ugc-upload</li>
   				    <li class="">ugc-blog</li>
   				    <li class="">social graph业务</li>
   				    <li class="">Register&Guide</li><br>
   				    
   				    <p style="text-align:center;color:red;font-size:15px;"> 17:00-19:00 </p>
				    <li class="">外部接入</li>
				    <li class="">应用中心</li>
   				    <li class="">人人connect</li><br>
   				    
					<c:forEach var="window2" items="${windowsList2}" varStatus="status">
						<li class=""><a title="${window2.descript }" href="#">${ window2.value}(${window2.descript })</a></li>
					</c:forEach>
				</ul>
				<p style="text-align:center;color:blue;font-size:20px;" > 周三 </p>
				<ul>
				
					<p style="text-align:center;color:red;font-size:15px;"> 10:00-12:00 </p>
				    <li class="">ugc-status</li>
				    <li class="">icode</li><br>
				    
				    <p style="text-align:center;color:red;font-size:15px;"> 15:00-17:00 </p>
				    <li class="">sns-www</li>
				    <li class="">ugc-music</li>
   				    <li class="">web-friend</li>
   				    <li class="">AdEngine业务系统</li>
   				    <li class="">search web</li>
   				    <li class="">antispam web</li><br>
   				    
					<c:forEach var="window3" items="${windowsList3}" varStatus="status">
						<li class=""><a title="${window3.descript }" href="#">${ window3.value}(${window3.descript })</a></li>
					</c:forEach>
				</ul>
				<p style="text-align:center;color:blue;font-size:20px;" > 周四 </p>
				<ul>
				    <p style="text-align:center;color:red;font-size:15px;" > 凌晨 </p>
				    <li class="">引擎部</li>
				    <li class="">AdEngine引擎</li>
   				    <li class="">search引擎</li>
   				    <li class="">social graph业务</li>
   				    <li class="">antispam引擎</li><br>
   				    <p style="text-align:center;color:red;font-size:15px;"> 10:00-12:00 </p>
				    <li class="">ugc-share</li><br>
				
				    <p style="text-align:center;color:red;font-size:15px;"> 15:00-17:00 </p>
				    <li class="">ugc-photo</li>
				    <li class="">Register&Guide</li>
   				    <li class="">social graph业务</li>
   				    <li class="">Api</li>
   				    <li class="">与我相关</li>
   				     <li class="">通讯平台</li>
   				    <li class="">短链接</li>
   				    <li class="">at服务</li>
   				    <li class="">小组</li>
   				    <li class="">增值服务</li>
   				    <li class="">公共主页Page</li><br>
   				    
   				    <p style="text-align:center;color:red;font-size:15px;"> 17:00-19:00 </p>
				    <li class="">外部接入</li>
				    <li class="">应用中心</li>
   				    <li class="">人人connect</li><br>
   				    
					<c:forEach var="window4" items="${windowsList4}" varStatus="status">
						<li class=""><a title="${window4.descript }" href="#">${ window4.value}(${window4.descript })</a></li>
					</c:forEach>
				</ul><br>
				<p style="text-align:center;color:blue;font-size:20px;" > 周五 </p>
						<p style="text-align:center;color:red;font-size:17px;" >暂无 </p>
				<ul>
					<c:forEach var="window5" items="${windowsList5}" varStatus="status">
						<li class=""><a title="${window5.descript }" href="#">${ window5.value}(${window5.descript })</a></li>
					</c:forEach>
				</ul><br>
				<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
				
		   </dl>
</div>
.md_border {
    background-color: #F0F5F8;
    border: 1px solid #E3E3E3;
    box-shadow: 0 0 5px #EEEEEE;
    padding: 15px 0 0 10px;
}
-->