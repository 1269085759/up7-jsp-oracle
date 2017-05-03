package up7;

import redis.clients.jedis.Jedis;

/**
 * @author Administrator
 *
 */
public class FolderCache 
{	
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
	public void process(String uid,String f_sign,String f_pos,String f_lenSvr,String f_perSvr,String fd_sign,String fd_lenSvr,String fd_perSvr,String cmp)
	{		
		Jedis j = JedisTool.con();
		
		//更新文件进度
		j.hset(f_sign, "pos", f_pos);
		j.hset(f_sign, "lenSvr", f_lenSvr);
		j.hset(f_sign, "perSvr", f_perSvr);
		j.hset(f_sign, "complete", cmp);
		
		//更新文件夹进度
		j.hset(fd_sign, "lenSvr", fd_lenSvr);
		j.hset(fd_sign, "perSvr", fd_perSvr);
		j.hset(fd_sign, "lenSvr", fd_lenSvr);		
	}
}
