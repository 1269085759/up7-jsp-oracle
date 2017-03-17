<%@ page language="java" import="up7.*" pageEncoding="UTF-8"%><%@
	page contentType="text/html;charset=UTF-8"%><%@
	page import="org.apache.commons.lang.StringUtils" %><%

String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String uid = request.getParameter("uid");
String sign = request.getParameter("sign");
String idSvr = request.getParameter("idSvr");
String perSvr = request.getParameter("perSvr");
String lenSvr = request.getParameter("lenSvr");
String lenLoc = request.getParameter("lenLoc");

//参数为空
if(		StringUtils.isBlank(lenSvr)
	||StringUtils.isBlank(uid)
	||StringUtils.isBlank(idSvr)
	||StringUtils.isBlank(perSvr)
	)
{
	XDebug.Output("lenLoc", lenLoc);
	XDebug.Output("uid", uid);
	XDebug.Output("idSvr", idSvr);
	out.write("param is null\n");
	return;
}

//文件夹进度
DBFolder.update(Integer.parseInt(idSvr),perSvr,Long.parseLong(lenSvr),Integer.parseInt(uid));
%>
