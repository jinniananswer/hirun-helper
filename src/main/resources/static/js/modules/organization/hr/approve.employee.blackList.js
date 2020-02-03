layui.extend({
}).define(['ajax', 'table', 'element', 'layer', 'form', 'select'], function (exports) {
    var $ = layui.$;
    var table = layui.table;
    var form = layui.form;
    var approveEmployeeBlackList = {
        init: function () {
            form.on('submit(confirm-submit)', function (data) {
                var field = data.field; //获取提交的字段
                var index = parent.layer.getFrameIndex(window.name);
                $.ajax({
                    url: 'api/organization/hr-pending/approveEmployeeBlackListPending',
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
    exports('approveEmployeeBlackList', approveEmployeeBlackList);
});