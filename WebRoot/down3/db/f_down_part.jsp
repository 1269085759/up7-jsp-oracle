<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%><%@ 
	page contentType="text/html;charset=UTF-8"%><%@ 
	page import="up7.*" %><%@
	page import="redis.clients.jedis.Jedis" %><%@
	page import="down3.biz.redis.*" %><%@
	page import="down3.model.*" %><%@
	page import="up7.model.*" %><%@ 
	page import="java.net.URLDecoder" %><%@ 
	page import="java.net.URLEncoder" %><%@ 
	page import="org.apache.commons.lang.*" %><%@ 
	page import="com.google.gson.FieldNamingPolicy" %><%@ 
	page import="com.google.gson.Gson" %><%@ 
	page import="com.google.gson.GsonBuilder" %><%@ 
	page import="com.google.gson.annotations.SerializedName" %><%@ 
	page import="java.io.*" %><%/*
	下载文件块，针对于合并后的文件处理
	相关错误：
		getOutputStream() has already been called for this response
			解决方法参考：http://stackoverflow.com/questions/1776142/getoutputstream-has-already-been-called-for-this-response
	更新记录：
		2015-05-13 创建
		2017-05-06 增加业务逻辑数据，简化处理逻辑
		2017-05-14 优化逻辑
		2017-05-21 支持下载文件块（未合并）
*/
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String lenSvr 		= request.getHeader("f-lenSvr");
String nameLoc 		= request.getHeader("f-nameLoc");
String sizeLoc 		= request.getHeader("f-sizeLoc");
String blockPath 	= request.getHeader("f-blockPath");
String blockIndex 	= request.getHeader("f-blockIndex");//基于1
String blockOffset	= request.getHeader("f-blockOffset");//块偏移，基于0
String blockSize	= request.getHeader("f-blockSize");//逻辑块大小
String rangeSize	= request.getHeader("f-rangeSize");//当前请求的块大小
String lenLoc 		= request.getHeader("f-lenLoc");
String signSvr 		= request.getHeader("f-signSvr");
String percent		= request.getHeader("f-percent");
String fd_signSvr 	= request.getHeader("fd-signSvr");
String fd_lenLoc 	= request.getHeader("fd-lenLoc");
String fd_sizeLoc 	= request.getHeader("fd-sizeLoc");
String fd_percent 	= request.getHeader("fd-percent");
if(!StringUtils.isEmpty(fd_sizeLoc)) sizeLoc = fd_sizeLoc;
if(!StringUtils.isEmpty(fd_signSvr)) signSvr = fd_signSvr;
if(!StringUtils.isEmpty(fd_lenLoc)) lenLoc = fd_lenLoc;
if(!StringUtils.isEmpty(fd_percent)) percent = fd_percent;

blockPath= PathTool.url_decode(blockPath);

if (	StringUtils.isEmpty(lenSvr)
	||	StringUtils.isEmpty(blockIndex)
	||	StringUtils.isEmpty(lenLoc)
	||	StringUtils.isEmpty(percent)
	)
{
	System.out.println("lenSvr:".concat(lenSvr));
	System.out.println("nameLoc:".concat(nameLoc));
	System.out.println("sizeLoc:".concat(sizeLoc));
	System.out.println("blockIndex:".concat(blockIndex));
	System.out.println("blockSize:".concat(blockSize));
	System.out.println("lenLoc:".concat(lenLoc));
	System.out.println("signSvr:".concat(signSvr));
	System.out.println("percent:".concat(percent));
	System.out.println("f_down.jsp 业务逻辑参数为空。");
	response.setStatus(500);
	return;
}

//更新进度信息
Jedis j = JedisTool.con();
FileRedis fr = new FileRedis(j);
fr.process(signSvr,percent,lenLoc,sizeLoc);
j.close();

String partPath = PathTool.combine(blockPath,blockIndex.concat(".part"));
System.out.println(partPath);
RandomAccessFile raf = new RandomAccessFile(partPath,"r");
FileInputStream in = new FileInputStream( raf.getFD() );

long dataToRead = Long.parseLong(rangeSize) - Long.parseLong(blockOffset);
String fileName = nameLoc;//QQ.exe
fileName = URLEncoder.encode(fileName,"UTF-8");
fileName = fileName.replace("+","%20");
response.setContentType("application/x-download");
response.setHeader("Pragma","No-cache");  
response.setHeader("Cache-Control","no-cache");  
response.setDateHeader("Expires", 0);
response.addHeader("Content-Disposition","attachment;filename=" + fileName);
response.addHeader("Content-Length",Long.toString(dataToRead) );

OutputStream os = null;
try
{
	os = response.getOutputStream();
	long offset_begin = Long.parseLong(blockOffset);
	in.skip(offset_begin);//定位索引
	
	byte[] buffer = new byte[1048576];//1MB
	int len = 0;	
	
	while( dataToRead > 0 )
	{
		len = in.read(buffer,0,Math.min(1048576,(int)dataToRead));		
		os.write(buffer, 0, len);
		os.flush();
		response.flushBuffer();
		buffer = new byte[1048576];
		dataToRead = dataToRead - len;
	}
	os.close();		
	os = null;
	
	out.clear();
	out = pageContext.pushBody();
}
catch(Exception e){response.setStatus(500);}
finally
{	
	if(os != null)
	{
		os.close();		
		os = null;
	}
	out.clear();
	out = pageContext.pushBody();
}
in.close();
in = null;%>