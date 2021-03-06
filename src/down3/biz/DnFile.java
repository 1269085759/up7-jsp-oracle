package down3.biz;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import down3.model.DnFileInf;
import down3.model.DnFolderInf;

import up7.DbHelper;

public class DnFile 
{
	public DnFile()
	{
	}
	
	/**
	 * 获取文件信息
	 * @param fid
	 * @return
	 */
	public down3.model.DnFileInf Find(int fid)
	{		
		String sql = "select * from down3_files where f_idSvr=?";
		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sql);
		try
		{
			down3.model.DnFileInf inf 	= new DnFileInf();
			cmd.setInt(1, fid);
			ResultSet r = db.ExecuteDataSet(cmd);
			if(r.next())
			{
				inf.idSvr 		= fid;
				inf.lenLoc 	= r.getLong(6);
				inf.lenSvr 	= r.getLong(7);
				inf.mac 		= r.getString(3);
				inf.pathLoc 	= r.getString(4);
				inf.fileUrl 	= r.getString(5);
				
				cmd.close();
				cmd.getConnection().close();
			}
			
			return inf;
		}
		catch(SQLException e){e.printStackTrace();}
		return null;
	}

    public int Add(down3.model.DnFileInf inf)
    {
    	int idSvr = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("insert into down3_files(");
        sb.append(" f_idSvr");
        sb.append(",f_uid");
        sb.append(",f_nameLoc");
        sb.append(",f_pathLoc");
        sb.append(",f_fileUrl");
        sb.append(",f_lenSvr");
        sb.append(",f_sizeSvr");
        sb.append(") values(");
        sb.append(" SEQ_dn_f_idSvr.NEXTVAL");//fid
        sb.append(",?");//uid
        sb.append(",?");//name
        sb.append(",?");//pathLoc
        sb.append(",?");//pathSvr
        sb.append(",?");//lenSvr
        sb.append(",?");//sizeSvr
        sb.append(")");
		
		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sb.toString(),"f_idSvr");

		try
		{
			cmd.setInt(1,inf.uid);
			cmd.setString(2,inf.nameLoc);
			cmd.setString(3,inf.pathLoc);
			cmd.setString(4,inf.fileUrl);
			cmd.setLong(5,inf.lenSvr);
			cmd.setString(6,inf.sizeSvr);
			idSvr = (int) db.ExecuteGenKey(cmd);			
		}
		catch (SQLException e){e.printStackTrace();}		

		return idSvr;
    }
    
    /**
     * 添加一个文件夹下载任务
     * @param inf
     * @return
     */
    public static int Add(DnFolderInf inf)
    {
    	int idSvr = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("insert into down3_files(");
        sb.append(" f_idSvr");
        sb.append(" f_uid");
        sb.append(",f_pathLoc");
        sb.append(") values(");
        sb.append(" SEQ_dn_f_idSvr.NEXTVAL");//fid
        sb.append(" ?");//uid
        sb.append(",?");//pathLoc
        sb.append(")");
		
		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sb.toString(),"f_idSvr");

		try
		{
			cmd.setInt(1,inf.uid);
			cmd.setString(2,inf.pathLoc);
			idSvr = (int)db.ExecuteGenKey(cmd);
		}
		catch (SQLException e){e.printStackTrace();}
		return idSvr;    	
    }

    /**
     * 将文件设为已完成
     * @param fid
     */
    public void Complete(int fid)
    {
		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand("update down3_files set f_complete=1 where f_idSvr=?");
		try
		{
			cmd.setInt(1,fid);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		db.ExecuteNonQuery(cmd);
    }

    /// <summary>
    /// 删除文件
    /// </summary>
    /// <param name="fid"></param>
    public void Delete(int fid,int uid,String mac)
    {
        String sql = "delete from down3_files where f_idSvr=? and f_uid=? and f_mac=?";
        DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sql);

		try
		{
			cmd.setInt(1,fid);
			cmd.setInt(2,uid);
			cmd.setString(3,mac);
			db.ExecuteNonQuery(cmd);
		}
		catch (SQLException e){e.printStackTrace();}
    }
    
    public static void Delete(String fid,String uid)
    {
        String sql = "delete from down3_files where f_idSvr=? and f_uid=?";
        DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sql);

		try
		{
			cmd.setInt(1,Integer.parseInt(fid) );
			cmd.setInt(2,Integer.parseInt(uid) );
			db.ExecuteNonQuery(cmd);
		}
		catch (SQLException e){e.printStackTrace();}
    }
    
    //删除文件夹的所有子文件
    public static void delFiles(String pidRoot,String uid)
    {
        String sql = "delete from down3_files where f_pidRoot=? and f_uid=?";
        DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sql);

		try
		{
			cmd.setInt(1,Integer.parseInt(pidRoot) );
			cmd.setInt(2,Integer.parseInt(uid) );
			db.ExecuteNonQuery(cmd);
		}
		catch (SQLException e){e.printStackTrace();}    	
    }

    /**
     * 更新文件进度信息
     * @param fid
     * @param uid
     * @param mac
     * @param lenLoc
     */
    public void updateProcess(int fid,int uid,String lenLoc,String perLoc)
    {
        String sql = "update down3_files set f_lenLoc=?,f_perLoc=? where f_idSvr=? and f_uid=?";
        DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sql);

		try
		{
			cmd.setString(1,lenLoc);
			cmd.setString(2,perLoc);
			cmd.setInt(3,fid);
			cmd.setInt(4,uid);
			
			db.ExecuteNonQuery(cmd);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
    }    
    
    public static void Clear()
    {
		DbHelper db = new DbHelper();
		db.ExecuteNonQuery("truncate table down3_files");
		//db.ExecuteNonQuery("truncate table hup_folders");
    }
}