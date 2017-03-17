package up7;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

/**
 *文件续传类，负责将文件块写入硬盘中
 */
public class FileBlockWriter {
	public long			m_RangeSize;	//当前文件块大小。由SaveFileRange()负责赋值
	public long 		m_RangePos;		//文件块起始位置。一般在ajax_post.jsp中赋值
	public String		m_pathSvr;	//远程文件路径。D:\\webapps\\upload\\2012\\05\\24\\QQ2012.exe
	HttpServletRequest 	m_hsr;
	ServletContext		m_sc;
	
	public FileBlockWriter()
	{
	}
	
	/*
	 * 参数：
	 * 	sc	this.getServletContext()
	 * 	hsr	request
	 * */
	public FileBlockWriter(ServletContext sc,HttpServletRequest hsr)
	{
		this.m_sc = sc;
		this.m_hsr = hsr;
	}

	//创建文件
	public void CreateFile()
	{
		try 
		{
		    RandomAccessFile raf = new RandomAccessFile(this.m_pathSvr, "rw");
		    raf.setLength(1);//
		    raf.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	/**
	 * 创建文件，一般在 ajax_create_fid.jsp 中调用。
	 * 这样做是为了避免多个用户同时上传相同文件时，频繁创建文件的问题。
	 * @param path	远程文件完整路径。例：d:\\soft\\qq.exe
	 * @param strLen 远程文件大小，以字节为单位。1201254
	 */
	public Boolean make(String path,long len)
	{
		Boolean ret = false;
		try 
		{  
			File fp = new File(path);
			PathTool.createDirectory( fp.getParent());//
			RandomAccessFile raf = new RandomAccessFile(path, "rw");
			raf.setLength(len);
			raf.close();
			ret = true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			//wrtLock.unlock();
		}
		return ret;
	}
	
	/*
	 * 创建临时文件名称。
	 * 逻辑：
	 * 	临时文件名称 = 远程文件名称 + tmp + 当前时间毫秒数
	 * 说明：
	 * 	加当前时间毫秒是为了防止多个用户同时上传相同文件时，创建临时文件会产生冲突的问题。
	 * 返回值：
	 * 	D:\\webapps\\upload\\2012\\05\\24\\QQ2012.exe.tmp201205241990
	 * */
	public String CreateTmpFileName()
	{
		SimpleDateFormat fmtDD = new SimpleDateFormat("dd");
		SimpleDateFormat fmtMM = new SimpleDateFormat("MM");
		SimpleDateFormat fmtYY = new SimpleDateFormat("yyyy");
		SimpleDateFormat fmtSS = new SimpleDateFormat("SSSS");
		
		Date date = new Date();
		String strDD = fmtDD.format(date);
		String strMM = fmtMM.format(date);
		String strYY = fmtYY.format(date);
		String strSS = fmtSS.format(date);
		
		String name = this.m_pathSvr + ".tmp" + strYY+strMM+strDD+strSS;
		return name;
		
	}
	
	/*
	 * 写入文件块数据
	 * 多线程参考：
	 * 	http://bbs.csdn.net/topics/80382727
	 * 
	 * */
	public synchronized void write(String path,long offset,FileItem rangeFile)
	{
		try {
			InputStream stream = rangeFile.getInputStream();			
			byte[] data = new byte[(int)rangeFile.getSize()];
			stream.read(data);//实际读取的大小
			stream.close();			
			
			//bug:在部分服务器中会出现错误：(另一个程序正在使用此文件，进程无法访问。)
			RandomAccessFile raf = new RandomAccessFile(path,"rw");
			//定位文件位置
			raf.seek(offset);
			raf.write(data);

			raf.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
