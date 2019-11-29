layui.extend({}).define(['ajax', 'table', 'element', 'select', 'layer', 'form'], function (exports) {
    var $ = layui.$;
    var table = layui.table;
    var layer = layui.layer;
    var form = layui.form;
    var employeeContractExtManager = {
        init: function () {
            table.render({
                elem: "#employeeContractTable",
                height: 500,
                url: 'api/organization/employee-contract/queryContractByParentId?parentContractId=' + $("#parentContractId").val(),
                loading: true,
                defaultToolbar: ['filter'],
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
                        {field: 'contractType',fixed:'left', title: '变更类型', align: 'center', width: 120, sort: true, templet: function (d) {
                                if (d.contractType == 6) {
                                    return '时间变更';
                                } else if (d.contractType == 7) {
                                    return '岗位变更';
                                } else if (d.contractType == 8) {
                                    return '地点变更';
                                }else if (d.contractType == 9) {
                                    return '试用期变更';
                                }else if (d.contractType == 10) {
                                    return '其他变更';
                                };
                            }
                        },
                        {field: 'contractSignTime', title: '签订时间', width: 200, align: 'center'},
                        {field: 'contractStartTime', title: '开始时间', width: 200, align: 'center'},
                        {field: 'contractEndTime', title: '结束时间', width: 200, align: 'center'},
                        {field: 'status', title: '状态', sort: true, width: 100, align: 'center', templet: function (d) {
                                if (d.status == 1) {
                                    return '<span style="color:#008000;">' + '正常' + '</span>';
                                } else if (d.status == 2) {
                                    return '<span style="color:#c00;">' + '终止' + '</span>';
                                }
                            }
                        },
                        {field: 'probation', title: '试用期', width: 100, align: 'center',templet: function (d) {
                                if (d.probation != null && d.probation != '') {
                                    return d.probation + "个月";
                                }else{
                                    return '';
                                }
                            }},
                        {field: 'registerNo', title: '房产编号', width: 100, align: 'center'},
                        {field: 'propertyNo', title: '户籍编号', width: 100, align: 'center'},
                        {field: 'remark', title: '备注', width: 300, align: 'center'},
                        {align: 'center', title: '操作',width: 300, fixed: 'right', templet: '#operateTmp'}
                    ]
                ],
                page: true,
                text: {
                    none: '暂无员工合同变更相关数据。'
                }
            });


            table.reload('employeeContractTable', {
                page: {
                    curr: 1
                },
                where: {
                    employeeId: $("input[name='parentContractId']").val(),
                }
            });

            table.on('tool(employeeContractTable)', function (obj) {
                var data = obj.data;//获取当前行数据
                var layEvent = obj.event;
                if (layEvent === 'edit') {
                    employeeContractExtManager.edit(data);
                } else if (layEvent === 'delete') {
                    employeeContractExtManager.delete(data);
                } else if (layEvent === 'stop') {
                    employeeContractExtManager.stopContract(data);
                }
            });
        },

        edit: function (data) {
            var index = layer.open({
                type: 2,
                title: '修改调动申请',
                content: 'openUrl?url=modules/organization/contract/update_employee_contract_ext',
                maxmin: true,
                btn: ['确定', '取消'],
                area: ['550px', '700px'],
                skin: 'layui-layer-molv',
                success: function (layero, index) {
                    var body = layer.getChildFrame('body', index);
                    body.find('#employeeId').val(data.employeeId);
                    body.find('#contractStartTime').val(data.contractStartTime.substr(0,10));
                    body.find('#contractEndTime').val(data.contractEndTime.substr(0,10));
                    body.find('#contractSignTime').val(data.contractSignTime.substr(0,10));
                    body.find('#remark').val(data.remark);
                    body.find('#contractType').val(data.contractType);
                    body.find('#id').val(data.id);
                    body.find('#jobRole').val(data.jobRole);
                    body.find('#name').val($('#name').val());
                    body.find('#registerNo').val(data.registerNo);
                    body.find('#propertyNo').val(data.propertyNo);
                    body.find('#contractTypeValue').val(data.contractType);
                    body.find('#probationValue').val(data.probation);
                    body.find('#parentContractId').val(data.parentContractId);
                    form.render();
                },
                yes: function (index, layero) {
                    var body = layer.getChildFrame('body', index);
                    var submit = body.find("#update-employeeContract-submit");
                    submit.click();
                }
            });
            layer.full(index);
        },


        delete: function (data) {
            layer.confirm('是否确定删除',
                {btn: ['确定', '取消']}, function (index, layero) {
                    $.ajax({
                        url: 'api/organization/employee-contract/deleteEmployeeContract',
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
                                    table.reload('employeeContractTable'); //重载表格
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
            var index = layer.open({
                type: 2,
                title: '新增合同变更协议',
                content: 'openUrl?url=modules/organization/contract/create_employee_contract_ext',
                maxmin: true,
                btn: ['保存', '取消'],
                skin: 'layui-layer-molv',
                success: function (layero, index) {
                    var employee_id = $("#employee_id").val();
                    var name = $("#name").val();
                    var jobRole = $("#jobRole").val();
                    var parentContractId = $("#parentContractId").val();

                    var body = layer.getChildFrame('body', index);
                    body.find('#employeeId').val(employee_id);
                    body.find('#name').val(name);
                    body.find('#jobRole').val(jobRole);
                    body.find('#parentContractId').val(parentContractId);

                    form.render();
                },
                yes: function (index, layero) {
                    var body = layer.getChildFrame('body', index);
                    var submit = body.find("#create-employeeContract-submit");
                    submit.click();
                }
            });
            layer.full(index);
        },

        stopContract: function (data) {
            layer.open({
                type: 2,
                title: '协议终止',
                content: 'openUrl?url=modules/organization/contract/stop_employee_contract_ext',
                maxmin: true,
                btn: ['确定', '取消'],
                area: ['550px', '700px'],
                skin: 'layui-layer-molv',
                success: function (layero, index) {
                    var body = layer.getChildFrame('body', index);
                    body.find('#id').val(data.id);
                    body.find('#remark').val(data.remark);
                    form.render();
                },
                yes: function (index, layero) {
                    var body = layer.getChildFrame('body', index);
                    var submit = body.find("#stop-employeeContract-submit");
                    submit.click();
                }
            });
        },

    };
    exports('employeeContractExtManager', employeeContractExtManager);
});