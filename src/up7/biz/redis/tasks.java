package up7.biz.redis;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import redis.clients.jedis.Jedis;
import up7.JedisTool;
import up7.biz.folder.fd_file_redis;

/*
 * 任务列表，redis中
 * */
public class tasks {
	String key = "tasks";
	Jedis con = null;	
	public tasks(Jedis j){this.con = j;}
	
	public void add(String sign)
	{		
		this.con.lpush(this.key, sign);
	}
	
	public void del(String sign)
	{
		this.con.lrem(this.key, 1, sign);
	}
	
	public void clear()
	{
		this.con.flushDB();//		
	}
	
	public List<fd_file_redis> all()
	{
		List<fd_file_redis> arr = null;		
		List<String> ls = this.con.lrange(this.key, 0, -1);
		if(ls.size() > 0) arr = new ArrayList<fd_file_redis>();
		
		for(String s : ls)
		{
			fd_file_redis f = new fd_file_redis();
			f.read(this.con, s);
			arr.add(f);
		}
		return arr;
	}
	
	public String toJson()
	{
		List<fd_file_redis> fs = this.all();
		if(fs == null) return "";
		
		Gson g = new Gson();
		String v = g.toJson(fs);
		return v;
	}
}
