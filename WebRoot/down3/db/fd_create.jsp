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
	page import="java.io.*" %><%
/*
	向redis添加一条记录（文件夹）
	更新记录：
		2015-05-13 创建
		2016-07-29 更新
		2017-05-14 完善
*/
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String uid 		= request.getParameter("uid");
String cbk 		= request.getParameter("callback");
String signSvr	= request.getParameter("signSvr");
String nameLoc	= request.getParameter("nameLoc");
String pathLoc	= request.getParameter("pathLoc");
nameLoc		 	= nameLoc.replace("+","%20");
nameLoc			= URLDecoder.decode(nameLoc,"UTF-8");
pathLoc		 	= pathLoc.replace("+","%20");
pathLoc			= URLDecoder.decode(pathLoc,"UTF-8");

if (StringUtils.isBlank(uid)
	||StringUtils.isBlank(nameLoc)
	||StringUtils.isBlank(pathLoc)
	)
{
	DnFileInf fd = new DnFileInf();
	fd.nameLoc = nameLoc;
	fd.pathLoc = pathLoc;
	fd.signSvr = signSvr;
	
	Jedis j = JedisTool.con();
	tasks svr = new tasks(uid,j);
	svr.add(fd);
	out.write(cbk.concat("(0)"));
	return;
}
%>