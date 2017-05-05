<%@ page language="java" import="up7.*" pageEncoding="UTF-8"%><%@
	page contentType="text/html;charset=UTF-8"%><%@
	page import="up7.biz.*" %><%@
	page import="up7.biz.redis.*" %><%@
	page import="redis.clients.jedis.Jedis" %><%@
	page import="org.apache.commons.lang.StringUtils" %><%@
	page import="java.net.URLEncoder" %><%
/*
	获取所有未上传完的文件和文件夹
	更新记录：
		2012-05-24 完善
		2012-06-29 增加创建文件逻辑，
		2016-01-08 规范JSON返回值格式
		2016-04-09 更新加载未完成列表的逻辑

	JSON格式化工具：http://tool.oschina.net/codeformat/json
*/
String uid = request.getParameter("uid");
String cbk = request.getParameter("callback");//jsonp

if( uid.length() > 0 )
{
	Jedis j = JedisTool.con();
	tasks t = new tasks(j);
	t.uid = uid;
	String json = t.toJson();
	j.close();
	if(!StringUtils.isBlank(json))
	{
		json = URLEncoder.encode(json,"utf-8");
		json = json.replace("+","%20");
		out.write( cbk + "({\"value\":\""+json + "\"})" );
		return;
	}
}
out.write(cbk + "({\"value\":null})");
%>