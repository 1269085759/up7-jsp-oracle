<%@ page language="java" import="up7.*" pageEncoding="UTF-8"%><%@
	page contentType="text/html;charset=UTF-8"%><%@ 
	page import="org.apache.commons.lang.StringUtils" %><%@ 
	page import="redis.clients.jedis.Jedis" %><%@
	page import="up7.biz.folder.*" %><%@
	page import="up7.biz.*" %><%@
	page import="up7.model.*" %><%@    
	page import="up7.biz.redis.*" %><%
/*
	此页面主要用来向数据库添加一条记录。
	一般在 HttpUploader.js HttpUploader_MD5_Complete(obj) 中调用
	更新记录：
		2012-05-24 完善
		2012-06-29 增加创建文件逻辑，
		2017-05-05 文件上传完后添加到数据库
*/

String uid 		= request.getParameter("uid");
String idSign	= request.getParameter("idSign");
String merge	= request.getParameter("merge");
String callback = request.getParameter("callback");//jsonp

//返回值。1表示成功
int ret = 0;
if ( !StringUtils.isBlank(uid)
	&& !StringUtils.isBlank(idSign))
{
	Jedis j = JedisTool.con();
	FileRedis cache = new FileRedis(j);
	xdb_files f = cache.read(idSign);
	BlockMeger bm = new BlockMeger();
	//合并文件
	if(merge.equalsIgnoreCase("1")) bm.merge(f);
	j.del(idSign);//删除文件缓存
		
	//从任务列表（未完成）中删除
	tasks t = new tasks(j);
	t.uid = uid;
	t.del(idSign);
	j.close();
	
	DBFile db = new DBFile();
	db.addComplete(f);	
		
	ret = 1;
}
XDebug.Output("文件上传完毕");
%><%=callback + "(" + ret + ")"%>