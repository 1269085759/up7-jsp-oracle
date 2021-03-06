package down3.biz;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import down3.model.DnFileInf;
import down3.model.DnFolderInf;

import oracle.jdbc.internal.OracleTypes;
import up7.DbHelper;
import up7.XDebug;


public class folder_appender 
{
	public folder_appender()
	{
		
	}
	
	public void add(DnFolderInf fd) throws SQLException
	{
        String sql = "call fd_add_batch(?,?,?)";
        DbHelper db = new DbHelper();
        CallableStatement stor = db.GetCommandStored(sql);
        stor.setInt(1, fd.files.size());
        stor.setInt(2, fd.uid);
        stor.registerOutParameter(3, OracleTypes.CLOB);
        stor.execute();
        String f_ids = stor.getString(3);
        stor.close();
        XDebug.Output("ids",f_ids);
        

        StringBuilder sb = new StringBuilder();
        sb.append("update down3_files set");
        sb.append(" f_nameLoc=?");
        sb.append(",f_pathLoc=?");
        sb.append(",f_fileUrl=?");
        sb.append(",f_lenSvr=?");
        sb.append(",f_sizeSvr=?");
        sb.append(",f_pidRoot=?");
        sb.append(",f_fdTask=?");
        sb.append(" where f_idSvr=?");
        db = new DbHelper();
        PreparedStatement cmd = db.GetCommand(sb.toString());
        
        String[] ids = f_ids.split(",");
        XDebug.Output("ids总数",ids.length);
        XDebug.Output("files总数",fd.files.size());

        //更新文件夹
        fd.idSvr = Integer.parseInt(ids[0]);
        this.update_file(cmd, fd);
        
        //更新文件列表        
        for(int i = 1 , l = ids.length;i< l;++i)
        {
        	DnFileInf f = fd.files.get(i-1);
        	f.idSvr = Integer.parseInt(ids[i]);
        	f.pidRoot = fd.idSvr;
            
            this.update_file(cmd, f);
        }    
	}

    void update_file(PreparedStatement cmd,DnFileInf f) throws SQLException
    {
        cmd.setString(1, f.nameLoc);
        cmd.setString(2, f.pathLoc);
        cmd.setString(3, f.fileUrl);
        cmd.setLong(4, f.lenSvr);
        cmd.setString(5, f.sizeSvr);
        cmd.setInt(6, f.pidRoot);
        cmd.setBoolean(7, f.fdTask);
        cmd.setInt(8, f.idSvr);
        cmd.execute();
    }
}
