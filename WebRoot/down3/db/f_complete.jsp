<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%><%@ 
	page contentType="text/html;charset=UTF-8"%><%@ 
	page import="down3.biz.*" %><%@
	page import="down3.model.*" %><%@
	page import="down3.biz.redis.*" %><%@
	page import="up7.*" %><%@
	page import="redis.clients.jedis.Jedis" %><%@ 
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
		2017-05-12 完善逻辑 
*/
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String uid	 	= request.getParameter("uid");
String signSvr	= request.getParameter("signSvr");
String cbk  	= request.getParameter("callback");//jsonp

if ( 	StringUtils.isBlank(signSvr)
	||  StringUtils.isBlank(uid))
{
	out.write(cbk + "(0)");	
	return;
}
Jedis j = JedisTool.con();
tasks svr = new tasks(uid,j);
svr.del(signSvr);//从缓存中清除

String json = cbk + "(1)";//返回jsonp格式数据。
out.write(json);%>