layui.extend({
    selectEmployee: 'employee'
}).define(['ajax', 'form', 'layer','selectEmployee'], function (exports) {
    var $ = layui.$;
    var form = layui.form;
    var resetPassword = {
        init: function () {

            form.on('submit(resetPassword)', function (data) {
                var filed = data.field;
                layui.resetPassword.resetPassword(filed);
            });

        },

        selectEmployee : function() {
            layui.selectEmployee.init('employeeList', 'employeeSearch', 'employeeId', 'employeeName', false);
        },

        resetPassword :function (data) {
            layui.ajax.post('api/user/user/resetPassword', data);
        }

    };
    exports('resetPassword', resetPassword);
});