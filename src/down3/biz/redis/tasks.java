package down3.biz.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;

import down3.model.DnFileInf;
import redis.clients.jedis.Jedis;

/**
 * 下载列表（未完成）
 * 下载的缓存管理器
 * @author Administrator
 *
 */
public class tasks {
	Jedis con=null;
	String uid ="";
	
	public tasks(String uid,Jedis j){this.uid=uid; this.con = j;}
	
	
	public void clear()
	{
		Set<String> keys = this.con.keys("d-*");
		for(String k : keys)
		{
			
		}
	}
	
	void clearUser(String key)
	{
		long len = this.con.scard(key);
		while(len > 0)
		{
			List<String> keys = this.con.srandmember(key, 500);
			len -= keys.size();
			for(String k : keys)
			{
				this.con.del(k);
			}
		}
	}
	
	public void add(DnFileInf f)
	{
		//添加文件信息
		FileRedis f_svr = new FileRedis(this.con);
		f_svr.create(f);
		
		KeyMaker km = new KeyMaker();
		String space = km.space(this.uid);
		this.con.sadd(space, f.signSvr);		
	}
	
	//删除文件
	public void del(String signSvr)
	{
		KeyMaker km = new KeyMaker();
		String space = km.space(this.uid);
		
		//从队列中删除(当前用户的下载列表)
		this.con.srem(space, signSvr);
		
		//删除文件信息
		this.con.del(signSvr);
	}
	
	/**
	 * 将队列元素转换成json
	 * @return
	 */
	public String toJson()
	{
		
		KeyMaker km = new KeyMaker();
		String space = km.space(this.uid);
		
		Set<String> keys = this.con.smembers(space);
		List<DnFileInf> files = new ArrayList<DnFileInf>();
		for(String key : keys)
		{
			FileRedis f_svr = new FileRedis(this.con);
			DnFileInf data = f_svr.read(key);
			files.add(data);
		}

    	Gson g = new Gson();
    	return g.toJson(files);		
	}
}