package up7.biz.folder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class FolderDbWriter 
{	
	fd_root root;//根目录
	Connection con;
	
	public FolderDbWriter(Connection con,fd_root fd)
	{
		this.con = con;
		this.root = fd;
	}
	
	PreparedStatement makeCmd(Connection con) throws SQLException
	{
        StringBuilder sb = new StringBuilder();        
        sb.append("insert into up7_folders(");
        sb.append(" fd_sign");
        sb.append(",fd_name");
        sb.append(",fd_pidSign");
        sb.append(",fd_uid");
        sb.append(",fd_length");
        sb.append(",fd_size");
        sb.append(",fd_pathLoc");
        sb.append(",fd_pathSvr");
        sb.append(",fd_folders");
        sb.append(",fd_files");
        sb.append(",fd_rootSign");
        sb.append(") values(");
        sb.append(" ?");//fd_sign
        sb.append(",?");//fd_name,
        sb.append(",?");//fd_pidSign,
        sb.append(",?");//fd_uid
        sb.append(",?");//fd_length
        sb.append(",?");//fd_size
        sb.append(",?");//fd_pathLoc
        sb.append(",?");//fd_pathSvr
        sb.append(",?");//fd_folders
        sb.append(",?");//fd_files
        sb.append(",?");//fd_rootSign
        sb.append(")");//

        PreparedStatement cmd = con.prepareStatement(sb.toString());
        cmd.setString(1, "");//fd_sign
        cmd.setString(2, "");//fd_name
        cmd.setString(3, "");//fd_pidSign
        cmd.setInt(4, 0);//fd_uid
        cmd.setLong(5, 0);//fd_length
        cmd.setString(6, "");//fd_size
        cmd.setString(7, "");//fd_pathLoc
        cmd.setString(8, "");//fd_pathSvr
        cmd.setInt(9, 0);//fd_folders
        cmd.setInt(10, 0);//fd_files
        cmd.setString(11, "");//fd_rootSign                
        return cmd;
	}
	
	public void save() throws SQLException
	{
		PreparedStatement cmd = this.makeCmd(con);
		
		//写根目录
        cmd.setString(1, this.root.idSign);//fd_id
		cmd.setString(2,this.root.nameLoc);
        cmd.setInt(3, this.root.pidSvr);//fd_pid
        cmd.setInt(4, this.root.uid);//fd_uid
        cmd.setLong(5, this.root.lenLoc);//fd_length
        cmd.setString(6, this.root.sizeLoc);//fd_size
        cmd.setString(7, this.root.pathLoc);//fd_pathLoc
        cmd.setString(8, this.root.pathSvr);//fd_pathSvr
        cmd.setInt(9, this.root.foldersCount);//fd_folders
        cmd.setInt(10, this.root.filesCount);//fd_files
        cmd.setString(11, this.root.rootSign);//fd_pidRoot
        cmd.execute();
    
		if(this.root.folders==null) return;
		if(this.root.folders.size() <1) return;
		//写子目录列表
		for(fd_child_redis fd : this.root.folders)
		{
	        cmd.setString(1, fd.idSign);//fd_id
	        cmd.setString(2,fd.nameLoc);
	        cmd.setInt(3, fd.pidSvr);//fd_pid
	        cmd.setInt(4, fd.uid);//fd_uid
	        cmd.setLong(5, fd.lenLoc);//fd_length
	        cmd.setString(6, fd.sizeLoc);//fd_size
	        cmd.setString(7, fd.pathLoc);//fd_pathLoc
	        cmd.setString(8, fd.pathSvr);//fd_pathSvr
	        cmd.setInt(8, fd.foldersCount);//fd_folders
	        cmd.setInt(10, fd.filesCount);//fd_files
	        cmd.setString(11, fd.rootSign);//fd_pidRoot
	        cmd.execute();			
		}
		cmd.close();
	}
}
