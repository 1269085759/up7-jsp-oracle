package up7.biz.folder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import redis.clients.jedis.Jedis;
import up7.DbHelper;
import up7.FileBlockWriter;
import up7.JedisTool;
import up7.PathTool;
import up7.biz.PathGuidBuilder;

import com.google.gson.Gson;

/**
 * @author redis，文件夹操作类
 * 将文件夹数据保存到redis中，提高文件夹初始化速度。
 * 用来初始化文件夹
 *
 */
public class fd_redis 
{
	//文件夹json数据
	public String data;
	fd_root m_root = null;
	Jedis con = null;
	Map<String/*guid*/,String/*pathSvr*/> parentPathMap = new HashMap<String,String>();
	
	public fd_redis(){}
	public fd_redis(Jedis j){this.con = j;}
	
	Jedis getCon()
	{
		if(this.con==null) this.con = JedisTool.con();
		return this.con;
	}
	
	void parse() throws IOException
	{
		Gson g = new Gson();
		this.m_root = g.fromJson(this.data,fd_root.class);
	}
	
	void init() throws IOException
	{
		//更新文件夹路径（Server）
		for(fd_child fd : this.m_root.folders)
		{
			//取父级文件夹路径
			if( parentPathMap.containsKey(fd.rootSign) )
			{
				String path = parentPathMap.get(fd.rootSign);//
				fd.pathSvr = PathTool.combine(path, fd.nameLoc);
			}
			
			//添加当前路径
			parentPathMap.put(fd.idSign, fd.pathSvr);
		}
		
		//更新文件路径（Server）
		for(fd_file f : this.m_root.files)
		{
			if(parentPathMap.containsKey(f.pidSign))
			{
				String path = parentPathMap.get(f.pidSign);//
				f.pathSvr = PathTool.combine(path, f.nameLoc);
				//创建空文件（原始大小）
				FileBlockWriter fr = new FileBlockWriter();
				fr.make(f.pathSvr, f.lenLoc);
			}
		}
	}
	
	//创建目录
	void makePath() throws IOException
	{		
		PathGuidBuilder pb = new PathGuidBuilder();
        this.m_root.pathRel = this.m_root.nameLoc;//
        
        this.m_root.pathSvr = pb.genFolder(this.m_root.uid, this.m_root.nameLoc);
        PathTool.createDirectory(this.m_root.pathSvr);
        parentPathMap.put(this.m_root.idSign, this.m_root.pathSvr);		
	}
	
	//保存到redis中
	public void save() throws IOException
	{
		this.parse();//解析
		this.makePath();//创建根级目录
		
		this.init();//初始化
		
		Jedis j = JedisTool.con();
		this.saveRoot(j);
		this.saveFiles(j);
		this.saveFolders(j);
		
		//保存到队列
	}
	
	//从redis中读取数据
	public void read(String idSign)
	{
		Jedis j = JedisTool.con();
		//folder不存在
		if(!j.exists(idSign)) 
		{
			j.close();
			System.out.println("redis-文件夹不存在");
			return;			
		}
		
		this.m_root = new fd_root();
		this.m_root.idSign = idSign;
		this.m_root.nameLoc = j.hget(idSign, "nameLoc");
		this.m_root.nameSvr = this.m_root.nameLoc;
		this.m_root.lenLoc = Long.parseLong(j.hget(idSign, "lenLoc") );
		this.m_root.lenSvr = Long.parseLong(j.hget(idSign, "lenSvr") );
		this.m_root.sizeLoc = j.hget(idSign, "sizeLoc");
		this.m_root.pathLoc = j.hget(idSign, "pathLoc");
		this.m_root.pathSvr = j.hget(idSign, "pathSvr");
		this.m_root.filesCount = Integer.parseInt(j.hget(idSign, "filesCount") );
		//
		this.loadFiles(j);//加载文件列表		
		this.loadFolders(j);//加载目录列表
		j.close();
	}
	
	public void mergeAll()
	{
		for(fd_file_redis f : this.m_root.files)
		{
			try {
				f.merge();
			} catch (IOException e) {
				System.out.println("合并文件块错误，文件路径：".concat(f.pathSvr));
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void del(String idSign)
	{
		Jedis j = JedisTool.con();
		//清除文件列表
		fd_files_redis fs = new fd_files_redis(j,idSign);		
		fs.del();
		
		//清除目录列表
		fd_folders_redis ds = new fd_folders_redis(j,idSign);
		ds.del();
		
		//清除文件夹
		j.del(idSign);
		j.close();
	}
	
	//保存到数据库
	public void saveToDb()
	{
		DbHelper db = new DbHelper();
		Connection con = db.GetCon();
		FolderDbWriter fd = new FolderDbWriter(con,this.m_root);
		try {
			fd.save();
		} catch (SQLException e) {
			System.out.println("保存子目录列表失败，数据库错误");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FileDbWriter fw = new FileDbWriter(con,this.m_root);
		try {
			fw.save();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("保存子文件列表失败，数据库错误");
			e.printStackTrace();
		}
		
	}
	
	void loadFiles(Jedis j)
	{
		//取文件ID列表
		fd_files_redis rfs = new fd_files_redis(j,this.m_root.idSign);		
		Set<String> fs = rfs.all();
		this.m_root.files = new ArrayList<fd_file_redis>();
		System.out.println("fd_redis.loadFiles() 文件数：".concat(Integer.toString(fs.size())));
		for(String s : fs)
		{
			fd_file_redis file = new fd_file_redis();
			file.read(j, s);
			this.m_root.files.add(file);
		}
	}
	
	void loadFolders(Jedis j)
	{		
		//取文件ID列表
		fd_folders_redis rfs = new fd_folders_redis(j,this.m_root.idSign);		
		this.m_root.folders = new ArrayList<fd_child_redis>();		
		List<String> fs = rfs.all();
		System.out.println("fd_redis.loadFolders() 文件夹数：".concat(Integer.toString(fs.size())));
		for(String s : fs)
		{
			fd_child_redis fd = new fd_child_redis();
			fd.read(j,s);
			this.m_root.folders.add(fd);
		}
	}
	
	void saveRoot(Jedis j)
	{
		j.del(this.m_root.idSign);		
		
		j.hset(this.m_root.idSign,"lenLoc", Long.toString(this.m_root.lenLoc) );//数字化的长度
		j.hset(this.m_root.idSign,"lenSvr", "0" );//格式化的
		j.hset(this.m_root.idSign,"sizeLoc", this.m_root.sizeLoc );//格式化的
		j.hset(this.m_root.idSign,"pathLoc", this.m_root.pathLoc);//
		j.hset(this.m_root.idSign,"pathSvr", this.m_root.pathSvr);//
		j.hset(this.m_root.idSign,"filesCount",Integer.toString( this.m_root.filesCount) );
		
		fd_files_redis rfs = new fd_files_redis(j,this.m_root.idSign);
		rfs.add(this.m_root.files);

		//保存目录
		fd_folders_redis rds = new fd_folders_redis(j,this.m_root.idSign);
		rds.add(this.m_root.folders);			
	}
	
	void saveFiles(Jedis j)
	{
		for(fd_file_redis f : this.m_root.files)
		{
			f.write(j);
		}
	}
	
	void saveFolders(Jedis j)
	{
		for(fd_child_redis fd : this.m_root.folders)
		{
			fd.write(j);
		}
	}
	
	public String toJson()
	{
		Gson g = new Gson();
		String json = g.toJson(this.m_root);
		try 
		{
			json = URLEncoder.encode(json,"utf-8");
		} 
		catch (UnsupportedEncodingException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		json = json.replace("+","%20");
		return json;
	}
}