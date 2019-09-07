layui.define(['ajax', 'form', 'layer'],function(exports){
    var $ = layui.$;
    var form = layui.form;
    var loginer = {
        init : function() {
            form.on('submit(LAY-user-login-submit)', function(data){
                layui.loginer.login(data.field);
                return false;
            });
        },
        login : function(formData) {
            layui.ajax.post('/api/user/user/login', formData);
        }
    };
    exports('loginer', loginer);
});