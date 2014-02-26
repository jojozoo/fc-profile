<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/oc-profile.tld" prefix="profile"%>
<%@ taglib uri="/WEB-INF/tld/oc-profile-functions.tld" prefix="profilefn"%>
<div class="nav-box">
	<profile:security actions="user_manage">
		<dl>
			<profile:menu prefix="admin_user">
				<a class="title" href="/admin/user">员工管理</a>
			</profile:menu>
		</dl>
	</profile:security>
	<profile:security actions="user_manage">
		<dl>
			<profile:menu prefix="admin_bug">
				<a class="title" href="/bug/manage">Bug管理</a>
			</profile:menu>
		</dl>
	</profile:security>
	<profile:security actions="user_manage">
		<dl>
			<profile:menu prefix="response">
				<a class="title" href="/perf/response">绩效反馈</a>
			</profile:menu>
		</dl>
	</profile:security>
	<profile:security actions="user_manage">
		<dl>
			<profile:menu prefix="team_score">
				<a class="title" href="/perf/team">团队绩效</a>
			</profile:menu>
		</dl>
	</profile:security>
	<profile:security actions="root">
		<dl>
			<profile:menu prefix="admin_role">
				<a class="title" href="/admin/roles">角色管理</a>
			</profile:menu>
		</dl>
	</profile:security>
	<profile:security actions="root">
		<dl>
			<profile:menu prefix="admin_security">
				<a class="title" href="/admin/security/actions">权限管理</a>
			</profile:menu>
		</dl>
	</profile:security>

	<profile:security actions="perf_manage">
		<dl>
			<profile:menu prefix="admin_perf">
				<a class="title" href="/admin/perf">绩效管理</a>
			</profile:menu>
		</dl>
	</profile:security>

	<profile:security actions="view_promotees">
		<dl>
			<profile:menu prefix="admin_promotion">
				<a class="title" href="/admin/promotion">升级管理</a>
			</profile:menu>
			<profile:security actions="view_history_promotees">
				<profile:menu prefix="admin_promotion_history" sub="true">
					<a class="title" href="/admin/promotion/history">历史升级申请</a>
				</profile:menu>
			</profile:security>
		</dl>
	</profile:security>
</div>