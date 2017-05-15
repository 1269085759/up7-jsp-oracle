package up7.biz;

import java.io.File;
import java.io.IOException;

import up7.PathTool;
import up7.model.xdb_files;

/**
 * 文件块路径构建器
 * @author zy-qwl
 *
 */
public class BlockPathBuilder 
{
	/**
	 * 生成文件块路径
         格式：
           文件夹：
             d:/webapps/folder-1/file-1-guid/1.part
           文件：
             d:/webapps/year/年/月/日/file-1-guid/1.part
	 * 
	 * @param idSign
	 * @param blockIndex
	 * @param pathSvr
	 * @return
	 */
	public String part(String idSign,String blockIndex,String pathSvr)
	{
        File f = new File(pathSvr);        
        
        //d:\\soft
        try {
			pathSvr = PathTool.combine(f.getParent(),idSign);
			pathSvr = PathTool.combine(pathSvr, blockIndex + ".part");
			pathSvr = pathSvr.replace("\\", "/");
		} 
        catch (IOException e) 
        {
        	System.out.println("拼接文件块路径错误");
			e.printStackTrace();
		}
        return pathSvr;        		
	}
	
	/**
        文件块根路径
          d:/webapps/files/年/月/日/file-guid/
	 * 
	 * @param idSign
	 * @param pathSvr
	 * @return
	 */
	public String root(String idSign,String pathSvr)
	{
		File f = new File(pathSvr);
		try {
			pathSvr = PathTool.combine(f.getParent(), idSign);
		} catch (IOException e) {
        	System.out.println("拼接根路径错误");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pathSvr;
	}
	
	/**
	 * 
        文件夹子文件块路径
          d:/webapps/files/年/月/日/folder/file-guid/1.part
	 * @param idSign
	 * @param blockIndex
	 * @param fd
	 * @return
	 */
	public String partFd(String idSign,String blockIndex,xdb_files fd)
	{
        String pathSvr = fd.pathSvr;
        try 
        {
			pathSvr = PathTool.combine(pathSvr, idSign);
			pathSvr = PathTool.combine(pathSvr, blockIndex + ".part");
		} 
        catch (IOException e) 
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return pathSvr;		
	}
	
	/**
	 * 
        文件夹子文件块根目录
          d:/webapps/files/年/月/日/folder/file-guid/
	 * @param idSign
	 * @param blockIndex
	 * @param fd
	 * @return
	 */
	public String rootFd(xdb_files f,String blockIndex,xdb_files fd)
	{
        String pathSvr = fd.pathSvr;
        try {
        	int pos = f.pathRel.lastIndexOf("\\");
        	if(pos == -1)
        	{
        		pathSvr = PathTool.combine(pathSvr, f.idSign);
        	}
        	else
        	{
        		String rel = f.pathRel.substring(0,pos);
        		pathSvr = PathTool.combine(pathSvr, rel);
        		pathSvr = PathTool.combine(pathSvr, f.idSign);
        	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return pathSvr;
	}
}
