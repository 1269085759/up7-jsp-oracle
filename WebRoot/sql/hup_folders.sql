--drop table hup_folders
CREATE TABLE hup_folders
(
	   fd_id				number NOT NULL   		 --文件夹ID
	  ,fd_name  			varchar(50) DEFAULT ''   --文件夹名称
	  ,fd_pid  				number DEFAULT '0'   	 --父级ID 
	  ,fd_uid  				number DEFAULT '0'   	 --用户ID
	  ,fd_length			varchar(19) DEFAULT '' 	 --数字化的大小。以字节为单位，示例：1024551
	  ,fd_size  			varchar(10) DEFAULT '' 	 --格式化的大小。示例：10G
	  ,fd_pathLoc			varchar(512) DEFAULT ''  --文件夹在客户端的路径。
	  ,fd_pathSvr			varchar(512) DEFAULT ''  --文件夹在服务端的路径。
	  ,fd_folders			number DEFAULT '0'  	 --子文件夹数
	  ,fd_files  			number DEFAULT '0'  	 --子文件数
	  ,fd_filesComplete  	number DEFAULT '0'  	 --已上传完的文件数量
	  ,fd_complete  		number(1) DEFAULT '0'  	 --是否已上传完毕
	  ,fd_delete  			number(1) DEFAULT '0'  	 --是否已删除
	  ,fd_json  			varchar(4000) DEFAULT '' --文件夹的JSON数据。保留字段
	  ,timeUpload			DATE DEFAULT sysdate  	 --上传时间
	  ,fd_pidRoot			number default '0'		 --根级ID
	  ,fd_pathRel			varchar(255) default ''  --相对路径。基于顶级节点。root\\child\\self
);

--创建主键
ALTER TABLE hup_folders ADD CONSTRAINT PK_hup_folders PRIMARY KEY(fd_id);

--创建自动编号列
--DROP SEQUENCE SEQ_fd_id
CREATE SEQUENCE SEQ_fd_id 
       MINVALUE 1
       START WITH 1
       NOMAXVALUE
       INCREMENT BY 1
       NOCYCLE
       CACHE 30
;