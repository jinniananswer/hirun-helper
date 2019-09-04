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
            layui.ajax.post('/api/hr/staff/save', formData);
        }
    };
    exports('loginer', loginer);
});