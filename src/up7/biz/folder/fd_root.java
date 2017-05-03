package up7.biz.folder;

import java.util.List;

public class fd_root extends fd_child{
    public List<fd_child_redis> folders;
    public List<fd_file_redis> files;
    public int idFile = 0;//文件夹id与up6_files.f_id对应。
}
