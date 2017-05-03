package up7.biz.folder;

import redis.clients.jedis.Jedis;

public class fd_child_redis extends fd_child
{
	public void read(Jedis j,String idSign)
	{
		this.idSign = idSign;
		if( !j.exists(idSign)) return;
		
		this.pathLoc = j.hget(idSign, "pathLoc");
		this.pathSvr = j.hget(idSign, "pathSvr");
		this.nameLoc = j.hget(idSign, "nameLoc");
		this.nameSvr = j.hget(idSign, "nameSvr");
		this.pidSign = j.hget(idSign, "pidSign");
		this.rootSign = j.hget(idSign, "rootSign");
	}
	
	public void write(Jedis j)
	{

		j.del(this.idSign);
		
		
		j.hset(this.idSign,"pathLoc", this.pathLoc);//
		j.hset(this.idSign,"pathSvr", this.pathSvr);//
		j.hset(this.idSign,"nameLoc", this.nameLoc);//
		j.hset(this.idSign,"nameSvr", this.nameSvr);//
		j.hset(this.idSign,"pidSign", this.pidSign);//
		j.hset(this.idSign,"rootSign", this.rootSign);//
	}
}
