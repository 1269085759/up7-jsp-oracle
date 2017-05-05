package up7.biz.folder;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import redis.clients.jedis.Jedis;
import up7.PathTool;

public class fd_file_redis extends fd_file
{
	public void read(Jedis j,String idSign)
	{
		this.idSign = idSign;
		if( !j.exists(idSign)) return;
		
		this.lenLoc = Long.parseLong( j.hget(idSign, "lenLoc") );
		this.lenSvr = Long.parseLong( j.hget(idSign, "lenSvr") );
		this.sizeLoc = j.hget(idSign, "sizeLoc");
		this.pathLoc = j.hget(idSign, "pathLoc");
		this.pathSvr = j.hget(idSign, "pathSvr");
		this.perSvr = j.hget(idSign, "perSvr");
		this.nameLoc = j.hget(idSign, "nameLoc");
		this.nameSvr = j.hget(idSign, "nameSvr");
		this.pidSign = j.hget(idSign, "pidSign");
		this.rootSign = j.hget(idSign, "rootSign");
		this.complete = j.hget(idSign, "complete")=="true";
		this.sign = j.hget(idSign, "sign");
	}
	
	public void write(Jedis j)
	{
		j.del(this.idSign);		
		
		j.hset(this.idSign,"lenLoc", Long.toString(this.lenLoc) );//数字化的长度
		j.hset(this.idSign,"lenSvr", Long.toString(this.lenSvr) );//数字化的长度
		j.hset(this.idSign,"sizeLoc", this.sizeLoc );//格式化的
		j.hset(this.idSign,"pathLoc", this.pathLoc);//
		j.hset(this.idSign,"pathSvr", this.pathSvr);//
		j.hset(this.idSign,"perSvr", this.lenLoc > 0 ? this.perSvr : "100%");//
		j.hset(this.idSign,"nameLoc", this.nameLoc);//
		j.hset(this.idSign,"nameSvr", this.nameSvr);//
		j.hset(this.idSign,"pidSign", this.pidSign);//
		j.hset(this.idSign,"rootSign", this.rootSign);//
		j.hset(this.idSign,"complete", this.lenLoc > 0 ? "false" : "true");//
		j.hset(this.idSign,"sign", this.sign);//
	}
	
	//合并所有块
	public void merge() throws IOException
	{
		File fp = new File(this.pathSvr);
		if(fp.exists()) return;//文件已存在
				
		PathTool.createDirectory( fp.getParent());//		
		
		RandomAccessFile dstFile = new RandomAccessFile(this.pathSvr, "rw");
		FileChannel fw = dstFile.getChannel();		
		
		//取文件块路径
		String part_path = fp.getParent();
		part_path = part_path.concat("/").concat(this.idSign).concat("/");//f:/files/folder/guid/
		fp = new File(part_path);
		File[] parts = fp.listFiles();		//
		ByteBuffer trunk = ByteBuffer.allocateDirect(1048576);//1mb		
		
		for(Integer i = 0,l = parts.length;i<l;++i)
		{
			RandomAccessFile partRead = new RandomAccessFile(parts[i],"rw");
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
		
		//dst.close();
		//删除文件块
		for(File part : parts)
		{
			part.delete();
		}
		//删除文件块目录
		File partFd = new File(part_path);
		partFd.delete();
	}
}
