package up7.biz.folder;

import org.apache.commons.lang.StringUtils;

import redis.clients.jedis.Jedis;
import up7.JedisTool;

public class FolderRedis 
{
	fd_root m_root;
	
	//从redis中读取数据
	public void read(String idSign)
	{
		Jedis j = JedisTool.con();
		//folder不存在
		if(!j.exists(idSign)) return;
		
		this.m_root = new fd_root();
		this.m_root.lenLoc = Long.parseLong(j.hget(idSign, "lenLoc") );
		this.m_root.lenSvr = Long.parseLong(j.hget(idSign, "lenSvr") );
		this.m_root.sizeLoc = j.hget(idSign, "sizeLoc");
		this.m_root.pathLoc = j.hget(idSign, "pathLoc");
		this.m_root.pathSvr = j.hget(idSign, "pathSvr");
		this.m_root.filesCount = Integer.parseInt(j.hget(idSign, "filesCount") );
		String[] files = StringUtils.split( j.hget(idSign, "files") ,',');
		this.readFiles(j, files);
		String[] folders = StringUtils.split( j.hget(idSign, "folders") ,',');
		this.readFolders(j, folders);
	}
	
	void readFiles(Jedis j,String[] fs)
	{		
		for(String s : fs)
		{
			fd_file_redis file = new fd_file_redis();
			file.read(j, s);
			this.m_root.files.add(file);
		}
	}
	
	void readFolders(Jedis j,String[] fs)
	{		
		for(String s : fs)
		{
			fd_child_redis fd = new fd_child_redis();
			fd.read(j,s);
			this.m_root.folders.add(fd);
		}
	}
	
	//保存到数据库
	public void writeDB()
	{}
	
	public void complete(String idSign)
	{
		//插入文件数据表
		//插入文件夹
	}
}
