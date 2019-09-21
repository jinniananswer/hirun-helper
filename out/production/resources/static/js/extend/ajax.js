layui.define(['layer', 'jquery'], function(exports){
    var $ = layui.$;
    var layer = layui.layer;
    var obj = {
        get : function(url, param, successFunc, errFunc) {
            this.execute(url, param, 'GET', successFunc, errFunc, false);
        },

        post : function(url, param, successFunc, errFunc) {
            this.execute(url, param, 'POST', successFunc, errFunc, false);
        },

        getAsync : function(url, param ,successFunc, errFunc) {
            this.execute(url, param, 'GET', successFunc, errFunc, true);
        },

        postAsync : function(url, param, successFunc, errFunc) {
            this.execute(url, param, 'POST', successFunc, errFunc, true);
        },

        execute : function(url, param, type, successFunc, errFunc, async) {
            var successMethod = null;

            successMethod = function(data) {
                var code = data.code;
                layer.closeAll('loading');
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
            }

            if(errFunc == null || typeof(errFunc) == "undefined" || typeof(errFunc) != "function") {
                errFunc = function(data){
                    layer.closeAll('loading');
                    layer.open({
                        type : 1,
                        skin : 'layui-layer-red', //样式类名
                        closeBtn: 1, //不显示关闭按钮
                        anim : 2,
                        shadeClose : true, //开启遮罩关闭
                        content : "对不起，55555555555，偶们的系统出错了，赶紧联系管理员吧"
                    });
                };
            }
            layer.load(1,{
                shade: [0.3,'#fff'] //0.1透明度的白色背景
            });
            $.ajax(
                {
                    url:url,
                    data: param,
                    type: type,
                    dataType: 'json',
                    async: async,
                    timeout: 5000,
                    success: successMethod,
                    error: errFunc
                }
            );

        }
    };
    exports('ajax', obj);
});