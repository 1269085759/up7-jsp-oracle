drop table hup_files;
CREATE TABLE hup_files
(
	 f_idSvr			number NOT NULL		--文件ID，唯一。由于f_fid与oracle数据库字段有冲突，现改为f_idSign
	,f_pid				number DEFAULT '0'	--
	,f_pidRoot			number DEFAULT '0'	--根级文件夹ID
	,f_fdTask			number(1) DEFAULT '0'--表示是否是一个文件夹上传任务。提供给 ajax_f_list.jsp使用
	,f_fdID				number DEFAULT '0'	--文件夹详细ID，与hub_folders.fd_id对应
	,f_fdChild			number DEFAULT '0'	--是文件夹中的子项
	,f_uid  			number DEFAULT '0'   --用户ID
	,f_nameLoc 			varchar(255)  DEFAULT ''--  文件在本地电脑中的名称。例：QQ.exe 
	,f_nameSvr  		varchar(255)  DEFAULT '' --文件在服务器中的名称。一般为文件MD5+扩展名。
	,f_pathLoc 			varchar(512)  DEFAULT '' --文件在本地电脑中的完整路径。
	,f_pathSvr 		 	varchar(512)  DEFAULT '' --文件在服务器中的完整路径。
	,f_pathRel  		varchar(512) DEFAULT ''  --文件在服务器中的相对路径。
	,f_md5  			char(40)				 --文件md5,sha1,crc
	,f_length  			varchar(19) DEFAULT '0'  --文件总长度。以字节为单位
	,f_size  			varchar(10) DEFAULT ''  --格式化的文件尺寸。示例：10MB
	,f_pos  			varchar(19) DEFAULT '0'  --文件续传位置。
	,f_PostedLength		varchar(19) DEFAULT '0'  --已上传长度。以字节为单位。
	,f_PostedPercent  	varchar(6) DEFAULT '0%'  --已上传百分比。示例：10%
	,f_PostComplete  	number(1) DEFAULT '0'  --是否已上传完毕。
	,f_PostedTime  		DATE DEFAULT sysdate    --文件上传时间
	,f_IsDeleted  		number(1) DEFAULT '0'  --是否已删除。
);

--创建主键
ALTER TABLE hup_files ADD CONSTRAINT PK_hup_files PRIMARY KEY(f_idSvr);

--创建存储过程
create or replace procedure spUpdateProcess6(posSvr in varchar,lenSvr in varchar,perSvr in varchar,uidSvr in number,fidSvr in number)is
begin
  update hup_files
  set f_pos=posSvr,f_PostedLength=lenSvr,f_PostedPercent=perSvr
  where f_uid=uidSvr and f_idSvr=fidSvr;
end;


--创建自动编号列
DROP SEQUENCE SEQ_f_idSvr;
CREATE SEQUENCE SEQ_f_idSvr 
       MINVALUE 1
       START WITH 1
       NOMAXVALUE
       INCREMENT BY 1
       NOCYCLE
       CACHE 30
;