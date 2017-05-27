package down3.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class DnFileInf 
{
	public DnFileInf(){}

    public int idSvr=0;
    
    public String idSign="";
    
    public String signSvr="";//服务端生成的一个标识
    
    public int uid =0;
    
    public String mac ="";
    
    public String pathLoc ="";
    
    public String pathSvr ="";
    public String blockPath="";
    public int blockSize = 0 ;
    
    public String fileUrl ="";
    
    public long lenLoc =0;
    
    public long lenSvr =0;
    
    public String sizeSvr ="";
    
    public String sizeLoc = "";
    
    public String perLoc ="";
    /// <summary>
    /// 是否已下载完成
    /// </summary>
    public Boolean complete =false;
    
    /// <summary>
    /// 本地文件名称，用来显示用的。
    /// </summary>
    public String nameLoc ="";
    
    public Boolean folder =false;
    
    public int fdID =0;
    
    public int pidRoot =0;    
        
    public int fileCount=0;
    
    @SerializedName("files")
    public List<DnFileInf> files =null;
    
	public int getIdSvr() {return idSvr;}
	public void setIdSvr(int idSvr) {this.idSvr = idSvr;}
	public int getUid() {return uid;}
	public void setUid(int uid) {this.uid = uid;}
	public String getMac() {return mac;}
	public void setMac(String mac) {this.mac = mac;}
	public String getPathLoc() {return pathLoc;}
	public void setPathLoc(String pathLoc) {this.pathLoc = pathLoc;}
	public String getFileUrl() {return fileUrl;}
	public void setFileUrl(String fileUrl) {this.fileUrl = fileUrl;}
	public long getLenLoc() {return lenLoc;}
	public void setLenLoc(long lenLoc) {this.lenLoc = lenLoc;}
	public long getLenSvr() {return lenSvr;}
	public void setLenSvr(long lenSvr) {this.lenSvr = lenSvr;}
	public String getSizeSvr() {return sizeSvr;}
	public void setSizeSvr(String sizeSvr) {this.sizeSvr = sizeSvr;}
	public String getPerLoc() {return perLoc;}
	public void setPerLoc(String perLoc) {this.perLoc = perLoc;}
	public Boolean getComplete() {return complete;}
	public void setComplete(Boolean complete) {this.complete = complete;}
	public String getNameLoc() {return nameLoc;}
	public void setNameLoc(String nameLoc) {this.nameLoc = nameLoc;}
	public Boolean getFdTask() {return folder;}
	public void setFdTask(Boolean fdTask) {this.folder = fdTask;}
	public int getFdID() {return fdID;}
	public void setFdID(int fdID) {this.fdID = fdID;}
	public int getPidRoot() {return pidRoot;}
	public void setPidRoot(int pidRoot) {this.pidRoot = pidRoot;}
	public List<DnFileInf> getFiles() {return files;}
	public void setFiles(List<DnFileInf> files) {this.files = files;}
}