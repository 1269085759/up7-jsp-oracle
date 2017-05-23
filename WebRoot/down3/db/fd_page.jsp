<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%><%@ 
	page contentType="text/html;charset=UTF-8"%><%@ 
	page import="up7.biz.folder.*" %><%@
	page import="up7.*" %><%@
	page import="java.net.URLDecoder" %><%@ 
	page import="java.net.URLEncoder" %><%@ 
	page import="org.apache.commons.lang.*" %><%@ 
	page import="com.google.gson.FieldNamingPolicy" %><%@ 
	page import="com.google.gson.Gson" %><%@ 
	page import="com.google.gson.GsonBuilder" %><%@ 
	page import="com.google.gson.annotations.SerializedName" %><%@ 
	page import="java.io.*" %><%
/*
	文件夹分布数据，提供给文件夹下载使用，
	默认每页加载100条数据，用于支持超大型文件夹下载。
	更新记录：
		2017-05-10 创建。
*/
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String id  		= request.getParameter("idSign");//文件夹ID，与up7_files.f_idSign对应。
String index	= request.getParameter("page");//页数，基于1

if (StringUtils.isBlank(id)
	||StringUtils.isBlank(index)
	)
{
	out.write("");
	return;
}

fd_page fd = new fd_page();
String json = fd.read(index,id);
json = PathTool.url_encode(json);
//json = URLEncoder.encode(json,"UTF-8");
//UrlEncode会将空格解析成+号
//json = json.replaceAll("\\+", "%20");
response.setHeader("Content-Length",json.length()+"");//返回Content-length标记，以便控件正确读取返回地址。
out.write(json);
%>