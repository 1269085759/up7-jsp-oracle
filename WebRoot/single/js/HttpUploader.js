/*
	版权所有 2009-2012 武汉命运科技有限公司
	保留所有权利
	官方网站：http://www.ncmem.com/
	产品首页：http://www.ncmem.com/webplug/http-uploader4/index.aspx
	产品介绍：http://www.cnblogs.com/xproer/archive/2012/05/29/2523757.html
	开发文档-ASP：http://www.cnblogs.com/xproer/archive/2012/02/17/2355458.html
	开发文档-PHP：http://www.cnblogs.com/xproer/archive/2012/02/17/2355467.html
	开发文档-JSP：http://www.cnblogs.com/xproer/archive/2012/02/17/2355462.html
	开发文档-ASP.NET：http://www.cnblogs.com/xproer/archive/2012/02/17/2355469.html
	升级日志：http://www.cnblogs.com/xproer/archive/2012/02/17/2355449.html
	文档下载：http://yunpan.cn/lk/sVRrBEVQ7w5cw
	问题反馈：http://www.ncmem.com/bbs/showforum-19.aspx
	VC运行库：http://www.microsoft.com/download/en/details.aspx?displaylang=en&id=29
	联系信箱：1085617561@qq.com
	联系QQ：1085617561
*/

//删除元素值
Array.prototype.remove = function(val)
{
	for (var i = 0, n = 0; i < this.length; i++)
	{
		if (this[i] != val)
		{
			this[n++] = this[i]
		}
	}
	this.length -= 1
}

//全局对象
var HttpUploaderMgrInstance = null; //上传管理器实例
var FileListerMgrInstance = null;//文件列表对象
//删除文件
function FileItemRemove(fid)
{
	FileListerMgrInstance.RemoveFile(fid);
}

//续传文件
function FileItemResumer(fid)
{
	FileListerMgrInstance.ResumerFile(fid);
}

//加截Chrome插件
function LoadChromeCtl(oid,mime,path)
{
	var acx = '<embed id="objHttpPartitionFF" type="' + mime + '" pluginspage="' + path + '" width="1" height="1"/>';
	$("#" + oid).append(acx);
}
/*
	2009-11-5 文件管理类
	属性：
		UpFileList
*/
function HttpUploaderMgr()
{
	var _this = this;
	this.Config = {
		"EncodeType"		: "GB2312"
		, "CompanyLicensed"	: "武汉命运科技有限公司"
		, "FileFilter"		: "*"//文件类型。所有类型：*。自定义类型：jpg,bmp,png,gif,rar,zip,7z,doc
		, "FileSizeLimit"	: "0"//自定义允许上传的文件大小，以字节为单位。0表示不限制。
		, "FilesLimit"		: 0//文件选择数限制。0表示不限制
		, "AllowMultiSelect": 1//多选开关。1:开启多选。0:关闭多选
		, "RangeSize"		: 131072//文件块大小，以字节为单位。必须为64KB的倍数。推荐大小：128KB。
		, "AppPath"			: ""//网站虚拟目录名称。子文件夹 web
		, "CabPath"			: "http://www.ncmem.com/products/http-uploader5/HttpUploader.cab#version=2,7,53,64571"
		, "UrlCreate"		: "http://localhost:8080/HttpUploader5ChrUtf8/db/ajax_create_fid.jsp"
		, "UrlPost"			: "http://localhost:8080/HttpUploader5ChrUtf8/db/ajax_post.jsp"
		, "UrlProcess"		: "http://localhost:8080/HttpUploader5ChrUtf8/db/ajax_process.jsp"
		, "UrlComplete"		: "http://localhost:8080/HttpUploader5ChrUtf8/db/ajax_complete.jsp"
		, "UrlList"			: "http://localhost:8080/HttpUploader5ChrUtf8/db/ajax_list.jsp"
		, "UrlDel"			: "http://localhost:8080/HttpUploader5ChrUtf8/db/ajax_del.jsp"
		, "ClsidDroper"		: "8720771E-E094-40da-A8C6-F3169F1D1CF4"
		, "ClsidUploader"	: "AC982331-5018-4b65-86A7-CA5E312A0FB7"
		, "ClsidPartition"	: "9FCC0A71-B2CF-4012-AA5A-E16E78A24785"
		//64bit
		, "ClsidDroper64"	: "F6C0572E-318D-4b24-B750-1DB17C32E7B5"
		, "ClsidUploader64"	: "2897904C-2691-45d4-8DA3-6A84D54ADEDC"
		, "ClsidPartition64": "0DCDFC19-A725-4d58-B53F-AF3F128CA006"
		, "CabPath64"		: "http://www.ncmem.com/products/http-uploader5/HttpUploader64.cab#version=2,7,47,55490"
		//Firefox
		, "CabPathFirefox"	: "http://files.cnblogs.com/xproer/HttpUploader5.xpi"
		, "MimeType"		: "application/npHttpUploader5"
		//Chrome
		, "CabPathChrome"	: "http://www.ncmem.com/bbs/attachment.aspx?attachmentid=5"
		, "MimeTypeChr"		: "application/chrHttpUploader5"
		, "CrxName"			: "chrHttpUploader5"
	};

	this.ActiveX = {
		"Uploader"		: "Xproer.HttpUploader5"
		, "Partition"	: "Xproer.HttpPartition5"
		//64bit
		, "Uploader64"	: "Xproer.HttpUploader5x64"
		, "Partition64"	: "Xproer.HttpPartition5x64"
	};
	
	//附加参数
	this.Fields = {
		"UserName": "test"
		, "UserPass": "test"
		,"uid":0
		,"fid":0
	};
	
	//检查版本 Win32/Win64/Firefox/Chrome
	var browserName = navigator.userAgent.toLowerCase();
	this.CheckVersion = function()
	{
		//Win64
		if (window.navigator.platform == "Win64")
		{
			_this.Config["CabPath"] = _this.Config["CabPath64"];

			_this.Config["ClsidDroper"] = _this.Config["ClsidDroper64"];
			_this.Config["ClsidUploader"] = _this.Config["ClsidUploader64"];
			_this.Config["ClsidPartition"] = _this.Config["ClsidPartition64"];

			_this.ActiveX["Uploader"] = _this.ActiveX["Uploader64"];
			_this.ActiveX["Partition"] = _this.ActiveX["Partition64"];
		} //Firefox
		else if ($.browser.mozilla)
		{
			_this.Config["CabPath"] = _this.Config["CabPathFirefox"];
			_this.ActiveX["Uploader"] = _this.ActiveX["UploaderFF"];
		}
		else if (/chrome/i.test(browserName) && /webkit/i.test(browserName) && /mozilla/i.test(browserName))
		{
			_this.Config["CabPath"] = _this.Config["CabPathChrome"];
			_this.Config["MimeType"] = _this.Config["MimeTypeChr"];
		}
	};
	_this.CheckVersion();
	
	//http://www.ncmem.com/
	_this.Domain = "http://" + document.location.host;

	HttpUploaderMgrInstance = this;
	_this.FileFilter = new Array(); //文件过滤器
	_this.UploaderListCount = 0; 	//上传项总数
	_this.PluginCount = 0;			//插件计数，只累加
	_this.UploaderList = new Object(); //上传项列表
	_this.UploadQueue = new Array();		//上传队列
	_this.UnUploaderIdList = new Array(); //未上传项ID列表
	_this.UploadIdList = new Array(); //正在上传的ID列表
	_this.CompleteList = new Array(); //已上传完的HttpUploader列表
	
	//服务器文件列表面板
	this.FileListMgr =
	{
		UploaderMgr: _this //文件上传管理器
		, ActiveX: _this.ActiveX
		, Config: _this.Config
		, Fields: _this.Fields
		, FileLister: null //文件列表容器,JQuery
		, FileItemTemp: null//文件项模板,JQuery
		, FileItemCtls: new Array()//文件列表项集合,JQuery
		, FileItems: new Array() //文件项集合
		, "GetHtml": function()//加载控件
		{
			var html = '<div id="divFileLister" class="FileListerBox">';
			html += '<ul class="ulFileHead">';
			html += '<li class="hcb"><input type="checkbox" /></li>';
			html += '<li class="hname">文件名称</li>';
			html += '<li class="hsize">大小</li>';
			html += '<li class="hper">进度</li>';
			html += '<li class="hop">操作</li>';
			html += '</ul>';
			html += '<div id="FileItems" class="FileItems">';
			html += '</div>';
			html += '</div>';
			//temp
			html += '<div class="divHidden"><ul id="FileItemTemp">';
			html += '<li class="fcb"><input type="checkbox" /></li>';
			html += '<li class="fname">文件名称</li>';
			html += '<li class="fsize">大小</li>';
			html += '<li class="fper">进度</li>';
			html += '<li class="fop">操作</li>';
			html += '</ul></div>';
			return html;
		}
		, "Load": function()
		{
			document.write(this.GetHtml());
		}
		, "LoadTo": function(oid)//加载到指定控件
		{
			$("#" + oid).html(this.GetHtml());
		}
		, "Init": function()//初始化
		{
			this.FileLister = $("#FileItems");
			this.FileItemTemp = $("#FileItemTemp");
			this.LoadData();
		}
		, "LoadData": function()//从服务器加载数据
		{
			var ref = this;
			$.ajax({
				type: "GET"
				, url: this.Config["UrlList"]
				, data: { uid: this.Fields["uid"], time: Date() }
			, success:
			function(msg)
			{
				var json = eval(msg);
				$.each(json, function(idx, item)
				{
					ref.AddItem(item);
				});
			}
			, error: function() { alert("加载文件列表错误！"); }
			, complete: function(req, sta) { req = null; }
			});
		}
		, "AddItem": function(item)//添加一项元素
		{
			this.FileItems[item.fid] = item;
			var ctl = this.FileItemTemp.clone();
			ctl.children().eq(1).text(item.FileNameLocal);
			ctl.children().eq(1).attr("title", item.FileNameLocal);
			ctl.children().eq(2).text(item.FileSize);
			ctl.children().eq(3).text(item.PostedPercent);

			if ("True" == item.PostComplete)
			{
				ctl.children().eq(4).html('<span fid="' + item.fid + '" onclick="FileItemRemove(' + item.fid + ');">删除</span>');
			}
			else
			{
				ctl.children().eq(4).html('<span fid="' + item.fid + '" onclick="FileItemResumer(' + item.fid + ')">续传</span>|<span onclick="FileItemRemove(' + item.fid + ');">删除</span>');
			}
			//添加到文件列表项集合
			this.FileItemCtls[item.fid] = ctl;
			this.FileLister.append(ctl);
		}
		, "AddItemHttpUploader": function(obj)//添加一个新项
		{
			var item = {
				fid: obj.fid
				, uid: obj.Fields["uid"]
				, FileNameLocal: obj.FileName
				, FileNameRemote: ""
				, FilePathLocal: obj.PathLocal
				, FilePathRemote: ""
				, FilePathRelat: obj.PathRelat
				, FileMD5: obj.MD5
				, FileLength: obj.FileLength
				, FileSize: obj.FileSize
				, FilePos: ""
				, PostedLength: obj.FileLength
				, PostedPercent: "100%"
				, PostComplete: "True"
				, PostedTime: ""
				, IsDeleted: "False"
			};
			this.AddItem(item);
		}
		, "UploadComplete": function(fid, refHttpUploader)//上传完成，将操作改为删除。
		{
			//文件已存在
			if (typeof (this.FileItems[fid]) != "undefined")
			{
				this.FileItemCtls[fid].children().eq(3).text("100%");
				this.FileItemCtls[fid].children().eq(4).html('<span fid="' + fid + '" onclick="FileItemRemove(' + fid + ');">删除</span>');
			}
			else
			{
				this.AddItemHttpUploader(refHttpUploader);
			}
		}
		, "ResumerFile": function(fid)//续传文件
		{
			var item = this.FileItems[fid];
			var path = decodeURIComponent(item.FilePathLocal);
			_this.AddResumeFile(path, item.PostedLength, item.PostedPercent, item.FileMD5, item.fid);
			_this.OpenPnlUpload(); //打开上传面板
			this.UploaderMgr.PostFirst();
		}
		, "RemoveFile": function(fid)//删除文件
		{
			var ref = this;
			var item = this.FileItems[fid];
			var ctl = this.FileItemCtls[fid];

			$.ajax({
				type: "GET"
				, url: this.Config["UrlDel"]
				, data: { uid: item.uid, fid: fid, time: Date() }
			, success:
			function(msg)
			{
				//删除成功
				if (msg == 1)
				{
					//删除HTML控件
					//ref.FileLister.removeChild(ctl);
					ctl.empty();
				}
			}
			, error: function() { alert("发送删除文件信息失败！"); }
			, complete: function(req, sta) { req = null; }
			});
		}
	};
	FileListerMgrInstance = this.FileListMgr;
	//初始化路径
	this.InitPath = function()
	{
		this.Config["CabPath"] = this.Domain + this.Config["AppPath"] + this.Config["CabPath"];
		this.Config["PostUrl"] = this.Domain + this.Config["AppPath"] + this.Config["PostUrl"];
	};

	//容器的HTML代码
	this.GetHtmlContainer = function()
	{
		var html = '<div class="combinBox">';
		html += '<ul id="cbHeader" class="cbHeader">';
		html += '<li id="liPnlUploader" class="hover">上传新文件</li>';
		html += '<li id="liPnlFiles" >文件列表</li>';
		html += '</ul>';
		html += '<div class="cbBody" id="cbBody">';
		html += '<ul name="cbItem" class="block"><li id="liUploadPanel"></li></ul>';
		html += '<ul name="cbItem" class="cbItem"><li id="liListerPanel"></li></ul>';
		html += '</div>';
		html += '</div>';
		return html;
	};
	
	//文件上传面板。
	this.GetHtml = function()
	{
		//加载拖拽控件
		var acx = "";
		//acx += '<object id="objFileDroper" classid="clsid:' + this.Config["ClsidDroper"] + '"';
		//acx += ' codebase="' + this.Config["CabPath"] + '" width="192" height="192" >';
		//acx += '</object>';
		//自动安装CAB
		acx += '<div style="display:none">';
		//文件上传控件
		acx += '<object id="objHttpUpLoader" classid="clsid:' + _this.Config["ClsidUploader"] + '"';
		acx += ' codebase="' + _this.Config["CabPath"] + '" width="1" height="1" ></object>';
		//文件夹选择控件
		acx += '<object id="objHttpUploaderPartition" classid="clsid:' + _this.Config["ClsidPartition"] + '"';
		acx += ' codebase="' + _this.Config["CabPath"] + '" width="1" height="1" ></object>';
		acx += '</div>';
		//Firefox文件上传控件
		//acx += '<embed id="objHttpPartitionFF" type="' + _this.Config["MimeType"] + '" pluginspage="' + _this.Config["CabPathFirefox"] + '" width="1" height="1"/>';
		//
		acx += '<div id="UploaderTemplate" class="sitem">';
		acx += '<div name="fileName" class="fileName">HttpUploader5-doc.rar</div>';
		acx += '<div name="fileSize" class="fileSize">(1.41MB)</div>';
		acx += '<div class="processbk"><div name="process" class="process"> </div></div>';
		acx += '<div name="btn" class="btn">取消</div>';
		acx += '<div name="btnCancel" class="btn hide">取消</div>';
		acx += '<div name="msg"></div>';
		acx += '</div>';
//		//分隔线
//		acx += '<div class="Line" id="FilePostLine"></div>';
//		//上传列表
//		acx += '<div id="UploaderPanel">';
//		//acx += '<div class="header">上传文件</div>';
//		acx += '<div class="toolbar">';
//		acx += '<input id="btnAddFiles" type="button" value="选择多个文件" />';
//		acx += '<input id="btnAddFolder" type="button" value="选择文件夹" />';
//		acx += '<input id="btnPasteFile" type="button" value="粘贴文件" />';
//		acx += '</div>';
//		acx += '<div class="content">';
//		acx += '<div id="FilePostLister"></div>';
//		acx += '</div>';
//		acx += '<div class="footer">';
//		acx += '<a href="javascript:void(0)" class="Btn" id="lnkClearComplete">清除已完成文件</a>';
//		acx += '</div>';
//		acx += '</div>';
		return acx;
	};
	
	//打开上传面板
	this.OpenPnlUpload = function()
	{
		$("#liPnlUploader").click();
	};
	//打开文件列表面板
	this.OpenPnlFiles = function()
	{
		$("#liPnlFiles").click();
	};
	
	_this.firefox = false;
	_this.ie = false;
	_this.chrome = false;
	//FireFox浏览器信息管理对象
	this.BrowserFF = {
		"Check": function()//检查插件是否已安装
		{
			var mimetype = navigator.mimeTypes[_this.Config["MimeType"]];
			if (mimetype)
			{
				return mimetype.enabledPlugin;
			}
			return mimetype;
		}
		, "Setup": function()//安装插件
		{
			var xpi = new Object();
			xpi["Calendar"] = _this.Config["MimeType"];
			InstallTrigger.install(xpi, function(name, result) { });
		}
		, "OpenFileDialog": function()//打开文件选择窗口
		{
			var obj = document.getElementById("objHttpPartitionFF");
			obj.FileFilter = _this.Config["FileFilter"];
			obj.FilesLimit = _this.Config["FilesLimit"];
			obj.AllowMultiSelect = _this.Config["AllowMultiSelect"];

			var files = obj.ShowDialog();
			if (files)
			{
				for (var i = 0, l = files.length; i < l; ++i)
				{
					if (!_this.Exist(files[i]))
					{
						_this.AddFile(files[i]);
					}
				}
				_this.PostFirst();
			}
		}
		, "GetSingleFile": function()//获取单个文件
		{
			var obj = document.getElementById("objHttpPartitionFF");
			obj.FileFilter = _this.Config["FileFilter"];
			obj.FilesLimit = _this.Config["FilesLimit"];
			obj.AllowMultiSelect = _this.Config["AllowMultiSelect"];

			var files = obj.ShowDialog();
			if (files)
			{
				return files[0];
			}
			return null;
		}
		, "OpenFolderDialog": function()//打开文件夹选择窗口
		{
			var obj = document.getElementById("objHttpPartitionFF");
			var list = obj.ShowFolder();
			if (list)
			{
				for (var i = 0, l = files.length; i < l; ++i)
				{
					if (!_this.Exist(list[i]))
					{
						_this.AddFile(list[i]);
					}
				}
				_this.PostFirst();
			}
		}
		, "PasteFiles": function()//从剪帖板中获取文件
		{
			var obj = document.getElementById("objHttpPartitionFF");
			var list = obj.GetClipboardFiles();
			if (list)
			{
				for (var i = 0, l = files.length; i < l; ++i)
				{
					if (!_this.Exist(list[index]))
					{
						_this.AddFile(list[index]);
					}
				}
				_this.PostFirst();
			}
		}
		, "Init": function()//初始化控件
		{
			var atl = document.getElementById("objHttpPartitionFF");
			atl.FileSizeLimit = _this.Config["FileSizeLimit"];
			atl.RangeSize = _this.Config["RangeSize"]; ;
			atl.PostUrl = _this.Config["UrlPost"]; ;
			atl.EncodeType = _this.Config["EncodeType"]; ;
			atl.Licensed = _this.Config["CompanyLicensed"]; ;
			atl.OnPost = HttpUploader_Process;
			atl.OnStateChanged = HttpUploader_StateChanged;
		}
	};
	//IE浏览器信息管理对象
	this.BrowserIE = {
		"Check": function()//检查插件是否已安装
		{
			try
			{
				var com = new ActiveXObject(_this.ActiveX["Uploader"]);
				return true;
			}
			catch (e) { return false; }
		}
		, "OpenFileDialog": function()//打开文件选择窗口
		{
			var obj = new ActiveXObject(_this.ActiveX["Partition"]);
			obj.FileFilter = _this.Config["FileFilter"];
			obj.AllowMultiSelect = _this.Config["AllowMultiSelect"];
			if (!obj.ShowDialog()) return;

			var list = obj.GetSelectedFiles();
			if (list == null) return;
			if (list.lbound(1) == null) return;

			for (var index = list.lbound(1); index <= list.ubound(1); index++)
			{
				if (!_this.Exist(list.getItem(index)))
				{
					_this.AddFile(list.getItem(index));
				}
			}
			_this.PostFirst();
		}
		, "GetSingleFile": function()//获取单个文件
		{
			var obj = new ActiveXObject(_this.ActiveX["Partition"]);
			obj.FileFilter = _this.Config["FileFilter"];
			obj.AllowMultiSelect = _this.Config["AllowMultiSelect"];
			if (!obj.ShowDialog()) return null;

			var list = obj.GetSelectedFiles();
			if (list == null) return null;
			if (list.lbound(1) == null) return null;
			return list.getItem(list.lbound(1));
		}
		, "OpenFolderDialog": function()//打开文件夹选择窗口
		{
			var obj = new ActiveXObject(_this.ActiveX["Partition"]);
			if (!obj.ShowFolder()) return;

			var list = obj.GetSelectedFiles();
			if (list == null) return;
			if (list.lbound(1) == null) return;

			for (var index = list.lbound(1); index <= list.ubound(1); index++)
			{
				if (!_this.Exist(list.getItem(index)))
				{
					_this.AddFile(list.getItem(index));
				}
			}
			_this.PostFirst();
		}
		, "PasteFiles": function()//从剪帖板中获取文件
		{
			var obj = new ActiveXObject(_this.ActiveX["Partition"]);
			var list = obj.GetClipboardFiles();
			if (list == null) return;
			if (list.lbound(1) == null) return;

			for (var index = list.lbound(1); index <= list.ubound(1); index++)
			{
				if (!_this.Exist(list.getItem(index)))
				{
					_this.AddFile(list.getItem(index));
				}
			}
			_this.PostFirst();
		}
		, "Init": function() { }
	};
	//Chrome浏览器
	this.BrowserChrome = {
		"Check": function()//检查插件是否已安装
		{
			for (var i = 0, l = navigator.plugins.length; i < l; i++)
			{
				if (navigator.plugins[i].name == _this.Config["CrxName"])
				{
					return true;
				}
			}
			return false;
		}
		, "Setup": function()//安装插件
		{
			document.write('<iframe style="display:none;" src="' + _this.Config["CabPathChrome"] + '"></iframe>');
		}
		, "OpenFileDialog": function()//打开文件选择窗口
		{
			var obj = document.getElementById("objHttpPartitionFF");
			obj.FileFilter = _this.Config["FileFilter"];
			obj.FilesLimit = _this.Config["FilesLimit"];
			obj.AllowMultiSelect = _this.Config["AllowMultiSelect"];

			var files = obj.ShowDialog();
			if (files)
			{
				for (var i = 0, l = files.length; i < l; ++i)
				{
					if (!_this.Exist(files[i]))
					{
						_this.AddFile(files[i]);
					}
				}
				_this.PostFirst();
			}
		}
		, "GetSingleFile": function()//获取单个文件
		{
			var obj = document.getElementById("objHttpPartitionFF");
			obj.FileFilter = _this.Config["FileFilter"];
			obj.FilesLimit = _this.Config["FilesLimit"];
			obj.AllowMultiSelect = _this.Config["AllowMultiSelect"];

			var files = obj.ShowDialog();
			if (files)
			{
				return files[0];
			}
			return null;
		}
		, "OpenFolderDialog": function()//打开文件夹选择窗口
		{
			var obj = document.getElementById("objHttpPartitionFF");
			var list = obj.ShowFolder();
			if (list)
			{
				for (var i = 0, l = files.length; i < l; ++i)
				{
					if (!_this.Exist(list[i]))
					{
						_this.AddFile(list[i]);
					}
				}
				_this.PostFirst();
			}
		}
		, "PasteFiles": function()//从剪帖板中获取文件
		{
			var obj = document.getElementById("objHttpPartitionFF");
			var list = obj.GetClipboardFiles();
			if (list)
			{
				for (var i = 0, l = files.length; i < l; ++i)
				{
					if (!_this.Exist(list[index]))
					{
						_this.AddFile(list[index]);
					}
				}
				_this.PostFirst();
			}
		}
		, "Init": function()//初始化控件
		{
			var atl = document.getElementById("objHttpPartitionFF");
			atl.FileSizeLimit = _this.Config["FileSizeLimit"];
			atl.RangeSize = _this.Config["RangeSize"]; ;
			atl.PostUrl = _this.Config["UrlPost"]; ;
			atl.EncodeType = _this.Config["EncodeType"]; ;
			atl.Licensed = _this.Config["CompanyLicensed"]; ;
			atl.OnPost = HttpUploader_Process;
			atl.OnStateChanged = HttpUploader_StateChanged;
		}
	};
	
	//浏览器环境检查
	_this.Browser = _this.BrowserIE;
	if ($.browser.msie)
	{
		_this.ie = true;
	}//Firefox
	else if ($.browser.mozilla)
	{
		_this.firefox = true;
		_this.Browser = this.BrowserFF;
		if (!_this.Browser.Check()) { _this.Browser.Setup(); }
	}//Chrome
	else if (/chrome/i.test(browserName) && /webkit/i.test(browserName) && /mozilla/i.test(browserName))
	{
		_this.chrome = true;
		_this.Browser = this.BrowserChrome;
		if (!_this.Browser.Check()) { _this.Browser.Setup(); }
	}

	//安全检查，在用户关闭网页时自动停止所有上传任务。
	this.SafeCheck = function()
	{
		$(window).bind("beforeunload", function()
		{
			if (_this.UploadIdList.length > 0)
			{
				event.returnValue = "您还有程序正在运行，确定关闭？";
			}
		});

		$(window).bind("unload", function()
		{ 
			if (_this.UploadIdList.length > 0)
			{
				_this.StopAll();
			}
		});
	};

	this.Load = function()
	{
		document.write(_this.GetHtml());
		_this.SafeCheck();
	};

	//加截容器，上传面板，文件列表面板
	this.LoadTo = function(oid)
	{
		//$("#" + oid).html(_this.GetHtmlContainer());
		$("#"+oid).html(_this.GetHtml());
		//this.FileListMgr.LoadTo("liListerPanel");
		_this.SafeCheck();
		LoadChromeCtl(oid,_this.Config["MimeType"],_this.Config["CabPath"]);
	};
	
	this.Droper = null;
	this.Partition = null;
	
	//初始化容器
	this.InitContainer = function()
	{
		var cbItemLast = null;
		$("#cbHeader li").each(function(n)
		{
			if (this.className == "hover")
			{
				cbItemLast = this;
			}

			$(this).click(function()
			{
				$("ul[name='cbItem']").each(function(i)
				{
					this.style.display = i == n ? "block" : "none"; /*确定主区域显示哪一个对象*/
				});
				if (cbItemLast) cbItemLast.className = "";

				if (this.className == "hover")
				{
					this.className = "";
				}
				else
				{
					this.className = "hover";
				}
				cbItemLast = this;
			});
		});
	};
	//初始化，一般在window.onload中调用
	this.Init = function()
	{
		//_this.InitContainer();
		//_this.FileListMgr.Init();
		//_this.UploaderListDiv = document.getElementById("FilePostLister");
		_this.UploaderTemplateDiv = document.getElementById("UploaderTemplate");
//		_this.Droper = document.getElementById("objFileDroper");
		_this.FirefoxAtl = document.getElementById("objHttpPartitionFF");
		_this.Browser.Init(); //
//		
//		var ref = this;
//		if (null != _this.Droper) _this.Droper.OnFileDrop = function()
//		{
//			var list = mgr.Droper.GetSelectedFiles();
//			for (var index = list.lbound(1); index <= list.ubound(1); ++index)
//			{
//				if (!mgr.Exist(list.getItem(index)))
//				{
//					mgr.AddFile(list.getItem(index));
//				}
//			}
//			mgr.PostFirst();
//		};

//		//添加多个文件
//		$("#btnAddFiles").click(function() { ref.OpenFileDialog(); });
//		//添加文件夹
//		$("#btnAddFolder").click(function() { ref.OpenFolderDialog(); });
//		//粘贴文件
//		$("#btnPasteFile").click(function() { ref.PasteFiles(); });
//		//清空已完成文件
//		$("#lnkClearComplete").click(function() { ref.ClearComplete(); });
	};
	
	//清除已完成文件
	this.ClearComplete = function()
	{
		for(var i = 0 ,l=_this.CompleteList.length; i < l; ++i)
		{
			_this.Delete(_this.CompleteList[i].FileID);
		}
		_this.CompleteList.length = 0;
	};

	//上传队列是否已满
	this.IsPostQueueFull = function()
	{
		//目前只支持同时上传三个文件
		if (_this.UploadIdList.length > 2)
		{
			return true;
		}
		return false;
	};

	//添加一个上传ID
	this.AppendUploadId = function(fid)
	{
		_this.UploadIdList.push(fid);
	};
	
	//添加到上传队列
	this.AppendQueue = function(fid)
	{
		_this.UploadQueue.push(fid);
	};

	//从队列中删除
	this.RemoveQueue = function(fid)
	{ 
		if (_this.UploadQueue.length < 1) return;
		
		for (var i = 0, l = _this.UploadQueue.length; i < l; ++i)
		{
			if (_this.UploadQueue[i] == fid)
			{
				_this.UploadQueue.remove(fid);
			}
		}
	};
	
	//添加到未上传ID列表，(停止，出错)
	this.AppendUnUploadIds = function(fid)
	{
		_this.UnUploaderIdList.push(fid);
	};
	
	//从未上传ID列表删除，(上传完成)
	this.RemoveUnUploadIds = function(fid)
	{ 
		if (_this.UnUploaderIdList.length < 1) return;
		
		for (var i = 0, l = _this.UnUploaderIdList.length; i < l; ++i)
		{
			if (_this.UnUploaderIdList[i] == fid)
			{
				_this.UnUploaderIdList.remove(fid);
			}
		}
	};

	/*
	从当前上传ID列表中删除指定项。
	此函数将会重新构造一个Array
	*/
	this.RemoveUploadId = function(fid)
	{
		if (_this.UploadIdList.length < 1) return;
		
		for (var i = 0, l = _this.UploadIdList.length; i < l; ++i)
		{
			if (_this.UploadIdList[i] == fid)
			{
				_this.UploadIdList.remove(fid);
			}
		}
	};

	//停止所有上传项
	this.StopAll = function()
	{
		for (var i = 0, l = _this.UploadIdList.length; i < l; ++i)
		{
			_this.UploaderList[_this.UploadIdList[i]].StopManual();
		}
		_this.UploadIdList.length = 0;
	};

	/*
	添加到上传列表
	参数
	fid 上传项ID
	uploaderItem 新的上传对象
	*/
	this.AppenToUploaderList = function(fid, uploaderItem)
	{
		_this.UploaderList[fid] = uploaderItem;
		_this.UploaderListCount++;
	};

	/*
	添加到上传列表层
	1.添加到上传列表层
	2.添加分隔线
	参数：
	fid 上传项ID
	uploaderDiv 新的上传信息层
	返回值：
		新添加的分隔线
	*/
	this.AppendToUploaderListDiv = function(fid, uploaderDiv)
	{
		//_this.UploaderListDiv.appendChild(uploaderDiv);
		$(_this.UploaderListDiv).append(uploaderDiv);

		var split = "<div class=\"Line\" style=\"display:block;\" id=\"FilePostLine" + fid + "\"></div>";
		//_this.UploaderListDiv.insertAdjacentHTML("beforeEnd", split);
		$(_this.UploaderListDiv).append(split);
		var obj = document.getElementById("FilePostLine" + fid);
		return obj;
	};

	//传送当前队列的第一个文件
	this.PostFirst = function()
	{
		//上传列表不为空
		if (_this.UploadQueue.length > 0)
		{
			while (_this.UploadQueue.length > 0)
			{
				//上传队列已满
				if (_this.IsPostQueueFull()) return;
				var index = _this.UploadQueue.shift();
				_this.UploaderList[index].Post();
			}
		}
	};

	/*
	验证文件名是否存在
	参数:
	[0]:文件名称
	*/
	this.Exist = function()
	{
		var fn = arguments[0];

		for (a in _this.UploaderList)
		{
			if (_this.UploaderList[a].LocalFile == fn)
			{
				return true;
			}
		}
		return false;
	};

	/*
	根据ID删除上传任务
	参数:
	fid 上传项ID。唯一标识
	*/
	this.Delete = function(fid)
	{
		var obj = _this.UploaderList[fid];
		if (null == obj) return;

		_this.RemoveQueue(fid); //从队列中删除
		_this.RemoveUnUploadIds(fid);//从未上传列表中删除

		//删除div
		_this.UploaderListDiv.removeChild(obj.div);
		//删除分隔线
		_this.UploaderListDiv.removeChild(obj.spliter);
		obj.LocalFile = "";
		obj.Dispose();
	};

	/*
		设置文件过滤器
		参数：
			filter 文件类型字符串，使用逗号分隔(exe,jpg,gif)
	*/
	this.SetFileFilter = function(filter)
	{
		_this.FileFilter.length = 0;
		_this.FileFilter = filter.split(",");
	};

	/*
	判断文件类型是否需要过滤
	根据文件后缀名称来判断。
	*/
	this.NeedFilter = function(fname)
	{
		if (_this.FileFilter.length == 0) return false;
		var exArr = fname.split(".");
		var len = exArr.length;
		if (len > 0)
		{
			for (var i = 0, l = _this.FileFilter.length; i < l; ++i)
			{
				//忽略大小写
				if (_this.FileFilter[i].toLowerCase() == exArr[len - 1].toLowerCase())
				{
					return true;
				}
			}
		}
		return false;
	};
	
	//打开文件选择对话框
	this.OpenFileDialog = function()
	{
		_this.Browser.OpenFileDialog();
	};
	
	//打开文件夹选择对话框
	this.OpenFolderDialog = function()
	{
		_this.Browser.OpenFolderDialog();
	};

	//粘贴文件
	this.PasteFiles = function()
	{
		_this.Browser.PasteFiles();
	};
	
	//检查续传大小是否合法。必须为全数字
	this.IsNumber = function(num)
	{
		var reg = /\D/;
		return reg.test(num);
	};
	
	/*
		添加一个续传文件
		参数：
			filePath 本地文件路径(urlencode编码)。D:\\Soft\\QQ2010.exe
			postedLength 已上传字节。控件将从此位置开始续传数据
			postedPercent 已上传百分比。示例：20%
			md5 文件MD5值。32个字符
			sfid 与服务器对应的fid，必须唯一
	*/
	this.AddResumeFile = function(filePath, postedLength, postedPercent, md5, sfid)
	{
		//本地文件名称存在
		if (_this.Exist(filePath)) return;

		var fileName = filePath.substr(filePath.lastIndexOf("\\") + 1);
		var fid = _this.UploaderListCount;
		_this.AppendQueue(fid);//添加到队列

		var upFile = new HttpUploader(fid, filePath, this);
		var newTable = _this.UploaderTemplateDiv.cloneNode(true);
		var jqItem = $(newTable);
		jqItem.css("display", "block");
		jqItem.attr("id", fid);
		//newTable.style.display = "block";
		//newTable.id = "item" + fid;

		var divLeft = jqItem.children().eq(0);
		var divRight = jqItem.children(1).eq(1);
		var objFileName = divLeft.children().eq(0).children().eq(0);
		$(objFileName).text(fileName);
		$(objFileName).attr("title", fileName);
		var fileSize = divLeft.children().eq(0).children().eq(1);
		fileSize.text(upFile.FileSize);
		upFile.pProcess = divLeft.children().eq(1).children().eq(0);
		upFile.pProcess.css("width", postedPercent);
		upFile.pMsg = divLeft.children().eq(2);
		upFile.pMsg.text("");
		upFile.pButton = divRight.children().eq(0).children().eq(0);
		upFile.pButton.attr("fid", fid);
		upFile.pButton.attr("domid", "item" + fid);
		upFile.pButton.attr("lineid", "FilePostLine" + fid);
		//upFile.pButton.click(BtnControlClick);
		$(upFile.pButton).click(function()
		{
			var obj = $(this);
			var objup = HttpUploaderMgrInstance.UploaderList[obj.attr("fid")];

			switch (obj.text())
			{
				case "暂停":
				case "停止":
					objup.Stop(obj.attr("fid"));
					break;
				case "取消":
					{
						objup.Stop();
						HttpUploaderMgrInstance.Delete(objup.FileID);
					}
					break;
				case "续传":
					if (!HttpUploaderMgrInstance.IsPostQueueFull())
					{
						HttpUploaderMgrInstance.AppendUploadId(objup.FileID);
						objup.Upload();
					}
					else
					{
						objup.Ready();
						//添加到队列
						HttpUploaderMgrInstance.AppendQueue(objup.FileID);
						objup.pButton.text("停止");
					}
					break;
				case "重试":
					objup.Post();
					break;
			}
		}); //模板控制按钮事件
		upFile.pPercent = divRight.children().eq(1);
		upFile.pPercent.text(postedPercent);
		upFile.ATL.SetPostedLength(postedLength); //设置续传位置
		upFile.ATL.SetMD5(md5);
		upFile.MD5 = md5;
		upFile.fid = sfid;
		upFile.ResetFields(); //设置UID,FID等附加字段信息

		//添加到上传列表
		this.AppenToUploaderList(fid, upFile);
		//添加到上传列表层
		upFile.spliter = _this.AppendToUploaderListDiv(fid, newTable);
		upFile.div = newTable;
		//upFile.Post(); //开始上传
		upFile.Ready(); //准备
	};

	/*
		添加一个文件到上传队列
		参数:
			fileName 包含完整路径的文件名称。D:\\Soft\\QQ.exe
	*/
	this.AddFile = function(filePath)
	{
		//本地文件名称存在
		if (_this.Exist(filePath)) return;
		//此类型为过滤类型
		if (_this.NeedFilter(filePath)) return;

		var fileName = filePath.substr(filePath.lastIndexOf("\\") + 1);
		var fid = _this.UploaderListCount;
		_this.AppendQueue(fid);//添加到队列

		var upFile = new HttpUploader(fid, filePath, _this);
		var newTable = _this.UploaderTemplateDiv.cloneNode(true);
		var jqItem = $(newTable);
		jqItem.css("display", "block");
		jqItem.attr("id", "item" + fid);

		var divLeft = jqItem.children().eq(0);
		var divRight = jqItem.children().eq(1);
		var objFileName = divLeft.children().eq(0).children().eq(0);
		objFileName.text(fileName);
		objFileName.attr("title", fileName);
		var fileSize = divLeft.children().eq(0).children().eq(1);
		fileSize.text(upFile.FileSize);
		upFile.pProcess = divLeft.children().eq(1).children().eq(0);
		upFile.pMsg = divLeft.children().eq(2);
		upFile.pMsg.text("");
		upFile.pButton = divRight.children().eq(0).children().eq(0);
		upFile.pButton.attr("fid", fid);
		upFile.pButton.attr("domid", "item" + fid);
		upFile.pButton.attr("lineid", "FilePostLine" + fid);
		//upFile.pButton.attachEvent("onclick", BtnControlClick);
		$(upFile.pButton).click(function()
		{
			var obj = $(this);
			var objup = HttpUploaderMgrInstance.UploaderList[obj.attr("fid")];

			switch (obj.text())
			{
				case "暂停":
				case "停止":
					objup.Stop(obj.attr("fid"));
					break;
				case "取消":
					{
						//var lister = HttpUploaderMgrInstance.UploaderListDiv;
						//lister.removeChild(objup.div);
						//lister.removeChild(objup.spliter);
						objup.Stop();
						HttpUploaderMgrInstance.Delete(objup.FileID);
					}
					break;
				case "续传":
						if (!HttpUploaderMgrInstance.IsPostQueueFull())
						{
							HttpUploaderMgrInstance.AppendUploadId(objup.FileID);
							objup.Upload();
						}
						else
						{
							objup.Ready();
							//添加到队列
							HttpUploaderMgrInstance.AppendQueue(objup.FileID);
							objup.pButton.text("停止");
						}
					break;
				case "重试":
					objup.Post();
					break;
			}
		}); //模板控制按钮事件
		upFile.pPercent = divRight.children().eq(1);
		upFile.pPercent.text("0%");
		upFile.Manager = _this;

		//添加到上传列表
		this.AppenToUploaderList(fid, upFile);
		//添加到上传列表层
		upFile.spliter = this.AppendToUploaderListDiv(fid, newTable);
		upFile.div = newTable;
		//upFile.Post(); //开始上传
		upFile.Ready(); //准备
	};
	
	//上传身份证
	this.UploadFile1 = function()
	{
		var path = _this.Browser.GetSingleFile();
		if (null == path) return;

		_this.UploadFile(path, "upPnl");
	};
	
	//上传文件
	this.UploadFile = function(filePath, oid)
	{
		var fileName = filePath.substr(filePath.lastIndexOf("\\") + 1);
		var fid = _this.UploaderListCount;
		//_this.AppendQueue(fid); //添加到队列

		var upFile = new HttpUploader(fid, filePath, _this);
		var newTable = _this.UploaderTemplateDiv.cloneNode(true);
		var jqItem = $(newTable);
		jqItem.css("display", "block");
		jqItem.attr("id", "item" + fid);
		var objFileName = jqItem.find("div[name='fileName']");
		objFileName.text(fileName);
		objFileName.attr("title", fileName);
		var fileSize = jqItem.find("div[name='fileSize']");
		fileSize.text("("+upFile.FileSize+")");
		upFile.pProcess = jqItem.find("div[name='process']");
		upFile.pMsg = jqItem.find("div[name='msg']");
		upFile.pMsg.text("");
		upFile.pButton = jqItem.find("div[name='btn']");
		upFile.pButton.attr("fid", fid);
		upFile.pButton.attr("domid", "item" + fid);
		upFile.pButton.attr("lineid", "FilePostLine" + fid);
		upFile.pBtnCancel = jqItem.find("div[name='btnCancel']");
		upFile.pBtnCancel.click(function(){
			upFile.Stop();
			upFile.Delete();
		});
		$(upFile.pButton).click(function()
		{
			var obj = $(this);
			var objup = HttpUploaderMgrInstance.UploaderList[obj.attr("fid")];

			switch (obj.text())
			{
				case "暂停":
				case "停止":
					objup.Stop(obj.attr("fid"));
					break;
				case "取消"://一般为取消MD5					
					objup.Stop();
					objup.Delete();
					break;
				case "续传":
					objup.Upload();
					break;
				case "重试":
					objup.Post();
					break;
				case "删除":
					objup.Delete();
					break;
			}
		}); //模板控制按钮事件
		//upFile.pPercent = divRight.children().eq(1);
		//upFile.pPercent = jqItem.find("div[name='percent']");
		//upFile.pPercent.text("0%");
		upFile.Manager = _this;
		//添加到上传列表
		this.AppenToUploaderList(fid, upFile);
		//添加到上传列表层
		//upFile.spliter = this.AppendToUploaderListDiv(fid, newTable);
		upFile.div = newTable;
		upFile.Post(); //开始上传
		//upFile.Ready(); //准备

		$("#" + oid).append(jqItem);
	};
}

var HttpUploaderErrorCode = {
	"0": "连接服务器错误"
	, "1": "发送数据错误"
	, "2": "接收数据错误"
	, "3": "未设置本地文件"
	, "4": "本地文件不存在"
	, "5": "打开本地文件错误"
	, "6": "不能读取本地文件"
	, "7": "公司未授权"
	, "8": "未设置IP"
	, "9": "域名未授权"
	, "10": "文件大小超过限制"//默认为2G
	//md5
	, "200": "无打打开文件"
	, "201": "文件大小为0"
};

var HttpUploaderState = {
	Ready: 0,
	Posting: 1,
	Stop: 2,
	Error: 3,
	GetNewID: 4,
	Complete: 5,
	WaitContinueUpload: 6,
	None: 7,
	Waiting: 8
	,MD5Working:9
};

//文件上传对象
function HttpUploader(fileID, filePath, mgr)
{
	//this.pMsg;
	//this.pProcess;
	//this.pPercent;
	//this.pButton
	//this.div
	//this.split
	//this.FileID
	this.ie = mgr.ie;
	this.firefox = mgr.firefox;
	this.chrome = mgr.chrome;
	this.FirefoxAtl = mgr.FirefoxAtl;
	this.Manager = mgr; //上传管理器指针
	this.FileListMgr = mgr.FileListMgr;//文件列表管理器
	this.Config = mgr.Config;
	this.Fields = mgr.Fields;
	this.ActiveX = mgr.ActiveX;
	this.State = HttpUploaderState.None;
	this.MD5 = "";
	var ref = this;
	
	//初始化控件
	this.ATL = {
		"Create": function() { this.Atl = new ActiveXObject(ref.ActiveX["Uploader"]); }
		, "SetObject": function(obj) { this.Atl.Object = obj; }
		, "SetPostUrl": function(url) { this.Atl.PostUrl = url; }
		, "SetEncodeType": function(encode) { this.Atl.EncodeType = encode; }
		, "SetLocalFile": function(filePath) { this.Atl.LocalFile = filePath; }
		, "SetPostedLength": function(len) { this.Atl.PostedLength = len; }
		, "SetFileSizeLimit": function(limit) { this.Atl.FileSizeLimit = limit; }
		, "SetRangeSize": function(size) { this.Atl.RangeSize = size; }
		, "SetLicensed": function(license) { this.Atl.CompanyLicensed = license; }
		, "SetMD5": function(md5) { this.Atl.MD5 = md5; }
		, "SetOnPost": function() { this.Atl.OnPost = HttpUploader_Process; }
		, "SetOnStateChanged": function() { this.Atl.OnStateChanged = HttpUploader_StateChanged; }
		//get
		, "GetFileSize": function() { return this.Atl.FileSize; }
		, "GetFileLength": function() { return this.Atl.FileLength; }
		, "GetResponse": function() { return this.Atl.Response; }
		, "GetMD5": function() { return this.Atl.MD5; }
		, "GetMd5Percent": function() { return this.Atl.Md5Percent; }
		, "GetPostedLength": function() { return this.Atl.PostedLength; }
		, "GetErrorCode": function() { return this.Atl.ErrorCode; }
		//methods
		, "CheckFile": function() { this.Atl.CheckFile(); }
		, "Post": function() { this.Atl.Post(); }
		, "Stop": function() { this.Atl.Stop(); }
		, "ClearFields": function() { this.Atl.ClearFields(); }
		, "AddField": function(fn, fv) { this.Atl.AddField(fn, fv); }
		, "Dispose": function() { delete this.Atl; }
		, "IsPosting": function() { return this.Atl.IsPosting(); }
		//property
		, "Atl": null
		, "FileID": 0//由控件分配的
	};
	//Firefox 插件
	this.ATLFF = {
		"Create": function() { }
		, "SetLocalFile": function(filePath) { this.FileID = this.Atl.AddFile(filePath); }
		, "SetObject": function(obj) { this.Atl.SetObject(this.FileID, obj); }
		, "SetPostUrl": function(url) { }
		, "SetEncodeType": function(encode) { }
		, "SetPostedLength": function(len) { this.Atl.SetPostedLength(this.FileID, len); }
		, "SetFileSizeLimit": function(limit) { }
		, "SetRangeSize": function(size) { }
		, "SetLicensed": function(licensed) { }
		, "SetMD5": function(md5) { this.Atl.SetMD5(this.FileID, md5); }
		, "SetOnPost": function() { }
		, "SetOnStateChanged": function() { }
		//get
		, "GetFileSize": function() { return this.Atl.GetFileSize(this.FileID); }
		, "GetFileLength": function() { return this.Atl.GetFileLength(this.FileID); }
		, "GetResponse": function() { return this.Atl.GetResponse(this.FileID); }
		, "GetMD5": function() { return this.Atl.GetMD5(this.FileID); }
		, "GetMd5Percent": function() { return this.Atl.GetMd5Percent(this.FileID); }
		, "GetPostedLength": function() { return this.Atl.GetPostedLength(this.FileID); }
		, "GetErrorCode": function() { return this.Atl.GetErrorCode(this.FileID); }
		//methods
		, "CheckFile": function() { this.Atl.CheckFile(this.FileID); }
		, "Post": function() { this.Atl.Post(this.FileID); }
		, "Stop": function() { this.Atl.Stop(this.FileID); }
		, "ClearFields": function() { this.Atl.ClearFields(this.FileID); }
		, "AddField": function(fn, fv) { this.Atl.AddField(this.FileID, fn, fv); }
		, "Dispose": function() { this.Atl.Remove(this.FileID); }
		, "IsPosting": function() { return this.Atl.IsPosting(this.FileID); }
		//property
		, "Atl": ref.FirefoxAtl
		, "FileID": 0//由控件分配的标识符，全局唯一。
	};
	//是Firefox或Chrome浏览器
	if (this.firefox||this.chrome){this.ATL = this.ATLFF;}
	this.ATL.Create();
	
	this.ATL.SetLocalFile(filePath);
	this.ATL.SetObject(this);
	this.ATL.SetPostUrl(this.Config["UrlPost"]);
	this.ATL.SetEncodeType(this.Config["EncodeType"]);
	this.ATL.SetOnPost();
	this.ATL.SetOnStateChanged();
	this.ATL.SetPostedLength("0"); //续传位置。
	this.ATL.SetFileSizeLimit(this.Config["FileSizeLimit"]);
	this.ATL.SetRangeSize(this.Config["RangeSize"]);
	this.ATL.SetLicensed(this.Config["CompanyLicensed"]);
	//this.ATL.FileID = fileID;
	this.FileName = filePath.substr(filePath.lastIndexOf("\\") + 1);
	this.LocalFile = filePath;
	//this.FileID = this.ATL.FileID;
	this.FileID = fileID;
	this.FileSize = this.ATL.GetFileSize(); //格式化后的文件大小 50MB
	this.FileLength = this.ATL.GetFileLength();//以字节为单位的字符串
	this.PathLocal = encodeURIComponent(filePath); //URL编码后的本地路径
	this.PathRelat = ""; //文件在服务器中的相对地址。示例：http://www.ncmem.con/upload/201204/03/QQ2012.exe
	this.fid = 0; //与服务器数据库对应的fid
	this.uid = this.Fields["uid"];
	
	//重置附加信息
	this.ResetFields = function()
	{
		//添加附加信息
		this.ATL.ClearFields(); //清空附加字段
//		$.each(upRef.Fields, function(name, val)
//		{
//			upRef.ATL.AddField(name, val);
//		});
		for (var field in this.Fields)
		{
			this.ATL.AddField(field, this.Fields[field]);
		}
		this.ATL.AddField("fid", this.fid);
		this.ATL.AddField("uid", this.uid);
	};
	
	//准备
	this.Ready = function()
	{
		//this.pButton.style.display = "none";
		//this.pMsg.text("正在上传队列中等待...");
		this.State = HttpUploaderState.Ready;
	};

	this.Post = function()
	{
		this.Manager.AppendUploadId(this.FileID);
		if (this.MD5.length > 0)
		{
			this.Upload();
		}
		else
		{
			//alert(this.ATL.FileID);
			this.CheckFile();
		}
	};
	
	//上传
	this.Upload = function()
	{
		//正在上传
		if (this.ATL.IsPosting())
		{
			this.Manager.RemoveUploadId(this.FileID);
		}
		else
		{
			this.pButton.css("display", "");
			this.pButton.text("停止");
			this.pBtnCancel.show();
			this.pMsg.text("正在连接服务器....");
			this.State = HttpUploaderState.Posting;

			this.ATL.Post();
		}
	};
	
	//检查文件
	this.CheckFile = function()
	{
		this.State = HttpUploaderState.MD5Working;
		this.ATL.CheckFile();
	};

	//启动下一个传输
	this.PostNext = function()
	{
		if (this.Manager.IsPostQueueFull()) return; //上传队列已满

		if (this.Manager.UploadQueue.length > 0)
		{
			var index = this.Manager.UploadQueue.shift();
			var obj = this.Manager.UploaderList[index];

			//空闲状态
			if (HttpUploaderState.Ready == obj.State)
			{
				obj.Post();
			}
		} //全部上传完成
		else
		{
			if (this.Manager.UnUploaderIdList.join("").length < 1)
			{
				//alert("所有文件上传完毕。");
			}
		}
	};
	
	//手动停止，一般在StopAll中调用
	this.StopManual = function()
	{
		if (HttpUploaderState.Posting == this.State)
		{
			this.pButton.text("续传");
			//this.pMsg.text("传输已停止....");
			this.ATL.Stop();
			this.State = HttpUploaderState.Stop;
		}
	};
	
	//停止传输，一般在用户点击停止按钮时调用
	this.Stop = function()
	{
		if (HttpUploaderState.Ready == this.State)
		{
			this.pButton.text("续传");
			this.pMsg.text("传输已停止....");
			this.State = HttpUploaderState.Stop;
			//this.Manager.RemoveQueue(this.FileID);
			//this.Manager.AppendUnUploadIds(this.FileID);//添加到未上传列表
			//this.PostNext();
			return;
		}
		
		this.pButton.text("续传");
		this.pMsg.text("传输已停止....");
		this.ATL.Stop();
		this.State = HttpUploaderState.Stop;

		//从上传列表中删除
		this.Manager.RemoveUploadId(this.FileID);
		//添加到未上传列表
		//this.Manager.AppendUnUploadIds(this.FileID);
		//传输下一个
		//this.PostNext();
	};

	//释放内存
	this.Dispose = function()
	{
		this.ATL.Dispose();
	};
	
	//删除DOM
	this.Delete = function()
	{
		$(this.div).empty();
		this.ATL.Dispose();
	};
	
	//快速上传完成，
	this.QuickComplete = function()
	{
		this.pButton.css("display","none");
		this.pProcess.css("width","100%");
		//this.pPercent.text("100%");
		//this.pMsg.text("服务器存在相同文件，快速上传成功。");
		this.Manager.CompleteList.push(this);
		this.State = HttpUploaderState.Complete;
		//从上传列表中删除
		this.Manager.RemoveUploadId(this.FileID);
		//从未上传列表中删除
		//this.Manager.RemoveUnUploadIds(this.FileID);
		this.Dispose();
		//添加到文件列表
		//this.FileListMgr.UploadComplete(this.fid, this);
		//this.PostNext();
	};
}

//上传错误
function HttpUploader_Error(obj)
{
	obj.pMsg.text(HttpUploaderErrorCode[obj.ATL.GetErrorCode()]);
	//文件大小超过限制,文件大小为0
	if (10 == obj.ATL.GetErrorCode()
		|| 201 == obj.ATL.GetErrorCode())
	{
		obj.pButton.text("取消");
	}
	else
	{
		obj.pButton.text("续传");
	}
	obj.State = HttpUploaderState.Error;
	//从上传列表中删除
	obj.Manager.RemoveUploadId(obj.FileID);
	//添加到未上传列表
	//obj.Manager.AppendUnUploadIds(obj.FileID);
	//obj.PostNext();
}

//上传完成，向服务器传送信息
function HttpUploader_Complete(obj)
{
	obj.pButton.css("display","none");
	obj.pProcess.css("width","100%");
	//obj.pPercent.text("100%");
	//obj.pMsg.text("上传完成");
	obj.pProcess.attr("title","上传完成");
	obj.Manager.CompleteList.push(obj);
	obj.State = HttpUploaderState.Complete;
	//从上传列表中删除
	obj.Manager.RemoveUploadId(obj.FileID);
	//从未上传列表中删除
	//obj.Manager.RemoveUnUploadIds(obj.FileID);
	//obj.Dispose();

	$.ajax({
		type: "POST"
		, url: obj.Config["UrlComplete"]
		, data: { md5: obj.MD5, uid: obj.Fields["uid"], fid: obj.fid, time: Date() }
		, success:
		function(msg)
		{
			//添加到文件列表
			//obj.FileListMgr.UploadComplete(obj.fid, obj);
			//obj.PostNext();
		}
        , error: function() { alert("向服务器发送Complete信息错误！"); }
        , complete: function(req, sta) { req = null; }
	});
}

//传输进度。频率为每秒调用一次
function HttpUploader_Process(obj, speed, postedLength, percent, times)
{
	//obj.pPercent.text(percent);
	obj.pProcess.css("width",percent);
	var str = "已上传:" + postedLength + " 速度:" + speed + "/S 剩余时间:" + times;
	obj.pMsg.text(str);
	//obj.pProcess.attr("title", str);
}

//服务器连接成功
function HttpUploader_Connected(obj)
{
	obj.pMsg.text("服务器连接成功");
	//obj.pProcess.attr("title", "服务器连接成功");
}

//MD5计算中
function HttpUploader_MD5_Working(obj)
{
	var msg = "正在扫描本地文件，已完成：" + obj.ATL.GetMd5Percent() + "%";
	obj.pMsg.text(msg);
	//obj.pProcess.attr("title",msg);
}

//MD5计算完毕
function HttpUploader_MD5_Complete(obj)
{
	obj.MD5 = obj.ATL.GetMD5();
	//在此处增加服务器验证代码。
	//obj.pMsg.text("MD5计算完毕，开始连接服务器...");

	$.ajax({
		type: "POST"
		, url: obj.Config["UrlCreate"]
		, data: { md5: obj.MD5, uid: obj.Fields["uid"], fileLength: obj.FileLength, fileSize: obj.FileSize, pathLocal: obj.PathLocal, time: Date() }
		, success:
		function(msg)
		{
			var json = eval(msg)
			json = json[0];
			obj.fid = json.fid;
			obj.ResetFields();
			//服务器已存在相同文件，且已上传完成
			if ("True" == json.PostComplete)
			{
				obj.QuickComplete();
			} //服务器文件没有上传完成
			else
			{
				obj.ATL.SetPostedLength(json.PostedLength);
				obj.pProcess.css("width", json.PostedPercent);
				obj.pProcess.attr("title",json.PostedPercent);
				obj.Upload();
			}
		}
        , error: function() { alert("向服务器发送MD5信息错误！"); }
        , complete: function(req, sta) { req = null; }

	});
}

/*
	HUS_Leisure			=0	//空闲
	,HUS_Uploading		=1	//上传中 
	,HUS_Stop  			=2	//停止 
	,HUS_UploadComplete	=3	//传输完毕 
	,HUS_Error 			=4	//错误 
	,HUS_Connected 		=5	//服务器已连接
	,HUS_Md5Working		=6	//MD5计算中
	,HUS_Md5Complete	=7	//MD5计算完毕
*/
function HttpUploader_StateChanged(obj,state)
{
	switch(state)
	{
		case 0:
			break;
		case 1:
			break;
		case 2:
			break;
		case 3:
			HttpUploader_Complete(obj);
			break;
		case 4:
			HttpUploader_Error(obj);
			break;
		case 5:
			HttpUploader_Connected(obj);
			break;
		case 6:
			HttpUploader_MD5_Working(obj);
			break;
		case 7:
			HttpUploader_MD5_Complete(obj);
			break;
	}
}