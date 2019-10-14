layui.define(['layer', 'jquery'], function(exports){
    var $ = layui.$;
    var layer = layui.layer;
    var obj = {
        get : function(url, param, successFunc, errFunc, needLoad) {
            this.execute(url, param, 'GET', successFunc, errFunc, true, needLoad);
        },

        post : function(url, param, successFunc, errFunc, needLoad) {
            this.execute(url, param, 'POST', successFunc, errFunc, true, needLoad);
        },

        getSync : function(url, param ,successFunc, errFunc, needLoad) {
            //注意，同步方式load不起作用
            this.execute(url, param, 'GET', successFunc, errFunc, false, needLoad);
        },

        postSync : function(url, param, successFunc, errFunc, needLoad) {
            //注意，同步方式load不起作用
            this.execute(url, param, 'POST', successFunc, errFunc, false, needLoad);
        },

        execute : function(url, param, type, successFunc, errFunc, async, needLoad) {
            if (needLoad == null || typeof(needLoad) == "undefined") {
                needLoad = true;
            }
            var successMethod = null;

            successMethod = function(data) {
                var code = data.code;
                if (code != '0') {
                    //后端有报错
                    layer.msg(data.code + ": " + data.message,{
                        time : -1,
                        closeBtn : 1, //显示关闭按钮
                        anim : 2,
                        shadeClose: false, //开启遮罩关闭
                        shade : [0.5, '#fff']
                    });
                } else if (successFunc == null || typeof(successFunc) == "undefined" || typeof(successFunc) != "function") {
                    layer.confirm('操作成功，点击确定按钮刷新本页面，点击关闭按钮关闭本界面？', {
                        btn : ['确定','关闭'],
                        closeBtn : 0,
                        icon : 6,
                        title : '提示信息',
                        shade : [0.5, '#fff'],
                        skin : 'layui-layer-admin layui-anim'
                    }, function(){
                        document.location.reload();
                    }, function(){
                        top.layui.admin.closeThisTabs();
                    });
                } else {
                    successFunc(data);
                }
                layer.closeAll('loading');
            }

            if(errFunc == null || typeof(errFunc) == "undefined" || typeof(errFunc) != "function") {
                errFunc = function(data){
                    layer.closeAll('loading');
                    layer.alert('对不起，55555555555，偶们的系统出错了，'+data.statusText+'，赶紧联系管理员吧', {
                        skin: 'layui-layer-molv'
                        ,closeBtn: 0
                    });
                };
            }

            if (needLoad) {
                layer.load(1,{
                    shade: [0.3,'#fff'] //0.1透明度的白色背景
                });
            }

            $.ajax(
                {
                    url:url,
                    data: param,
                    type: type,
                    dataType: 'json',
                    async: async,
                    success: successMethod,
                    error: errFunc
                }
            );

        }
    };
    exports('ajax', obj);
});