package up7;

import java.io.InputStream;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.MongoGridFSException;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.model.Filters;

public class GridFSSvr {
	private String dbName="up7";//数据库名称
	private String server="127.0.0.1";//服务器地址
	private int port = 27017;//端口
	private String bucket="files";
	
	public GridFSBucket getCon()
	{
		MongoClient c = new MongoClient(this.server,this.port);
		MongoDatabase d = c.getDatabase(this.dbName);
		GridFSBucket bucket = GridFSBuckets.create(d, this.bucket);
		return bucket;
	}
	
	public String save(InputStream in,String fileID)
	{
		GridFSBucket b = this.getCon();		
		ObjectId id = b.uploadFromStream(fileID, in);
		return id.toHexString();
	}
	
	/**
	 * OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            downloadFile(objectId, os);
        } catch (IOException e) {
            log.info("download fail:" + e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    log.info("close outputstream fail:" + e);
                }
            }
        }

	 * @param fileID
	 * @param out
	 * @return
	 */
	public GridFSDownloadStream read(String fileID)
	{
		GridFSBucket bk  = this.getCon();
        GridFSDownloadStream stream = null;
		try
		{
			stream = bk.openDownloadStream(fileID);			
		}
		catch(MongoGridFSException e)
		{

			System.out.println("读取数据错误");
			System.out.println(e.toString());
		}
		finally
		{
			if (stream != null) stream.close();
			stream = null;
		}
		return stream;
	}
	
	public Boolean exist(String fileID)
	{
		GridFSBucket bk  = this.getCon();
		GridFSFindIterable iter = bk.find(Filters.eq("filename", fileID));
		if(iter != null)
		{
			System.out.println("迭代器为空");
			//com.mongodb.client.gridfs.model.GridFSFile out
			com.mongodb.client.gridfs.model.GridFSFile out = iter.first();
			if(out!=null) return true;
		}
		return false;		
	}
	
	public void all()
	{

		GridFSBucket bk  = this.getCon();
		GridFSFindIterable iter = bk.find();
		if(iter!= null)
		{
			com.mongodb.client.gridfs.model.GridFSFile f = iter.first();
			Document doc = f.getMetadata();
			//System.out.println(doc.toJson()
			System.out.println(f.getFilename());
		}
	}
}
