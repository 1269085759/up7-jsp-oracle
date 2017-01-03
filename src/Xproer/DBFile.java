package Xproer;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import net.sf.json.JSONArray;
import com.google.gson.Gson;

/*
 * 原型
*/
public class DBFile {

	public DBFile()
	{
	}
	
	/**
	 * 获取指定文件夹下面的所有文件，
	 * @param fid
	 * @param files
	 * @param ids
	 */
	public static void GetCompletes(int fid,ArrayList<FileInf> files,ArrayList<String> ids)
	{
        StringBuilder sql = new StringBuilder("select ");
        sql.append("f_idSvr");
        sql.append(",f_nameLoc");
        sql.append(",f_pathLoc");
        sql.append(",f_length");
        sql.append(",f_size");
        sql.append(",f_md5");
        sql.append(",f_pidRoot");
        sql.append(",f_pid");
        sql.append(",f_postedLength");
        sql.append(",f_pathSvr");//fix:服务器会重复创建文件项的问题
        sql.append(",fd_pathRel");//
        sql.append(" from hup_files");
        sql.append(" left join hup_folders");
        sql.append(" on fd_id = f_pid");
        sql.append(" where f_pidRoot=? and f_postComplete=1");//bug:在部分环境中测试发现f_postComplete为0，可以考虑取消f_postComplete判断

        DbHelper db = new DbHelper();
        PreparedStatement cmd = db.GetCommand(sql.toString());
        try
        {
        	cmd.setInt(1, fid);
        	ResultSet r = db.ExecuteDataSet(cmd);
            while (r.next())
            {
                FileInf fi 		= new FileInf();
                fi.idSvr 		= r.getInt(1);
                fi.name 		= r.getString(2);
                fi.pathLoc 		= r.getString(3);
                fi.length 		= r.getString(4);
                fi.size 		= r.getString(5);
                fi.md5 			= r.getString(6);
                fi.pidRoot 		= r.getInt(7);
                fi.pidSvr 		= r.getInt(8);
                fi.postLength 	= r.getString(9);
                fi.pathSvr 		= r.getString(10);//fix:服务器会重复创建文件项的问题
                fi.pathRel 		= r.getString(11) + "\\";//相对路径：root\\child\\folder\\
                files.add(fi);
                //添加到列表
                ids.add( Integer.toString(fi.idSvr) );
            }
            r.close();            
            cmd.close();
        }
        catch (SQLException e) {e.printStackTrace();}
        
	}
	
	static public String GetAllUnComplete(int uid)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		sb.append(" f_idSvr");
		sb.append(",f_fdTask");
		sb.append(",f_fdID");
		sb.append(",f_nameLoc");
		sb.append(",f_pathLoc");
		sb.append(",f_md5");
		sb.append(",f_length");
		sb.append(",f_size");
		sb.append(",f_pos");
		sb.append(",f_PostedLength");
		sb.append(",f_PostedPercent");
		sb.append(",f_PostComplete");
		sb.append(",f_pathSvr");//fix(2015-03-16):修复无法续传文件的问题。
		//文件夹信息
		sb.append(",fd_files");
		sb.append(",fd_filesComplete");
		sb.append(" from hup_files left join hup_folders on hup_files.f_fdID = hup_folders.fd_id");//change(2015-03-18):联合查询文件夹数据
		sb.append(" where f_uid=? and f_IsDeleted=0 and f_fdChild=0 and f_PostComplete=0");//fix(2015-03-18):只加载未完成列表

		ArrayList<xdb_files> files = new ArrayList<xdb_files>();
		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sb.toString());
		try {
			cmd.setInt(1, uid);
			ResultSet r = db.ExecuteDataSet(cmd);
			while(r.next())
			{
				xdb_files f 	= new xdb_files();
				f.idSvr 		= r.getInt(1);
				f.f_fdTask 		= r.getBoolean(2);
				f.f_fdID 		= r.getInt(3);
				f.nameLoc 		= r.getString(4);
				f.pathLoc 		= r.getString(5);
				f.md5 			= r.getString(6);
				f.lenLoc 		= Long.parseLong(r.getString(7));
				f.sizeLoc 		= r.getString(8);
				f.FilePos 		= Long.parseLong(r.getString(9));
				f.lenSvr 		= Long.parseLong(r.getString(10));
				f.perSvr 		= r.getString(11);
				f.complete 		= r.getBoolean(12);
				f.pathSvr		= r.getString(13);//fix(2015-03-19):修复无法续传文件的问题。
				f.filesCount 	= r.getInt(14);//add(2015-03-18):
				f.filesComplete = r.getInt(15);//add(2015-03-18):

				files.add(f);
				
			}
			r.close();
			cmd.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		ArrayList<xdb_files> arrFiles = new ArrayList<xdb_files>();
		for(xdb_files f : files)
		{
			//是文件夹任务=>取文件夹JSON
			if (f.f_fdTask)
			{
				FolderInf fd = new FolderInf();
				f.fd_json = DBFolder.GetFilesUnComplete(f.f_fdID,fd);
                float pdPer = 0;
                long lenPosted = DBFolder.GetLenPosted(f.f_fdID);
                fd.lenSvr = Long.toString( lenPosted );
                f.lenSvr = lenPosted;//给客户端使用。
                fd.filesCount = f.filesCount;//add(2015-03-18):
                fd.filesComplete = f.filesComplete;//add(2015-03-18)
                long len = Long.parseLong(fd.length);
                if (lenPosted > 0 && len > 0)
                {
                    pdPer = (float)Math.round(((lenPosted*1.0f) / len*1.0f) * 100.0f);
                }
                f.idSvr = f.f_fdID;//将文件ID改为文件夹的ID，客户端续传文件夹时将会使用这个ID。
				f.perSvr = Float.toString( pdPer ) + "%";
                f.sizeLoc = fd.size;
			}
			arrFiles.add( f );
		}
		Gson g = new Gson();
	    return g.toJson( arrFiles );//bug:arrFiles为空时，此行代码有异常	
	}
	
	/**
	 * 获取所有文件和文件夹列表，不包含子文件夹，包含已上传完的和未上传完的
	 * @param uid
	 * @return
	 */
	static public String GetAll(int uid)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		sb.append("f_idSvr");
		sb.append(",f_fdTask");
		sb.append(",f_fdID");
		sb.append(",f_nameLoc");
		sb.append(",f_pathLoc");
		sb.append(",f_md5");
		sb.append(",f_length");
		sb.append(",f_size");
		sb.append(",f_pos");
		sb.append(",f_PostedLength");
		sb.append(",f_PostedPercent");
		sb.append(",f_PostComplete");
		sb.append(" from hup_files where f_uid=? and f_IsDeleted=0 and f_fdChild=0");

		ArrayList<xdb_files> files = new ArrayList<xdb_files>();
		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sb.toString());
		try {
			cmd.setInt(1, uid);
			ResultSet r = db.ExecuteDataSet(cmd);
			while(r.next())
			{
				xdb_files f 	= new xdb_files();
				f.idSvr 		= r.getInt(1);
				f.f_fdTask 		= r.getBoolean(2);
				f.f_fdID 		= r.getInt(3);
				f.nameLoc 		= r.getString(4);
				f.pathLoc 		= r.getString(5);
				f.md5 			= r.getString(6);
				f.lenLoc 		= Long.parseLong(r.getString(7));
				f.sizeLoc 		= r.getString(8);
				f.FilePos 		= Long.parseLong(r.getString(9));
				f.lenSvr 		= Long.parseLong(r.getString(10));
				f.perSvr 		= r.getString(11);
				f.complete 		= r.getBoolean(12);

				files.add(f);
				
			}
			r.close();
			cmd.close();
			//cmd.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		ArrayList<xdb_files> arrFiles = new ArrayList<xdb_files>();
		for(xdb_files f : files)
		{
			//是文件夹任务=>取文件夹JSON
			if (f.f_fdTask)
			{
				FolderInf fd = new FolderInf();
				f.fd_json = DBFolder.GetFilesUnComplete(f.f_fdID,fd);
                float pdPer = 0;
                long lenPosted = DBFolder.GetLenPosted(f.f_fdID);
                fd.lenSvr = Long.toString( lenPosted );
                f.lenSvr = lenPosted;//给客户端使用。
                long len = Long.parseLong(fd.length);
                if (lenPosted > 0 && len > 0)
                {
                    pdPer = (float)Math.round(((lenPosted*1.0f) / len*1.0f) * 100.0f);
                }
                f.idSvr = f.f_fdID;//将文件ID改为文件夹的ID，客户端续传文件夹时将会使用这个ID。
				f.perSvr = Float.toString( pdPer ) + "%";
                f.sizeLoc = fd.size;
			}
			arrFiles.add( f );
		}
		Gson g = new Gson();
	    return g.toJson( arrFiles );//bug:arrFiles为空时，此行代码有异常		
	}
	
	/**
	 * 获取所有已经上传完的文件和文件夹供下载列表使用。
	 * @param uid
	 * @return
	 */
	public static String GetAllComplete(int uid)
	{
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append(" f_idSvr");
        sb.append(",f_fdTask");
        sb.append(",f_fdID");
        sb.append(",f_nameLoc");
        sb.append(",f_pathLoc");
        sb.append(",f_length");
        sb.append(",f_size");
        sb.append(",fd_size");
        sb.append(" from hup_files");
        sb.append(" left join hup_folders");
        sb.append(" on f_fdID = fd_id");
        sb.append(" where f_IsDeleted=0 and f_fdChild=0 and f_PostComplete=1");

        ArrayList<xdb_files> files = new ArrayList<xdb_files>();
        DbHelper db = new DbHelper();
        PreparedStatement cmd = db.GetCommand(sb.toString());
        try
        {
        	ResultSet r = db.ExecuteDataSet(cmd);
            while (r.next())
            {
                xdb_files f = new xdb_files();
                f.idSvr 	= r.getInt(1);
                f.f_fdTask 	= r.getBoolean(2);
                f.f_fdID 	= r.getInt(3);
                f.nameLoc 	= r.getString(4);
                f.pathLoc 	= r.getString(5);
                f.lenLoc 	= Long.parseLong(r.getString(6));
                f.sizeLoc 	= r.getString(7);
                //是文件夹
                if (f.f_fdTask)
                {
                    f.sizeLoc = r.getString(8);
                }

                files.add(f);

            }
            r.close();
        }
        catch (SQLException e) {e.printStackTrace();}
        
        Gson g = new Gson();
	    return g.toJson( files );//bug:arrFiles为空时，此行代码有异常
	}

	/**
	 * 根据文件ID获取文件信息
	 * @param fid
	 * @param inf
	 * @return
	 */
	public boolean GetFileInfByFid(int fid,xdb_files inf)
	{
		boolean ret = false;
		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		sb.append("f_uid");
		sb.append(",f_nameLoc");
		sb.append(",f_nameSvr");
		sb.append(",f_pathLoc");
		sb.append(",f_pathSvr");
		sb.append(",f_pathRel");
		sb.append(",f_md5");
		sb.append(",f_length");
		sb.append(",f_size");
		sb.append(",f_pos");
		sb.append(",f_PostedLength");
		sb.append(",f_PostedPercent");
		sb.append(",f_PostComplete");
		sb.append(",f_PostedTime");
		sb.append(",f_IsDeleted");
		sb.append(" from hup_files where f_idSvr=? ");
		
		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sb.toString());
		try {
			cmd.setInt(1, fid);
			ResultSet r = db.ExecuteDataSet(cmd);

			if (r.next())
			{
				inf.idSvr 			= fid;
				inf.uid 			= r.getInt(1);
				inf.nameLoc 		= r.getString(2);
				inf.nameSvr 		= r.getString(3);
				inf.pathLoc 		= r.getString(4);
				inf.pathSvr 		= r.getString(5);
				inf.pathRel 		= r.getString(6);
				inf.md5 			= r.getString(7);
				inf.lenLoc 			= Long.parseLong( r.getString(8) );
				inf.sizeLoc 		= r.getString(9);
	            inf.FilePos 		= Long.parseLong( r.getString(10) );
	            inf.lenSvr 			= Long.parseLong(r.getString(11) );
				inf.perSvr 			= r.getString(12);
				inf.complete 		= r.getBoolean(13);
				inf.PostedTime 		= r.getDate(14);
				inf.IsDeleted 		= r.getBoolean(15);
				ret = true;
			}
			cmd.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret;
	}

	/// <summary>
	/// 根据文件MD5获取文件信息
	/// </summary>
	/// <param name="md5"></param>
	/// <param name="inf"></param>
	/// <returns></returns>
	public boolean GetFileInfByMd5(String md5, xdb_files inf)
	{
		boolean ret = false;
		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		sb.append(" f_idSvr");
		sb.append(",f_uid");
		sb.append(",f_nameLoc");
		sb.append(",f_nameSvr");
		sb.append(",f_pathLoc");
		sb.append(",f_pathSvr");
		sb.append(",f_pathRel");
		sb.append(",f_md5");
		sb.append(",f_length");
		sb.append(",f_size");
		sb.append(",f_pos");
		sb.append(",f_PostedLength");
		sb.append(",f_PostedPercent");
		sb.append(",f_PostComplete");
		sb.append(",f_PostedTime");
		sb.append(",f_IsDeleted");
		sb.append(" from hup_files");
		sb.append(" where f_md5=? and rownum<=1");
		sb.append(" order by to_number(f_PostedLength) DESC");

		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sb.toString());
		try {
			cmd.setString(1, md5);
			ResultSet r = db.ExecuteDataSet(cmd);
			if (r.next())
			{
				inf.idSvr 			= r.getInt(1);
				inf.uid 			= r.getInt(2);
				inf.nameLoc 		= r.getString(3);
				inf.nameSvr 		= r.getString(4);
				inf.pathLoc 		= r.getString(5);
				inf.pathSvr 		= r.getString(6);
				inf.pathRel 		= r.getString(7);
				inf.md5 			= r.getString(8);
				inf.lenLoc 			= Long.parseLong( r.getString(9) );
				inf.sizeLoc 		= r.getString(10);
				inf.FilePos 		= Long.parseLong( r.getString(11) );
				inf.lenSvr 			= Long.parseLong(r.getString(12) );
				inf.perSvr 			= r.getString(13);
				inf.complete 		= r.getBoolean(14);
				inf.PostedTime 		= r.getDate(15);
				inf.IsDeleted 		= r.getBoolean(16);
				ret = true;
			}
			cmd.close();			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret;
	}

	/// <summary>
	/// 增加一条数据，并返回新增数据的ID
	/// 在ajax_create_fid.aspx中调用
	/// 文件名称，本地路径，远程路径，相对路径都使用原始字符串。
	/// d:\soft\QQ2012.exe
	/// </summary>
	public int Add(xdb_files model)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("insert into hup_files(");
		sb.append(" f_idSvr");
		sb.append(",f_size");
		sb.append(",f_pos");
		sb.append(",f_PostedLength");
		sb.append(",f_PostedPercent");
		sb.append(",f_PostComplete");
		sb.append(",f_PostedTime");
		sb.append(",f_IsDeleted");
		sb.append(",f_fdChild");
		sb.append(",f_uid");
		sb.append(",f_nameLoc");
		sb.append(",f_nameSvr");
		sb.append(",f_pathLoc");
		sb.append(",f_pathSvr");
		sb.append(",f_pathRel");
		sb.append(",f_md5");
		sb.append(",f_length");
		
		sb.append(") values (");
		
		sb.append("SEQ_f_idSvr.NEXTVAL");
		sb.append(",?");//sb.append("@f_size");
		sb.append(",?");//sb.append(",@FilePos");
		sb.append(",?");//sb.append(",@PostedLength");
		sb.append(",?");//sb.append(",@PostedPercent");
		sb.append(",?");//sb.append(",@PostComplete");
		sb.append(",?");//sb.append(",@PostedTime");
		sb.append(",?");//sb.append(",@IsDeleted");
		sb.append(",?");//sb.append(",@f_fdChild");
		sb.append(",?");//sb.append(",@uid");
		sb.append(",?");//sb.append(",@f_nameLoc");
		sb.append(",?");//sb.append(",@FileNameRemote");
		sb.append(",?");//sb.append(",@f_pathLoc");
		sb.append(",?");//sb.append(",@FilePathRemote");
		sb.append(",?");//sb.append(",@FilePathRelative");
		sb.append(",?");//sb.append(",@FileMD5");
		sb.append(",?");//sb.append(",@FileLength");
		sb.append(") ");

		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sb.toString(),"f_idSvr");
		
		try {
			cmd.setString(1, model.sizeLoc);
			cmd.setString(2, Long.toString(model.FilePos) );
			cmd.setString(3, Long.toString(model.lenSvr) );
			cmd.setString(4, model.perSvr);
			cmd.setBoolean(5, model.complete);
			cmd.setDate(6, (java.sql.Date) model.PostedTime);
			cmd.setBoolean(7, false);
			cmd.setBoolean(8, model.f_fdChild);
			cmd.setInt(9, model.uid);
			cmd.setString(10, model.nameLoc);
			cmd.setString(11, model.nameSvr);
			cmd.setString(12, model.pathLoc);
			cmd.setString(13, model.pathSvr);
			cmd.setString(14, model.pathRel);
			cmd.setString(15, model.md5);
			cmd.setString(16, Long.toString(model.lenLoc) );
		} catch (SQLException e) {e.printStackTrace();}

		int fid = (int)db.ExecuteGenKey(cmd);

		return fid;
	}

	/// <summary>
	/// 添加一个文件夹上传任务
	/// </summary>
	/// <param name="inf"></param>
	/// <returns></returns>
	static public void Add(FolderInf inf)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("insert into hup_files(");
		sb.append("f_idSvr");
		sb.append(",f_nameLoc");
		sb.append(",f_fdTask");
		sb.append(",f_fdID");
		sb.append(") values(");
		
		sb.append("SEQ_f_idSvr.NEXTVAL");
		sb.append(",?");
		sb.append(",1");
		sb.append(",?");
		sb.append(")");

		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sb.toString());
		try 
		{
			cmd.setString(1, inf.name);
			cmd.setInt(2, inf.idSvr);
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		db.ExecuteNonQuery(cmd);
	}

	/// <summary>
	/// 添加一条文件信息，一船提供给ajax_fd_create.aspx使用。
	/// </summary>
	/// <param name="inf"></param>
	/// <returns></returns>
	static public int Add(FileInf inf)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("insert into hup_files(");
		sb.append(" f_idSvr");
		sb.append(",f_pid");
		sb.append(",f_pidRoot");
		sb.append(",f_fdChild");
		sb.append(",f_uid");
		sb.append(",f_nameLoc");
		sb.append(",f_nameSvr");
		sb.append(",f_pathLoc");
		sb.append(",f_pathSvr");
		sb.append(",f_length");
		sb.append(",f_size");
		
		sb.append(") values(");
		
		sb.append("SEQ_f_idSvr.NEXTVAL");
		sb.append(",?");//sb.append("@f_pid");
		sb.append(",?");//sb.append(",@f_pidRoot");
		sb.append(",?");//sb.append(",@f_fdChild");
		sb.append(",?");//sb.append(",@uid");
		sb.append(",?");//sb.append(",@f_nameLoc");
		sb.append(",?");//sb.append(",@FileNameRemote");
		sb.append(",?");//sb.append(",@f_pathLoc");
		sb.append(",?");//sb.append(",@FilePathRemote");
		sb.append(",?");//sb.append(",@FileLength");
		sb.append(",?");//sb.append(",@FileSize");
		sb.append(")");

		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sb.toString(),"f_idSvr");
		try {
			cmd.setInt(1, inf.pidSvr);
			cmd.setInt(2, inf.pidRoot);
			cmd.setBoolean(3, true);
			cmd.setInt(4, inf.uid);
			cmd.setString(5, inf.name);
			cmd.setString(6, inf.name);
			cmd.setString(7, inf.pathLoc);
			cmd.setString(8, inf.pathSvr);
			cmd.setString(9, inf.length);
			cmd.setString(10, inf.size);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		int fid = (int)db.ExecuteGenKey(cmd);		
		
		return fid;
	}

	/// <summary>
	/// 更新文件夹中子文件信息，
	/// filePathRemote
	/// md5
	/// fid
	/// </summary>
	/// <param name="inf"></param>
	public void UpdateChild(FileInf inf)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("update hup_files set ");
		sb.append(" f_pathSvr = ?, ");
		sb.append(" f_md5 = ? ");
		sb.append(" where f_idSvr=? ");

		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sb.toString());
		try {
			cmd.setString(1, inf.pathSvr);
			cmd.setString(1, inf.md5);
			cmd.setInt(3, inf.idSvr);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.ExecuteNonQuery(cmd);
	}

    /// <summary>
    /// 根据文件idSvr信息，更新文件数据表中对应项的MD5。
    /// </summary>
    /// <param name="inf"></param>
    public void UpdateMD5_path(xdb_files inf)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("update hup_files set ");
		sb.append(" f_md5 = ? ");
		sb.append(" f_pathSvr = ? ");
		sb.append(" f_nameSvr = ? ");
		sb.append(" where f_idSvr=? ");

		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sb.toString());
		try {
			cmd.setString(1, inf.md5);
			cmd.setString(2, inf.pathSvr);
			cmd.setString(3, inf.nameSvr);
			cmd.setInt(4, inf.idSvr);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		db.ExecuteNonQuery(cmd);
	}

	/**
	 * 清空文件表，文件夹表数据。
	 */
	static public void Clear()
	{
		DbHelper db = new DbHelper();
		db.ExecuteNonQuery("truncate table hup_files");
		db.ExecuteNonQuery("truncate table hup_folders");
	}

	/**
	 * @param uid
	 * @param fid
	 */
	static public void Complete(int uid, int fid)
	{
		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand("update hup_files set f_PostComplete=1 where f_uid=? and f_fdID=?");
		try {
			cmd.setInt(1, uid);
			cmd.setInt(2, fid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		db.ExecuteNonQuery(cmd);
	}

	/// <summary>
	/// 更新上传进度
	/// </summary>
	///<param name="uid">用户ID</param>
	///<param name="fid">文件ID</param>
	///<param name="filePos">文件位置，大小可能超过2G，所以需要使用long保存</param>
	///<param name="postedLength">已上传长度，文件大小可能超过2G，所以需要使用long保存</param>
	///<param name="postedPercent">已上传百分比</param>
	public boolean UpdateProgress(int uid,int fid,long filePos,long postedLength,String postedPercent)
	{
		//String sql = "update hup_files set f_pos=?,f_PostedLength=?,f_PostedPercent=? where f_uid=? and f_idSvr=?";
		String sql = "call spUpdateProcess6(?,?,?,?,?)";//使用存储过程
		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommandStored(sql);
		
		try 
		{
			cmd.setString(1, Long.toString(filePos) );
			cmd.setString(2, Long.toString(postedLength) );
			cmd.setString(3, postedPercent);
			cmd.setInt(4, uid);
			cmd.setInt(5, fid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		db.ExecuteNonQuery(cmd);
		return true;
	}

	/// <summary>
	/// 上传完成。将所有相同MD5文件进度都设为100%
	/// </summary>
	public void UploadComplete(String md5)
	{
		String sql = "update hup_files set f_PostedLength=f_length,f_PostedPercent='100%',f_PostComplete=1 where f_md5='"+md5+"'";
		DbHelper db = new DbHelper();
		//PreparedStatement cmd = db.GetCommand(sql);
		
		//cmd.setString(1, md5);
		//db.ExecuteNonQuery(cmd);//在部分环境中测试发现执行后没有效果。f_PostComplete仍然为0
		db.ExecuteNonQuery(sql);
	}

	/// <summary>
	/// 检查相同MD5文件是否有已经上传完的文件
	/// </summary>
	/// <param name="md5"></param>
	public boolean HasCompleteFile(String md5)
	{
		//为空
		if (md5 == null) return false;
		if(md5.isEmpty()) return false;

		String sql = "select f_idSvr from hup_files where f_PostComplete=1 and f_md5=?";
		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sql);

		try {
			cmd.setString(1, md5);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean ret = db.Execute(cmd);

		return ret;
	}

	/// <summary>
	/// 删除一条数据，并不真正删除，只更新删除标识。
	/// </summary>
	/// <param name="uid"></param>
	/// <param name="fid"></param>
	public void Delete(int uid,int fid)
	{
		String sql = "update hup_files set f_IsDeleted=1 where f_uid=? and f_idSvr=?";
		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sql);

		try {
			cmd.setInt(1, uid);
			cmd.setInt(2, fid);
			db.ExecuteNonQuery(cmd);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/// <summary>
	/// 根据根文件夹ID获取未上传完成的文件列表，并转换成JSON格式。
	/// 说明：
	///		1.此函数会自动对文件路径进行转码
	/// </summary>
	/// <param name="fidRoot"></param>
	/// <returns></returns>
	static public String GetUnCompletes(int fidRoot) throws UnsupportedEncodingException
	{
		StringBuilder sql = new StringBuilder("select ");
		sql.append("f_nameLoc");
		sql.append(",f_pathLoc");
		sql.append(",f_length");
		sql.append(",f_size");
		sql.append(",f_md5");
		sql.append(",f_pidRoot");
		sql.append(",f_pid");
		sql.append(" from hup_files where f_pidRoot=?");
		ArrayList<FileInf> arrFiles = new ArrayList<FileInf>();

		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sql.toString());
		try 
		{
			cmd.setInt(1, fidRoot);
			ResultSet r = db.ExecuteDataSet(cmd);
			while (r.next())
			{
				FileInf fi = new FileInf();
				fi.name = r.getString(0);
				fi.pathLoc = r.getString(1);
				fi.pathLoc = URLEncoder.encode(fi.pathLoc,"utf-8");
				fi.pathLoc = fi.pathLoc.replace("+", "%20");
				fi.length = r.getString(2);
				fi.size = r.getString(3);
				fi.md5 = db.GetStringSafe(r.getString(4),"");			
				fi.pidRoot = r.getInt(5);
				fi.pidSvr = r.getInt(6);
				arrFiles.add( fi );
			}
			r.close();
		}
		catch (SQLException e){e.printStackTrace();}
		
	    JSONArray json = JSONArray.fromObject( arrFiles );
		return json.toString();
	}
	
	public static void UpdateProcess(String uid,String fid,String per,String lenSvr)
	{		
		String sql = "update xdb_files set PostedPercent='"+per+"',PostedLength="+lenSvr+" where uid="+uid+" and fid="+fid;
		DbHelper db = new DbHelper();
		db.ExecuteNonQuery(sql);
	}

    /// <summary>
    /// 获取未上传完的文件列表
    /// </summary>
    /// <param name="fidRoot"></param>
    /// <param name="files"></param>
	static public void GetUnCompletes(int fidRoot,ArrayList files)
	{
		StringBuilder sql = new StringBuilder("select ");
        sql.append("f_idSvr");
        sql.append(",f_nameLoc");
		sql.append(",f_pathLoc");
		sql.append(",f_length");
		sql.append(",f_size");
		sql.append(",f_md5");
		sql.append(",f_pidRoot");
        sql.append(",f_pid");
        sql.append(",f_PostedLength");
        sql.append(",f_pathSvr");//fix(2015-03-18):续传文件时服务器会创建重复文件项信息
		sql.append(" from hup_files where f_pidRoot=? and f_PostComplete=0");

		DbHelper db = new DbHelper();
		PreparedStatement cmd = db.GetCommand(sql.toString());
		try 
		{
			cmd.setInt(1, fidRoot);
			ResultSet r = db.ExecuteDataSet(cmd);
			while (r.next())
			{
				FileInf fi = new FileInf();
	            fi.idSvr = r.getInt(1);
				fi.name = r.getString(2);
				fi.pathLoc = r.getString(3);
				fi.length = r.getString(4);
				fi.size = r.getString(5);
				fi.md5 = db.GetStringSafe(r.getString(6),"");
				fi.pidRoot = r.getInt(7);
				fi.pidSvr = r.getInt(8);
	            fi.postLength = r.getString(9);
	            fi.pathSvr = r.getString(10);//fix(2015-03-18):修复续传文件时服务器会创建重复文件信息的问题。
				files.add(fi);
			}
			r.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}