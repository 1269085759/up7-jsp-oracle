<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%><%@ 
	page contentType="text/html;charset=UTF-8"%><%@ 
	page import="down3.biz.*" %><%@
	page import="up7.*" %><%@
	page import="up7.biz.*" %><%@
	page import="down3.biz.redis.*" %><%@
	page import="redis.clients.jedis.Jedis" %><%@  
	page import="java.net.URLDecoder" %><%@ 
	page import="java.net.URLEncoder" %><%@ 
	page import="org.apache.commons.lang.*" %><%@ 
	page import="com.google.gson.FieldNamingPolicy" %><%@ 
	page import="com.google.gson.Gson" %><%@ 
	page import="com.google.gson.GsonBuilder" %><%@ 
	page import="com.google.gson.annotations.SerializedName" %><%@ 
	page import="java.io.*" %><%
/*
	加载未完成的文件和文件夹任务。
	更新记录：
		2015-05-13
		2016-07-29 更新
		2017-05-06 从缓存中加载文件列表（未完成）
*/
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String uid 		 = request.getParameter("uid");
String cbk		 = request.getParameter("callback");

if (!StringUtils.isBlank(uid))
{
	Jedis j = JedisTool.con();
	tasks svr = new tasks(uid,j);
	String json = svr.toJson();
	j.close();
	
	if(!StringUtils.isBlank(json))
	{
		json = URLEncoder.encode(json,"utf-8");
		json = json.replaceAll("\\+","%20");//
		json = cbk + "({\"value\":\""+json+"\"})";
		out.write(json);
		return;
	}
}

out.write(cbk + "({\"value\":null})");
%>