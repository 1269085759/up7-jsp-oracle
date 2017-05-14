package up7.biz.folder;

import java.util.List;

import redis.clients.jedis.Jedis;

public class fd_folders_redis {

	public String idSign;
	Jedis con;
	
	public fd_folders_redis(Jedis j,String idSign/*文件夹ID*/){this.con = j;this.idSign = idSign;}
	
	String getKey()
	{
		String key = idSign+"-folders";
		return key;
	}
	
	public void add(String fSign)
	{
		this.con.lpush(this.getKey(), fSign);
	}
	
	public void del()
	{		
		this.con.del(this.getKey());
	}
	
	public void add(List<fd_child_redis> fs)
	{
		String key = this.getKey();		
		for(fd_child_redis f : fs)
		{
			this.con.lpush(key, f.idSign);
		}
	}
	
	public List<String> all()
	{		
		List<String> ids = this.con.lrange(this.getKey(), 0, -1);		
		return ids;		
	}
}
