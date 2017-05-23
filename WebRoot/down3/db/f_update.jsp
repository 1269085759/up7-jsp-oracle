<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%><%@ 
	page contentType="text/html;charset=UTF-8"%><%@ 
	page import="down3.biz.*" %><%@
	page import="down3.model.*" %><%@ 
	page import="up7.*" %><%@	
	page import="redis.clients.jedis.Jedis" %><%@
	page import="down3.biz.redis.*" %><%@
	page import="java.net.URLDecoder" %><%@ 
	page import="java.net.URLEncoder" %><%@ 
	page import="org.apache.commons.lang.*" %><%@ 
	page import="com.google.gson.FieldNamingPolicy" %><%@ 
	page import="com.google.gson.Gson" %><%@ 
	page import="com.google.gson.GsonBuilder" %><%@ 
	page import="com.google.gson.annotations.SerializedName" %><%@ 
	page import="java.io.*" %><%/*
	此页面主要用来向数据库添加一条记录。
	一般在 HttpUploader.js HttpUploader_MD5_Complete(obj) 中调用
	更新记录：
		2012-05-24 完善
		2012-06-29 增加创建文件逻辑，
*/
String fid 		= request.getParameter("signSvr");
String uid 		= request.getParameter("uid");
String lenLoc	= request.getParameter("lenLoc");
String per		= request.getParameter("perLoc");
String cbk 		= request.getParameter("callback");//jsonp
//
if (StringUtils.isEmpty(uid)
	||StringUtils.isBlank(fid)
	||StringUtils.isBlank(cbk)
	||StringUtils.isBlank(lenLoc))
{
	out.write(cbk + "({\"value\":0})");
	return;
}

//添加到缓存
Jedis j = JedisTool.con();
FileRedis fr = new FileRedis(j);
fr.process(fid,per,lenLoc);

out.write(cbk + "({\"value\":1})");%>