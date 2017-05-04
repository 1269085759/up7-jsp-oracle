package up7.biz;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;

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
	
	/**
	 * 保存文件块（文件上传逻辑）
	 * @param idSign
	 * @param rangeIndex
	 * @param rangeData
	 */
	public void savePart(String idSign,String rangeIndex,FileItem rangeData)
	{
		up7.biz.redis.file rf = new up7.biz.redis.file();
		String fpart = rf.getPartPath(idSign, rangeIndex);
		if(StringUtils.isBlank(fpart)) 
		{
			System.out.println("未找到文件块路径，保存文件块数据失败");
			return;
		}
		

		//创建文件夹：目录/guid/1
		File part_path = new File(fpart);		
		PathTool.createDirectory( part_path.getParent());//
		
		try {
			rangeData.write(new File(fpart));
		} catch (Exception e) {
			System.out.println("写入文件块数据错误,路径：".concat(fpart));
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 子文件块（文件夹上传逻辑）
	 * @param idSign
	 * @param fd_sign
	 * @param rangeIndex
	 * @param rangeData
	 * @throws IOException 
	 */
	public void savePart(String pathSvr,FileItem rangeData) throws IOException
	{
		if( StringUtils.isBlank(pathSvr))
		{
			System.out.println("未找到子文件块路径，保存子文件块数据失败");
			return;
		}
		
		//创建文件夹：目录/guid/1
		File part_path = new File(pathSvr);		
		PathTool.createDirectory( part_path.getParent());//		

		try {
			rangeData.write(new File(pathSvr));
		} catch (Exception e) {
			System.out.println("保存文件块错误,路径：".concat(pathSvr));
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}