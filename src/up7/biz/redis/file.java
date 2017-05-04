package up7.biz.redis;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import redis.clients.jedis.Jedis;
import up7.JedisTool;
import up7.PathTool;
import up7.model.xdb_files;

public class file {
	public void process(String idSign,String perSvr,String lenSvr)
	{
		Jedis j = JedisTool.con();
		j.hset(idSign, "perSvr", perSvr);
		j.hset(idSign, "lenSvr", lenSvr);
	}
	
	public void complete(String idSign)
	{
		Jedis j = JedisTool.con();
		j.del(idSign);
	}
	
	public void create(xdb_files f)
	{
		Jedis j = JedisTool.con();
		j.hset(f.idSign, "pathLoc", f.pathLoc);
		j.hset(f.idSign, "pathSvr", f.pathSvr);
		j.hset(f.idSign, "nameLoc", f.nameLoc);
		j.hset(f.idSign, "nameSvr", f.nameSvr);
		j.hset(f.idSign, "lenLoc", Long.toString(f.lenLoc) );
		j.hset(f.idSign, "sizeLoc",f.sizeLoc);
	}
	
	public xdb_files read(String idSign)
	{
		Jedis j = JedisTool.con();
		if(!j.exists(idSign)) return null;
		
		xdb_files f = new xdb_files();
		f.pathLoc = j.hget(idSign, "pathLoc");
		f.pathSvr = j.hget(idSign, "pathSvr");
		f.nameLoc = j.hget(idSign, "nameLoc");
		f.nameSvr = j.hget(idSign, "nameSvr");
		f.lenLoc = Long.parseLong(j.hget(idSign, "lenLoc") );
		f.sizeLoc = j.hget(idSign, "sizeLoc");
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
		
		String pathSvr = j.hget(idSign, "pathSvr");
		File path = new File(pathSvr);
		pathSvr = path.getParent();
		pathSvr.concat("/");
		pathSvr.concat(blockIndex);
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
	public String makePath(Jedis j,String idSign,String fdSign)
	{		
		String pathSvrF = "";
		String fPath = j.hget(idSign, "pathSvr");
		if(StringUtils.isBlank(fPath))
		{
			String pathLocFD = j.hget(fdSign, "pathLoc");
			String pathSvrFD = j.hget(fdSign, "pathSvr");
			String pathLocF = j.hget(idSign, "pathLoc");
			//将文件的本地根路径替换为服务器路径
			pathSvrF = pathLocF.replace(pathLocFD, pathSvrFD);
			pathSvrF.replaceAll("\\", "/");
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
	public String getPartPath(String idSign,String blockIndex,String fdSign) throws IOException
	{
		Jedis j = JedisTool.con();
		if( !j.exists(idSign)) return "";//文件不存在
		if( !j.exists(fdSign)) return "";//文件夹不存在
		
		String pathSvr = this.makePath(j, idSign, fdSign);
		Integer index = pathSvr.lastIndexOf("/");
		if(index != -1) pathSvr = pathSvr.substring(0,index);		
		pathSvr.concat("/").concat(idSign).concat("/").concat(blockIndex);
		return pathSvr;
	}
}