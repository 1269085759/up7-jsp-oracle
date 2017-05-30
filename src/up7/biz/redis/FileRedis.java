package up7.biz.redis;

import redis.clients.jedis.Jedis;
import up7.JedisTool;
import up7.model.xdb_files;

public class FileRedis 
{
	Jedis con=null;
	public FileRedis(Jedis c)
	{
		this.con = c;
	}
	public FileRedis()
	{		
	}
	Jedis getCon()
	{
		if(this.con == null) this.con = JedisTool.con();
		return this.con;
	}
	
	public void process(String idSign,String perSvr,String lenSvr,String blockCount,String blockSize)
	{
		Jedis j = JedisTool.con();
		j.hset(idSign, "perSvr", perSvr);
		j.hset(idSign, "lenSvr", lenSvr);
		if( !blockCount.equalsIgnoreCase("0"))
		j.hset(idSign, "blockCount", blockCount);
		
		if( !blockSize.equalsIgnoreCase("0") )
			j.hset(idSign, "blockSize", blockSize);
	}
	
	public void complete(String idSign)
	{
		Jedis j = JedisTool.con();
		j.del(idSign);
	}
	
	public void create(xdb_files f)
	{	
		Jedis j = this.getCon();
		if(j.exists(f.idSign)) return;
		
		j.hset(f.idSign, "fdTask", f.f_folder==true?"true":"false");
		j.hset(f.idSign, "rootSign", f.rootSign);
		j.hset(f.idSign, "pidSign", f.pidSign);
		j.hset(f.idSign, "pathLoc", f.pathLoc);
		j.hset(f.idSign, "pathSvr", f.pathSvr);
		j.hset(f.idSign, "pathRel", f.pathRel);
		j.hset(f.idSign, "blockPath", f.blockPath);
		j.hset(f.idSign, "nameLoc", f.nameLoc);
		j.hset(f.idSign, "nameSvr", f.nameSvr);
		j.hset(f.idSign, "lenLoc", Long.toString(f.lenLoc) );
		j.hset(f.idSign, "lenSvr", "0" );
		j.hset(f.idSign, "blockCount", Integer.toString(f.blockCount) );
		j.hset(f.idSign, "blockSize", Integer.toString(f.blockSize) );
		j.hset(f.idSign, "sizeLoc",f.sizeLoc);
		j.hset(f.idSign, "filesCount", Integer.toString(f.filesCount) );
		j.hset(f.idSign, "foldersCount", "0" );
	}
	
	public xdb_files read(String idSign)
	{
		Jedis j = JedisTool.con();
		if(!j.exists(idSign)) return null;
		
		xdb_files f = new xdb_files();
		f.idSign 	 = idSign;
		f.f_folder	 = j.hget(idSign, "fdTask").compareTo("true")==0;
		f.rootSign 	 = j.hget(idSign, "rootSign");
		f.pidSign 	 = j.hget(idSign, "pidSign");
		f.pathLoc 	 = j.hget(idSign, "pathLoc");
		f.pathSvr 	 = j.hget(idSign, "pathSvr");
		f.pathRel	 = j.hget(idSign, "pathRel");
		f.blockPath  = j.hget(idSign, "blockPath");
		f.nameLoc 	 = j.hget(idSign, "nameLoc");
		f.nameSvr 	 = j.hget(idSign, "nameSvr");
		f.lenLoc 	 = Long.parseLong(j.hget(idSign, "lenLoc") );
		f.sizeLoc 	 = j.hget(idSign, "sizeLoc");
		f.lenSvr 	 = Long.parseLong( j.hget(idSign, "lenSvr") );
		f.perSvr 	 = j.hget(idSign, "perSvr");
		f.blockCount = Integer.parseInt(j.hget(idSign, "blockCount"));
		String blockSize = j.hget(idSign, "blockSize");
		if(null == blockSize) blockSize="0";
		f.blockSize = Integer.parseInt(blockSize);
		f.filesCount = Integer.parseInt( j.hget(idSign, "filesCount") );
		return f;
	}
}