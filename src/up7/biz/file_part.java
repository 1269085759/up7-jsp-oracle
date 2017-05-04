package up7.biz;

import java.io.File;
import java.io.IOException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import up7.PathTool;

/*
 * 文件操作
 * 
 * */
public class file_part {
	String idSign;//
	
	/**
	 * 子文件块（文件夹上传逻辑）
	 * @param path
	 * @param Data
	 * @throws IOException 
	 */
	public void save(String path,FileItem Data) throws IOException
	{
		if( StringUtils.isBlank(path))
		{
			System.out.println("未找到子文件块路径，保存子文件块数据失败");
			return;
		}
		
		//创建文件夹：目录/guid/1
		File part_path = new File(path);		
		PathTool.createDirectory( part_path.getParent());//		

		try {
			Data.write(new File(path));
		} catch (Exception e) {
			System.out.println("保存文件块错误,路径：".concat(path));
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}