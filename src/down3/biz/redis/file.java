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
		//if(j.exists(f.signSvr)) return;
						
		j.hset(f.signSvr, "pathLoc", f.pathLoc);
		j.hset(f.signSvr, "pathSvr", f.pathSvr);
		j.hset(f.signSvr, "lenLoc", Long.toString(f.lenLoc) );//已下载大小		
		j.hset(f.signSvr, "lenSvr",Long.toString( f.lenSvr ) );//文件大小
		j.hset(f.signSvr, "perLoc",f.perLoc );//已下载百分比		
	}
	
	public DnFileInf read(String signSvr)
	{
		if(!this.con.exists(signSvr)) return null;
		DnFileInf f = new DnFileInf();
		f.signSvr = signSvr;
		f.lenLoc = Long.parseLong( this.con.hget(signSvr, "lenLoc") );//已经下载的大小
		f.lenSvr = Long.parseLong( this.con.hget(signSvr, "lenSvr"));//
		f.perLoc = this.con.hget(signSvr, "perLoc");
		f.pathLoc = this.con.hget(signSvr, "pathLoc");
		f.pathSvr = this.con.hget(signSvr, "pathSvr");
		return f;
	}
	
	//更新进度
	public void process(String signSvr,String perLoc,String lenLoc)
	{
		Jedis j = this.con;
		if(j.exists(signSvr)) return;
		
		j.hset(signSvr, "lenLoc", lenLoc );//已下载大小		
		j.hset(signSvr, "perLoc", perLoc );//已下载百分比
	}
}