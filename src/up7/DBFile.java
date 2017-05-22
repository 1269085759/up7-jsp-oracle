package up7;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import up7.model.xdb_files;

/*
 * 原型
*/
/**
 * @author Administrator
 * 更新记录：
 * 	2015-07-30 修复获取文件夹大小错误的问题。
 *
 */
public class DBFile {

	public DBFile()
	{
	}	

	/**
	 * 将文件缓存信息添加到数据库，
	 * @param inf
	 */
	public void addComplete(xdb_files inf)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("insert into up7_files(");
		sb.append(" f_idSign");//1		
		sb.append(",f_uid");//2
		sb.append(",f_nameLoc");//3
		sb.append(",f_nameSvr");//4
		sb.append(",f_pathLoc");//5
		sb.append(",f_pathSvr");//6		
		sb.append(",f_lenLoc");//7
		sb.append(",f_lenSvr");//8
		sb.append(",f_perSvr");//
		sb.append(",f_sizeLoc");//9
		sb.append(",f_complete");//
		sb.append(",f_blockCount");//10
		sb.append(",f_blockSize");//11
		sb.append(",f_blockPath");//12
		
		sb.append(") values(");
				
		sb.append(" ?");//sb.append("@f_idSign");
		sb.append(",?");//sb.append(",@f_uid");
		sb.append(",?");//sb.append(",@f_nameLoc");
		sb.append(",?");//sb.append(",@f_nameSvr");
		sb.append(",?");//sb.append(",@f_pathLoc");
		sb.append(",?");//sb.append(",@f_pathSvr");
		sb.append(",?");//sb.append(",@f_lenLoc");
		sb.append(",?");//sb.append(",@f_lenSvr");
		sb.append(",'100%'");//sb.append(",@f_perSvr");
		sb.append(",?");//sb.append(",@f_sizeLoc");
		sb.append(",1");//sb.append(",@f_complete");
		sb.append(",?");//sb.append(",@f_blockCount");
		sb.append(",?");//sb.append(",@f_blockSize");
		sb.append(",?");//sb.append(",@f_blockPath");
		sb.append(")");

		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sb.toString());
		try {
			cmd.setString(1, inf.idSign);
			cmd.setInt(2, inf.uid);
			cmd.setString(3, inf.nameLoc);
			cmd.setString(4, inf.nameLoc);
			cmd.setString(5, inf.pathLoc);
			cmd.setString(6, inf.pathSvr);
			cmd.setLong(7, inf.lenLoc);
			cmd.setLong(8, inf.lenLoc);			
			cmd.setString(9, inf.lenLoc>1024 ? inf.sizeLoc : PathTool.getDataSize(inf.lenLoc));
			cmd.setInt(10, inf.blockCount);
			cmd.setInt(11,inf.blockSize);
			cmd.setString(12,inf.blockPath);
			cmd.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 清空文件表，文件夹表数据。
	 */
	static public void Clear()
	{
		DbHelper db = new DbHelper();
		db.ExecuteNonQuery("truncate table up7_files");
		db.ExecuteNonQuery("truncate table up7_folders");
	}

	/// <summary>
	/// 上传完成。将所有相同MD5文件进度都设为100%
	/// </summary>
	public void complete(int uid,String idSign)
	{
		String sql = "update up7_files set f_lenSvr=f_lenLoc,f_perSvr='100%',f_complete=1 where f_idSign=? and f_uid=?";
		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sql);
		
		try
		{
			cmd.setString(1,idSign);
			cmd.setInt(2, uid);
		} catch(SQLException e){
			e.printStackTrace();
		}
		db.ExecuteNonQuery(cmd);//在部分环境中测试发现执行后没有效果。f_complete仍然为0
	}
	
	public void remove(String idSign)
	{
		String sql = "update up7_files set f_deleted=1 where f_idSign=?";
		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sql);

		try {			
			cmd.setString(1, idSign);
			db.ExecuteNonQuery(cmd);
		} catch (SQLException e) {
			System.out.println("更新数据库文件信息失败，");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}