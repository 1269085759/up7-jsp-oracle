package down3.model;

import java.util.ArrayList;

public class DnFolderInf extends DnFileInf
{
	public DnFolderInf()
	{
		this.folder = true;
		this.files = new ArrayList<DnFileInf>();
	}
}
