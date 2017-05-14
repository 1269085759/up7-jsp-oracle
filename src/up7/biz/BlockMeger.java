package up7.biz;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import up7.PathTool;
import up7.model.xdb_files;

/**
 * 文件块合并器
 * @author zy-qwl
 *
 */
public class BlockMeger {
	public void merge(xdb_files fileSvr)
	{
		File fp = new File(fileSvr.pathSvr);
		if(fp.exists()) return;//文件已存在
		
		//创建文件夹
		PathTool.createDirectory( fp.getParent());//		
		
		RandomAccessFile dstFile;
		try 
		{
			dstFile = new RandomAccessFile(fileSvr.pathSvr, "rw");
			FileChannel fw = dstFile.getChannel();		
			
			fp = new File(fileSvr.blockPath);
			File[] parts = fp.listFiles();		//
			ByteBuffer trunk = ByteBuffer.allocateDirect(1048576);//1mb		
			
			for(Integer i = 0,l = parts.length;i<l;++i)
			{
				
				String partName = PathTool.combine(fileSvr.blockPath, Integer.toString(i+1).concat(".part"));
				System.out.println(partName);
				RandomAccessFile partRead = new RandomAccessFile(partName,"rw");
				FileChannel partC = partRead.getChannel();
				while(partC.read(trunk) != -1)
				{
					trunk.flip();
					while(trunk.hasRemaining()) fw.write(trunk);
					//fw.write(trunk);
					//fw.force(true);
					trunk.clear();
				}
				partC.close();
				partRead.close();
			}
			fw.close();
			dstFile.close();
			
			//删除所有文件块
			for(File f : parts) f.delete();
						
			//删除文件块目录
			File partFd = new File(fileSvr.blockPath);
			partFd.delete();
		
		} catch (FileNotFoundException e) {
			System.out.println("文件不存在");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("合并文件块错误");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
