package up7;
import java.sql.*;

/*
 * 原型
*/
public class DBFolder {

	public DBFolder()
	{
	}
	
	public void remove(String idSign,Integer uid)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("begin ");
		sb.append("update up7_files set f_deleted=1 where f_idSign=? and f_uid=?;");
		sb.append("update up7_files set f_deleted=1 where f_rootSign=? and f_uid=?;");
		sb.append("update up7_folders set fd_delete=1 where f_idSign=? and fd_uid=?;");
		sb.append(" end;");		
		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sb.toString());
		try 
		{
			cmd.setString(1, idSign);
			cmd.setInt(2, uid);
			cmd.setString(3, idSign);
			cmd.setInt(4, uid);
			cmd.setString(5, idSign);
			cmd.setInt(6, uid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.ExecuteNonQuery(cmd);		
	}
}