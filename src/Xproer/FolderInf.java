package Xproer;

import java.io.File;

import com.google.gson.annotations.SerializedName;

/*
 * 文件夹信息
*/
public class FolderInf {

	public FolderInf()
	{
		try {
			Class.forName("Xproer.FolderInf");
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
	 * @return the lenPosted
	 */
	public String getLenPosted() {
		return lenSvr;
	}

	/**
	 * @param lenPosted the lenPosted to set
	 */
	public void setLenPosted(String v) {
		this.lenSvr = v;
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
	 * @return 子文件夹总数
	 */
	public int getFolders() {
		return folders;
	}

	/**
	 * @param folders the folders to set
	 */
	public void setFolders(int v) {
		this.folders = v;
	}

	/**
	 * @return the files
	 */
	public int getFiles() {
		return files;
	}

	/**
	 * @param files the files to set
	 */
	public void setFiles(int v) {
		this.files = v;
	}
	
	public int getFilesCount(){return filesCount;}
	public void setFilesCount(int v){this.filesCount=v;}

	/**
	 * @return the filesComplete
	 */
	public int getFilesComplete() {
		return filesComplete;
	}

	/**
	 * @param filesComplete the filesComplete to set
	 */
	public void setFilesComplete(int v) {
		this.filesComplete = v;
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
	 * 在服务端创建文件夹。
	 * @param path 父级文件夹路径。E:\\web
	 */
	public void CreateDirectory(String path)
	{
		this.pathSvr = path + "/" + this.name;
		if (path == "")
		{
			UploaderCfg cfg = new UploaderCfg();
			this.pathSvr = cfg.GetUploadPath() + this.name;
		}
		
		File folder = new File(this.pathSvr);
		if(!folder.exists()) folder.mkdirs();
	}

	
	public String name = "";
	
	/// <summary>
	/// 数字化的长度，以字节为单位。示例：10252412
	/// </summary>
	
	public String length = "0";

	/// <summary>
	/// 已上传大小
	/// </summary>
	
	public String lenSvr = "0";
	
	/// <summary>
	/// 格式化的长度，示例：10GB
	/// </summary>
	
	public String size = "";
	
	/// <summary>
	/// 客户端父ID，提供给JS使用。
	/// </summary>
	
	public int pidLoc = 0;
	
	/// <summary>
	/// 服务端父ID，与数据库对应。
	/// </summary>
	
	public int pidSvr = 0;
	
	/// <summary>
	/// 客户端文件夹ID，提供给JS使用。
	/// </summary>
	
	public int idLoc = 0;
	
	/// <summary>
	/// 服务端文件夹ID,与数据库对应
	/// </summary>
	
	public int idSvr = 0;
	
	/// <summary>
	/// 子文件夹总数
	/// </summary>
	@SerializedName("foldersCount")
	public int folders = 0;
	
	/// <summary>
	/// 子文件数
	/// </summary>	
	public int files = 0;
	
	//fix(2015-03-18):单独读取文件总数，供JS使用
	public int filesCount=0;

	/// <summary>
	/// 已上传完的文件数
	/// </summary>
	
	public int filesComplete = 0;

	/// <summary>
	/// 用户ID
	/// </summary>
	
	public int uid = 0;
		
	/**
	 * 文件夹在服务端路径。E:\\Web
	 */
	public String pathSvr = "";
	
	/// <summary>
	/// 文件夹在客户端的路径。D:\\Soft\\Image
	/// </summary>
	
	public String pathLoc = "";
	
	//根级路径
	public int pidRoot;//
	/**
	 * 相对路径。基于根节点。root\\child\\self
	 */
	public String pathRel="";
}