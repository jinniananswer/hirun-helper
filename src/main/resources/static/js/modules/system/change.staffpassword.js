layui.define(['ajax', 'form', 'layer'], function (exports) {
    var $ = layui.$;
    var form = layui.form;
    var changestaffpwd = {
        init: function () {

            form.verify({
                passwordlength: function (password) {
                    if (password.length != 6) {
                        return '密码长度必须为6';
                    }
                },
            });

            form.on('submit(changeStaffPassword)', function (data) {
                var filed = data.field;
                if (filed.password !== filed.repassword) {
                    return layer.msg("两次密码输入不一致");
                }

                layui.changestaffpwd.changestaffpwd(data.field);

            });

        },

        changestaffpwd: function (formData) {
            layui.ajax.post('/api/user/user/changeStaffPassword', formData, function (data) {
                layer.confirm('密码修改成功。点击确定返回页面。', {
                    btn: ['确定']
                }, function () {
                    document.location.reload();
                },);
            });
        }
    };
    exports('changestaffpwd', changestaffpwd);
});