<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%><%@ 
	page contentType="text/html;charset=UTF-8"%><%@ 
	page import="up7.*" %><%@
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
*/
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String fid 	= request.getParameter("fid");
if (StringUtils.isBlank(fid))
{
	return;
}

xdb_files inf = new xdb_files();
DBFile db = new DBFile();
//文件不存在
if(!db.find(Integer.parseInt(fid),inf))
{
	response.addHeader("Content-Range","0-0/0");
	response.addHeader("Content-Length","0");
	return;
}
File f = new File(inf.pathSvr);
long fileLen = f.length();
RandomAccessFile raf = new RandomAccessFile(inf.pathSvr,"r");
FileInputStream in = new FileInputStream( raf.getFD() );

String fileName = inf.nameLoc;//QQ.exe
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
	long dataToRead = fileLen;
	os = response.getOutputStream();
	String range = request.getHeader("Range");
	long rangePos = 0;
	if( !StringUtils.isBlank(range) )
	{
		/*
	        表示头500个字节：bytes=0-499
	        表示第二个500字节：bytes=500-999
	        表示最后500个字节：bytes=-500
	        表示500字节以后的范围：bytes=500-
	        第一个和最后一个字节：bytes=0-0,-1
	        同时指定几个范围：bytes=500-600,601-999
    	*/
		String[] rs = range.split("-");//bytes=10254
		int numBegin = rs[0].indexOf("=")+1;
		String pos = rs[0].substring(numBegin);
		long offset_begin = Long.parseLong(pos);
		in.skip(offset_begin);//定位索引
		
		//rangePos = Long.parseLong(pos);//起始位置
		String con_range;
		if(rs.length == 2)
		{
			String offset_end = rs[1];
			long totalLen = Long.parseLong(offset_end) - offset_begin;
			//只有1byte
			if(totalLen>1) ++totalLen;//
			dataToRead = totalLen;
			con_range = String.format("bytes %s-%s/%s",offset_begin,offset_end,fileLen);
		}
		else
		{
			dataToRead -= offset_begin;
			con_range = String.format("bytes %s-%s/%s",offset_begin,dataToRead,fileLen);
		}
		response.addHeader("Content-Range",con_range);
	}
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