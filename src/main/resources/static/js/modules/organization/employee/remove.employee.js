layui.extend({}).define(['ajax', 'form', 'layer', 'element', 'laydate'], function (exports) {
    var $ = layui.$;
    var form = layui.form;
    var layer = layui.layer;
    var laydate = layui.laydate;
    var employeequit = {
        init: function () {

            laydate.render({
                elem: '#remove-date',
            });

            laydate.render({
                elem: '#social-stop-date',
            });

            form.on('submit(employee-remove-submit)', function (data) {
                var field = data.field; //获取提交的字段
                var index = parent.layer.getFrameIndex(window.name);
                $.ajax({
                    url: '/api/organization/employee/removeEmployee',
                    type: 'POST',
                    data: field,
                    success: function (data) {
                        if (data.code == 0) {
                                layer.msg("提交成功", {icon: 6,time :3000},function () {
                                parent.layui.table.reload('employee_table'); //重载表格
                                parent.layer.close(index); //再执行关闭
                            });
                        } else {
                            parent.layer.msg("提交失败", {icon: 5});
                        }
                    }
                });
            });
        },

    };
    exports('employeeremove', employeequit);
});