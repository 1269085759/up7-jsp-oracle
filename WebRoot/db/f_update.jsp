<%@ page language="java" import="up7.*" pageEncoding="UTF-8"%><%@
	page contentType="text/html;charset=UTF-8"%><%@
	page import="org.apache.commons.lang.StringUtils" %><%

/*
	更新文件或文件夹进度信息
	在停止时调用
	在出错时调用
*/
	
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String uid 		= request.getParameter("uid");
String sign 	= request.getParameter("sign");
String idSign 	= request.getParameter("idSign");
String perSvr 	= request.getParameter("perSvr");
String lenSvr 	= request.getParameter("lenSvr");
String lenLoc 	= request.getParameter("lenLoc");

//参数为空
if (	StringUtils.isBlank(lenLoc)
	|| StringUtils.isBlank(uid)
	|| StringUtils.isBlank(idSign)
	)
{
	XDebug.Output("lenLoc", lenLoc);
	XDebug.Output("uid", uid);
	XDebug.Output("idSvr", idSign);
	out.write("param is null\n");
	return;
}

//文件夹进度
//DBFile db = new DBFile();
//db.f_process(Integer.parseInt(uid),Integer.parseInt(idSvr),0,Long.parseLong(lenSvr),perSvr,false);
//更新redis进度
up7.biz.redis.file rf = new up7.biz.redis.file();
rf.process(idSign,perSvr,lenSvr);
%>


