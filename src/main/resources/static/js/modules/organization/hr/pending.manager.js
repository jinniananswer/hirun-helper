layui.extend({
    orgTree: 'org'
}).define(['ajax', 'table', 'element', 'layer', 'form', 'select'], function (exports) {
    var $ = layui.$;
    var table = layui.table;
    var layer = layui.layer;
    var form = layui.form;
    var pendingManager = {
        init: function () {

            layui.select.init('pendingType', 'PENDING_TYPE', '', true);

            table.render({
                elem: "#pending_table",
                height: 550,
                url: 'api/organization/hr-pending/queryPendingByExecuteId',
                loading: true,
                defaultToolbar: ['filter'],
                toolbar: '#toolbar',
                parseData: function (res) { //res 即为原始返回的数据
                    return {
                        "code": res.code, //解析接口状态
                        "msg": res.message, //解析提示文本
                        "count": res.rows.total, //解析数据长度
                        "data": res.rows.records //解析数据列表
                    };
                },
                cols: [
                    [
                        {field: 'employeeName', title: '姓名', width: 120, fixed: 'left', align: 'center'},
                        {
                            field: 'pendingType', title: '待办类型', align: 'center', width: 120, templet: function (d) {
                                if (d.pendingType == 1) {
                                    return '借调';
                                } else if (d.pendingType == 2) {
                                    return '调出';
                                }else if (d.pendingType == 3) {
                                    return '员工借调归还确认';
                                }
                            }
                        },
                        {field: 'startTime', title: '开始时间', align: 'center', width: 200},
                        {field: 'endTime', title: '结束时间', width: 200, align: 'center'},
                        {
                            field: 'pendingStatus',
                            align: 'center',
                            title: '待办状态',
                            sort: true,
                            width: 100,
                            templet: function (d) {
                                if (d.pendingStatus == 1) {
                                    return '<span style="color:#FF4500;">' + '未处理' + '</span>';
                                } else if (d.pendingStatus == 2) {
                                    return '<span style="color:#008000;">' + '已处理' + '</span>';
                                } else if (d.pendingStatus == 3) {
                                    return '<span style="color:#c00;">' + '删除' + '</span>';
                                }
                            }
                        },
                        {field: 'pendingCreateName', title: '创建人', width: 150, align: 'center'},
                        {field: 'createTime', title: '创建时间', width: 200, align: 'center'},
                        {field: 'pendingExecuteName', title: '执行人', width: 150, align: 'center'},
                        {field: 'remark', title: '备注', width: 150, align: 'center'},
                        {align: 'center', title: '操作', width: 150, fixed: 'right', templet: '#operateTmp'}
                    ]
                ],
                page: true,
                text: {
                    none: '暂无相关数据，请检查查询条件。'
                }
            });


            $('#queryPending').on('click', function () {
                table.reload('pending_table', {
                    page: {
                        curr: 1
                    },
                    where: {
                        pendingType: $("select[name='pendingType']").val(),
                        pendingStatus: $("select[name='pendingStatus']").val(),
                    }
                })
            });

            table.on('tool(pending_table)', function (obj) {
                var data = obj.data;//获取当前行数据
                var layEvent = obj.event;
                if (layEvent === 'operate') {
                    pendingManager.operate(data);
                } else if (layEvent === 'delete') {
                    pendingManager.delete(data);
                } else if (layEvent === 'detail') {
                    pendingManager.detail(data);
                }else if (layEvent === 'confirmReturn') {
                    pendingManager.confirmReturn(data);
                }
            });

        },

        operate: function (data) {
            var index = layer.open({
                type: 2,
                title: '员工调动确认',
                content: 'openUrl?url=modules/organization/hr/transorg_pending_confirm',
                maxmin: true,
                btn: ['确定', '取消'],
                area: ['550px', '700px'],
                skin: 'layui-layer-molv',
                success: function (layero, index) {
                    var body = layer.getChildFrame('body', index);
                    body.find('#employeeId').val(data.employeeId);
                    body.find('#name').val(data.employeeName);
                    body.find('#id').val(data.id);
                    body.find('#startTime').val(data.startTime);
                    body.find('#endTime').val(data.endTime);
                    body.find('#pendingTypeValue').val(data.pendingType);
                    form.render();
                },
                yes: function (index, layero) {
                    var body = layer.getChildFrame('body', index);
                    var submit = body.find("#confirm-submit");
                    submit.click();
                }
            });
            layer.full(index);
        },

        delete: function (data) {
            layer.confirm('是否确定删除',
                {btn: ['确定', '取消']}, function (index, layero) {
                    $.ajax({
                        url: 'api/organization/hr-pending/deleteHrPending',
                        type: 'POST',
                        data: 'id=' + data.id,
                        success: function (data) {
                            if (data.code == 0) {
                                layer.confirm('操作成功，点击确定按钮刷新本页面，点击关闭按钮关闭本界面？', {
                                    btn: ['确定', '关闭'],
                                    closeBtn: 0,
                                    icon: 6,
                                    title: '提示信息',
                                    shade: [0.5, '#fff'],
                                    skin: 'layui-layer-admin layui-anim'
                                }, function (index) {
                                    table.reload('pending_table'); //重载表格
                                    layer.close(index);
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

        detail: function (data) {
            var index = layer.open({
                type: 2,
                title: '员工调动详情',
                content: 'openUrl?url=modules/organization/hr/transorg_pending_detail',
                maxmin: true,
                area: ['550px', '700px'],
                skin: 'layui-layer-molv',
                success: function (layero, index) {
                    var body = layer.getChildFrame('body', index);
                    body.find('#employeeName').val(data.employeeName);
                    body.find('#id').val(data.id);
                    form.render();
                },
            });
            layer.full(index);
        },

        confirmReturn: function (data) {
            var index = layer.open({
                type: 2,
                title: '员工归还确认',
                content: 'openUrl?url=modules/organization/hr/transorg_return_confirm',
                maxmin: true,
                btn: ['确定', '取消'],
                area: ['550px', '700px'],
                skin: 'layui-layer-molv',
                success: function (layero, index) {
                    var body = layer.getChildFrame('body', index);
                    body.find('#name').val(data.employeeName);
                    body.find('#relPendingId').val(data.id);
                    form.render();
                },
                yes: function (index, layero) {
                    var body = layer.getChildFrame('body', index);
                    var submit = body.find("#confirm-submit");
                    submit.click();
                }
            });
            layer.full(index);
        },

    };
    exports('pendingManager', pendingManager);
});