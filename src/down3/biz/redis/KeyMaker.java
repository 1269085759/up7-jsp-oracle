package down3.biz.redis;

public class KeyMaker {
	public String space(String uid)
	{
		return "d-".concat(uid);
	}
}