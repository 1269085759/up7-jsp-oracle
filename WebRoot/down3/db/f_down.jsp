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
	下载数据库中的文件。
	相关错误：
		getOutputStream() has already been called for this response
			解决方法参考：http://stackoverflow.com/questions/1776142/getoutputstream-has-already-been-called-for-this-response
	更新记录：
		2015-05-13 创建
		2017-05-06 增加业务逻辑数据，简化处理逻辑
*/
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String lenSvr 		= request.getHeader("f-lenSvr");
String nameLoc 		= request.getHeader("f-nameLoc");
String sizeSvr 		= request.getHeader("f-sizeSvr");
String pathSvr 		= request.getHeader("f-pathSvr");
String pathLoc 		= request.getHeader("f-pathLoc");
String blockIndex 	= request.getHeader("f-blockIndex");
String blockOffset	= request.getHeader("f-blockOffset");
String blockSize	= request.getHeader("f-blockSize");//逻辑块大小
String rangeSize	= request.getHeader("f-rangeSize");//当前请求的块大小
String lenLoc 		= request.getHeader("f-lenLoc");
String signSvr 		= request.getHeader("f-signSvr");
String fd_signSvr 	= request.getHeader("fd-signSvr");
String fd_lenLoc 	= request.getHeader("fd-lenLoc");
String fd_sizeLoc 	= request.getHeader("fd-sizeLoc");
String uid 			= request.getHeader("f-uid");
String percent		= request.getHeader("f-percent");

pathSvr	 = pathSvr.replaceAll("\\+","%20");
pathLoc	 = pathLoc.replaceAll("\\+","%20");
nameLoc	 = nameLoc.replaceAll("\\+","%20");
pathSvr	 = URLDecoder.decode(pathSvr,"UTF-8");//utf-8解码
pathLoc	 = URLDecoder.decode(pathLoc,"UTF-8");//utf-8解码
nameLoc	 = URLDecoder.decode(nameLoc,"UTF-8");//utf-8解码

if (	StringUtils.isEmpty(lenSvr)
	//||	StringUtils.isEmpty(sizeSvr)
	||	StringUtils.isEmpty(pathSvr)
	//||	StringUtils.isEmpty(nameLoc)
	||	StringUtils.isEmpty(pathLoc)
	||	StringUtils.isEmpty(blockIndex)
	||	StringUtils.isEmpty(lenLoc)
	//||	StringUtils.isEmpty(signSvr)
	//||	StringUtils.isEmpty(uid)
	||	StringUtils.isEmpty(percent)
	)
{
	System.out.println("lenSvr:".concat(lenSvr));
	System.out.println("nameLoc:".concat(nameLoc));
	System.out.println("sizeSvr:".concat(sizeSvr));
	System.out.println("pathSvr:".concat(pathSvr));
	System.out.println("pathLoc:".concat(pathLoc));
	System.out.println("blockIndex:".concat(blockIndex));
	System.out.println("blockSize:".concat(blockSize));
	System.out.println("lenLoc:".concat(lenLoc));
	System.out.println("signSvr:".concat(signSvr));
	System.out.println("percent:".concat(percent));
	System.out.println("f_down.jsp 业务逻辑参数为空。");
	return;
}

DnFileInf fileSvr = new DnFileInf();
fileSvr.signSvr = signSvr;
//子文件项时仅保存文件夹信息
if(fd_signSvr != null) fileSvr.signSvr = fd_signSvr;
fileSvr.uid = uid==null?0:Integer.parseInt(uid);
fileSvr.lenLoc = Long.parseLong(lenLoc);
if(fd_lenLoc != null) fileSvr.lenLoc = Long.parseLong(fd_lenLoc);
fileSvr.lenSvr = Long.parseLong(lenSvr);
fileSvr.sizeSvr = sizeSvr==null?"":sizeSvr;
fileSvr.perLoc = percent;
fileSvr.pathSvr = pathSvr;
fileSvr.pathLoc = pathLoc;
fileSvr.nameLoc = nameLoc==null?"":nameLoc;

//添加到缓存
Jedis j = JedisTool.con();
tasks svr = new tasks(uid,j);
svr.add(fileSvr);
j.close();

//文件不存在（未创建下载任务）
if( fileSvr == null)
{
	response.addHeader("Content-Range","0-0/0");
	response.addHeader("Content-Length","0");
	return;
}
long fileLen = fileSvr.lenSvr;
RandomAccessFile raf = new RandomAccessFile(fileSvr.pathSvr,"r");
FileInputStream in = new FileInputStream( raf.getFD() );

String fileName = fileSvr.nameLoc;//QQ.exe
fileName = URLEncoder.encode(fileName,"UTF-8");
fileName = fileName.replaceAll("\\+","%20");
response.setContentType("application/x-download");
response.setHeader("Pragma","No-cache");  
response.setHeader("Cache-Control","no-cache");  
response.setDateHeader("Expires", 0);
response.addHeader("Content-Disposition","attachment;filename=" + fileName);

OutputStream os = null;
try
{
	long dataToRead = Long.parseLong(rangeSize);
	os = response.getOutputStream();
	long offset_begin = Long.parseLong(blockOffset);
	in.skip(offset_begin);//定位索引
	response.addHeader("Content-Length",Long.toString(dataToRead) );
	
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
catch(Exception e){}
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