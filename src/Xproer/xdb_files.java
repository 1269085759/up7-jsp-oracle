package Xproer;
import java.net.URLEncoder;
import java.util.Date;

/*
 * 原型
*/
public class xdb_files {

	public xdb_files()
	{
		this.complete = false;
	}
	/// <summary>
	/// 返回JSON字符串 [{fid:"0",uid:"1",fileNameLocal:"urlencode()"}]
	/// FilePathLocal进行UrlEncode编码
	/// FilePathRelative进行UrlEncode编码
	/// </summary>
	/// <returns></returns>
	public String ToJson()
	{
		String pathSvr = this.pathSvr;
		String pathLoc = this.pathLoc;
		try{
			pathSvr = URLEncoder.encode(pathSvr,"UTF-8");
			pathSvr = pathSvr.replaceAll("\\+", "%20");
			pathLoc = URLEncoder.encode(pathLoc,"UTF-8");
			pathLoc = pathLoc.replaceAll("\\+", "%20");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("[{");
		sb.append("fid:\""); sb.append(this.idSvr); sb.append("\"");
		sb.append(",uid:\""); sb.append(this.uid); sb.append("\"");
		sb.append(",nameLoc:\""); sb.append(this.nameLoc); sb.append("\"");
		sb.append(",nameSvr:\""); sb.append(this.nameSvr); sb.append("\"");
		sb.append(",pathLoc:\""); sb.append(pathLoc); sb.append("\"");
		sb.append(",pathSvr:\""); sb.append(pathSvr); sb.append("\"");
		sb.append(",FileMd5:\""); sb.append(this.md5); sb.append("\"");
		sb.append(",FileLength:\""); sb.append(this.lenLoc); sb.append("\"");
		sb.append(",FileSize:\""); sb.append(this.sizeLoc); sb.append("\"");
		sb.append(",FilePos:\""); sb.append(this.FilePos); sb.append("\"");
		sb.append(",PostedLength:\""); sb.append(this.lenSvr); sb.append("\"");
		sb.append(",PostedPercent:\""); sb.append(this.perSvr); sb.append("\"");
		sb.append(",PostComplete:\""); sb.append(this.complete); sb.append("\"");
		sb.append(",PostedTime:\""); sb.append(this.PostedTime); sb.append("\"");
		sb.append("}]");
		return sb.toString();
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
	 * @return the pid
	 */
	public int getPid() {
		return pid;
	}
	/**
	 * @param pid the pid to set
	 */
	public void setPid(int v) {
		this.pid = v;
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
	 * @return the f_fdTask
	 */
	public boolean isF_fdTask() {
		return f_fdTask;
	}
	/**
	 * @param fFdTask the f_fdTask to set
	 */
	public void setF_fdTask(boolean v) {
		f_fdTask = v;
	}
	/**
	 * @return the f_fdID
	 */
	public int getF_fdID() {
		return f_fdID;
	}
	/**
	 * @param fFdID the f_fdID to set
	 */
	public void setF_fdID(int v) {
		f_fdID = v;
	}
	/**
	 * @return the f_fdChild
	 */
	public boolean isF_fdChild() {
		return f_fdChild;
	}
	/**
	 * @param fFdChild the f_fdChild to set
	 */
	public void setF_fdChild(boolean v) {
		f_fdChild = v;
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
	 * @return the nameLoc
	 */
	public String getNameLoc() {
		return nameLoc;
	}
	/**
	 * @param nameLoc the nameLoc to set
	 */
	public void setNameLoc(String v) {
		this.nameLoc = v;
	}
	/**
	 * @return the nameSvr
	 */
	public String getNameSvr() {
		return nameSvr;
	}
	/**
	 * @param nameSvr the nameSvr to set
	 */
	public void setNameSvr(String v) {
		this.nameSvr = v;
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
	 * @return the pathRel
	 */
	public String getPathRel() {
		return pathRel;
	}
	/**
	 * @param pathRel the pathRel to set
	 */
	public void setPathRel(String v) {
		this.pathRel = v;
	}
	/**
	 * @return the fileMD5
	 */
	public String getFileMD5() {
		return md5;
	}
	/**
	 * @param fileMD5 the fileMD5 to set
	 */
	public void setFileMD5(String v) {
		md5 = v;
	}
	/**
	 * @return the fileLength
	 */
	public long getFileLength() {
		return lenLoc;
	}
	/**
	 * @param fileLength the fileLength to set
	 */
	public void setFileLength(long v) {
		lenLoc = v;
	}
	/**
	 * @return the fileSize
	 */
	public String getFileSize() {
		return sizeLoc;
	}
	/**
	 * @param fileSize the fileSize to set
	 */
	public void setFileSize(String v) {
		sizeLoc = v;
	}
	/**
	 * @return the filePos
	 */
	public long getFilePos() {
		return FilePos;
	}
	/**
	 * @param filePos the filePos to set
	 */
	public void setFilePos(long v) {
		FilePos = v;
	}
	/**
	 * @return the postedLength
	 */
	public long getPostedLength() {
		return lenSvr;
	}
	/**
	 * @param postedLength the postedLength to set
	 */
	public void setPostedLength(long v) {
		lenSvr = v;
	}
	/**
	 * @return the postedPercent
	 */
	public String getPostedPercent() {
		return perSvr;
	}
	/**
	 * @param postedPercent the postedPercent to set
	 */
	public void setPostedPercent(String v) {
		perSvr = v;
	}
	/**
	 * @return the postComplete
	 */
	public boolean isPostComplete() {
		return complete;
	}
	/**
	 * @param postComplete the postComplete to set
	 */
	public void setPostComplete(boolean v) {
		complete = v;
	}
	/**
	 * @return the postedTime
	 */
	public Date getPostedTime() {
		return PostedTime;
	}
	/**
	 * @param postedTime the postedTime to set
	 */
	public void setPostedTime(Date v) {
		PostedTime = v;
	}
	/**
	 * @return the isDeleted
	 */
	public boolean isIsDeleted() {
		return IsDeleted;
	}
	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setIsDeleted(boolean v) {
		IsDeleted = v;
	}
	/**
	 * @return the fd_json
	 */
	public String getFd_json() {
		return fd_json;
	}
	/**
	 * @param fdJson the fd_json to set
	 */
	public void setFd_json(String v) {
		fd_json = v;
	}

	public int idSvr=0;
	/**
	 * 文件夹ID
	 */
	public int pid=0;
    /**
     * 根级文件夹ID
     */
    public int pidRoot=0;
	/**
	 * 表示当前项是否是一个文件夹项。
	 */
	public boolean f_fdTask;
	/**
	 * 与xdb_folders.fd_id对应
	 */
	public int f_fdID=0;
	/// <summary>
	/// 是否是文件夹中的子文件
	/// </summary>
	public boolean f_fdChild;
	/**
	 * 用户ID。与第三方系统整合使用。
	 */
	public int uid=0;
	/**
	 * 文件在本地电脑中的名称
	 */
	public String nameLoc="";
	/**
	 * 文件在服务器中的名称。
	 */
	public String nameSvr="";
	/**
	 * 文件在本地电脑中的完整路径。示例：D:\Soft\QQ2012.exe
	 */
	public String pathLoc="";
	/**
	 * 文件在服务器中的完整路径。示例：F:\\ftp\\uer\\md5.exe
	 */
	public String pathSvr="";
	/**
	 * 文件在服务器中的相对路径。示例：/www/web/upload/md5.exe
	 */
	public String pathRel="";
	/**
	 * 文件MD5
	 */
	public String md5="";
	/**
	 * 数字化的文件长度。以字节为单位，示例：120125
	 */
	public long lenLoc=0;
	/**
	 * 格式化的文件尺寸。示例：10.03MB
	 */
	public String sizeLoc="";
	/**
	 * 文件续传位置。
	 */
	public long FilePos=0;
	/**
	 * 已上传大小。以字节为单位
	 */
	public long lenSvr=0;
	/**
	 * 已上传百分比。示例：10%
	 */
	public String perSvr="";
	public boolean complete=false;
	public Date PostedTime;
	public boolean IsDeleted=false;
	/**
	 * 文件夹JSON信息
	 */
	public String fd_json="";	
	public int filesCount=0;//add(2015-03-18):供JS调用
	public int getFilesCount(){return this.filesCount;}
	public void setFilesCount(int v){this.filesCount = v;}	
	public int filesComplete=0;
	public int getFilesComplete(){return this.filesComplete;}//add(2015-03-18):
}