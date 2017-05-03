package up7.biz.folder;

import java.util.List;

import redis.clients.jedis.Jedis;
import up7.JedisTool;

/*
 * key：文件夹GUID-files
 * 添加：
 * fd_files_redis fs = new fd_files_redis();
 * fs.idSign = "";//设置文件夹GUID
 * fs.add(file-guid);
 * 
 * 读取：
 * fd_files_redis fs = new fd_files_redis();
 * fs.idSign = "";
 * List<String> ids = fs.all();
 * for(String id : ids)
 * {}
 * */
public class fd_files_redis 
{
	public String idSign;
	
	String getKey()
	{
		String key = idSign+"-files";
		return key;
	}
	
	public void add(String fSign)
	{
		Jedis j = JedisTool.con();
		j.lpush(this.getKey(), fSign);
	}
	
	public void add(Jedis j,List<fd_file_redis> fs)
	{
		String key = this.getKey();		
		for(fd_file_redis f : fs)
		{
			j.lpush(key, f.idSign);
		}
	}
	
	public List<String> all(Jedis j)
	{		
		List<String> ids = j.lrange(this.getKey(), 0, -1);		
		return ids;		
	}
}
