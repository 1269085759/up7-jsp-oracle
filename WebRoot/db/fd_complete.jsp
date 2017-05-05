<%@ page language="java" import="up7.DBFile" pageEncoding="UTF-8"%><%@
	page contentType="text/html;charset=UTF-8"%><%@ 
	page import="up7.DBFolder" %><%@
	page import="up7.biz.folder.*" %><%@
	page import="up7.*" %><%@
	page import="redis.clients.jedis.Jedis" %><%@
	page import="org.apache.commons.lang.StringUtils" %><%
/*
	此页面主要更新文件夹数据表。已上传字段
	更新记录：
		2014-07-23 创建
*/
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String sign = request.getParameter("idSign");
String uid	= request.getParameter("uid");
String cbk 	= request.getParameter("callback");//jsonp
int ret = 0;

//参数为空
if ( !StringUtils.isBlank(sign) )
{
	fd_redis fd = new fd_redis();
	fd.read(sign);	
	fd.mergeAll();//合并文件块
	fd.saveToDb();//保存到数据库
	ret = 1;
}
out.write(cbk + "(" + ret + ")");
%>