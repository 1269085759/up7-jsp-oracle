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
        sb.append(" f_pidSign=?");
        sb.append(",f_rootSign=?");
        sb.append(",f_fdTask=?");        
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
        sb.append(" where f_idSign=?");

        PreparedStatement cmd = con.prepareStatement(sb.toString());
        cmd.setString(1, "");//pidSign
        cmd.setString(2, "");//rootSign
        cmd.setBoolean(3, false);//f_fdTask        
        cmd.setBoolean(4, false);//f_fdChild
        cmd.setInt(5, 0);//f_uid
        cmd.setString(6, "");//f_nameLoc
        cmd.setString(7, "");//f_nameSvr
        cmd.setString(8, "");//f_pathLoc
        cmd.setString(9, "");//f_pathSvr
        cmd.setString(10, "");//f_pathRel
        cmd.setString(11, "");//f_md5
        cmd.setInt(12, 0);//f_lenLoc
        cmd.setString(13, "");//f_sizeLoc
        cmd.setLong(14, 0);//f_pos
        cmd.setLong(15, 0);//f_lenSvr
        cmd.setString(16, "");//f_perSvr
        cmd.setBoolean(17, false);//f_complete
        cmd.setString(18, "");//f_sign
        cmd.setString(19, "");//f_idSign
        return cmd;
	}
	
	public void save() throws SQLException
	{
		if(this.root.files==null) return;
		if(this.root.files.size() < 1) return;
		PreparedStatement cmd = this.makeCmd(con);
		
		//写根目录
        cmd.setString(1, this.root.pidSign);//f_pidSign
        cmd.setString(2, this.root.rootSign);//f_rootSign
        cmd.setBoolean(3, this.root.fdTask);//f_fdTask
        cmd.setBoolean(4, this.root.fdChild);//f_fdChild
        cmd.setInt(5, this.root.uid);//f_uid
        cmd.setString(6, this.root.nameLoc);//f_nameLoc
        cmd.setString(7, this.root.nameSvr);//f_nameSvr
        cmd.setString(8, this.root.pathLoc);//f_pathLoc
        cmd.setString(9, this.root.pathSvr);//f_pathSvr
        cmd.setString(10, this.root.pathRel);//f_pathRel
        cmd.setString(11, this.root.md5);//f_md5
        cmd.setLong(12, this.root.lenLoc);//f_lenLoc
        cmd.setString(13, this.root.sizeLoc);//f_sizeLoc
        cmd.setLong(14, this.root.pos);//f_pos
        cmd.setLong(15, this.root.lenSvr);//f_lenSvr
        cmd.setString(16, this.root.lenLoc > 0 ? this.root.perSvr : "100%");//f_perSvr
        cmd.setBoolean(17, this.root.lenLoc > 0 ? this.root.complete : true);//f_complete
        cmd.setString(18, this.root.sign);
        cmd.setString(19, this.root.idSign);//f_id
        cmd.execute();
		
		//写子文件列表
		for(fd_file_redis f : this.root.files)
		{
	        cmd.setString(1, f.pidSign);//f_pidSign
	        cmd.setString(2, f.rootSign);//f_rootSign
	        cmd.setBoolean(3, f.fdTask);//f_fdTask
	        cmd.setBoolean(4, f.fdChild);//f_fdChild
	        cmd.setInt(5, f.uid);//f_uid
	        cmd.setString(6, f.nameLoc);//f_nameLoc
	        cmd.setString(7, f.nameSvr);//f_nameSvr
	        cmd.setString(8, f.pathLoc);//f_pathLoc
	        cmd.setString(9, f.pathSvr);//f_pathSvr
	        cmd.setString(10, f.pathRel);//f_pathRel
	        cmd.setString(11, f.md5);//f_md5
	        cmd.setLong(12, f.lenLoc);//f_lenLoc
	        cmd.setString(13, f.sizeLoc);//f_sizeLoc
	        cmd.setLong(14, f.pos);//f_pos
	        cmd.setLong(15, f.lenSvr);//f_lenSvr
	        cmd.setString(16, f.lenLoc > 0 ? f.perSvr : "100%");//f_perSvr
	        cmd.setBoolean(17, f.lenLoc > 0 ? f.complete : true);//f_complete
	        cmd.setString(18, f.sign);
	        cmd.setString(19, f.idSign);//f_id
	        cmd.execute();	
		}
		cmd.close();
	}

}
