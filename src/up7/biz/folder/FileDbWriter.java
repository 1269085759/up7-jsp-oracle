package up7.biz.folder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.Jedis;
import up7.biz.BlockMeger;
import up7.biz.redis.FileRedis;
import up7.model.xdb_files;

/**
 * 将文件夹中的子文件添加到数据库
 * @author Administrator
 *
 */
public class FileDbWriter 
{
	fd_root root;
	Connection m_db;
	Jedis m_cache;
	//是否合并文件
	Boolean autoMerge = true;
	
	public FileDbWriter(Connection con,Jedis j,fd_root fd,Boolean merge)
	{
		this.m_cache = j;
		this.m_db = con;
		this.root = fd;
		this.autoMerge = merge;
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
        sb.append(",f_pathRel");
        sb.append(",f_lenLoc");
        sb.append(",f_sizeLoc");
        sb.append(",f_lenSvr");
        sb.append(",f_perSvr");
        sb.append(",f_sign");
        sb.append(",f_complete");
        sb.append(",f_fdTask");
        sb.append(",f_blockCount");
        sb.append(",f_blockSize");
        sb.append(",f_blockPath");
        
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
        sb.append(",?");//f_pathRel
        sb.append(",?");//f_lenLoc
        sb.append(",?");//f_sizeLoc
        sb.append(",?");//f_lenSvr
        sb.append(",?");//f_perSvr
        sb.append(",?");//f_sign
        sb.append(",1");//f_complete
        sb.append(",?");//f_fdTask
        sb.append(",?");//f_blockCount
        sb.append(",?");//f_blockSize
        sb.append(",?");//f_blockPath
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
        cmd.setString(10, "");//pathRel
        cmd.setLong(11, 0);//lenLoc
        cmd.setString(12, "");//sizeLoc
        cmd.setLong(13, 0);//lenSvr
        cmd.setString(14, "");//perSvr
        cmd.setString(15, "");//sign
        cmd.setBoolean(16, false);//fdTask
        cmd.setInt(17, 1);
        cmd.setInt(18, 0);
        cmd.setString(19, "");//blockPath
        return cmd;
	}
	
	void save(PreparedStatement cmd,xdb_files f)
	{
        try 
        {
			cmd.setString(1, f.idSign);//idSign
	        cmd.setString(2, f.pidSign);//pidSign
	        cmd.setString(3, f.rootSign);//rootSign
	        cmd.setBoolean(4, f.child);//fdChild
	        cmd.setInt(5, f.uid);//uid
	        cmd.setString(6, f.nameLoc);//nameLoc
	        cmd.setString(7, f.nameSvr);//nameSvr
	        cmd.setString(8, f.pathLoc);//pathLoc
	        cmd.setString(9, f.pathSvr);//pathSvr
	        cmd.setString(10, f.pathRel);//pathRel
	        cmd.setLong(11, f.lenLoc);//lenLoc
	        cmd.setString(12, f.sizeLoc);//sizeLoc
	        cmd.setLong(13, f.lenLoc);//lenSvr
	        cmd.setString(14, "100%");//perSvr
	        cmd.setString(15, f.sign);//sign
	        cmd.setBoolean(16, f.f_folder);//fdTask
	        cmd.setInt(17, f.blockCount);//blockCount
	        cmd.setInt(18, f.blockSize);//blockSize
	        cmd.setString(19, f.blockPath);//blockPath
	        cmd.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveFiles() throws SQLException
	{		
		PreparedStatement cmd = this.makeCmd(m_db);
		//保存文件夹
		this.save(cmd, this.root);
		
		String key = this.root.idSign.concat("-files");
		int index = 0;
		long len = this.m_cache.llen(key);
		FileRedis svr = new FileRedis(this.m_cache);
		BlockMeger bm = new BlockMeger();
		List<String> keys = null;
		List<xdb_files> files = null;
		
		while(index<len)
		{
			//每次取100条数据插入数据库
			keys = this.m_cache.lrange(key, index, index+100);
			
			index += keys.size();
			files = new ArrayList<xdb_files>();
			
			//添加到数据库
			for(String k : keys)
			{
				xdb_files f = svr.read(k);
				f.child = true;//
				this.save(cmd, f);
				files.add(f);
			}

			//合并文件
			if(this.autoMerge)
			{
				for(xdb_files f : files)
				{
					bm.merge(f);
				}
			}
			files.clear();
			keys.clear();
		}
		cmd.close();
	}

}
