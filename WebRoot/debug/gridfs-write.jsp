<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%><%@ 
	page contentType="text/html;charset=UTF-8"%><%@ 
	page import="up7.*" %><%@
	page import="up7.model.*" %><%@
	page import="up7.biz.folder.*" %><%@
	page import="java.sql.*" %><%@
	page import="oracle.sql.*" %><%@
	page import="oracle.jdbc.*" %><%@ 
	page import="org.apache.commons.lang.StringUtils" %><%@
	page import="org.apache.commons.fileupload.FileItem" %><%@
	page import="org.apache.commons.fileupload.FileItemFactory" %><%@
	page import="org.apache.commons.fileupload.FileUploadException" %><%@
	page import="org.apache.commons.fileupload.disk.DiskFileItemFactory" %><%@
	page import="org.apache.commons.fileupload.servlet.ServletFileUpload" %><%@
	page import="java.net.URLDecoder" %><%@ 
	page import="net.sf.json.JSONArray" %><%@ 
	page import="net.sf.json.JSONObject" %><%@ 
	page import="net.sf.json.util.JSONUtils" %><%@ 
	page import="com.google.gson.Gson" %><%@ 
	page import="com.google.gson.GsonBuilder" %><%@ 
	page import="com.google.gson.annotations.SerializedName" %><%@
	page import="redis.clients.jedis.Jedis" %><%@ 
	page import="java.io.*" %><%/*
	此页面主要用来向数据库添加一条记录。
	一般在 HttpUploader.js HttpUploader_MD5_Complete(obj) 中调用
	更新记录：
		2012-05-24 完善
		2012-06-29 增加创建文件逻辑，
*/
boolean isMultipart = ServletFileUpload.isMultipartContent(request);
FileItemFactory factory = new DiskFileItemFactory();   
ServletFileUpload upload = new ServletFileUpload(factory);
List files = null;
try 
{
	files = upload.parseRequest(request);
} 
catch (FileUploadException e) 
{// 解析文件数据错误  
    out.println("read file data error:" + e.toString());
    return;
   
}

FileItem rangeFile = null;
// 得到所有上传的文件
Iterator fileItr = files.iterator();
// 循环处理所有文件
while (fileItr.hasNext()) 
{
	// 得到当前文件
	rangeFile = (FileItem) fileItr.next();
	// 忽略简单form字段而不是上传域的文件域(<input type="text" />等)
	if(rangeFile.isFormField())
	{
	}
	else 
	{
		break;
	}
}

//连接本地的 Redis 服务
GridFSSvr svr = new GridFSSvr ();
	out.write( svr.save(rangeFile.getInputStream(),"test") );

%>