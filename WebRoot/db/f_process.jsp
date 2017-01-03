<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%><%@ 
	page contentType="text/html;charset=UTF-8"%><%@ 
	page import="Xproer.*" %><%@ 
	page import="org.apache.commons.lang.*" %><%@ 
	page import="java.net.URLDecoder" %><%@ 
	page import="java.net.URLEncoder" %><%@ 
	page import="java.io.*" %><%
/*
	此页面负责将文件块数据写入文件中。
	此页面一般由控件负责调用
	参数：
		uid
		fid
		md5
		fileSize
		rangePos
	更新记录：
		2012-04-12 更新文件大小变量类型，增加对2G以上文件的支持。
		2012-04-18 取消更新文件上传进度信息逻辑。
		2012-10-25 整合更新文件进度信息功能。减少客户端的AJAX调用。
		2014-07-23 优化代码。
		2015-03-19 客户端提供pathSvr，此页面减少一次访问数据库的操作。
*/
//String path = request.getContextPath();
//String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String uid 		= request.getParameter("uid");
String fid		= request.getParameter("fid");
String per		= request.getParameter("percent");
String lenSvr 	= request.getParameter("lenSvr");
 
//参数为空
if ( 	StringUtils.isBlank( uid )
	|| 	StringUtils.isBlank( fid )
	|| 	StringUtils.isBlank( per ) 
	|| 	StringUtils.isBlank(lenSvr))
{
	XDebug.Output("uid", uid);
	XDebug.Output("fid", fid);
	XDebug.Output("per", per);
	XDebug.Output("lenSvr",lenSvr);
	return;
}

DBFile.UpdateProcess(uid,fid,per,lenSvr);

%>