layui.define(function(exports){
    var obj = {
        open : function(url) {

        },

        close : function() {
            top.layui.admin.closeThisTabs();
        }
    };
    exports('redirectTo', obj);
});