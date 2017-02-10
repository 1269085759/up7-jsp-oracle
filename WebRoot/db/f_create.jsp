<%@ page language="java" import="up7.*" pageEncoding="UTF-8"%><%@
	page contentType="text/html;charset=UTF-8"%><%@	
	page import="net.sf.json.*" %><%@
	page import="up7.*" %><%@
	page import="up7.model.*" %><%@
	page import="up7.biz.*" %><%@	
	page import="org.apache.commons.lang.StringUtils" %><%@
	page import="java.net.URLDecoder" %><%@
	page import="java.net.URLEncoder" %><%/*
	所有单个文件均以md5模式存储。
	更新记录：
		2012-05-24 完善
		2012-06-29 增加创建文件逻辑，
		2015-07-30 取消文件夹层级结构存储规则，改为使用日期存储规则，文件夹层级结构仅保存在数据库中。
		2016-01-07 返回值改为JSON
		2016-04-09 完善逻辑。
*/

String uid 			= request.getParameter("uid");
String lenLoc 		= request.getParameter("lenLoc");//数字化的文件大小。12021
String sizeLoc 		= request.getParameter("sizeLoc");//格式化的文件大小。10MB
String callback     = request.getParameter("callback");
String pathLoc		= request.getParameter("pathLoc");
pathLoc			= pathLoc.replace("+","%20");
pathLoc			= URLDecoder.decode(pathLoc,"UTF-8");//utf-8解码

//参数为空
if (	StringUtils.isBlank(uid)
	&& StringUtils.isBlank(sizeLoc))
{
	out.write(callback + "({\"value\":null,\"inf\":\"参数为空，请检查uid,sizeLoc参数。\"})");
	return;
}

xdb_files fileSvr= new xdb_files();
fileSvr.f_fdChild = false;
fileSvr.uid = Integer.parseInt(uid);
fileSvr.nameLoc = PathTool.getName(pathLoc);
fileSvr.pathLoc = pathLoc;
fileSvr.lenLoc = Long.parseLong(lenLoc);
fileSvr.sizeLoc = sizeLoc;
fileSvr.deleted = false;
fileSvr.nameSvr = fileSvr.nameLoc;

//所有单个文件均以guid方式存储
PathGuidBuilder pb = new PathGuidBuilder();
fileSvr.pathSvr = pb.genFile(fileSvr.uid,fileSvr);

	DBFile db = new DBFile();
	xdb_files fileExist = new xdb_files();
	
	fileSvr.idSvr = db.Add(fileSvr);
	
	FileResumerPart fr = new FileResumerPart();
	fr.CreateFile(fileSvr.pathSvr);		

JSONObject obj = JSONObject.fromObject(fileSvr);
String json = obj.toString();
json = URLEncoder.encode(json,"UTF-8");//编码，防止中文乱码
json = json.replace("+","%20");
json = callback + "({\"value\":\"" + json + "\"})";//返回jsonp格式数据。
out.write(json);%>