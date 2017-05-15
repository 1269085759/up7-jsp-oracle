<%@ page language="java" import="up7.DBFile" pageEncoding="UTF-8"%><%@
	page contentType="text/html;charset=UTF-8"%><%@ 
	page import="java.io.*" %><%@
	page import="up7.FileBlockWriter" %><%@
	page import="up7.biz.folder.*" %><%@
	page import="up7.biz.*" %><%@
	page import="up7.model.*" %><%@
	page import="up7.biz.redis.*" %><%@
	page import="up7.FolderCache" %><%@
	page import="up7.XDebug" %><%@
	page import="up7.*" %><%@
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
		RangePos
		fd_idSvr
		fd_lenSvr
	更新记录：
		2012-04-12 更新文件大小变量类型，增加对2G以上文件的支持。
		2012-04-18 取消更新文件上传进度信息逻辑。
		2012-10-25 整合更新文件进度信息功能。减少客户端的AJAX调用。
		2014-07-23 优化代码。
		2016-04-09 优化文件存储逻辑，增加更新文件夹进度逻辑
		2017-05-14 优化参数传递方式，改为使用head方式传递参数
*/
//String path = request.getContextPath();
//String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String uid 			= request.getHeader("f-uid");// 		= request.getParameter("uid");
String idSign 		= request.getHeader("f-idSign");// 		= request.getParameter("fid");
String perSvr 		= request.getHeader("f-perSvr");// 	= request.getParameter("FileSize");
String lenSvr		= request.getHeader("f-lenSvr");
String lenLoc		= request.getHeader("f-lenLoc");
String nameLoc		= request.getHeader("f-nameLoc");
String pathLoc		= request.getHeader("f-pathLoc");
String sizeLoc		= request.getHeader("f-sizeLoc");
String f_pos 		= request.getHeader("f-RangePos");// 	= request.getParameter("RangePos");
String rangeIndex	= request.getHeader("f-rangeIndex");
String rangeCount	= request.getHeader("f-rangeCount");
String rangeSize	= request.getHeader("f-rangeSize");//逻辑块大小信息，提供给下载使用
String complete		= "false";//文件块是否已发送完毕（最后一个文件块数据）
String fd_idSign	= request.getHeader("fd-idSign");
String fd_lenSvr	= request.getHeader("fd-lenSvr");
String fd_perSvr	= request.getHeader("fd-perSvr");
pathLoc	= pathLoc.replace("+","%20");
pathLoc	= URLDecoder.decode(pathLoc,"UTF-8");//utf-8解码
nameLoc	= nameLoc.replace("+","%20");
nameLoc	= URLDecoder.decode(nameLoc,"UTF-8");//utf-8解码
 
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
}

//参数为空 
if(	   StringUtils.isBlank( lenSvr )
	|| StringUtils.isBlank( uid )
	|| StringUtils.isBlank( idSign )
	|| StringUtils.isBlank( f_pos ))
{
	System.out.println("lenSvr:".concat(lenSvr));
	XDebug.Output("uid", uid);
	XDebug.Output("idSign", idSign);
	XDebug.Output("f_pos", f_pos);
	XDebug.Output("param is null");
	return;
}
	
	Jedis j = JedisTool.con();
	FileRedis cache = new FileRedis(j);
		
	//文件块
	if(StringUtils.isBlank(fd_idSign))
	{
		//从缓存中取文件信息
		xdb_files fileSvr = cache.read(idSign);
		
		//生成文件块路径
		BlockPathBuilder bpb = new BlockPathBuilder();
		String partPath = bpb.part(idSign,rangeIndex,fileSvr.pathSvr);
		String ps = cache.getPartPath(idSign, rangeIndex);
		
		//保存文件块
		up7.biz.file_part part = new up7.biz.file_part();
		part.save(ps,rangeFile);
		
		//更新文件进度
		if(f_pos == "0") cache.process(idSign,perSvr,lenSvr,rangeCount,rangeSize);
	}//子文件块
	else
	{
		//取根文件夹信息
		xdb_files fd = cache.read(fd_idSign);
		
		//向redis添加子文件信息
		up7.model.xdb_files f_child = new up7.model.xdb_files();
		f_child.blockCount = Integer.parseInt(rangeCount);
		f_child.idSign = idSign;
		File path_loc = new File(pathLoc);
		f_child.nameLoc = path_loc.getName();
		f_child.nameSvr = nameLoc;
		f_child.lenLoc = Long.parseLong( lenLoc );		
		f_child.sizeLoc = f_child.lenLoc<1024 ? PathTool.getDataSize(f_child.lenLoc) : sizeLoc;
		f_child.pathLoc = pathLoc.replace("\\","/");//路径规范化处理
		f_child.pathSvr = pathLoc.replace(fd.pathLoc,fd.pathSvr);
		f_child.pathSvr = f_child.pathSvr.replace('\\','/'); 
		f_child.pathRel = pathLoc.replace(fd.pathLoc.concat("\\"),"");
		f_child.rootSign = fd_idSign;
		f_child.blockCount = Integer.parseInt(rangeCount);
		//子文件块路径
        BlockPathBuilder bpb = new BlockPathBuilder();
		f_child.blockPath = bpb.rootFd(f_child,rangeIndex,fd);
		String partPath = PathTool.combine(f_child.blockPath,rangeIndex+".part");
				
		//将文件信息添加到缓存,文件夹上传完毕后会将缓存数据写入数据库
		cache.create(f_child);
		
		//添加到文件夹
		up7.biz.folder.fd_files_redis root = new up7.biz.folder.fd_files_redis(j,fd_idSign);		
		root.add(idSign);
		
		
		//保存块
		up7.biz.file_part part = new up7.biz.file_part();
		part.save(partPath,rangeFile);		


		//更新文件夹进度
		if(f_pos == "0") cache.process(fd_idSign,fd_perSvr,fd_lenSvr,"0","0");
	}
	j.close();
		
	out.write("ok");
 %>