package down3.biz;

import com.google.gson.Gson;

import down3.model.DnFolderInf;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import up7.DBFile;
import up7.DbHelper;
import up7.model.FileInf;
import up7.model.FolderInf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DnFolder 
{
    public DnFolder()
    { }
    
    public static int Add(DnFolderInf inf)
    {
		StringBuilder sb = new StringBuilder();
		sb.append("insert into down3_folders(");		
		sb.append(" fd_name");
		sb.append(",fd_uid");
		sb.append(",fd_mac");
		sb.append(",fd_pathLoc");
		sb.append(",fd_id_old");
		
		sb.append(") values(");		
		
		sb.append(" ?");//sb.append("@fd_name");
		sb.append(",?");//sb.append(",@fd_uid");
		sb.append(",?");//sb.append(",@fd_mac");
		sb.append(",?");//sb.append(",@fd_pathLoc");
		sb.append(",?");//sb.append(",@fd_id_old");
		sb.append(")");

		DbHelper db = new DbHelper();
		//PreparedStatement cmd = db.GetCommand(sb.toString(),"fd_id");
		PreparedStatement cmd = db.GetCommandPK(sb.toString());
		try {
			cmd.setString(1, inf.nameLoc);
			cmd.setInt(2, inf.uid);
			cmd.setString(3, inf.mac);
			cmd.setString(4, inf.pathLoc);
			cmd.setInt(5, inf.fdID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		int fid = (int)db.ExecuteGenKey(cmd);		
		
		return fid;    	
    }
    
    public static void Clear()
    {
		DbHelper db = new DbHelper();
		db.ExecuteNonQuery("truncate table down3_folders");
		db.ExecuteNonQuery("truncate table down3_files");
    }
}