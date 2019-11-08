layui.extend({}).define(['ajax', 'table', 'element', 'select', 'layer', 'form','redirect'], function (exports) {
    var $ = layui.$;
    var table = layui.table;
    var layer = layui.layer;
    var form = layui.form;
    var employeeContractManager = {
        init: function () {
            table.render({
                elem: "#employeeContractTable",
                height: 500,
                url: 'api/organization/employee-contract/queryEmployeeContracts?employeeId=' + $("#employee_id").val(),
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
                        {field: 'contractType', fixed:'left', title: '合同类型', align: 'center', width: 120, sort: true, templet: function (d) {
                                if (d.contractType == 1) {
                                    return '第一份合同';
                                } else if (d.contractType == 2) {
                                    return '第二份合同';
                                } else if (d.contractType == 3) {
                                    return '第三方合同';
                                }else if (d.contractType == 4) {
                                    return '培训协议';
                                }else if (d.contractType == 5) {
                                    return '保密协议';
                                }
                                ;
                            }
                        },
                        {field: 'contractSignTime', title: '签订时间', width: 200, align: 'center'},
                        {field: 'contractStartTime', title: '开始时间', width: 200, align: 'center'},
                        {field: 'contractEndTime', title: '结束时间', width: 200, align: 'center'},
                        {field: 'status', title: '状态', sort: true, width: 100, align: 'center', templet: function (d) {
                                if (d.status == 1) {
                                    return '<span style="color:#008000;">' + '正常' + '</span>';
                                } else if (d.status == 2) {
                                    return '<span style="color:#c00;">' + '删除' + '</span>';
                                }
                            }
                        },
                        {field: 'registerNo', title: '房产编号', width: 100, align: 'center'},
                        {field: 'propertyNo', title: '户籍编号', width: 100, align: 'center'},
                        {field: 'remark', title: '备注', width: 300, align: 'center'},
                        {align: 'center', title: '操作',width: 300, fixed: 'right', templet: '#operateTmp'}
                    ]
                ],
                page: true,
                text: {
                    none: '暂无员工合同相关数据。'
                }
            });


            table.reload('employeeContractTable', {
                page: {
                    curr: 1
                },
                where: {
                    employeeId: $("input[name='employeeId']").val(),
                }
            });

            table.on('tool(employeeContractTable)', function (obj) {
                var data = obj.data;//获取当前行数据
                var layEvent = obj.event;
                if (layEvent === 'edit') {
                    employeeContractManager.edit(data);
                } else if (layEvent === 'delete') {
                    employeeContractManager.delete(data);
                } else if (layEvent === 'change') {
                    employeeContractManager.contractExtManager(data);
                }
            });
        },

        edit: function (data) {
            var index = layer.open({
                type: 2,
                title: '修改调动申请',
                content: 'openUrl?url=/modules/organization/contract/update_employee_contract',
                maxmin: true,
                btn: ['确定', '取消'],
                area: ['550px', '700px'],
                skin: 'layui-layer-molv',
                success: function (layero, index) {
                    var body = layer.getChildFrame('body', index);
                    var employee_id = $("#employee_id").val();
                    body.find('#employeeId').val(employee_id);
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
                title: '新增合同·协议',
                content: 'openUrl?url=/modules/organization/contract/create_employee_contract',
                maxmin: true,
                btn: ['保存', '取消'],
                skin: 'layui-layer-molv',
                success: function (layero, index) {
                    var employee_id = $("#employee_id").val();
                    var name = $("#name").val();
                    var jobRole = $("#jobRole").val();

                    var body = layer.getChildFrame('body', index);
                    body.find('#employeeId').val(employee_id);
                    body.find('#name').val(name);
                    body.find('#jobRole').val(jobRole);

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

        contractExtManager: function (data) {
            var contractType=data.contractType;
            var contractTypeName='';
            if(contractType==1){
                contractTypeName='第一份合同';
            }else if(contractType==2){
                contractTypeName='第二份合同';
            }else{
                contractTypeName='第三份合同';
            }
            var param='&employee_id='+data.employeeId+'&name='+$('#name').val()+'&jobRole='+$('#jobRole').val()+'&parentContractId='+data.id+'&contractTypeName='+contractTypeName+
            '&mobileNo='+$('#mobileNo').val()+'&orgName='+$('#orgName').val()+'&jobRoleName='+$('#jobRoleName').val();
            var url=encodeURI('openUrl?url=modules/organization/contract/employee_contract_ext_manager'+param);
            layui.redirect.open(url, '合同变更管理');
        },

    };
    exports('employeeContractManager', employeeContractManager);
});