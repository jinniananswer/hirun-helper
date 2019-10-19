layui.define(['layer'], function(exports){
    var obj = {
        open : function(url, title) {
            var topLayui = parent === self ? layui : top.layui;
            topLayui.index.openTabsPage(url, title);
        },

        close : function() {
            top.layui.admin.closeThisTabs();
        }
    };
    exports('redirect', obj);
});