layui.extend({
}).define(['ajax', 'form', 'layer', 'element','laydate'], function (exports) {
    let $ = layui.$;
    let form = layui.form;
    let layer = layui.layer;
    var laydate = layui.laydate;

    let updateEmployeeHistory = {
        init: function () {

            laydate.render({
                elem: '#eventDate',
                type: 'date',
            });

            form.on('submit(confirm-submit)', function (data) {
                let field = data.field; //获取提交的字段
                let index = parent.layer.getFrameIndex(window.name);
                $.ajax({
                    url: 'api/organization/employee-history/updateEmployeeHistory',
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
                                parent.layui.table.reload('employeeHistoryTable'); //重载表格
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
    exports('updateEmployeeHistory', updateEmployeeHistory);
});