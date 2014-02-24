<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/oc-profile.tld" prefix="profile"%>
<%@ taglib uri="/WEB-INF/tld/oc-profile-functions.tld" prefix="profilefn"%>

<div id="left-nav" class="nav">
	<c:if test="${_owner!=null }">
		<div class="nav-box">
		<dl class="person_info clearfix">
				<dt>
					<a title="${_owner.name}" href="/info/${_owner.id}"> <img alt="${_owner.name}" width="80" src="${_owner.mainUrl}"></a>
				</dt>
				<dd>
					<a title="${_owner.name}" href="/info/${_owner.id}">${_owner.name}</a>
				</dd>
				<dt style="margin-top:4px;">
					<c:if test='${_owner_score_rank != null }'>${_owner_score_rank.systemKey}</c:if>
					<img src="<c:if test='${ _owner_score_rank != null }'>${_owner_score_rank.descript }</c:if>" />
				</dt>
			    <dt style="margin-top:7px;">
					<a class="name" href="/score/index/other/${_owner.id}">积分&nbsp;</a>
					<c:choose>
						<c:when test="${_owner_score_count!=null}">
							${_owner_score_count }
						</c:when>
						<c:otherwise>
							0
						</c:otherwise>
			       	</c:choose> 
				</dt>
				 <dt style="margin-top:7px;">
					<a class="name" href="/reward/${_owner.id}">红花数目&nbsp;</a>
					<c:choose>
					<c:when test="${_owner_reward_flower!=null}">
						${_owner_reward_flower}
					</c:when>
					<c:otherwise>
						0
					</c:otherwise>
				</c:choose> 
				</dt>		
                <c:if test="${c_dev_env }">
					<dt style="margin-top:7px;">
						<a href="/login/as/${_owner.id}">我要变Ta</a>
					</dt>
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
						<a class="title" href="/info/${_owner.id}">Ta的资料</a>
					</c:when>
					<c:otherwise>
						<span class="title">Ta的资料</span>
					</c:otherwise>
				</c:choose>
			</profile:menu>
		</dl>
		<dl>
			<profile:menu prefix="resume">
				<c:choose>
					<c:when test="${profilefn:access('view_resume') }">
						<a class="title" href="/resume/${_owner.id}/view">Ta的简历</a>
					</c:when>
					<c:otherwise>
						<span class="title">Ta的简历</span>
					</c:otherwise>
				</c:choose>
			</profile:menu>
		</dl>
		<dl>
			<profile:menu prefix="kpi_history">
				<c:choose>
					<c:when test="${profilefn:access('view_kpi') }">
						<a class="title" href="/kpi/history/view/${_owner.id }/0">Ta的KPI</a>
					</c:when>
					<c:otherwise>
						<span class="title">Ta的KPI</span>
					</c:otherwise>
				</c:choose>
			</profile:menu>
		</dl>
		<dl>
			<profile:menu prefix="otherSvn">
				<a class="title" href="/svn/${_owner.id}">Ta的SVN</a>
			</profile:menu>
		</dl>
		<dl>
			<profile:menu prefix="weeklyreport">
				<c:choose>
					<c:when test="${profilefn:access('view_weekly_report') }">
						<a class="title" href="/weeklyreport/${_owner.id}">Ta的周报</a>
					</c:when>
					<c:otherwise>
						<span class="title">Ta的周报</span>
					</c:otherwise>
				</c:choose>
			</profile:menu>
			
			<c:if test="${_owner_is_leader}">
				<c:if test="${profilefn:access_user_owner('view_weekly_report',_user.id,_owner.id) }">
					<profile:menu prefix="weeklyreport_subordinates" sub="true">
						<a class="title" href="/weeklyreport/${_owner.id }/subordinates">下属汇总</a>
					</profile:menu>
				</c:if>
			</c:if>
		</dl>

		<dl>
			<profile:menu prefix="reward">
				<a class="title" href="/reward/${_owner.id}">Ta的红花</a>
			</profile:menu>
		</dl>
		<dl>
			<profile:menu prefix="score_other">
				<a class="title" href="/score/index/other/${_owner.id}">Ta的积分</a>
			</profile:menu>
		</dl>
		
		
				<dl>
			<profile:menu prefix="perf">
				<c:choose>
					<c:when test="${profilefn:access('view_perf_record_list') }">
						<a class="title" href="/perf/${_owner.id}/view">绩效考评</a>
					</c:when>
					<c:otherwise>
						<span class="title">绩效考评</span>
					</c:otherwise>
				</c:choose>
			</profile:menu>
		</dl>
	</div>
	<hr class="nav-spliter"></hr>
	<%@include file="/inc/left-nav-bottom.jsp"%>
</div>