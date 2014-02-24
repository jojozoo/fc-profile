<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/oc-profile.tld" prefix="profile"%>
<%@ taglib uri="/WEB-INF/tld/oc-profile-functions.tld" prefix="profilefn"%>
<!--  
<div id="browser-warning" style="display: none;">
		<div class="message-warning">IEç¯å¢ä¸æ¬ç³»ç»æªéè¿æµè¯ï¼é¨ååè½æ æ³ä½¿ç¨ãå»ºè®®æ¨ä½¿ç¨ChromeæFirefox!</div>
</div>
-->
<div id="left-nav" class="nav">
	<c:if test="${_user!=null }">
		<div class="nav-box">
			<dl class="person_info clearfix">
				<dt>
					<a title="${_user.name}" href="/info/${_user.id }">
						<img alt="${_user.name}" width="80" src="${_user.tinyUrl}">
					</a>
				</dt>
				<dd>
					<a class="name" title="${_user.name}" href="/info/${_user.id }">${_user.name}</a>
				</dd>
				<dt style="margin-top:4px;">
					<a href="/score/index/rank"><c:if test='${_score_rank != null }'>${_score_rank.systemKey}</c:if></a>
					<img src="<c:if test='${ _score_rank != null }'>${ _score_rank.descript }</c:if>" />
				</dt>
			    <dt style="margin-top:7px;">
					<a class="name" href="/score/index">ç§¯å&nbsp;</a>
					<c:choose>
					<c:when test="${_score_count!=null}">
						${_score_count }
					</c:when>
					<c:otherwise>
						0
					</c:otherwise>
				</c:choose> 
				</dt>
				 <dt style="margin-top:7px;">
					<a class="name" href="/reward/index">小红花&nbsp;</a>
					<c:choose>
					<c:when test="${_reward_flower!=null}">
						${_reward_flower }
					</c:when>
					<c:otherwise>
						0
					</c:otherwise>
				</c:choose> 
				</dt>		
				<c:if test="${not empty _user.virtualRewardItem()}">
					<dd>
						<img alt="${_user.virtualRewardItem().name}" width="30" src="${_user.virtualRewardItem().img}">
					</dd>
				</c:if>
			</dl>
		</div>
		<hr class="nav-spliter"></hr>
	</c:if>
	<div class="nav-box">
		<dl>
			<profile:menu prefix="info">
				<c:choose>
					<c:when test="${profilefn:access('view_info') }">
						<a class="title" href="/info/my">我</a>
					</c:when>
					<c:otherwise>
						<span class="title">XXX</span>
					</c:otherwise>
				</c:choose>
			</profile:menu>
		</dl>
		<dl>
			<profile:menu prefix="resume">
				<c:choose>
					<c:when test="${profilefn:access('view_resume') }">
						<a class="title" href="/resume/my/view">æçç®å</a>
					</c:when>
					<c:otherwise>
						<span class="title">个人简历 </span>
					</c:otherwise>
				</c:choose>
			</profile:menu>
		</dl>
		<dl>
			<profile:menu prefix="weeklyreport">
				<c:choose>
					<c:when test="${profilefn:access('view_weekly_report') }">
						<a class="title" href="/weeklyreport/my">æçå¨æ¥</a>
					</c:when>
					<c:otherwise>
						<span class="title">周报</span>
					</c:otherwise>
				</c:choose>
			</profile:menu>
			<c:if test="${_is_leader}"> 
				<profile:menu prefix="weeklyreport_subordinates" sub="true">
					<a class="title" href="/weeklyreport/my/subordinates">ä¸å±æ±æ»</a>
				</profile:menu>
			</c:if>
		</dl>
		
		<dl>
			<profile:menu prefix="kpi">
				<c:choose>
					<c:when test="${profilefn:access('view_kpi') }">
						<a class="title" href="/kpi/index">æçKPI</a>
					</c:when>
					<c:otherwise>
						<span class="title">我的KPI</span>
					</c:otherwise>
				</c:choose>
			</profile:menu>
		</dl>
		
		<dl>
			<profile:menu prefix="svn">
				<a class="title" href="/svn/index">SVN  &nbsp <img src="/static/images/new.gif"></a>
			</profile:menu>
		</dl>
		
		<dl>
			<profile:menu prefix="perf">
				<c:choose>
					<c:when test="${profilefn:access('view_perf_record_list') }">
						<a class="title" href="/perf">绩效</a>
					</c:when>
					<c:otherwise>
						<span class="title">绩效</span>
					</c:otherwise>
				</c:choose>
			</profile:menu>
			<c:set var="_done_img">
				<img class="status-icon" src="/static/images/status-done.png" />
			</c:set>
			<profile:menu prefix="perf_self" sub="true">
				<c:set var="_visiable" value="${(_current_perf_time.status() == c_perf_time_status$started || _current_perf_time.status() == c_perf_time_status$will_end) && _kpi_status == 1}"></c:set>
				<c:set var="_show_done_img" value="${_self_perf_status == c_self_perf_status$submitted }"></c:set>
				<c:if test="${_visiable }">
					<a class="title" href="/perf/self">æçèªè¯</a>
				</c:if>
				<c:if test="${not _visiable}">
					<span class="title">
						æçèªè¯
					</span>
				</c:if>
			</profile:menu>
			<profile:menu prefix="perf_eachother" sub="true">
				<c:set var="_visiable" value="${_self_perf_status == c_self_perf_status$submitted }"></c:set>
				<c:set var="_peerAccess" value="${_peer_perf_status == c_peer_perf_status$on_invitations }"></c:set>
				
				<c:set var="_show_done_img" value="${_peer_perf_status == c_self_perf_status$complete_invitations }"></c:set>
				<c:if test="${_visiable || _peerAccess }">
					<a class="title" href="/perfApply/myPerf">æçäºè¯${_peerAccess ? '<span style=color:red>*</span>' : ''}</a>
				</c:if>
				<c:if test="${not _visiable && !_peerAccess}">
					<span class="title">
						æçäºè¯
					</span>
				</c:if>
			</profile:menu>
			<c:if test="${_is_leader}">
				<profile:menu prefix="perf_subordinates" sub="true">
					<c:set var="_visiable" value="${_current_perf_time.status() == c_perf_time_status$started || _current_perf_time.status() == c_perf_time_status$will_end}"></c:set>
					<c:set var="_show_done_img" value="${_peer_perf_status == c_self_perf_status$complete_invitations }"></c:set><%--æ·»å æ¡ä»¶ --%>
					<c:if test="${_visiable }">
						<a class="title" href="/perfApply/myReport">è¯ä»·ä¸å±${_show_done_img ? _done_img:''}</a>
					</c:if>
					<c:if test="${not _visiable}">
						<span class="title">
							è¯ä»·ä¸å±
						</span>
					</c:if>
				</profile:menu>
			</c:if>
			<c:if test="${_is_leader}">
				<profile:menu prefix="perf_adjust_score" sub="true">
					<c:set var="_visiable" value="${_current_perf_time.status() == c_perf_time_status$started || _current_perf_time.status() == c_perf_time_status$will_end}"></c:set>
					<c:set var="_show_done_img" value="${_peer_perf_status == c_self_perf_status$complete_invitations }"></c:set><%--æ·»å æ¡ä»¶ --%>
					<c:if test="${_visiable }">
						<a class="title" href="/perf/adjustScore">ç»©æè°å${_show_done_img ? _done_img:''}</a>
					</c:if>
					<c:if test="${not _visiable}">
						<span class="title">
							ç»©æè°å
						</span>
					</c:if>
				</profile:menu>
			</c:if>
			 <!--  
			<profile:menu prefix="perf_eachother" sub="true">
				<c:if test="${_visiable }">
					<a class="title" href="/kpi/self">kpièªè¯</a>
				</c:if>
				<c:if test="${not _visiable}">
					<span class="title">
						kpièªè¯
					</span>
				</c:if>
			</profile:menu>
			
			<c:if test="${_is_leader}">
				<profile:menu prefix="perf_subordinates" sub="true">
					<c:if test="${_visiable }">
						<a class="title" href="/kpi/leader">è¯ä»·ä¸å±kpi</a>
					</c:if>
					<c:if test="${not _visiable}">
						<span class="title">
							è¯ä»·ä¸å±kpi
						</span>
					</c:if>
				</profile:menu>
				
			</c:if>
			-->
		</dl>
		
		<dl>
			<profile:menu prefix="reward">
				<a class="title" href="/reward/index">çº¢è±å¥å±</a>
			</profile:menu>
			<profile:menu prefix="reward_send" sub="true">
				<a class="title" href="/reward/flower">æè¦ç®è±</a>
			</profile:menu>
			<!--  
			<profile:menu prefix="reward_obtain" sub="true">
				<a class="title" href="/reward/exchange">æè¦åæ¢</a>
			</profile:menu>
			-->
		</dl>
		<dl>
			<profile:menu prefix="score">
				<a class="title" href="/score/index">æçç§¯å</a>
			</profile:menu>
			<profile:menu prefix="score_rank" sub="true">
				<a class="title" href="/score/index/rank">ç§¯åæè¡æ¦</a>
			</profile:menu>
		</dl>
		<dl>
			<profile:menu prefix="patent">
				<a target="_blank" class="title" href="/patent" title="ç³è¯·ä¸å©">ç³è¯·ä¸å©
				 &nbsp </a>
			</profile:menu>
		</dl>
	</div>
	<hr class="nav-spliter"></hr>
	<%@include file="/inc/left-nav-bottom.jsp" %>
</div>