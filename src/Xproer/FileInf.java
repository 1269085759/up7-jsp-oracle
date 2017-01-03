package Xproer;

/*
 * 文件信息
*/
public class FileInf {

	public FileInf()
	{
		try {
			Class.forName("Xproer.FileInf");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String v) {
		this.name = v;
	}

	/**
	 * @return the pathLoc
	 */
	public String getPathLoc() {
		return pathLoc;
	}

	/**
	 * @param pathLoc the pathLoc to set
	 */
	public void setPathLoc(String v) {
		this.pathLoc = v;
	}

	/**
	 * @return the pathSvr
	 */
	public String getPathSvr() {
		return pathSvr;
	}

	/**
	 * @param pathSvr the pathSvr to set
	 */
	public void setPathSvr(String v) {
		this.pathSvr = v;
	}

	/**
	 * @return the pidLoc
	 */
	public int getPidLoc() {
		return pidLoc;
	}

	/**
	 * @param pidLoc the pidLoc to set
	 */
	public void setPidLoc(int v) {
		this.pidLoc = v;
	}

	/**
	 * @return the pidSvr
	 */
	public int getPidSvr() {
		return pidSvr;
	}

	/**
	 * @param pidSvr the pidSvr to set
	 */
	public void setPidSvr(int v) {
		this.pidSvr = v;
	}

	/**
	 * @return the pidRoot
	 */
	public int getPidRoot() {
		return pidRoot;
	}

	/**
	 * @param pidRoot the pidRoot to set
	 */
	public void setPidRoot(int v) {
		this.pidRoot = v;
	}

	/**
	 * @return the idLoc
	 */
	public int getIdLoc() {
		return idLoc;
	}

	/**
	 * @param idLoc the idLoc to set
	 */
	public void setIdLoc(int v) {
		this.idLoc = v;
	}

	/**
	 * @return the idSvr
	 */
	public int getIdSvr() {
		return idSvr;
	}

	/**
	 * @param idSvr the idSvr to set
	 */
	public void setIdSvr(int v) {
		this.idSvr = v;
	}

	/**
	 * @return the uid
	 */
	public int getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(int v) {
		this.uid = v;
	}

	/**
	 * @return the length
	 */
	public String getLength() {
		return length;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(String v) {
		this.length = v;
	}

	/**
	 * @return the size
	 */
	public String getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(String v) {
		this.size = v;
	}

	/**
	 * @return the postPos
	 */
	public String getPostPos() {
		return postPos;
	}

	/**
	 * @param postPos the postPos to set
	 */
	public void setPostPos(String v) {
		this.postPos = v;
	}

	/**
	 * @return the postPercent
	 */
	public String getPostPercent() {
		return postPercent;
	}

	/**
	 * @param postPercent the postPercent to set
	 */
	public void setPostPercent(String v) {
		this.postPercent = v;
	}

	/**
	 * @return the postLength
	 */
	public String getPostLength() {
		return postLength;
	}

	/**
	 * @param postLength the postLength to set
	 */
	public void setPostLength(String v) {
		this.postLength = v;
	}

	/**
	 * @return the md5
	 */
	public String getMd5() {
		return md5;
	}

	/**
	 * @param md5 the md5 to set
	 */
	public void setMd5(String v) {
		this.md5 = v;
	}
	
	/// <summary>
	/// 文件名称。示例：QQ2014.exe
	/// </summary>	
	public String name = "";
	
	/// <summary>
	/// 文件在客户端中的路径。示例：D:\\Soft\\QQ2013.exe
	/// </summary>	
	public String pathLoc = "";

	/// <summary>
	/// 文件在服务器上面的路径。示例：E:\\Web\\Upload\\QQ2013.exe
	/// </summary>	
	public String pathSvr = "";

	/// <summary>
	/// 客户端父ID(文件夹ID)
	/// </summary>	
	public int pidLoc = 0;

	/// <summary>
	/// 服务端父ID(文件夹在数据库中的ID)
	/// </summary>	
	public int pidSvr = 0;

	/// <summary>
	/// 根级文件夹ID，数据库ID，与xdb_folders.fd_id对应
	/// </summary>	
	public int pidRoot = 0;

	/// <summary>
	/// 本地文件ID。
	/// </summary>	
	public int idLoc = 0;

	/// <summary>
	/// 文件在服务器中的ID。
	/// </summary>	
	public int idSvr = 0;

	/// <summary>
	/// 用户ID
	/// </summary>	
	public int uid = 0;

	/// <summary>
	/// 数字化的长度。以字节为单位，示例：1021021
	/// </summary>	
	public String length = "0";

	/// <summary>
	/// 格式化的长度。示例：10G
	/// </summary>	
	public String size = "0bytes";

	/// <summary>
	/// 文件上传位置。
	/// </summary>	
	public String postPos = "0";

	/// <summary>
	/// 上传百分比
	/// </summary>	
	public String postPercent = "0%";

	/// <summary>
	/// 已上传大小
	/// </summary>	
	public String postLength = "0";

	/// <summary>
	/// 文件MD5
	/// </summary>	
	public String md5 = "";

	public String pathRel;
	
}