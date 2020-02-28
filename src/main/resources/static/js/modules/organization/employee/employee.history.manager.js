layui.extend({
    orgTree: 'org',
    selectEmployee: 'employee'
}).define(['ajax', 'table', 'element', 'layer', 'form', 'select','selectEmployee','orgTree'], function (exports) {
    let $ = layui.$;
    let table = layui.table;
    let form = layui.form;
    let employeeHistoryManager = {
        init: function () {


            table.render({
                elem: "#employeeHistoryTable",
                height: 550,
                loading:false,
                defaultToolbar: ['filter'],
                toolbar: '#operateBar',
                parseData: function (res) { //res 即为原始返回的数据
                    return {
                        "code": res.code, //解析接口状态
                        "msg": res.message, //解析提示文本
                        "count": res.total, //解析数据长度
                        "data": res.rows //解析数据列表
                    };
                },
                cols: [
                    [
                        {type: 'radio'},
                        {field: 'id', title: '编号', width: 150,align: 'center'},
                        {field: 'eventDate', title: '事件时间', width: 150,align: 'center'},
                        {field: 'eventContent', title: '事件内容', align: 'center'},
                    ]
                ],
                page: true,
                text: {
                    none: '暂无相关数据，请检查查询条件。'
                }
            });


            $('#queryOrgHrRel').on('click', function () {
                let id=$('#employeeId').val();
                if(id==''){
                    layer.msg('请选择员工再查询');
                    return;
                }
                table.reload('employeeHistoryTable', {
                    page: {
                        curr: 1
                    },
                    url: 'api/organization/employee-history/queryEmployeeHistory',
                    where: {
                        employeeId: $("input[name='employeeId']").val(),
                    }
                })
            });

            table.on('toolbar(employeeHistoryTable)', function (obj) {
                let checkStatus = table.checkStatus(obj.config.id); //获取选中行状态
                let data = checkStatus.data;
                let event = obj.event;

                if (data.length <= 0) {
                    layer.msg('请选中一条数据，再进行操作。');
                    return;
                }

                switch (event) {
                    case 'update':

                        employeeHistoryManager.update(data);
                        break;

                    case 'delete':
                        employeeHistoryManager.delete(data);
                        break;
                }
                ;
            });

        },

        selectEmployee : function() {
            layui.selectEmployee.init('employeeList', 'employeeSearch', 'employeeId', 'employeeName', false);
        },


        update: function (data) {
            let index = layer.open({
                type: 2,
                title: '修改鸿扬工作经历',
                content: 'openUrl?url=modules/organization/employee/update_employee_history',
                maxmin: true,
                btn: ['确定', '取消'],
                area: ['550px', '700px'],
                skin: 'layui-layer-molv',
                success: function (layero, index) {
                    let body = layer.getChildFrame('body', index);
                    console.log(data[0].id,data[0].eventContent);
                    body.find('#id').val(data[0].id);
                    body.find('#eventDate').val(data[0].eventDate);
                    body.find('#eventContent').val(data[0].eventContent);
                    form.render();
                },
                yes: function (index, layero) {
                    let body = layer.getChildFrame('body', index);
                    let submit = body.find("#confirm-submit");
                    submit.click();
                }
            });
            layer.full(index);
        },


        delete: function (data) {
            layer.confirm('是否确定删除',
                {btn: ['确定', '取消']}, function (index, layero) {
                    $.ajax({
                        url: 'api/organization/employee-history/deleteEmployeeHistory',
                        type: 'POST',
                        data: 'id=' + data[0].id,
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
                                    table.reload('employeeHistoryTable'); //重载表格
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

    };
    exports('employeeHistoryManager', employeeHistoryManager);
});