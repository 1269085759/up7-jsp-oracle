package up7.biz.folder;

import java.util.List;
import java.util.Set;

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
	Jedis con=null;
	public fd_files_redis(Jedis c)
	{
		this.con = c;
	}
	public fd_files_redis(){}
	public Jedis getCon(){
		if(this.con==null) this.con = JedisTool.con();
		return this.con;
	}
	
	String getKey()
	{
		String key = idSign+"-files";
		return key;
	}
	
	public void del(Jedis j)
	{
		j.del(this.getKey());
	}
	
	public void add(String fSign)
	{	
		Jedis j = this.getCon();
		j.sadd(this.getKey(), fSign);	
	}
	
	public void add(Jedis j,List<fd_file_redis> fs)
	{
		String key = this.getKey();		
		for(fd_file_redis f : fs)
		{
			j.sadd(key, f.idSign);
		}
	}
	
	public Set<String> all(Jedis j)
	{
		Set<String> ids = j.smembers(this.getKey());		
		return ids;		
	}
}
