package down3.biz.redis;

import down3.model.DnFileInf;
import redis.clients.jedis.Jedis;

/**
 * 下载的缓存管理器
 * @author Administrator
 *
 */
public class tasks {
	String key = "tasks-down3";
	Jedis con=null;
	String uid ="";
	
	public tasks(String uid,Jedis j){this.uid=uid; this.con = j;}
	
	//tasks-down3-uid
	String getKey(){return key.concat("-").concat(uid);}
	
	public void add(DnFileInf f)
	{
		this.con.sadd(this.getKey(), f.idSign);		
	}
	
	public String toJson()
	{
		
		return "";
	}
}
