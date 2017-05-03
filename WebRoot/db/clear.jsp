<%@ page language="java" import="up7.DBFile" pageEncoding="UTF-8"%><%@
	page contentType="text/html;charset=UTF-8"%><%@
	page import="up7.biz.redis.*" %><%
/*
	清空数据库记录
	更新记录：
		2014-07-21 创建
*/
DBFile.Clear();
tasks t = new tasks();
t.clear();
%>