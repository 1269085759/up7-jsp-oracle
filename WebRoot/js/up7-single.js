/*
	版权所有 2009-2015 荆门泽优软件有限公司
	保留所有权利
	官方网站：http://www.ncmem.com/
	产品首页：http://www.ncmem.com/webplug/http-uploader5/index.asp
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
    更新记录：
        2015-08-01 优化
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

/*
    方法：
    postAuto(dom-id),打开文件选择窗口并自动上传
    postLoc(pathLoc,dom-id),上传指定路径的文件。
	更新记录：
        2009-11-05 文件管理类
        2016-02-26 优化
*/
function HttpUploaderMgr()
{
	var _this = this;
	this.Config = {
		"EncodeType"		: "GB2312"
		, "Company"			: "荆门泽优软件有限公司"
		, "Version"			: "2,8,104,31473"
		, "License"			: "159FFE4029895139C58DD0B959749C59BA521C1B59074B4AB1F57296115BFC8CCB8157762B203AFA597034A95D1530EEF52047D9FA36B363137718601FEEA3E1ED08F913195A39D1080EC794CB443F94BC2096B85B78AE365BFBEFFB95859D2CA09B7F95CE3F2063B29CBF7DA44CE84914"//
		, "Cloud"			: "aliyun"//云存储：qiniu,upyun,aliyun
		, "CloudBucket"		: "ncmem"//空间名
		, "Authenticate"	: ""//域验证方式：basic,ntlm
		, "AuthName"		: ""//域帐号
		, "AuthPass"		: ""//域密码
		, "FileFilter"		: "*"//文件类型。所有类型：*。自定义类型：jpg,bmp,png,gif,rar,zip,7z,doc
		, "FileSizeLimit"	: "0"//自定义允许上传的文件大小，以字节为单位。0表示不限制。字节计算工具：http://www.beesky.com/newsite/bit_byte.htm
		, "FilesLimit"		: 0//文件选择数限制。0表示不限制
		, "AllowMultiSelect": 0//多选开关。1:开启多选。0:关闭多选
		, "RangeSize"		: 1048576//文件块大小，以字节为单位。必须为64KB的倍数。推荐大小：1MB。
		, "Debug"			: true//是否打开调式模式。true,false
		, "LogFile"			: "F:\\log.txt"//日志文件路径。需要先打开调试模式。
		, "InitDir"			: ""//初始化路径。示例：D:\\Soft
		, "AppPath"			: ""//网站虚拟目录名称。子文件夹 web
		, "UrlCreate"		: "http://localhost:8080/HttpUploader7Oracle/db/f_create.jsp"
		, "UrlPost"			: "http://ncmem.oss-cn-shenzhen.aliyuncs.com"//upyun:http://m0.api.upyun.com,qiniu:http://upload.qiniu.com,aliyun:http://ncmem.oss-cn-shenzhen.aliyuncs.com
		, "UrlProcess"		: "http://localhost:8080/HttpUploader7Oracle/db/f_process.jsp"
		, "UrlComplete"		: "http://localhost:8080/HttpUploader7Oracle/db/f_complete.jsp"
		, "UrlList"			: "http://localhost:8080/HttpUploader7Oracle/db/f_list.jsp"
		, "UrlDel"			: "http://localhost:8080/HttpUploader7Oracle/db/f_del.jsp"
        //x86
		, "ClsidDroper"		: "21EAADB3-0EE2-41F1-A4D7-4C54234C282D"
		, "ClsidUploader"	: "BABD5A8C-EB79-47FB-92EE-6D8C0133BD8C"
		, "ClsidPartition"	: "6E8B070D-1289-4989-8CDA-022522E01E8C"
		, "CabPath"			: "http://www.ncmem.com/download/HttpUploader7/HttpUploader7.cab"
		//x64
		, "ClsidDroper64"	: "D439C5D0-E405-4997-B6FF-FC89B14876EC"
		, "ClsidUploader64"	: "44ED205C-58DF-4EFE-B87B-04865237B02E"
		, "ClsidPartition64": "035C0F00-F788-4823-99E8-08CEB9455EAA"
		, "CabPath64"		: "http://www.ncmem.com/download/HttpUploader7/HttpUploader64.cab"
		//Firefox
		, "XpiType"		    : "application/npHttpUp7"
		, "XpiPath"	        : "http://www.ncmem.com/download/HttpUploader7/HttpUploader7.xpi"
		//Chrome
		, "CrxName"			: "npHttpUp7"
		, "CrxType"		    : "application/npHttpUp7"
		, "CrxPath"	        : "http://www.ncmem.com/download/HttpUploader7/HttpUploader7.crx"
		, "SetupPath"		: "http://localhost:4955/demoAccess/js/setup.htm"
		, "ExePath"			: "http://www.ncmem.com/download/HttpUploader7/HttpUploader7.exe"
	};

    //biz event
	this.event = {
	    "md5Complete": function (obj, md5) { }
        , "postComplete": function (obj) { }
	};

	this.ActiveX = {
		"Uploader"		: "Xproer.HttpUploader7"
		, "Partition"	: "Xproer.HttpPartition7"
		//64bit
		, "Uploader64"	: "Xproer.HttpUploader7x64"
		, "Partition64"	: "Xproer.HttpPartition7x64"
	};
	
	//附加参数
	this.Fields = {
		 "uname": "test"
		,"upass": "test"
		,"uid":0
		,"fid":0
	};
	
	//检查版本 Win32/Win64/Firefox/Chrome
	var browserName = navigator.userAgent.toLowerCase();
	this.ie = browserName.indexOf("msie") > 0;
	this.firefox = browserName.indexOf("firefox") > 0;
	this.chrome = browserName.indexOf("chrome") > 0;

	this.CheckVersion = function()
	{
		//Win64
		if (window.navigator.platform == "Win64")
		{
		    this.Config["CabPath"] = this.Config["CabPath64"];

		    this.Config["ClsidDroper"] = this.Config["ClsidDroper64"];
		    this.Config["ClsidUploader"] = this.Config["ClsidUploader64"];
		    this.Config["ClsidPartition"] = this.Config["ClsidPartition64"];

		    this.ActiveX["Uploader"] = this.ActiveX["Uploader64"];
		    this.ActiveX["Partition"] = this.ActiveX["Partition64"];
		} //Firefox
		else if (this.firefox)
		{
		    this.Config["CabPath"] = this.Config["XpiPath"];
		    this.ActiveX["Uploader"] = this.ActiveX["UploaderFF"];
		}
		else if (this.chrome)
		{
		    this.Config["CabPath"] = this.Config["CrxPath"];
		    this.Config["XpiType"] = this.Config["CrxType"];
		}
	};
	this.CheckVersion();
	
	//http://www.ncmem.com/
	this.Domain = "http://" + document.location.host;

	this.FileFilter = new Array(); //文件过滤器
	this.filesIdCount = 0; 	//上传项总数
	this.filesMap = new Object(); //上传项列表
	this.UploadQueue = new Array();		//上传队列
	this.UnUploaderIdList = new Array(); //未上传项ID列表
	this.UploadIdList = new Array(); //正在上传的ID列表
	this.iePart = null;
	this.ieUploader = null;
	this.ffPart = null;
	this.tmpFile = null;//jquery object
	this.fileCur = null;//当前文件上传项
	
	//文件上传面板。
	this.GetHtml = function()
	{
		//加载拖拽控件
		var acx = "";
		//自动安装CAB
		acx += '<div style="display:none">';
		//文件上传控件
		acx += '<object name="ieUper" classid="clsid:' + _this.Config["ClsidUploader"] + '"';
		acx += ' codebase="' + _this.Config["CabPath"] + '" width="1" height="1" ></object>';
		//文件夹选择控件
		acx += '<object name="iePart" classid="clsid:' + _this.Config["ClsidPartition"] + '"';
		acx += ' codebase="' + _this.Config["CabPath"] + '" width="1" height="1" ></object>';
		acx += '</div>';
	    //上传列表项模板
		acx += '<div class="UploaderItem" name="fileItem" id="UploaderTemplate">\
		            <div class="UploaderItemLeft">\
		                <div class="FileInfo">\
		                    <div name="fileName" class="FileName top-space">HttpUploader程序开发.pdf</div>\
		                    <div name="fileSize" class="FileSize" child="1">100.23MB</div>\
		                </div>\
		                <div class="ProcessBorder top-space"><div name="process" class="Process"></div></div>\
		                <div name="msg" class="PostInf top-space">已上传:15.3MB 速度:20KB/S 剩余时间:10:02:00</div>\
		            </div>\
		            <div class="UploaderItemRight">\
		                <div class="BtnInfo"><span name="btnCancel" class="Btn">取消</span>&nbsp;<span name="btnDel" class="Btn">删除</span></div>\
		                <div name="percent" class="ProcessNum">35%</div>\
		            </div>\
		        </div>';
		return acx;
	};
		
    //IE浏览器信息管理对象
	this.BrowserIE = {
	    "Check": function ()//检查插件是否已安装
	    {
	        try
	        {
	            var com = new ActiveXObject(_this.ActiveX["Uploader"]);
	            return true;
	        }
	        catch (e) { return false; }
	    }
        , "NeedUpdate": function (){return this.GetVersion() != _this.Config["Version"];}
		, "GetVersion": function (){return _this.iePart.Version;}
        , "GetFileSize": function (path) { return _this.iePart.GetFileSize(path); }
        , "GetFileLength": function (path){return _this.iePart.GetFileLength(path);}
		, "GetSingleFile": function ()//获取单个文件
		{
		    var obj = _this.iePart;
		    obj.FileFilter = _this.Config["FileFilter"];
		    obj.AllowMultiSelect = _this.Config["AllowMultiSelect"];
		    obj.InitDir = _this.Config["InitDir"];
		    if (!obj.ShowDialog()) return null;

		    var list = obj.GetSelectedFiles();
		    if (list == null) return null;
		    if (list.lbound(1) == null) return null;
		    return list.getItem(list.lbound(1));
		}
		, "GetMacs": function ()
		{
		    var obj = _this.iePart;
		    var list = obj.GetMacs();
		    if (list == null) return null;
		    if (list.lbound(1) == null) return null;
		    var arr = new Array();

		    for (var index = list.lbound(1) ; index <= list.ubound(1) ; index++)
		    {
		        arr.push(list.getItem(index));
		    }
		    return arr;
		}
        , "GetIP": function () { return _this.iePart.IP; }
		, "Init": function () { }
	};
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
        , "NeedUpdate": function (){return this.GetVersion() != _this.Config["Version"];}
		, "GetVersion": function (){return _this.ffPart.Version;}
        , "GetFileSize": function (path) { return _this.ffPart.GetFileSize(path); }
        , "GetFileLength": function (path){return _this.ffPart.GetFileLength(path);}
		, "Setup": function()//安装插件
		{
			var xpi = new Object();
			xpi["Calendar"] = _this.Config["CabPathFirefox"];
			InstallTrigger.install(xpi, function(name, result) { });
		}
		, "GetSingleFile": function()//获取单个文件
		{
		    var obj = _this.ffPart;
			obj.FileFilter = _this.Config["FileFilter"];
			obj.FilesLimit = _this.Config["FilesLimit"];
			obj.AllowMultiSelect = _this.Config["AllowMultiSelect"];
			obj.InitDir = _this.Config["InitDir"];

			var files = obj.ShowDialog();
			if (files)
			{
				return files[0];
			}
			return null;
		}
		, "GetMacs": function ()
		{
		    var obj = _this.ffPart;
		    var list = obj.GetMacs();
		    if (list == null) return null;
		    if (list.lbound(1) == null) return null;
		    var arr = new Array();

		    for (var index = list.lbound(1) ; index <= list.ubound(1) ; index++)
		    {
		        arr.push(list.getItem(index));
		    }
		    return arr;
		}
        , "GetIP": function () { return _this.ffPart.IP; }
		, "Init": function()//初始化控件
		{
		    var atl             = _this.ffPart;
			atl.FileSizeLimit   = _this.Config["FileSizeLimit"];
			atl.RangeSize       = _this.Config["RangeSize"];
			atl.EncodeType      = _this.Config["EncodeType"];
			atl.Licensed        = _this.Config["Company"];
			atl.License         = _this.Config["License"];
            atl.Cloud           = _this.Config["Cloud"];
			atl.CloudBucket     = _this.Config["CloudBucket"];
			atl.Authenticate    = _this.Config["Authenticate"];
			atl.AuthName        = _this.Config["AuthName"];
			atl.AuthPass        = _this.Config["AuthPass"];
			atl.PostUrl         = _this.Config["UrlPost"];
			atl.Debug           = _this.Config["Debug"];
			atl.LogFile         = _this.Config["LogFile"];
			atl.OnPost          = HttpUploader_Process;
			atl.OnStateChanged  = HttpUploader_StateChanged;
		}
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
        , "NeedUpdate": function () { return this.GetVersion() != _this.Config["Version"]; }
		, "GetVersion": function () { return _this.ffPart.Version; }
        , "GetFileSize": function (path) { return _this.ffPart.GetFileSize(path); }
        , "GetFileLength": function (path) { return _this.ffPart.GetFileLength(path); }
		, "Setup": function()//安装插件
		{
			document.write('<iframe style="display:none;" src="' + _this.Config["CabPathChrome"] + '"></iframe>');
		}
		, "GetSingleFile": function()//获取单个文件
		{
		    var obj = _this.ffPart;
			obj.FileFilter = _this.Config["FileFilter"];
			obj.FilesLimit = _this.Config["FilesLimit"];
			obj.AllowMultiSelect = _this.Config["AllowMultiSelect"];
			obj.InitDir = _this.Config["InitDir"];

			var files = obj.ShowDialog();
			if (files)
			{
				return files[0];
			}
			return null;
		}
		, "GetMacs": function ()
		{
		    var obj = _this.ffPart;
		    var list = obj.GetMacs();
		    if (list == null) return null;
		    if (list.lbound(1) == null) return null;
		    var arr = new Array();

		    for (var index = list.lbound(1) ; index <= list.ubound(1) ; index++)
		    {
		        arr.push(list.getItem(index));
		    }
		    return arr;
		}
        , "GetIP": function () { return _this.ffPart.IP; }
		, "Init": function()//初始化控件
		{
		    var atl             = _this.ffPart;
			atl.FileSizeLimit   = _this.Config["FileSizeLimit"];
			atl.RangeSize       = _this.Config["RangeSize"];
			atl.EncodeType      = _this.Config["EncodeType"];
			atl.Licensed        = _this.Config["Company"];
			atl.License         = _this.Config["License"];
            atl.Cloud           = _this.Config["Cloud"];
			atl.CloudBucket     = _this.Config["CloudBucket"];
            atl.Authenticate    = _this.Config["Authenticate"];
            atl.AuthName        = _this.Config["AuthName"];
            atl.AuthPass        = _this.Config["AuthPass"];
			atl.PostUrl         = _this.Config["UrlPost"];
			atl.Debug           = _this.Config["Debug"];
			atl.LogFile         = _this.Config["LogFile"];
			atl.OnPost          = HttpUploader_Process;
			atl.OnStateChanged  = HttpUploader_StateChanged;
		}
	};
	
	//浏览器环境检查
	this.Browser = this.BrowserIE;
	//Firefox
	if (this.firefox)
	{
	    this.Browser = this.BrowserFF;
	    //if (!this.Browser.Check()) { this.Browser.Setup(); }
	} //Chrome
	else if (this.chrome)
	{
	    this.Browser = this.BrowserChrome;
		//if (!this.Browser.Check()) { this.Browser.Setup(); }
	}

	//安全检查，在用户关闭网页时自动停止所有上传任务。
	this.SafeCheck = function()
	{
		$(window).bind("beforeunload", function()
		{
			if (null != _this.fileCur)
			{
				event.returnValue = "您还有程序正在运行，确定关闭？";
			}
		});

		$(window).bind("unload", function()
		{ 
		    if (null != _this.fileCur)
			{
		        _this.fileCur.Stop();
			}
		});
	};

	this.Load = function()
	{
	    var html = this.GetHtml();
	    html += '<embed type="' + this.Config["XpiType"] + '" pluginspage="' + this.Config["CabPath"] + '" width="1" height="1"/>';
	    var dom = $(document.body).append(html);
	    this.tmpFile   = dom.find('div[name="fileItem"]');
	    this.ffPart     = dom.find('embed').get(0);
	    this.iePart     = dom.find('object[name="iePart"]').get(0);
	    this.ieUploader = dom.find('object[name="ieUper"]').get(0);
	    this.SafeCheck();
	    this.Browser.Init(); //
	};

	//加截容器，上传面板，文件列表面板
	this.LoadTo = function(oid)
	{
	    var html = this.GetHtml();
	    html += '<embed type="' + this.Config["XpiType"] + '" pluginspage="' + this.Config["CabPath"] + '" width="1" height="1"/>';
		var dom         = $("#"+oid).html(html);
		this.tmpFile    = dom.find('div[name="fileItem"]');
		this.ffPart     = dom.find('embed').get(0);
		this.iePart     = dom.find('object[name="iePart"]').get(0);
		this.ieUploader = dom.find('object[name="ieUper"]').get(0);
		this.SafeCheck();
		this.Browser.Init(); //
	};

	/*
	根据ID删除上传任务
	参数:
	fid 上传项ID。唯一标识
	*/
	this.Delete = function(fid)
	{
		var obj = _this.filesMap[fid];
		if (null == obj) return;

		//删除div
		obj.div.remove();
		obj.spliter.remove();
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

    //oid,显示上传项的层ID
	this.postAuto = function (dom_id)
	{
	    var path = this.Browser.GetSingleFile();
	    if (null == path) return;

	    if (this.fileCur != null) return;
	    var fileName = path.substr(path.lastIndexOf("\\") + 1);
	    var fid = this.filesIdCount;

	    var upFile = new HttpUploader(fid, path, this);
	    this.filesMap[fid] = upFile;
	    var ui = this.tmpFile.clone();
	    ui.css("display", "block").attr("id", "item" + fid);
	    $("#" + dom_id).append(ui);

	    var ui_name     = ui.find("div[name='fileName']");
	    var ui_size     = ui.find("div[name='fileSize']");
	    var ui_process  = ui.find("div[name='process']");
	    var ui_msg      = ui.find("div[name='msg']");
	    var btnDel      = ui.find("span[name='btnDel']");
	    var btnCancel   = ui.find("span[name='btnCancel']");
	    var uiPercent   = ui.find("div[name='percent']");
	    var ui_eles = { process: ui_process,percent:uiPercent, msg: ui_msg, btn: {cancel:btnCancel,del:btnDel},panel:ui};

	    ui_name.text(fileName).attr("title", fileName);
	    ui_size.text(upFile.FileSize);
	    ui_msg.text("");
	    uiPercent.text("0%");

	    upFile.ui = ui_eles;
	    btnCancel.attr("fid", fid).click(function ()
            {
                switch (btnCancel.text())
                {
                    case "暂停":
                    case "停止":
                        upFile.Stop();
                        break;
                    case "取消":
                        {
                            upFile.Cancel();
                        }
                        break;
                    case "续传":
                        upFile.Upload();
                        break;
                    case "重试":
                        upFile.Post();
                        break;
                }
            }); //模板控制按钮事件
	    btnDel.click(function () { upFile.Cancel(); });
	    upFile.Post(); //开始上传
	    this.fileCur = upFile;
	};
	
	//上传文件
	this.postLoc = function(filePath, oid)
	{
		if (_this.filesIdCount > 0) return;
		var fileName = filePath.substr(filePath.lastIndexOf("\\") + 1);
		var fid = _this.filesIdCount++;

		var ui = _this.tmpFile.cloneNode(true);
		ui.css("display", "block").attr("id", "item" + fid);
		var ui_name = ui.find("div[name='fileName']");
		var ui_size	= ui.find("div[name='fileSize']");
		var ui_process	= ui.find("div[name='process']");
		var ui_msg		= ui.find("div[name='msg']");
		var btnDel		= ui.find("span[name='btnDel']");
		var btnCancel	= ui.find("span[name='btnCancel']");
		var divPercent	= ui.find("div[name='percent']");
		var ui_eles = { process: ui_process, percent: uiPercent, msg: ui_msg, btn: { cancel: btnCancel, del: btnDel }, panel: ui };

		var upFile = new HttpUploader(fid, filePath, _this);
		this.filesMap[fid] = upFile;
		ui_name.text(fileName).attr("title", fileName);
		ui_size.text(upFile.FileSize);
		ui_msg.text("");
		divPercent.text("0%");

		upFile.ui = ui_eles;
		btnDel.attr("fid", fid).click(function ()
	        {
	            switch (btnDel.text())
	            {
	                case "暂停":
	                case "停止":
	                    upFile.Stop();
	                    break;
	                case "取消":
	                    {
	                        upFile.Cancel();
	                    }
	                    break;
	                case "续传":
	                    upFile.Upload();
	                    break;
	                case "重试":
	                    upFile.Post();
	                    break;
	            }
	        }); //模板控制按钮事件
		btnCancel.click(function (){upFile.Cancel();});
		upFile.Post(); //开始上传

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
    //biz
    , "300": "服务器md5验证错误,这个错误一般由f_create.aspx页面引起\n请检查：\n1.数据库是否已经创建\n2.数据库连接信息是否配置正确(在DbHelper.cs设置)\n3.UrlCreate路径配置是否正确\n4.请检查f_create.aspx.cs中业务逻辑是否正确"
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
    var _this = this;
    this.ui = { process: null, percent: null, msg: null, btn: {cancel:null,del:null},panel:null};
	this.ie = mgr.ie;
	this.firefox = mgr.firefox;
	this.chrome = mgr.chrome;
	this.ffPart = mgr.ffPart;
	this.browser = mgr.Browser;
	this.ieUploader = mgr.ieUploader;
	this.Manager = mgr; //上传管理器指针
	this.event = mgr.event;
	this.Config = mgr.Config;
	this.Fields = mgr.Fields;
	this.ActiveX = mgr.ActiveX;
	this.State = HttpUploaderState.None;
	this.MD5 = "";
	this.pathLoc = filePath;
	this.pathSvr = "";
	this.lenSvr = 0;//
	this.FileName = filePath.substr(filePath.lastIndexOf("\\") + 1);
	this.LocalFile = filePath;
    //this.FileID = this.ATL.FileID;
	this.FileID = fileID;
	this.idLoc = fileID;
	this.idSvr = 0;
	this.FileSize = this.browser.GetFileSize(filePath); //格式化后的文件大小 50MB
	this.FileLength = this.browser.GetFileLength(filePath);//以字节为单位的字符串
	this.PathLocal = encodeURIComponent(filePath); //URL编码后的本地路径
	this.PathRelat = ""; //文件在服务器中的相对地址。示例：http://www.ncmem.con/upload/201204/03/QQ2012.exe
	this.fid = 0; //与服务器数据库对应的fid
	this.uid = this.Fields["uid"];
	this.percentSvr = "0%";
	this.fileSvr = null;//json obj
	var ref = this;
	
	//初始化控件
	this.ATL = {
		"Create": function()
		{
		    this.Atl                = _this.ieUploader;
		    this.Atl.Debug          = _this.Config["Debug"];
		    this.Atl.LogFile        = _this.Config["LogFile"];
		    this.Atl.Authenticate   = _this.Config["Authenticate"];
		    this.Atl.AuthName       = _this.Config["AuthName"];
		    this.Atl.AuthPass       = _this.Config["AuthPass"];
		    this.Atl.LocalFile      = _this.pathLoc;
		    this.Atl.Object         = _this;
		    this.Atl.EncodeType     = _this.Config["EncodeType"];
		    this.Atl.PostedLength   = _this.lenSvr;
		    this.Atl.FileSizeLimit  = _this.Config["FileSizeLimit"];
		    this.Atl.RangeSize      = _this.Config["RangeSize"];
		    this.Atl.Company        = _this.Config["Company"];
		    this.Atl.License        = _this.Config["License"];
		    this.Atl.Cloud          = _this.Config["Cloud"];
		    this.Atl.CloudBucket    = _this.Config["CloudBucket"];
		    this.Atl.PostUrl        = _this.Config["UrlPost"];
            this.Atl.MD5            = _this.MD5;
            this.Atl.Cookie         = document.cookie;
		    this.Atl.OnPost         = HttpUploader_Process;
		    this.Atl.OnStateChanged = HttpUploader_StateChanged;
		}
		, "SetMD5": function(md5) { this.Atl.MD5 = md5; }
		//get
		, "GetResponse": function() { return this.Atl.Response; }
		, "GetMD5": function() { return this.Atl.MD5; }
		, "GetMd5Percent": function() { return this.Atl.Md5Percent; }
		, "GetFileLenSvr": function () { return this.Atl.PostedLength; }
        , "SetFileLenSvr": function (len) { this.Atl.PostedLength = len;}
		, "GetErrorCode": function() { return this.Atl.ErrorCode; }
		//methods
		, "CheckFile": function() { this.Atl.CheckFile(); }
		, "Post": function ()
		{
		    this.SetFileLenSvr(_this.lenSvr);
		    this.Atl.Post();
		}
		, "Stop": function() { this.Atl.Stop(); }
		, "ClearFields": function() { this.Atl.ClearFields(); }
		, "AddField": function(fn, fv) { this.Atl.AddField(fn, fv); }
		, "Dispose": function() { delete this.Atl; }
		, "IsPosting": function() { return this.Atl.IsPosting(); }
		//property
		, "Atl": _this.ieUploader
	};
	//Firefox 插件
	this.ATLFF = {
	    "Create": function ()
	    {
	        if (0 == this.id)
	        {
	            this.id = this.Atl.AddFile(_this.pathLoc);
	        }
	        this.Atl.SetObject(this.id, _this);
	        this.Atl.SetPostedLength(this.id, _this.lenSvr);
	        this.SetMD5(_this.MD5);
	    }
		, "SetLocalFile": function(filePath) { this.id = this.Atl.AddFile(filePath); }
		, "SetObject": function(obj) { this.Atl.SetObject(this.id, obj); }
		, "SetPostedLength": function (len) { this.Atl.SetPostedLength(this.id, len); }
		, "SetFileLenSvr": function (len) { this.Atl.SetPostedLength(this.id, len); }
		, "SetMD5": function(md5) { this.Atl.SetMD5(this.id, md5); }
		//get
		, "GetResponse": function() { return this.Atl.GetResponse(this.id); }
		, "GetMD5": function() { return this.Atl.GetMD5(this.id); }
		, "GetMd5Percent": function() { return this.Atl.GetMd5Percent(this.id); }
		, "GetFileLenSvr": function () { return this.Atl.GetPostedLength(this.id); }
		, "GetErrorCode": function() { return this.Atl.GetErrorCode(this.id); }
		//methods
		, "CheckFile": function() { this.Atl.CheckFile(this.id); }
		, "Post": function ()
		{
		    this.SetFileLenSvr(_this.lenSvr);
		    this.Atl.Post(this.id);
		}
		, "Stop": function() { this.Atl.Stop(this.id); }
		, "ClearFields": function() { this.Atl.ClearFields(this.id); }
		, "AddField": function(fn, fv) { this.Atl.AddField(this.id, fn, fv); }
		, "Dispose": function() { this.Atl.Remove(this.id); }
		, "IsPosting": function() { return this.Atl.IsPosting(this.id); }
		//property
		, "Atl": _this.ffPart
		, "id": 0//由控件分配的标识符，全局唯一。
	};
	//是Firefox或Chrome浏览器
	if (this.firefox||this.chrome){this.ATL = this.ATLFF;}
	this.ATL.Create();
	
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
		this.ATL.AddField("pathSvr", this.pathSvr);//add(2015-03-19):
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
		}
		else
		{
			this.ui.btn.cancel.show();
			this.ui.btn.cancel.text("停止");
			this.ui.msg.innerText = "正在连接服务器....";
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

	this.UploadPercent = function ()
	{
	    if (this.percentSvr == "0%") return;
	    $.ajax({
	        type: "GET"
			, dataType: 'jsonp'
			, jsonp: "callback" //自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名
			, url: _this.Config["UrlProcess"]
			, data: { uid: _this.Fields["uid"], fid: _this.fid, percent: _this.percentSvr, lenSvr: _this.FileLengthSvr, time: new Date().getTime() }
			, success: function (msg) { }
			, error: function () { alert("更新文件进度错误！" + req.responseText); }
			, complete: function (req, sta) { req = null; }
	    });
	};
	
	//停止传输，一般在用户点击停止按钮时调用
	this.Stop = function()
	{
		if (HttpUploaderState.Ready == this.State)
		{
			this.ui.btn.cancel.text("续传");
			this.ui.msg.text("传输已停止....");
			this.State = HttpUploaderState.Stop;
			return;
		}
		
		this.ui.btn.cancel.text("续传");
		this.ui.btn.cancel.show();
		this.ui.msg.text("传输已停止....");
		this.ATL.Stop();
		this.State = HttpUploaderState.Stop;
		this.UploadPercent();//更新进度
	};
	
	//取消，一般在上传过程中执行
	this.Cancel = function()
	{
		this.Stop();
		this.ui.panel.remove();
		this.Manager.fileCur = null;//
	};

	//释放内存
	this.Dispose = function()
	{
		this.ATL.Dispose();
	};
	
	//快速上传完成，
	this.QuickComplete = function()
	{
	    this.event.postComplete(this);//biz event
		this.ui.btn.cancel.hide();
		this.ui.btn.del.hide();
		this.ui.process.css("width","100%");
		this.ui.percent.text("100%");
		this.ui.msg.text("服务器存在相同文件，快速上传成功。");
		this.State = HttpUploaderState.Complete;
		this.Dispose();
		obj.Manager.fileCur = null;
	};
	this.svr_error = function ()
	{
	    this.ui.msg.text("服务器MD5验证错误").click(function () { alert(HttpUploaderErrorCode["300"]); }).css("cursor", "pointer");
	    this.ui.btn.cancel.text("续传");
	};
	this.svr_create = function (sv)
	{
	    if (sv.value == null)
	    {
	        this.svr_error(); return;
	    }

	    var str = decodeURIComponent(sv.value);//
	    var json = JSON.parse(str);//
	    this.fileSvr = json;
	    this.fid = json.idSvr;
	    this.idSvr = json.idSvr;
	    this.pathSvr = json.pathSvr;
	    //obj.ResetFields();
	    //服务器已存在相同文件，且已上传完成
	    if (json.complete)
	    {
	        this.QuickComplete();
	    } //服务器文件没有上传完成
	    else
	    {
	        this.FileLengthSvr = json.lenSvr;
	        //是文件
	        if (null == this.root)
	        {
	            this.ui.process.css("width", json.perSvr);
	            this.ui.percent.text(json.perSvr);
	        }
	        this.Upload();
	    }
	};
}

//上传错误
function HttpUploader_Error(obj)
{
	obj.ui.msg.text(HttpUploaderErrorCode[obj.ATL.GetErrorCode()]);
	//文件大小超过限制,文件大小为0
	if (10 == obj.ATL.GetErrorCode()
		|| 201 == obj.ATL.GetErrorCode())
	{
		obj.ui.btn.cancel.text("取消");
	}
	else
	{
		obj.ui.btn.cancel.text("续传");
	}
	obj.State = HttpUploaderState.Error;
	obj.Manager.fileCur = null;
}

//上传完成，向服务器传送信息
function HttpUploader_Complete(obj)
{
    obj.event.postComplete(obj);//biz event
	obj.ui.btn.cancel.hide();
	//obj.ui.btn.del.hide();
	obj.ui.process.css("width","100%").attr("title", "上传完成");
	obj.ui.percent.text("100%");
	obj.ui.msg.text("上传完成");
	obj.State = HttpUploaderState.Complete;
	obj.Manager.fileCur = null;

	$.ajax({
	    type: "GET"
		, dataType: 'jsonp'
		, jsonp: "callback" //自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名
		, url: obj.Config["UrlComplete"]
		, data: { md5: obj.MD5, uid: obj.Fields["uid"], fid: obj.fid, time: new Date().getTime() }
		, success:function(msg){}
        , error: function() { alert("向服务器发送Complete信息错误！"); }
        , complete: function(req, sta) { req = null; }
	});
}

//传输进度。频率为每秒调用一次
function HttpUploader_Process(obj, speed, postedLength, percent, times)
{
    obj.lenSvr = obj.ATL.GetFileLenSvr();
	obj.ui.percent.text(percent);
	obj.ui.process.css("width",percent).attr("title", str);
	var str = "已上传:" + postedLength + " 速度:" + speed + "/S 剩余时间:" + times;
	obj.ui.msg.text(str);
}

//服务器连接成功
function HttpUploader_Connected(obj)
{
	obj.ui.msg.text("服务器连接成功");
	obj.ui.process.attr("title", "服务器连接成功");
}

//MD5计算中
function HttpUploader_MD5_Working(obj)
{
	var msg = "正在扫描本地文件，已完成：" + obj.ATL.GetMd5Percent() + "%";
	obj.ui.msg.text(msg);
	obj.ui.process.attr("title",msg);
}

//MD5计算完毕
function HttpUploader_MD5_Complete(obj)
{
    obj.MD5 = obj.ATL.GetMD5();
    obj.event.md5Complete(obj, obj.MD5);//biz event
	//在此处增加服务器验证代码。
	obj.ui.msg.text("MD5计算完毕，开始连接服务器...");

	$.ajax({
	    type: "GET"
		, dataType: 'jsonp'
		, jsonp: "callback" //自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名
		, url: obj.Config["UrlCreate"]
		, data: { md5: obj.MD5, uid: obj.Fields["uid"], fileLength: obj.FileLength, fileSize: obj.FileSize, pathLocal: obj.PathLocal, time: new Date().getTime() }
		, success: function(msg){obj.svr_create(msg);}
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