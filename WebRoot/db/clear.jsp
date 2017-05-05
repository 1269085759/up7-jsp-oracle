<%@ page language="java" import="up7.DBFile" pageEncoding="UTF-8"%><%@
	page contentType="text/html;charset=UTF-8"%><%@
	page import="redis.clients.jedis.Jedis" %><%@
	page import="up7.*" %><%@
	page import="up7.biz.redis.*" %><%
/*
	清空数据库记录
	更新记录：
		2014-07-21 创建
*/
DBFile.Clear();
out.write("数据库清除成功<br/>");
Jedis j = JedisTool.con();
tasks t = new tasks(j);
t.clear();
j.close();
out.write("redis缓存清除成功<br/>");
%>