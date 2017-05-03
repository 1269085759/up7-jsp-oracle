package up7.biz.folder;

import redis.clients.jedis.Jedis;

public class fd_file_redis extends fd_file
{
	public void read(Jedis j,String idSign)
	{
		this.idSign = idSign;
		if( !j.exists(idSign)) return;
		
		this.lenLoc = Long.parseLong( j.hget(idSign, "lenLoc") );
		this.lenSvr = Long.parseLong( j.hget(idSign, "lenSvr") );
		this.sizeLoc = j.hget(idSign, "sizeLoc");
		this.pathLoc = j.hget(idSign, "pathLoc");
		this.pathSvr = j.hget(idSign, "pathSvr");
		this.perSvr = j.hget(idSign, "perSvr");
		this.nameLoc = j.hget(idSign, "nameLoc");
		this.nameSvr = j.hget(idSign, "nameSvr");
		this.pidSign = j.hget(idSign, "pidSign");
		this.rootSign = j.hget(idSign, "rootSign");
		this.complete = j.hget(idSign, "complete")=="true";
		this.sign = j.hget(idSign, "sign");
	}
	
	public void write(Jedis j)
	{
		j.del(this.idSign);		
		
		j.hset(this.idSign,"lenLoc", Long.toString(this.lenLoc) );//数字化的长度
		j.hset(this.idSign,"lenSvr", Long.toString(this.lenSvr) );//数字化的长度
		j.hset(this.idSign,"sizeLoc", this.sizeLoc );//格式化的
		j.hset(this.idSign,"pathLoc", this.pathLoc);//
		j.hset(this.idSign,"pathSvr", this.pathSvr);//
		j.hset(this.idSign,"perSvr", this.lenLoc > 0 ? this.perSvr : "100%");//
		j.hset(this.idSign,"nameLoc", this.nameLoc);//
		j.hset(this.idSign,"nameSvr", this.nameSvr);//
		j.hset(this.idSign,"pidSign", this.pidSign);//
		j.hset(this.idSign,"rootSign", this.rootSign);//
		j.hset(this.idSign,"complete", this.lenLoc > 0 ? "false" : "true");//
		j.hset(this.idSign,"sign", this.sign);//
	}
}
