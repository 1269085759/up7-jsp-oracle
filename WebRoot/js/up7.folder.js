/*
	文件夹上传对象，内部包含多个HttpUploader对象
	参数：
		json 文件夹信息结构体，一个JSON对象。
*/
function FolderUploader(fdLoc, mgr)
{
    var _this = this;
    this.ui = { msg: null, process: null, percent: null, btn: { del: null, cancel: null,stop:null,post:null }, div: null, split: null };
    this.isFolder = true; //是文件夹
    this.folderInit = false;//文件夹已初始化
    this.folderScan = false;//已经扫描
    this.folderSvr = { nameLoc: "",nameSvr:"",lenLoc:0,sizeLoc: "0byte", lenSvr: 0,perSvr:"0%", idSign: "", uid: 0, foldersCount: 0, filesCount: 0, filesComplete: 0, pathLoc: "", pathSvr: "", pathRel: "", pidRoot: 0, complete: false, folders: [], files: [] };
    jQuery.extend(true,this.folderSvr, fdLoc);//续传信息
    this.manager = mgr;
    this.event = mgr.event;
    this.arrFiles = new Array(); //子文件列表(未上传文件列表)，存HttpUploader对象
    this.FileListMgr = mgr.FileListMgr;//文件列表管理器
    this.Config = mgr.Config;
    this.fields = jQuery.extend({}, mgr.Config.Fields);//每一个对象自带一个fields幅本
    this.app = mgr.app;
    this.LocalFile = ""; //判断是否存在相同项
    this.FileName = "";

    //准备
    this.Ready = function ()
    {
        this.ui.msg.text("正在上传队列中等待...");
        this.State = HttpUploaderState.Ready;
    };
    this.svr_create = function ()
    {
        if (this.folderSvr.lenLoc==0)
        {
            this.all_complete();
            return;
        }
        this.ui.btn.stop.show();
        this.ui.btn.cancel.hide();
        this.ui.btn.post.hide();
        this.ui.btn.del.hide();
        this.folderInit = true;
        this.post_fd();
    };
    this.svr_create_err = function ()
    {
        this.folderInit = false;
        this.ui.msg.text("向服务器发送文件夹信息错误").css("cursor", "pointer").click(function ()
        {
            alert(up6_err_solve.errFolderCreate);
        });
        this.ui.btn.post.show();
    };
    this.svr_update = function ()
    {
        var param = jQuery.extend({}, this.fields, { uid: this.folderSvr.uid, sign: this.folderSvr.sign, idSign: this.folderSvr.idSign, lenSvr: this.folderSvr.lenSvr, perSvr: this.folderSvr.perSvr, time: new Date().getTime() });
        $.ajax({
            type: "GET"
            , dataType: 'jsonp'
            , jsonp: "callback" //自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名
            , url: this.Config["UrlFdUpdate"]
            , data: param
            , success: function (sv) { }
            , error: function (req, txt, err) { }
            , complete: function (req, sta) { req = null; }
        });
    };
    this.scan = function ()
    {
        this.ui.btn.stop.hide();
        this.ui.btn.del.hide();
        this.ui.btn.cancel.show();
        this.app.scanFolder(this.folderSvr);
    };
    //上传，创建文件夹结构信息
    this.post = function ()
    {
        if (!this.folderScan) { this.scan(); return; }

        this.ui.btn.stop.show();
        this.ui.btn.del.hide();
        this.ui.btn.cancel.hide();
        this.ui.btn.post.hide();
        this.manager.AppendQueuePost(this.folderSvr.idSign);//添加到队列中
        this.State = HttpUploaderState.Posting;
        //如果文件夹已初始化，表示续传。
        if (this.folderInit)
        {
            this.post_fd();
        }
        else
        {
            if (!this.check_opened()) return;
            //在此处增加服务器验证代码。
            this.ui.msg.text("初始化...");
            var loc_path = encodeURIComponent(this.folderSvr.pathLoc);
            var f_data = jQuery.extend({}, this.fields, {
                nameLoc: this.folderSvr.nameLoc
                , pathLoc: loc_path
                , idSign: this.folderSvr.idSign
                , lenLoc: this.folderSvr.lenLoc
                , sizeLoc: this.folderSvr.sizeLoc
                , filesCount: this.folderSvr.filesCount
                , uid: this.folderSvr.uid
                , time: new Date().getTime()
            });

            $.ajax({
                type: "GET"
                , dataType: 'jsonp'
                , jsonp: "callback" //自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名
                , url: this.Config["UrlFdCreate"]
                , data: f_data
                , success: function (msg)
                {
                    _this.svr_create();
                }
                , error: function (req, txt, err)
                {
                    alert("向服务器发送文件夹信息错误！" + req.responseText);
                    _this.svr_create_err();
                }
                , complete: function (req, sta) { req = null; }

            });
            return;
        }
    };
    this.check_opened = function ()
    {
        if (this.folderSvr.files == null) return false;
        for (var i = 0, l = this.folderSvr.files.length; i < l; ++i)
        {
            var f = this.folderSvr.files[i];
            if (f.opened)
            {
                this.ui.btn.del.show();
                this.ui.btn.stop.hide();
                this.manager.RemoveQueuePost(this.folderSvr.idSign);//从上传队列中删除
                this.ui.msg.text("文件被占用，请关闭后重选文件夹：" + f.pathLoc);
                return false;
            }
        }
        return true;
    };
    this.check_fd = function ()
    {
        this.ui.btn.stop.show();
        this.ui.btn.post.hide();
        this.State = HttpUploaderState.MD5Working;
        this.app.checkFolder(this.folderSvr);
    };
    this.post_fd = function ()
    {
        this.ui.btn.stop.show();
        this.ui.btn.post.hide();
        this.State = HttpUploaderState.Posting;
        this.app.postFolder(jQuery.extend({}, this.folderSvr, { fields: this.fields }));
    };
    this.post_error = function (json)
    {
        this.ui.msg.text("错误数：" + json.errors + " " + json.msg + " " + json.pathLoc);
        //文件大小超过限制,文件大小为0
        //if (4 == json.value || 5 == json.value){}
        //if (6 == json.value){this.ui.msg.text("文件被占用:"+json.pathLoc);}
        //debugMsg(JSON.stringify(json));

        this.ui.btn.stop.hide();
        this.ui.btn.post.show();
        this.ui.btn.del.show();
        
        this.State = HttpUploaderState.Error;
        //从上传列表中删除
        this.manager.RemoveQueuePost(this.folderSvr.idSign);
        //添加到未上传列表
        this.manager.AppendQueueWait(this.folderSvr.idSign);

        this.svr_update();//

        setTimeout(function () { _this.manager.PostNext(); }, 500);
    };
    this.post_stoped = function (json)
    {
        this.ui.msg.text("传输已停止....");
        this.ui.btn.stop.hide();
        this.ui.btn.post.show();
        this.ui.btn.del.show();

        this.State = HttpUploaderState.Stop;
        //从上传列表中删除
        this.manager.RemoveQueuePost(this.folderSvr.idSign);
        //添加到未上传列表
        this.manager.AppendQueueWait(this.folderSvr.idSign);
    };
    this.post_process = function (json)
    {
        if (this.State == HttpUploaderState.Stop) return;
        this.folderSvr.lenSvr = json.lenSvr;
        this.folderSvr.perSvr = json.percent;
        this.ui.percent.text("("+json.percent+")");
        this.ui.process.css("width", json.percent);
        var str = "(" + json.fileCmps + "/" + json.fileCount + ") " + json.lenPost + " " + json.speed + " " + json.time;
        this.ui.msg.text(str);
    };
    this.post_complete = function (json)
    {
        if (!json.all)
        {
            //this.folderSvr.files[json.id_f].complete = true;
            //this.folderSvr.filesComplete = json.compCount;//
            return;
        }

        this.event.fdComplete(this);
        $.each(this.ui.btn, function (i, n)
        {
            n.hide();
        });
        this.ui.process.css("width", "100%");
        this.ui.percent.text("(100%)");
        //obj.pMsg.text("上传完成");
        this.State = HttpUploaderState.Complete;
        this.folderSvr.complete = true;
        this.folderSvr.perSvr = "100%";
        //从上传列表中删除
        this.manager.RemoveQueuePost(this.folderSvr.idSign);
        //从未上传列表中删除
        this.manager.RemoveQueueWait(this.folderSvr.idSign);
        var str = "文件数：" + json.fileCount + "，成功：" + json.compCount;
        if (json.errorCount > 0) str += " 失败：" + json.errorCount
        this.ui.msg.text(str);

        $.ajax({
            type: "GET"
			, dataType: 'jsonp'
			, jsonp: "callback" //自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名
			, url: this.Config["UrlFdComplete"]
			, data: { uid: this.fields["uid"], idSign: this.folderSvr.idSign,time: new Date().getTime() }
			, success: function (msg)
			{
			    //添加到文件列表
			    _this.FileListMgr.UploadComplete(_this.folderSvr);
			    _this.manager.PostNext();
			}
			, error: function (req, txt, err) { alert("向服务器发送文件夹Complete信息错误！" + req.responseText); }
			, complete: function (req, sta) { req = null; }
        });
    };
    this.md5_error = function (json)
    {
        this.ui.btn.post.show();
        this.ui.btn.cancel.hide();
    };
    this.md5_process = function (json)
    {
        if (this.State == HttpUploaderState.Stop) return;
        this.ui.msg.text(json.percent);
    };
    this.md5_complete = function (json)
    {
        //单个文件计算完毕
        if (!json.all)
        {
            this.folderSvr.files[json.id_f].md5 = json.md5;
            return;
        }

        //在此处增加服务器验证代码。
        this.ui.msg.text("初始化...");
		var f_data = jQuery.extend({},this.fields,{folder: encodeURIComponent(JSON.stringify(this.folderSvr)), time: new Date().getTime()});

        $.ajax({
            type: "POST"
            //, dataType: 'jsonp'
            //, jsonp: "callback" //自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名
			, url: this.Config["UrlFdCreate"]
			, data: f_data
			, success: function (msg)
			{
				try
				{
					var json = JSON.parse(decodeURIComponent(msg));
					_this.svr_create(json);
				}
				catch(e)
				{
					_this.post_error({"value":"7"});
				}
			}
			, error: function (req, txt, err)
			{
			    alert("向服务器发送文件夹信息错误！" + req.responseText);
			    _this.svr_create_err();
			}
			, complete: function (req, sta) { req = null; }

        });
    };

    this.scan_process = function (json)
    {
        this.ui.size.text(json.sizeLoc);
        this.ui.msg.text("已扫描:"+json.fileCount);
    };

    this.scan_complete = function (json)
    {
        this.ui.msg.text("扫描完毕，开始上传...");
        jQuery.extend(this.folderSvr, json);
        this.folderScan = true;
        setTimeout(function () {
            _this.post();
        }, 1000);
    };
    
    //所有文件全部上传完成
    this.all_complete = function ()
    {
        this.event.fdComplete(this);
        $.each(this.ui.btn, function (i, n)
        {
            n.hide();
        });
        this.ui.process.css("width", "100%");
        this.ui.percent.text("(100%)");
        this.State = HttpUploaderState.Complete;
        this.folderSvr.complete = true;
        this.folderSvr.perSvr = "100%";
        //从上传列表中删除
        this.manager.RemoveQueuePost(this.folderSvr.idSign);
        //从未上传列表中删除
        this.manager.RemoveQueueWait(this.folderSvr.idSign);
        this.ui.msg.text("共" + this.folderSvr.filesCount + "个文件，成功上传" + this.folderSvr.filesCount + "个文件");

        $.ajax({
            type: "GET"
			, dataType: 'jsonp'
			, jsonp: "callback" //自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名
			, url: this.Config["UrlFdComplete"]
			, data: { uid: this.fields["uid"], id_folder: this.folderSvr.fdID,id_file:this.folderSvr.idFile, time: new Date().getTime() }
			, success: function (msg)
			{
			    //添加到文件列表
			    _this.FileListMgr.UploadComplete(_this.folderSvr);
			    _this.manager.PostNext();
			}
			, error: function (req, txt, err) { alert("向服务器发送文件夹Complete信息错误！" + req.responseText); }
			, complete: function (req, sta) { req = null; }
        });
    };

    //一般在StopAll()中调用
    this.stop_manual = function ()
    {
        this.app.stopFile({ idSign: this.folderSvr.idSign });
        this.State = HttpUploaderState.Stop;
    };
    //手动点击“停止”按钮时
    this.stop = function ()
    {
        this.svr_update();//
        this.ui.btn.post.hide();
        this.ui.btn.stop.hide();
        this.ui.btn.cancel.hide();
        this.State = HttpUploaderState.Stop;
        if (HttpUploaderState.Ready == this.State)
        {
            this.ui.btn.cancel.text("续传").show;
            this.ui.msg.text("传输已停止....");
            this.ui.btn.del.show();
            this.manager.RemoveQueue(this.folderSvr.idSign);
            this.manager.AppendQueueWait(this.folderSvr.idSign);//添加到未上传列表
            this.post_next();
            return;
        }
        //
        this.app.stopFile({ idSign: this.folderSvr.idSign });
        this.manager.RemoveQueuePost(this.folderSvr.idSign);
        this.manager.AppendQueueWait(this.folderSvr.idSign);
    };

    //从上传列表中删除上传任务
    this.remove = function ()
    {
        //清除缓存
        this.app.delFolder({ idSign: this.folderSvr.idSign });
        this.manager.Delete(this.folderSvr.idSign);
        this.ui.div.remove();
        this.ui.split.remove();
    };
}