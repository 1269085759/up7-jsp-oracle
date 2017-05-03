<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%><%@ 
	page contentType="text/html;charset=UTF-8"%><%@ 
	page import="up7.*" %><%@
	page import="up7.model.*" %><%@
	page import="up7.biz.folder.*" %><%@
	page import="java.sql.*" %><%@
	page import="oracle.sql.*" %><%@
	page import="oracle.jdbc.*" %><%@ 
	page import="org.apache.commons.lang.StringUtils" %><%@
	page import="java.net.URLDecoder" %><%@ 
	page import="net.sf.json.JSONArray" %><%@ 
	page import="net.sf.json.JSONObject" %><%@ 
	page import="net.sf.json.util.JSONUtils" %><%@ 
	page import="com.google.gson.Gson" %><%@ 
	page import="com.google.gson.GsonBuilder" %><%@ 
	page import="com.google.gson.annotations.SerializedName" %><%@
	page import="redis.clients.jedis.Jedis" %><%@ 
	page import="java.io.*" %><%/*
	此页面主要用来向数据库添加一条记录。
	一般在 HttpUploader.js HttpUploader_MD5_Complete(obj) 中调用
	更新记录：
		2012-05-24 完善
		2012-06-29 增加创建文件逻辑，
*/

//连接本地的 Redis 服务
Jedis j = new Jedis("localhost");
System.out.println("Connection to server sucessfully");
//查看服务是否运行
      j.lpush("tutorial-list", "Redis");
System.out.println("Server is running: "+j.ping());

%>