package up7.biz.folder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;

import up7.DbHelper;

//文件夹读取分页数据
public class fd_page 
{
	/**
	 * 
	 * @param pageIndex 页索引，基于1
	 */
	public String read(String pageIndex,String id) 
	{
		Integer pageSize = 100;//每页100条数据
		Integer index = Integer.parseInt(pageIndex);
		Integer pageStart = ((index-1) * pageSize) + 1;
		Integer pageEnd = index * pageSize;
		String sqlData = "select f_nameLoc,f_pathLoc,f_pathSvr,f_lenLoc,f_sizeLoc from up7_files where f_rootSign='"+id+"'";
        String sql = String.format("select * from (select a.*, rownum rn from (%s) a where rownum <= %d) where rn >= %d",sqlData,pageEnd,pageStart);

        List<fd_file> files = new ArrayList<fd_file>();
        DbHelper db = new DbHelper();
        PreparedStatement cmd = db.GetCommand(sql);        
        try
        {
			ResultSet r = db.ExecuteDataSet(cmd);
			while(r.next())
			{
	        	fd_file f = new fd_file();
	        	f.nameLoc = r.getString(1);//f_nameLoc
	        	f.pathLoc = r.getString(2);//f_pathLoc
	        	f.pathSvr = r.getString(3);
	        	f.lenLoc = r.getLong(4);
	        	f.sizeLoc = r.getString(5);
	        	files.add(f);
			}
		} catch (SQLException e) {
			System.out.println("fd_page.read 取文件数据错误");
			e.printStackTrace();
		}
		
		if(files.size() > 0)
		{
			Gson gson = new Gson();
			return gson.toJson(files);
		}
		return "";
	}
}
