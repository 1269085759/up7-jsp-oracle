package up7.biz.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;

import redis.clients.jedis.Jedis;
import up7.biz.folder.fd_file_redis;
import up7.biz.folder.fd_files_redis;
import up7.biz.folder.fd_folders_redis;
import up7.model.xdb_files;

/*
 * 任务列表，redis中
 * 只保存文件夹，和文件任务，不保存子文件任务。
 * 用户任务Key：uid-tasks
 * */
public class tasks {
	public String uid = "";
	String keyName = "tasks";
	Jedis con = null;
	
	public tasks(Jedis j){this.con = j;}
	
	String getKey()
	{
		return this.uid.concat("-").concat(this.keyName);
	}
	
	public void add(String sign)
	{		
		this.con.sadd(this.getKey(), sign);
		System.out.println(this.getKey());
	}
	
	/**
	 * 将文件信息添加到缓存
	 * 将文件idSign添加到任务列表
	 * @param f
	 */
	public void add(xdb_files f)
	{
		//添加到任务列表
		this.add(f.idSign);
		
		//添加key
		up7.biz.redis.FileRedis fs = new up7.biz.redis.FileRedis(this.con);
		fs.create(f);
	}
	
	public void del(String sign)
	{
		//从队列中删除
		this.con.srem(this.getKey(), sign);
		//删除key
		this.con.del(sign);
	}
	
	public void delFd(String sign)
	{
		//清除文件列表缓存
		fd_files_redis files = new fd_files_redis(this.con,sign);
		files.del();
		
		//清除目录列表缓存
		fd_folders_redis folders = new fd_folders_redis(this.con,sign);
		folders.del();
		
		//从队列中清除
		this.del(sign);
	}
	
	public void clear()
	{
		this.con.flushDB();//		
	}
	
	public List<fd_file_redis> all()
	{
		List<fd_file_redis> arr = null;		
		Set<String> ls = this.con.smembers(this.getKey());
		System.out.println("用户uid=".concat(this.uid)
				.concat(" 任务数：").concat(Integer.toString(ls.size())));
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
