package up7.biz.folder;

import java.util.List;

import up7.model.xdb_files;

public class fd_root extends fd_child
{
    public List<fd_child_redis> folders;
    public List<xdb_files> files;
    
    public fd_root()
    {
    	this.f_fdChild = false;
    	this.f_folder = true;
    }
}
