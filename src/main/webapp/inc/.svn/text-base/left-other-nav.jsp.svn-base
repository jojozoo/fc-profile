<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.renren.com/profile" prefix="profile"%>
<%@ taglib uri="http://www.renren.com/profile/functions" prefix="profilefn"%>
<div id="left-nav" class="nav">
	<c:if test="${_owner!=null }">
		<div class="nav-box">
			<div class="owner_info clearfix">
				<div class="name">
					<a title="${_owner.name}" href="/">${_owner.name}</a>
				</div>
				<div>
					<a title="${_owner.name}" href="/"> <img alt="${_owner.name}" width="160" src="${_owner.headPic()}">
					</a>
				</div>
				<c:if test="${profilefn:isRoot(_user.id) || sessionScope['$profile.canloginas'] != null}">
					<div>
						<a href="/login/as/${_owner.id}">我要变Ta</a>
					</div>
				</c:if>
			</div>
		</div>
		<hr class="nav-spliter"></hr>
	</c:if>
	<div class="nav-box">
		<dl>
			<profile:menu prefix="info">
				<a class="title" href="/info/${_owner.id}">Ta的资料</a>
			</profile:menu>
		</dl>
		<dl>
			<profile:menu prefix="resume">
				<a class="title" href="/resume/${_owner.id}/view">Ta的简历</a>
			</profile:menu>
		</dl>
		<dl>
			<profile:menu prefix="weeklyreport">
				<a class="title" href="/weeklyreport/${_owner.id}">Ta的周报</a>
			</profile:menu>
		</dl>
		<dl>
			<profile:menu prefix="kpi">
				<a class="title" href="/kpi/${_owner.id}/view">Ta的KPI</a>
			</profile:menu>
		</dl>
		<dl>
			<profile:menu prefix="">
				<a class="title">绩效考评</a>
			</profile:menu>
			<profile:menu prefix="" sub="true">
				<a class="title"  href="perf/summary/${_owner.id }/current">Ta的汇报汇总</a>
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
		<dl>
			<profile:menu prefix="admin_role">
				<a class="title" href="/admin/roles">角色管理</a>
			</profile:menu>
		</dl>
		<profile:security action="root">
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
		<dl>
			<profile:menu prefix="admin_promotion">
				<a class="title" href="/admin/promotion">升职管理</a>
			</profile:menu>
		</dl>
	</div>
</div>