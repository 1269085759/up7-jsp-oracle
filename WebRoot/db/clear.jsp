<%@ page language="java" import="up7.DBFile" pageEncoding="UTF-8"%><%@
	page contentType="text/html;charset=UTF-8"%><%@
	page import="up7.biz.redis.*" %><%
/*
	清空数据库记录
	更新记录：
		2014-07-21 创建
*/
DBFile.Clear();
out.write("数据库清除成功<br/>");
tasks t = new tasks();
t.clear();
out.write("redis缓存清除成功<br/>");
%>