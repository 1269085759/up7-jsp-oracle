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
	String keySpace = "down3";//下载，命名空间，防止与上传冲突
	String key = "tasks-down3-";
	Jedis con=null;
	String uid ="";
	
	public tasks(String uid,Jedis j){this.uid=uid; this.con = j;}
	
	//tasks-down3-uid
	String getKey(){return key.concat(uid);}
	
	//所有下载项都要加到下载的空间下
	void addSpace(String key)
	{
		this.con.lpush(this.keySpace, key);
	}
	
	public void clear()
	{
		long len = this.con.llen(this.keySpace);//取总长度
		while(len > 0)
		{
			List<String> list = this.con.lrange(this.keySpace, 0 ,500);
			for(String k : list)
			{
				this.con.del(k);
			}
			len = len - list.size();
		}
	}
	
	public void add(DnFileInf f)
	{
		//添加到队列
		this.con.sadd(this.getKey(), f.signSvr);
		
		//添加一条信息
		file f_svr = new file(this.con);
		f_svr.create(f);
		
		this.addSpace(this.getKey());
		this.addSpace(f.signSvr);
	}
	
	//删除文件
	public void del(String signSvr)
	{
		//从队列中删除
		this.con.srem(this.getKey(), signSvr);
		//从空间中删除
		this.con.lrem(this.keySpace, 1,signSvr);
		
		//删除文件信息
		this.con.del(signSvr);
	}
	
	/**
	 * 将队列元素转换成json
	 * @return
	 */
	public String toJson()
	{
		System.out.println("下载队列：".concat(Long.toString( this.con.scard(this.getKey() ) ) ) );
		Set<String> keys = this.con.smembers(this.getKey());
		List<DnFileInf> files = new ArrayList<DnFileInf>();
		for(String key : keys)
		{
			System.out.println(key);
			file f_svr = new file(this.con);
			DnFileInf data = f_svr.read(key);
			files.add(data);
		}

    	Gson g = new Gson();
    	return g.toJson(files);		
	}
}