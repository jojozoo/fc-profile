<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tld/oc-profile.tld" prefix="profile"%>
<%@ taglib uri="/WEB-INF/tld/oc-profile-functions.tld" prefix="profilefn"%>

<c:set var="nav_menu" value="admin_user"></c:set>

<!DOCTYPE html>
<html>
<head>
    <%@include file="/inc/head.jsp" %>
    <title>员工列表- 员工管理 -${_siteName }</title>
    <style>
    </style>
    <script type="text/javascript">
        function deleteUser(userId, userName) {
            if (!userId) {
                return;
            }
            PU.confirmDialog('你确定删除用户"' + userName + '"吗？', '警告', function () {
                $('#delete_form').attr('action', '/admin/user/delete/' + userId+"?curpage=${curpage}").submit();
            });
        }
        jQuery(function ($) {
            (function () {
                var inputObj = $('#user-search-input');
                inputObj.suggest({
                    select:function (event, ui) {
                        location.href = '/admin/user/search/' + ui.item.id;
                        return false;
                    }
                });
            })();
        });
    </script>
</head>
<body>
<%@include file="/inc/header.jsp" %>

<div id="main">
   <%--  <%@include file="/inc/left-nav.jsp" %> --%>
    <div id="content">
        <div class="content-title">
            <div class="content-action">
                <a class="button" href="/admin/user/add">创建用户</a>
            </div>
            <h2>用户</h2>
        </div>
        <div class="messages-box">
            <%@include file="/inc/messages.jsp" %>
        </div>
        <form id="delete_form" action="" method="POST"></form>

        <div class="content-box">
            <div>
                <form action="/admin/user/list" method="get">
                    <label>Email或者姓名</label>
                    <input type="text" id="user-search-input" name="ul_keyword" class="middle-input" value="${profilefn:escapeHtml(ul_keyword)}"/>
                    <input type="submit" value="查询">
                </form>
            </div>

            <c:if test="${empty userList}">
                没有查询到用户
            </c:if>

            <c:if test="${not empty userList}">
                <table class="user-full-table">
                    <thead>
                    <c:forEach var="item" items="${userList}" varStatus="status">
                        <tr class="user-item">
                            <td class="user-headpic">
                                <a href="/info/${item.id }" target="_blank">
                                    <img width="50" height="50" src="${item.tinyUrl()}">
                                </a>
                            </td>
                            <td class="user-info">
                                <a href="/info/${item.id }" target="_blank">
                                        ${item.name }
                                </a>
                                <br/>
                                    ${profilefn:escapeHtml(item.email)}
                            </td>
                            <td>
                                    ${item.jobTitle}
                            </td>
                            <td>
                                    ${item.department().departmentName }
                            </td>
                            <td class="actionlist">
                                <a href="/admin/user/edit/${item.id+0}">编辑</a> <a href="javascript:;"
                                                                                  onclick="deleteUser(${item.id+0},'${item.name}')">删除</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </thead>
                </table>
                <profile:page page="${curpage}" total="${total}" href="?ul_keyword=${profilefn:urlEncode(ul_keyword)}&curpage={page}" hidewhen="single"/>
            </c:if>
        </div>
    </div>
    <%@include file="/inc/right-nav.jsp" %>
    <div class="fc"></div>
</div>

<%@include file="/inc/footer.jsp" %>
</body>
</html>