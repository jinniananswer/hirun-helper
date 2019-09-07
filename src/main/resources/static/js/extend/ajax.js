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
            if(successFunc == null || typeof(successFunc) == "undefined" || typeof(successFunc) != "function") {
                successMethod = function(data) {
                    layer.closeAll('loading');

                    layer.confirm('操作成功，点击确定按钮刷新本页面，点击关闭按钮关闭本界面？', {
                        btn : ['确定','关闭'],
                        closeBtn : 0,
                        icon : 6,
                        title : '提示信息',
                        skin : 'layui-layer-admin layui-anim'
                    }, function(){
                        document.location.reload();
                    }, function(){
                        top.layui.admin.closeThisTabs();
                    });
                }
            }
            else {

                successMethod = function(data) {
                    layer.closeAll('loading');
                    successFunc(data);
                }
            }


            if(errFunc == null || typeof(errFunc) == "undefined" || typeof(errFunc) != "function") {
                errFunc = function(data){
                    layer.closeAll('loading');
                    layer.alert("对不起，偶们的系统出错了，55555555555555,亲，赶紧联系管理员报告功能问题吧");
                };
            }
            layer.load();
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
            )
        }
    };
    exports('ajax', obj);
});