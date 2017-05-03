package up7;

import redis.clients.jedis.Jedis;

public class JedisTool 
{

	public static Jedis con()
	{
	      Jedis j = new Jedis("localhost");	      
	      return j;
	}
}
