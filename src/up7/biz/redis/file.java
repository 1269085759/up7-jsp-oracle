package up7.biz.redis;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import redis.clients.jedis.Jedis;
import up7.JedisTool;
import up7.model.xdb_files;

public class file 
{
	Jedis con=null;
	public file(Jedis c)
	{
		this.con = c;
	}
	public file()
	{		
	}
	Jedis getCon()
	{
		if(this.con == null) this.con = JedisTool.con();
		return this.con;
	}
	
	public void process(String idSign,String perSvr,String lenSvr,String blockCount)
	{
		Jedis j = JedisTool.con();
		j.hset(idSign, "perSvr", perSvr);
		j.hset(idSign, "lenSvr", lenSvr);
		j.hset(idSign, "blockCount", blockCount);
	}
	
	public void complete(String idSign)
	{
		Jedis j = JedisTool.con();
		j.del(idSign);
	}
	
	public void create(xdb_files f)
	{	
		Jedis j = this.getCon();
		if(j.exists(f.idSign)) return;
		
		j.hset(f.idSign, "fdTask", f.f_fdTask==true?"true":"false");
		j.hset(f.idSign, "rootSign", f.rootSign);
		j.hset(f.idSign, "pathLoc", f.pathLoc);
		j.hset(f.idSign, "pathSvr", f.pathSvr);
		j.hset(f.idSign, "nameLoc", f.nameLoc);
		j.hset(f.idSign, "nameSvr", f.nameSvr);
		j.hset(f.idSign, "lenLoc", Long.toString(f.lenLoc) );
		j.hset(f.idSign, "lenSvr", "0" );
		j.hset(f.idSign, "blockCount", Integer.toString(f.blockCount) );
		j.hset(f.idSign, "sizeLoc",f.sizeLoc);
		j.hset(f.idSign, "filesCount", Integer.toString(f.filesCount) );
		j.hset(f.idSign, "foldersCount", "0" );
	}
	
	public xdb_files read(String idSign)
	{
		Jedis j = JedisTool.con();
		if(!j.exists(idSign)) return null;
		
		xdb_files f = new xdb_files();
		f.idSign = idSign;
		f.pathLoc = j.hget(idSign, "pathLoc");
		f.pathSvr = j.hget(idSign, "pathSvr");
		f.nameLoc = j.hget(idSign, "nameLoc");
		f.nameSvr = j.hget(idSign, "nameSvr");
		f.lenLoc = Long.parseLong(j.hget(idSign, "lenLoc") );
		f.sizeLoc = j.hget(idSign, "sizeLoc");
		f.blockCount = Integer.parseInt(j.hget(idSign, "blockCount"));
		f.filesCount = Integer.parseInt( j.hget(idSign, "filesCount") );
		return f;
	}
	
	/**
	 * 获取文件块存储路径
	 * pathSvr/guid/
	 * @param idSign
	 * @return pathSvr/guid/1
	 * 	pathSvr/guid/1
	 * 	pathSvr/guid/2
	 */
	public String getPartPath(String idSign,String blockIndex)
	{
		Jedis j = JedisTool.con();
		if(!j.exists(idSign)) return "";
		
		String pathSvr = j.hget(idSign, "pathSvr");//f:/files/guid/QQ2013.exe
		File path = new File(pathSvr);
		pathSvr = path.getParent();//d:\\soft
		pathSvr = pathSvr.concat("/").concat(idSign).concat("/");
		pathSvr = pathSvr.concat(blockIndex).concat(".part");
		pathSvr = pathSvr.replace("\\", "/");
		return pathSvr;
	}
	
	/**
	 * 生成子文件路径（文件夹）
	 * 格式：
	 * 	f:/files/文件夹/文件名称.exe
	 * @param idSign
	 * @param fdSign
	 * @return
	 */
	public String makePathFile(Jedis j,String idSign,String fdSign)
	{		
		String pathSvrF = "";
		String fPath = j.hget(idSign, "pathSvr");
		if(StringUtils.isBlank(fPath))
		{
			String pathLocFD = j.hget(fdSign, "pathLoc");
			String pathSvrFD = j.hget(fdSign, "pathSvr");
			String pathLocF = j.hget(idSign, "pathLoc");
			
			
			//将文件的本地根路径替换为服务器路径
			pathLocFD = pathLocFD.replace("\\", "/");
			pathSvrFD = pathSvrFD.replace("\\", "/");
			pathSvrF = pathLocF.replace(pathLocFD, pathSvrFD);			
			j.hset(idSign, "pathSvr", pathSvrF);
		}
		else
		{
			pathSvrF = j.hget(idSign, "pathSvr");
		}
		return pathSvrF;
	}
	
	/**
	 * 获取文件块存储路径（文件夹）
	 * 格式：
	 * 	f:/files/文件夹/文件名称/1
	 * @param idSign
	 * @param blockIndex
	 * @param fdSign
	 * @return
	 * @throws IOException 
	 */
	public String getPartPath(String idSign,String blockIndex,String blockCount,String fdSign) throws IOException
	{		
		String pathSvr = "";
		Jedis j = this.getCon();
		Boolean hasData = j.exists(idSign);
		if(hasData) hasData = j.exists(fdSign);
		if(hasData)
		{
			pathSvr = this.makePathFile(j, idSign, fdSign);
			//需要分块			
			if( !StringUtils.equals(blockCount, "1") )
			{
				Integer index = pathSvr.lastIndexOf("/");
				if(index != -1) pathSvr = pathSvr.substring(0,index);		
				pathSvr = pathSvr.concat("/").concat(idSign).concat("/").concat(blockIndex).concat(".part");
			}
		}
		j.close();
		return pathSvr;		
	}
}