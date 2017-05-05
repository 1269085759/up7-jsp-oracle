<%@ page language="java" import="up7.*" pageEncoding="UTF-8"%><%@
	page contentType="text/html;charset=UTF-8"%><%@ 
	page import="org.apache.commons.lang.StringUtils" %><%@ 
	page import="redis.clients.jedis.Jedis" %><%@
	page import="up7.biz.folder.*" %><%@  
	page import="up7.biz.redis.*" %><%
/*
	此页面主要用来向数据库添加一条记录。
	一般在 HttpUploader.js HttpUploader_MD5_Complete(obj) 中调用
	更新记录：
		2012-05-24 完善
		2012-06-29 增加创建文件逻辑，
*/

String uid 		= request.getParameter("uid");
String idSign	= request.getParameter("idSign");
String callback = request.getParameter("callback");//jsonp

//返回值。1表示成功
int ret = 0;
if ( !StringUtils.isBlank(uid)
	&& !StringUtils.isBlank(idSign))
{
	Jedis j = JedisTool.con();
	fd_file_redis f_svr = new fd_file_redis();
	f_svr.read(j,idSign);//加载信息
	f_svr.merge();//合并文件
	j.del(idSign);//删除文件缓存
	j.close();
	
	tasks t = new tasks();
	t.del(idSign);
	
	DBFile db = new DBFile();
	db.complete(Integer.parseInt(uid),idSign);	
	ret = 1;
}
XDebug.Output("文件上传完毕");
%><%=callback + "(" + ret + ")"%>