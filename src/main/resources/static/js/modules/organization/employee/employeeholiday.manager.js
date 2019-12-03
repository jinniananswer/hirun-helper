layui.extend({

}).define(['ajax', 'table', 'element', 'select', 'layer', 'form'], function (exports) {
    var $ = layui.$;
    var table = layui.table;
    var layer = layui.layer;
    var form = layui.form;
    var employeeholiday = {
        init: function () {
            table.render({
                elem: "#employeeholiday_table",
                height: 500,
                url: 'api/organization/employee-holiday/queryEmployeeHoliday?employeeId=' + $("#employee_id").val(),
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
                        {
                            field: 'holidayType', title: '休假类型', width: 120, sort: true, templet: function (d) {
                                if (d.holidayType == 1) {
                                    return '产假';
                                } else if (d.holidayType == 2) {
                                    return '事假';
                                } else if (d.holidayType == 3) {
                                    return '病假';
                                } else if (d.holidayType == 4) {
                                    return '丧假';
                                } else if (d.holidayType == 5) {
                                    return '年假';
                                }else{
                                    return '未知';
                                }
                                ;
                            }
                        },
                        {
                            field: 'startTime', title: '开始时间', width: 200, sort: true
                        },
                        {
                            field: 'endTime', title: '结束时间', width: 200, sort: true
                        },
                        {
                            field: 'isSurrenderInsurance', title: '缴纳请假期间保险', width: 150, templet: function (d) {
                                if (d.isSurrenderInsurance == 0) {
                                    return '否';
                                } else {
                                    return '是';
                                }
                                ;
                            }
                        },
                        {field: 'createTime', title: '创建时间', width: 200, sort: true},
                        {field: 'remark', title: '备注', width: 300},
                        {align: 'center', title: '操作', fixed: 'right', toolbar: '#bar'}
                    ]
                ],
                page: true,
                text: {
                    none: '暂无员工休假相关数据。'
                }
            });


            table.reload('employeeholiday_table', {
                page: {
                    curr: 1
                },
                where: {
                    employeeId: $("input[name='employeeId']").val(),
                }
            });

            table.on('tool(employeeholiday_table)', function (obj) {
                var data = obj.data;//获取当前行数据
                var layEvent = obj.event;
                if (layEvent === 'edit') {
                    employeeholiday.edit(data);
                } else if (layEvent === 'delete') {
                    employeeholiday.delete(data);
                }
            });
        },

        edit: function (data) {
            var index=layer.open({
                type: 2,
                title: '休假信息修改',
                content: 'openUrl?url=modules/organization/employee/update_employeeholiday',
                maxmin: true,
                btn: ['确定', '取消'],
                area: ['550px', '700px'],
                skin: 'layui-layer-molv',
                success: function (layero, index) {
                    var body = layer.getChildFrame('body', index);
                    var employee_id = $("#employee_id").val();
                    var name = $("#name").val();
                    body.find('#employeeId').val(employee_id);
                    body.find('#name').val(name);
                    body.find('#startTime').val(data.startTime);
                    body.find('#endTime').val(data.endTime);
                    body.find('#remark').val(data.remark);
                    body.find('#isSurrenderInsurance').val(data.remark);
                    body.find('#holidayTypeValue').val(data.holidayType);
                    body.find('#id').val(data.id);
                    form.render();
                },
                yes: function (index, layero) {
                    var body = layer.getChildFrame('body', index);
                    var submit = body.find("#update-employeeholiday-submit");
                    submit.click();
                }
            });
            layer.full(index);
        },

        delete: function (data) {
            layer.confirm('是否确定删除',
                {btn: ['确定', '取消']},function (index,layero) {
                    var param = 'employeeId=' + data.employeeId + '&startTime=' + data.startTime + '&id=' + data.id;
                    $.ajax({
                        url: 'api/organization/employee-holiday/deleteEmployeeHoliday',
                        type: 'POST',
                        data: param,
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
                                    table.reload('employeeholiday_table'); //重载表格
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

        add :function () {
           var index= layer.open({
                type: 2,
                title: '员工休假新增',
                content: 'openUrl?url=modules/organization/employee/create_employeeholiday',
                maxmin: true,
                btn: ['保存', '取消'],
                area: ['550px', '700px'],
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
                    var submit = body.find("#add-employeeholiday-submit");
                    submit.click();
                }
            });
           layer.full(index);
        },

    };
    exports('employeeholiday', employeeholiday);
});