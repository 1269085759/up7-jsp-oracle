<%@ page language="java" import="up7.*" pageEncoding="UTF-8"%><%@
	page contentType="text/html;charset=UTF-8"%><%@
	page import="redis.clients.jedis.Jedis" %><%@
	page import="up7.biz.redis.*" %><%@	
	page import="org.apache.commons.lang.StringUtils" %><%

String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String idSign = request.getParameter("idSign");
String perSvr = request.getParameter("perSvr");
String lenSvr = request.getParameter("lenSvr");

//参数为空
if(	  StringUtils.isBlank(lenSvr)
	||StringUtils.isBlank(idSign)
	||StringUtils.isBlank(perSvr)
	)
{	
	XDebug.Output("idSign", idSign);
	XDebug.Output("perSvr", perSvr);
	XDebug.Output("lenSvr", lenSvr);
	out.write("param is null\n");
	return;
}

//文件夹进度
Jedis j = JedisTool.con();
FileRedis fr = new FileRedis(j);
fr.process(idSign,perSvr,lenSvr,"0","0");
%>
