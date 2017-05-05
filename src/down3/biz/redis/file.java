package down3.biz.redis;

import down3.model.DnFileInf;
import redis.clients.jedis.Jedis;

/**
 * 下载文件信息
 * @author Administrator
 *
 */
public class file {
	Jedis con = null;
	
	public file(Jedis j){this.con = j;}
	
	public void create(DnFileInf f)
	{
		Jedis j = this.con;
		if(j.exists(f.idSign)) return;
						
		j.hset(f.idSign, "pathLoc", f.pathLoc);
		j.hset(f.idSign, "lenLoc", Long.toString(f.lenLoc) );//已下载大小		
		j.hset(f.idSign, "lenSvr",Long.toString( f.lenSvr ) );//文件大小
		j.hset(f.idSign, "perLoc",f.perLoc );//已下载百分比		
	}
}