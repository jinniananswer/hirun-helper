layui.extend({
}).define(['ajax', 'table', 'element', 'layer', 'form', 'select','laydate'], function (exports) {
    var $ = layui.$;
    var table = layui.table;
    var form = layui.form;
    let laydate = layui.laydate;

    var updateEmployeeRegularDate = {
        init: function () {

            laydate.render({
                elem: '#inDate',
            });

            laydate.render({
                elem: '#regularDate',
            });

            laydate.render({
                elem: '#sourceRegularDate',
            });

            layui.ajax.get('api/organization/hr-pending/queryEmployeeInfo4UpdateRegularDate', 'employeeId=' + $('#employeeId').val(), function (data) {
                var employee = data.rows;
                if (employee == null) {
                    return;
                }

                let inDate = employee.inDate;
                let regularDate = employee.regularDate;
                if (inDate != null) {
                    $("#inDate").val(inDate.substring(0, 10));
                }
                if (regularDate != null) {
                    $("#sourceRegularDate").val(regularDate.substring(0, 10));
                }

                $("#name").val(employee.name);

            });

            form.on('submit(confirm-submit)', function (data) {
                var field = data.field; //获取提交的字段
                var index = parent.layer.getFrameIndex(window.name);
                $.ajax({
                    url: 'api/organization/hr-pending/updateEmployeeRegularDate',
                    type: 'POST',
                    data: field,
                    success: function (data) {
                        if (data.code == 0) {
                            layer.confirm('操作成功，点击确定按钮刷新本页面，点击关闭按钮关闭本界面？', {
                                btn: ['确定', '关闭'],
                                closeBtn: 0,
                                icon: 6,
                                title: '提示信息',
                                shade: [0.5, '#fff'],
                                skin: 'layui-layer-admin layui-anim'
                            }, function () {
                                parent.layui.table.reload('pending_table'); //重载表格
                                parent.layer.close(index); //再执行关闭
                            }, function () {
                                top.layui.admin.closeThisTabs();
                            });
                        } else {
                            parent.layer.msg("提交失败", {icon: 5});
                        }
                    }
                });
            });
        },

    };
    exports('updateEmployeeRegularDate', updateEmployeeRegularDate);
});