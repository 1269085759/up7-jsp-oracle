<%@ page language="java" import="up7.*" pageEncoding="UTF-8"%><%@
	page contentType="text/html;charset=UTF-8"%><%@	
	page import="net.sf.json.*" %><%@
	page import="redis.clients.jedis.Jedis" %><%@
	page import="up7.*" %><%@
	page import="up7.model.*" %><%@
	page import="up7.biz.*" %><%@
	page import="up7.biz.redis.*" %><%@	
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
		2017-05-05 取消添加数据库操作，在文件上传完后添加到数据库
*/

String uid 			= request.getParameter("uid");
String lenLoc 		= request.getParameter("lenLoc");//数字化的文件大小。12021
String sizeLoc 		= request.getParameter("sizeLoc");//格式化的文件大小。10MB
String callback     = request.getParameter("callback");
String pathLoc		= request.getParameter("pathLoc");
String idSign		= request.getParameter("idSign");
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
fileSvr.idSign = idSign;
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
//文件块目录
BlockPathBuilder bpb = new BlockPathBuilder();
fileSvr.blockPath = bpb.root(idSign,fileSvr.pathSvr);

//添加到redis
Jedis j = JedisTool.con();
tasks taskSvr = new tasks(j);
taskSvr.uid = uid;
taskSvr.add(fileSvr);
j.close();

JSONObject obj = JSONObject.fromObject(fileSvr);
String json = obj.toString();
json = URLEncoder.encode(json,"UTF-8");//编码，防止中文乱码
json = json.replace("+","%20");
json = callback + "({\"value\":\"" + json + "\"})";//返回jsonp格式数据。
out.write(json);
%>