package up7.biz;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import org.apache.commons.fileupload.FileItem;

import redis.clients.jedis.Jedis;
import up7.JedisTool;
import up7.PathTool;
import up7.model.xdb_files;

/*
 * 文件操作
 * 
 * */
public class file {
	String idSign;//
	
	public void combin()
	{
		up7.biz.redis.file f = new up7.biz.redis.file();
	}
	
	public void savePart(String idSign,String rangeIndex,FileItem rangeData)
	{
		up7.biz.redis.file rf = new up7.biz.redis.file();
		xdb_files fdata = rf.read(idSign);
		if(fdata == null) 
		{
			System.out.println("未找到文件信息，保存文件块失败");
			return;
		}
	}
	
	/**
	 * 子文件块
	 * @param idSign
	 * @param fd_sign
	 * @param rangeIndex
	 * @param rangeData
	 * @throws IOException 
	 */
	public void savePart(String idSign,String fd_sign,String rangeIndex,FileItem rangeData) throws IOException
	{
		up7.biz.redis.file rf = new up7.biz.redis.file();
		xdb_files fdata = rf.read(idSign);
		if(fdata == null) 
		{
			System.out.println("未找到文件信息，保存文件块失败");
			return;
		}
		xdb_files fd = rf.read(fd_sign);
		if(fd==null)
		{
			System.out.println("未找到根目录信息，保存文件块失败");
			return;			
		}
		
		if(fdata.pathSvr == null)
		{
			fdata.pathSvr = fdata.pathLoc.replace(fd.pathLoc, fd.pathSvr);
			fdata.pathSvr = fdata.pathSvr.replaceAll("\\", "/");
			rf.create(fdata);
		}
		File f_svr = new File(fdata.pathSvr);
		String path_svr = f_svr.getParent();//d:\\soft
		path_svr.concat("/");
		path_svr.concat(fdata.idSign);
		path_svr.concat("/");
		path_svr.concat(rangeIndex);
		
		//创建文件夹：目录/guid/1
		File part_path = new File(path_svr);		
		PathTool.createDirectory( part_path.getParent());//
		

		try {
			rangeData.write(new File(path_svr));
		} catch (Exception e) {
			System.out.println("保存文件块错误");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}