package up7;

import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;

/**
 * @author Administrator
 *
 */
public class FolderCache 
{
	public Jedis getCon()
	{
	      Jedis jedis = new Jedis("localhost");	      
	      return jedis;
	}
	
	/*
	 * 更新文件夹子文件进度，文件夹进度
	 * */	
	/**
	 * @param uid
	 * @param f_id
	 * @param f_post
	 * @param f_len
	 * @param fd_id
	 * @param fd_lenSvr
	 * @param fd_perSvr
	 * @param cmp
	 */
	public void process(String uid,String f_id,String f_pos,String f_lenSvr,String f_perSvr,String fd_id,String fd_lenSvr,String fd_perSvr,String cmp)
	{		
		Jedis j = this.getCon();
		
		this.appendChild(j, fd_id, f_id);//添加子文件
		this.fd_process(j, uid, fd_id, fd_lenSvr, fd_perSvr);//更新文件夹进度
		this.f_process(j, uid, f_id, f_pos, f_lenSvr, f_perSvr,cmp);//更新文件进度
	}
	
	public void appendChild(Jedis j,String idFolder,String idFile)
	{
		String key = "folder-"+idFolder+"-"+"files";
		j.zadd(key, Long.parseLong(idFile), "files-"+idFile);
	}
	
	/**
	 * 更新文件夹进度
	 * */
	public void fd_process(Jedis j,String uid,String idSvr,String lenSvr,String perSvr)
	{
		String key = "folder-" + idSvr;
		j.del(key);
		
		Map<String,String> fd = new HashMap<String,String>();
		fd.put("uid", uid);
		fd.put("idSvr", idSvr);
		fd.put("lenSvr", lenSvr);
		j.hmset(key, fd);
	}
	
	/**
	 * 更新文件进度
	 * */
	public void f_process(Jedis j,String uid,String idSvr,String pos,String lenSvr,String perSvr,String cmp)
	{
		String key = "file-" + idSvr;
		j.del(key);
		
		Map<String,String> fd = new HashMap<String,String>();
		fd.put("uid", uid);
		fd.put("idSvr", idSvr);
		fd.put("pos", pos);
		fd.put("lenSvr", lenSvr);
		fd.put("perSvr", perSvr);
		fd.put("complete", cmp);
		j.hmset("file-"+idSvr, fd);	
	}
}
