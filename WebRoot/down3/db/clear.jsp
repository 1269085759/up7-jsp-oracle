<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%><%@ 
	page contentType="text/html;charset=UTF-8"%><%@ 
	page import="down3.biz.redis.*" %><%@
	page import="up7.*" %><%@  
	page import="redis.clients.jedis.Jedis" %><%@
	page import="down3.biz.*" %><%/*
	此页面主要用来向数据库添加一条记录。
	一般在 HttpUploader.js HttpUploader_MD5_Complete(obj) 中调用
	更新记录：
		2015-05-13 创建
*/
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

Jedis j =JedisTool.con();
tasks svr = new tasks("0",j);
svr.clear();
j.close();
%>