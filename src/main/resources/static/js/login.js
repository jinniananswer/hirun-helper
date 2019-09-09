layui.define(['ajax', 'form', 'layer'],function(exports){
    var $ = layui.$;
    var form = layui.form;
    var loginer = {
        init : function() {
            form.on('submit(LAY-user-login-submit)', function(data){
                layui.loginer.login(data.field);
                return false;
            });

            $('#password').bind('keyup', function(event) {
                if (event.keyCode == "13") {
                    //回车执行查询
                    $('#lay-submit').trigger("click");
                }
            });

            $('#username').bind('input propertychange', function(event) {
                var username = $("#username").val();
                if (username.length != 11) {
                    return;
                }

                $("#password").focus();
            });
        },

        login : function(formData) {
            layui.ajax.post('/login', formData, function(data) {
                window.location.href = "/";
            });
        }
    };
    exports('loginer', loginer);
});