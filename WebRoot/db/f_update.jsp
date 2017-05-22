<%@ page language="java" import="up7.*" pageEncoding="UTF-8"%><%@
	page contentType="text/html;charset=UTF-8"%><%@
	page import="redis.clients.jedis.Jedis" %><%@
	page import="up7.biz.redis.*" %><%@
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
String blockSize= request.getParameter("blockSize");

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

//更新redis进度
Jedis j = JedisTool.con();
FileRedis fr = new FileRedis(j);
fr.process(idSign,perSvr,lenSvr,"0",blockSize);
%>


