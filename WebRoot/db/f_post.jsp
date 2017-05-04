<%@ page language="java" import="up7.DBFile" pageEncoding="UTF-8"%><%@
	page contentType="text/html;charset=UTF-8"%><%@ 
	page import="up7.FileBlockWriter" %><%@
	page import="up7.biz.folder.*" %><%@
	page import="up7.FolderCache" %><%@
	page import="up7.XDebug" %><%@
	page import="org.apache.commons.fileupload.FileItem" %><%@
	page import="org.apache.commons.fileupload.FileItemFactory" %><%@
	page import="org.apache.commons.fileupload.FileUploadException" %><%@
	page import="org.apache.commons.fileupload.disk.DiskFileItemFactory" %><%@
	page import="org.apache.commons.fileupload.servlet.ServletFileUpload" %><%@
	page import="org.apache.commons.lang.StringUtils" %><%@
	page import="java.net.URLDecoder"%><%@ 
	page import="java.util.Iterator"%><%@
	page import="redis.clients.jedis.Jedis"%><%@ 
	page import="java.util.List"%><%
/*
	此页面负责将文件块数据写入文件中。
	此页面一般由控件负责调用
	参数：
		uid
		idSvr
		md5
		lenSvr
		pathSvr
		RangePos
		fd_idSvr
		fd_lenSvr
	更新记录：
		2012-04-12 更新文件大小变量类型，增加对2G以上文件的支持。
		2012-04-18 取消更新文件上传进度信息逻辑。
		2012-10-25 整合更新文件进度信息功能。减少客户端的AJAX调用。
		2014-07-23 优化代码。
		2015-03-19 客户端提供pathSvr，此页面减少一次访问数据库的操作。
		2016-04-09 优化文件存储逻辑，增加更新文件夹进度逻辑
*/
//String path = request.getContextPath();
//String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String uid 			= "";// 		= request.getParameter("uid");
String idSign 		= "";// 		= request.getParameter("fid");
String perSvr 		= "";// 	= request.getParameter("FileSize");
String lenSvr		= "";
String lenLoc		= "";
String f_pos 		= "";// 	= request.getParameter("RangePos");
String rangeIndex	= "1";
String complete		= "false";//文件块是否已发送完毕（最后一个文件块数据）
String fd_idSign	= "";
String fd_lenSvr	= "";
String fd_perSvr	= "0%";
String pathSvr		= "";//add(2015-03-19):服务器文件路径由客户端提供，此页面减少一次访问数据库的操作。
 
// Check that we have a file upload request
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
		String fn = rangeFile.getFieldName();
		String fv = rangeFile.getString(); 
		if(fn.equals("uid")) uid = fv;
		if(fn.equals("idSign")) idSign = fv;
		if(fn.equals("lenSvr")) lenSvr = fv;
		if(fn.equals("lenLoc")) lenLoc = fv;
		if(fn.equals("perSvr")) perSvr = fv;
		if(fn.equals("RangePos")) f_pos = fv;
		if(fn.equals("rangeIndex")) rangeIndex = fv;
		if(fn.equals("complete")) complete = fv;
		if(fn.equals("pathSvr")) pathSvr = fv;//add(2015-03-19):
		if(fn.equals("fd-idSign")) fd_idSign = fv;
		if(fn.equals("fd-lenSvr")) fd_lenSvr = fv;
		if(fn.equals("fd-perSvr")) fd_perSvr = fv;
	}
	else 
	{
		break;
	}
}

//参数为空 
if(	 StringUtils.isBlank( lenSvr )
	|| StringUtils.isBlank( uid )
	|| StringUtils.isBlank( idSign )
	|| StringUtils.isBlank( f_pos ) 
	|| StringUtils.isBlank(pathSvr))
{
	XDebug.Output("uid", uid);
	XDebug.Output("idSign", idSign);
	XDebug.Output("f_pos", f_pos);
	XDebug.Output("param is null");
	return;
}

	pathSvr	= pathSvr.replace("+","%20");	
	pathSvr = URLDecoder.decode(pathSvr,"UTF-8");//utf-8解码//客户端使用的是encodeURIComponent编码，

	XDebug.Output("perSvr", perSvr);
	XDebug.Output("lenSvr", lenSvr);
	XDebug.Output("lenLoc", lenLoc);
	XDebug.Output("uid", uid);
	XDebug.Output("idSign", idSign);
	XDebug.Output("f_pos", f_pos);
	XDebug.Output("complete", complete);
	XDebug.Output("pathSvr",pathSvr);
	XDebug.Output("fd_idSign",fd_idSign);
	XDebug.Output("fd_lenSvr",fd_lenSvr);
	XDebug.Output("fd_perSvr",fd_perSvr);
	boolean cmp = StringUtils.equals(complete,"true");
	
	//保存文件块数据
	FileBlockWriter res = new FileBlockWriter();
	res.write(pathSvr,Long.parseLong(f_pos),rangeFile);
	
	up7.biz.file part = new up7.biz.file();
	Boolean folder = false;
	//文件块
	if(StringUtils.isBlank(fd_idSign))
	{
		part.savePart(idSign,rangeIndex,rangeFile);
	}//子文件块
	else
	{
		part.savePart(idSign,fd_idSign,rangeIndex,rangeFile);
		folder  = true;
	}
	
	//第一块数据
	if(Long.parseLong(f_pos) == 0 )
	{
		//更新文件进度
		up7.biz.redis.file rf = new up7.biz.redis.file();
		rf.process(idSign,perSvr,lenSvr);
	
		//更新文件夹进度
		if(folder) rf.process(fd_idSign,fd_perSvr,fd_lenSvr);
	}
			
	out.write("ok");
%>