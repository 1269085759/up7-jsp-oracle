package up7.biz.redis;

import redis.clients.jedis.Jedis;
import up7.JedisTool;
import up7.model.xdb_files;

public class file {
	public void process(String idSign,String perSvr,String lenSvr)
	{
		Jedis j = JedisTool.con();
		j.hset(idSign, "perSvr", perSvr);
		j.hset(idSign, "lenSvr", lenSvr);
	}
	
	public void complete(String idSign)
	{
		Jedis j = JedisTool.con();
		j.del(idSign);
	}
	
	public void create(xdb_files f)
	{
		Jedis j = JedisTool.con();
		j.hset(f.idSign, "pathLoc", f.pathLoc);
		j.hset(f.idSign, "pathSvr", f.pathSvr);
		j.hset(f.idSign, "nameLoc", f.nameLoc);
		j.hset(f.idSign, "nameSvr", f.nameSvr);
		j.hset(f.idSign, "lenLoc", Long.toString(f.lenLoc) );
		j.hset(f.idSign, "sizeLoc",f.sizeLoc);
	}
	
	public xdb_files read(String idSign)
	{
		Jedis j = JedisTool.con();
		if(!j.exists(idSign)) return null;
		
		xdb_files f = new xdb_files();
		f.pathLoc = j.hget(idSign, "pathLoc");
		f.pathSvr = j.hget(idSign, "pathSvr");
		f.nameLoc = j.hget(idSign, "nameLoc");
		f.nameSvr = j.hget(idSign, "nameSvr");
		f.lenLoc = Long.parseLong(j.hget(idSign, "lenLoc") );
		f.sizeLoc = j.hget(idSign, "sizeLoc");
		return f;
	}
}
