package up7.biz.folder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class FileDbWriter 
{
	fd_root root;
	Connection con;
	
	public FileDbWriter(Connection con,fd_root fd)
	{
		this.con = con;
		this.root = fd;
	}
	
	PreparedStatement makeCmd(Connection con) throws SQLException
	{
        StringBuilder sb = new StringBuilder();
        sb.append("update up7_files set");
        sb.append(" f_pid=?");
        sb.append(",f_pidRoot=?");
        sb.append(",f_fdTask=?");
        sb.append(",f_fdID=?");
        sb.append(",f_fdChild=?");
        sb.append(",f_uid=?");
        sb.append(",f_nameLoc=?");
        sb.append(",f_nameSvr=?");
        sb.append(",f_pathLoc=?");
        sb.append(",f_pathSvr=?");
        sb.append(",f_pathRel=?");
        sb.append(",f_md5=?");
        sb.append(",f_lenLoc=?");
        sb.append(",f_sizeLoc=?");
        sb.append(",f_pos=?");
        sb.append(",f_lenSvr=?");
        sb.append(",f_perSvr=?");
        sb.append(",f_complete=?");
        sb.append(",f_sign=?");
        sb.append(" where f_idSvr=?");

        PreparedStatement cmd = con.prepareStatement(sb.toString());
        cmd.setInt(1, 0);//f_pid
        cmd.setInt(2, 0);//f_pidRoot
        cmd.setBoolean(3, false);//f_fdTask
        cmd.setInt(4, 0);//f_fdID
        cmd.setBoolean(5, false);//f_fdChild
        cmd.setInt(6, 0);//f_uid
        cmd.setString(7, "");//f_nameLoc
        cmd.setString(8, "");//f_nameSvr
        cmd.setString(9, "");//f_pathLoc
        cmd.setString(10, "");//f_pathSvr
        cmd.setString(11, "");//f_pathRel
        cmd.setString(12, "");//f_md5
        cmd.setInt(13, 0);//f_lenLoc
        cmd.setString(14, "");//f_sizeLoc
        cmd.setLong(15, 0);//f_pos
        cmd.setLong(16, 0);//f_lenSvr
        cmd.setString(17, "");//f_perSvr
        cmd.setBoolean(18, false);//f_complete
        cmd.setString(19, "");//f_sign
        cmd.setInt(20, 0);//f_id
        return cmd;
	}
	
	public void save() throws SQLException
	{
		PreparedStatement cmd = this.makeCmd(con);
		
		//写根目录
        cmd.setInt(1, this.root.pidSvr);//f_pid
        cmd.setInt(2, this.root.pidRoot);//f_pidRoot
        cmd.setBoolean(3, this.root.fdTask);//f_fdTask
        cmd.setInt(4, this.root.fdID);//f_fdID
        cmd.setBoolean(5, this.root.fdChild);//f_fdChild
        cmd.setInt(6, this.root.uid);//f_uid
        cmd.setString(7, this.root.nameLoc);//f_nameLoc
        cmd.setString(8, this.root.nameSvr);//f_nameSvr
        cmd.setString(9, this.root.pathLoc);//f_pathLoc
        cmd.setString(10, this.root.pathSvr);//f_pathSvr
        cmd.setString(11, this.root.pathRel);//f_pathRel
        cmd.setString(12, this.root.md5);//f_md5
        cmd.setLong(13, this.root.lenLoc);//f_lenLoc
        cmd.setString(14, this.root.sizeLoc);//f_sizeLoc
        cmd.setLong(15, this.root.pos);//f_pos
        cmd.setLong(16, this.root.lenSvr);//f_lenSvr
        cmd.setString(17, this.root.lenLoc > 0 ? this.root.perSvr : "100%");//f_perSvr
        cmd.setBoolean(18, this.root.lenLoc > 0 ? this.root.complete : true);//f_complete
        cmd.setString(19, this.root.sign);
        cmd.setInt(20, this.root.idSvr);//f_id
        cmd.execute();
		
		//写子文件列表
		for(fd_file_redis f : this.root.files)
		{
	        cmd.setInt(1, f.pidSvr);//f_pid
	        cmd.setInt(2, f.pidRoot);//f_pidRoot
	        cmd.setBoolean(3, f.fdTask);//f_fdTask
	        cmd.setInt(4, f.fdID);//f_fdID
	        cmd.setBoolean(5, f.fdChild);//f_fdChild
	        cmd.setInt(6, f.uid);//f_uid
	        cmd.setString(7, f.nameLoc);//f_nameLoc
	        cmd.setString(8, f.nameSvr);//f_nameSvr
	        cmd.setString(9, f.pathLoc);//f_pathLoc
	        cmd.setString(10, f.pathSvr);//f_pathSvr
	        cmd.setString(11, f.pathRel);//f_pathRel
	        cmd.setString(12, f.md5);//f_md5
	        cmd.setLong(13, f.lenLoc);//f_lenLoc
	        cmd.setString(14, f.sizeLoc);//f_sizeLoc
	        cmd.setLong(15, f.pos);//f_pos
	        cmd.setLong(16, f.lenSvr);//f_lenSvr
	        cmd.setString(17, f.lenLoc > 0 ? f.perSvr : "100%");//f_perSvr
	        cmd.setBoolean(18, f.lenLoc > 0 ? f.complete : true);//f_complete
	        cmd.setString(19, f.sign);
	        cmd.setInt(20, f.idSvr);//f_id
	        cmd.execute();	
		}
		cmd.close();
	}

}
