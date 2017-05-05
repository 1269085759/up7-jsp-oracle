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
        sb.append("insert into up7_files(");
        sb.append(" f_idSign");
        sb.append(",f_pidSign");
        sb.append(",f_rootSign");
        sb.append(",f_fdChild");
        sb.append(",f_uid");
        sb.append(",f_nameLoc");
        sb.append(",f_nameSvr");
        sb.append(",f_pathLoc");
        sb.append(",f_pathSvr");
        sb.append(",f_lenLoc");
        sb.append(",f_sizeLoc");
        sb.append(",f_lenSvr");
        sb.append(",f_perSvr");
        sb.append(",f_sign");
        sb.append(",f_complete");
        sb.append(",f_fdTask");
        
        sb.append(") values(");
        
        sb.append(" ?");//f_idSign
        sb.append(",?");//f_pidSign
        sb.append(",?");//f_rootSign
        sb.append(",?");//f_fdChild
        sb.append(",?");//f_uid
        sb.append(",?");//f_nameLoc
        sb.append(",?");//f_nameSvr
        sb.append(",?");//f_pathLoc
        sb.append(",?");//f_pathSvr
        sb.append(",?");//f_lenLoc
        sb.append(",?");//f_sizeLoc
        sb.append(",?");//f_lenSvr
        sb.append(",?");//f_perSvr
        sb.append(",?");//f_sign
        sb.append(",1");//f_complete
        sb.append(",?");//f_fdTask
        sb.append(")");

        PreparedStatement cmd = con.prepareStatement(sb.toString());
        cmd.setString(1, "");//idSign
        cmd.setString(2, "");//pidSign
        cmd.setString(3, "");//rootSign
        cmd.setBoolean(4, false);//fdChild
        cmd.setInt(5, 0);//uid
        cmd.setString(6, "");//nameLoc
        cmd.setString(7, "");//nameSvr
        cmd.setString(8, "");//pathLoc
        cmd.setString(9, "");//pathSvr
        cmd.setLong(10, 0);//lenLoc
        cmd.setString(11, "");//sizeLoc
        cmd.setLong(12, 0);//lenSvr
        cmd.setString(13, "");//perSvr
        cmd.setString(14, "");//sign
        cmd.setBoolean(15, false);//fdTask
        return cmd;
	}
	
	public void save() throws SQLException
	{
		if(this.root.files==null) return;
		if(this.root.files.size() < 1) return;
		PreparedStatement cmd = this.makeCmd(con);
		
		//写根目录
        cmd.setString(1, this.root.idSign);//idSign
        cmd.setString(2, this.root.pidSign);//pidSign
        cmd.setString(3, "");//rootSign
        cmd.setBoolean(4, false);//fdChild
        cmd.setInt(5, this.root.uid);//uid
        cmd.setString(6, this.root.nameLoc);//nameLoc
        cmd.setString(7, this.root.nameSvr);//nameSvr
        cmd.setString(8, this.root.pathLoc);//pathLoc
        cmd.setString(9, this.root.pathSvr);//pathSvr
        cmd.setLong(10, this.root.lenLoc);//lenLoc
        cmd.setString(11, this.root.sizeLoc);//sizeLoc
        cmd.setLong(12, this.root.lenLoc);//lenSvr
        cmd.setString(13, "100%");//perSvr
        cmd.setString(14, this.root.sign);//sign
        cmd.setBoolean(15, true);//sign
        cmd.execute();
		
		//写子文件列表
		for(fd_file_redis f : this.root.files)
		{
	        cmd.setString(1, f.idSign);//idSign
	        cmd.setString(2, f.pidSign);//pidSign
	        cmd.setString(3, f.rootSign);//rootSign
	        cmd.setBoolean(4, true);//fdChild
	        cmd.setInt(5, f.uid);//uid
	        cmd.setString(6, f.nameLoc);//nameLoc
	        cmd.setString(7, f.nameSvr);//nameSvr
	        cmd.setString(8, f.pathLoc);//pathLoc
	        cmd.setString(9, f.pathSvr);//pathSvr
	        cmd.setLong(10, f.lenLoc);//lenLoc
	        cmd.setString(11, f.sizeLoc);//sizeLoc
	        cmd.setLong(12, f.lenLoc);//lenSvr
	        cmd.setString(13, "100%");//perSvr
	        cmd.setString(14, f.sign);//sign
	        cmd.setBoolean(15, false);//fdTask
	        cmd.execute();	
		}
		cmd.close();
	}

}
