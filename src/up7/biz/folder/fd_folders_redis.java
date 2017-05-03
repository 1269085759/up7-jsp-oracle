package up7.biz.folder;

import java.util.List;

import redis.clients.jedis.Jedis;
import up7.JedisTool;

public class fd_folders_redis {

	public String idSign;
	
	String getKey()
	{
		String key = idSign+"-folders";
		return key;
	}
	
	public void add(String fSign)
	{
		Jedis j = JedisTool.con();
		j.lpush(this.getKey(), fSign);
	}
	
	public void add(Jedis j,List<fd_child_redis> fs)
	{
		String key = this.getKey();		
		for(fd_child_redis f : fs)
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
