<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.renren.com/profile" prefix="profile"%>
<%@ taglib uri="http://www.renren.com/profile/functions" prefix="profilefn"%>
<div id="left-nav" class="nav">
	<c:if test="${_user!=null }">
		<div class="nav-box">
			<dl class="person_info clearfix">
				<dt>
					<a title="${_user.name}" href="/info/${_user.id }">
						<img alt="${_user.name}" width="80" src="${_user.headPic()}">
					</a>
				</dt>
				<dd>
					<a class="name" title="${_user.name}" href="/info/${_user.id }">${_user.name}</a>
				</dd>
			</dl>
		</div>
		<hr class="nav-spliter"></hr>
	</c:if>
	<div class="nav-box">
		<dl>
			<profile:menu prefix="info">
				<a class="title" href="/info/my">我的资料</a>
			</profile:menu>
		</dl>
		<dl>
			<profile:menu prefix="resume">
				<a class="title" href="/resume/my/view">我的简历</a>
			</profile:menu>
		</dl>
		<dl>
			<profile:menu prefix="weeklyreport">
				<a class="title" href="/weeklyreport/my">我的周报</a>
			</profile:menu>
		</dl>
		<dl>
			<profile:menu prefix="kpi">
				<a class="title" href="/kpi/my/view">我的KPI</a>
			</profile:menu>
		</dl>
		<dl>
			<profile:menu prefix="perf">
				<a class="title" href="/perf">绩效考评</a>
			</profile:menu>
			<profile:menu prefix="perf_self" sub="true">
				<a class="title" href="/perf/self">我的自评</a>
			</profile:menu>
			<profile:menu prefix="perf_eachother" sub="true">
				<a class="title" href="/perfApply/myPerf">我的互评</a>
			</profile:menu>
			<profile:menu prefix="" sub="true">
				<a class="title" href="/perfApply/myReport">评价我的下属</a>
			</profile:menu>
			<profile:menu prefix="perf_summary" sub="true">
				<a class="title" href="/perf/summary/my/current">汇总页面</a>
			</profile:menu>
		</dl>
	</div>
	<hr class="nav-spliter"></hr>
	<div class="nav-box">
		<dl>
			<profile:menu prefix="admin_user">
				<a class="title" href="/admin/user">员工管理</a>
			</profile:menu>
		</dl>
		<profile:security action="root">
			<dl>
				<profile:menu prefix="admin_role">
					<a class="title" href="/admin/roles">角色管理</a>
				</profile:menu>
			</dl>
			<dl>
				<profile:menu prefix="admin_security">
					<a class="title" href="/admin/security/actions">权限管理</a>
				</profile:menu>
			</dl>
		</profile:security>
		<dl>
			<profile:menu prefix="admin_perf">
				<a class="title" href="/admin/perf">绩效管理</a>
			</profile:menu>
		</dl>
	</div>
</div>