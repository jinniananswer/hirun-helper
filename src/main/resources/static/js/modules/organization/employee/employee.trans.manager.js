layui.extend({}).define(['ajax', 'table', 'element', 'select', 'layer', 'form'], function (exports) {
    var $ = layui.$;
    var table = layui.table;
    var layer = layui.layer;
    var form = layui.form;
    var employeeTransManager = {
        init: function () {
            table.render({
                elem: "#employeeTransTable",
                height: 500,
                url: 'api/organization/hr-pending/queryTransPendingByEmployeeId?employeeId=' + $("#employee_id").val(),
                loading: true,
                toolbar: '#toolbar',
                parseData: function (res) { //res 即为原始返回的数据
                    return {
                        "code": res.code, //解析接口状态
                        "msg": res.message, //解析提示文
                        "count": res.rows.total, //解析数据长度
                        "data": res.rows.records //解析数据列表
                    };
                },
                cols: [
                    [
                        {field: 'pendingType', title: '调动类型', width: 120, sort: true,templet: function (d) {
                                if (d.pendingType == 1) {
                                    return '借调';
                                } else if (d.pendingType == 2) {
                                    return '调出';
                                };
                            }},
                        {field: 'startTime', title: '开始时间', width: 200},
                        {field: 'endTime', title: '结束时间', width: 200},
                        {field: 'pendingStatus', title: '状态',sort: true, width: 100,style:'color :red',templet:function (d) {
                                if (d.pendingStatus == 1) {
                                    return '未处理';
                                } else if (d.pendingStatus == 2) {
                                    return '已处理';
                                }else if (d.pendingStatus == 3) {
                                    return '删除';
                                };
                            }},
                        {field: 'pendingCreateName', title: '创建员工', width: 100},
                        {field: 'pendingExecuteName', title: '处理员工', width: 100},
                        {field: 'remark', title: '备注', width: 300},
                        {align: 'center', title: '操作', fixed: 'right',templet:'#operateTmp'}
                    ]
                ],
                page: true,
                text: {
                    none: '暂无员工调动相关数据。'
                }
            });


            table.reload('employeeTrans_table', {
                page: {
                    curr: 1
                },
                where: {
                    employeeId: $("input[name='employeeId']").val(),
                }
            });

            table.on('tool(employeeTransTable)', function (obj) {
                var data = obj.data;//获取当前行数据
                var layEvent = obj.event;
                if (layEvent === 'edit') {
                    employeeTransManager.edit(data);
                } else if (layEvent === 'delete') {
                    employeeTransManager.delete(data);
                }
            });
        },

        edit: function (data) {
            var index=layer.open({
                type: 2,
                title: '修改调动申请',
                content: 'openUrl?url=/modules/organization/employee/update_employee_trans',
                maxmin: true,
                btn: ['确定', '取消'],
                area: ['550px', '700px'],
                skin: 'layui-layer-molv',
                success: function (layero, index) {
                    var body = layer.getChildFrame('body', index);
                    var employee_id = $("#employee_id").val();
                    body.find('#employeeId').val(employee_id);
                    body.find('#startTime').val(data.startTime);
                    body.find('#endTime').val(data.endTime);
                    body.find('#remark').val(data.remark);
                    body.find('#pendingExecuteId').val(data.pendingExecuteId);
                    body.find('#id').val(data.id);
                    body.find('#pendingTypeValue').val(data.pendingType);
                    body.find('#pendExecuteName').val(data.pendingExecuteName);
                    form.render();
                },
                yes: function (index, layero) {
                    var body = layer.getChildFrame('body', index);
                    var submit = body.find("#update-employeetrans-submit");
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
                        data: 'id='+data.id,
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
                                    table.reload('employeeTransTable'); //重载表格
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

        add: function () {
            var index=layer.open({
                type: 2,
                title: '调动申请新增',
                content: 'openUrl?url=/modules/organization/employee/create_employee_trans',
                maxmin: true,
                btn: ['保存', '取消'],
                skin: 'layui-layer-molv',
                success: function (layero, index) {
                    var employee_id = $("#employee_id").val();
                    var name = $("#name").val();
                    var body = layer.getChildFrame('body', index);
                    body.find('#employeeId').val(employee_id);
                    body.find('#name').val(name);
                    form.render();
                },
                yes: function (index, layero) {
                    var body = layer.getChildFrame('body', index);
                    var submit = body.find("#add-employeetrans-submit");
                    submit.click();
                }
            });
            layer.full(index);
        },

    };
    exports('employeeTransManager', employeeTransManager);
});