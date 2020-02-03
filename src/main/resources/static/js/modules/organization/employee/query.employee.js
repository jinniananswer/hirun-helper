layui.extend({
    orgTree: 'org',
}).define(['ajax', 'table', 'element', 'orgTree', 'layer', 'form', 'select', 'redirect', 'laydate'], function (exports) {
    var $ = layui.$;
    var table = layui.table;
    var layer = layui.layer;
    var form = layui.form;
    var laydate = layui.laydate;

    var employee = {
        init: function () {

            layui.select.init('sex', 'SEX', '', true);
            layui.select.init('employeeStatus', 'EMPLOYEE_STATUS', '', true);
            layui.select.init('type', 'EMPLOYEE_TYPE', '', true);
            layui.select.init('isBlackList', 'YES_NO', '', true);
            layui.select.init('jobRole', 'JOB_ROLE', null, true);
            layui.select.init('jobRoleNature', 'JOB_NATURE', null, true);

            laydate.render({
                elem: '#inDateEnd',
            });

            laydate.render({
                elem: '#inDateStart',
            });

            laydate.render({
                elem: '#destroyDateStart',
            });

            laydate.render({
                elem: '#destroyDateEnd',
            });

            var ins = table.render({
                elem: "#employee_table",
                height: 550,
                toolbar: '#toolbar',
                defaultToolbar: ['filter'],
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
                        {type: 'radio', fixed: 'left'},
                        {
                            field: 'name',
                            title: '姓名',
                            width: 120,
                            fixed: 'left',
                            align: 'center',
                            templet: '#templetArchive'
                        },
                        {
                            field: 'sex', title: '性别', width: 80, sort: true, align: 'center', templet: function (d) {
                                if (d.sex == 1) {
                                    return '男'
                                } else {
                                    return '女'
                                }
                            }
                        },
                        {field: 'typeName', title: '员工类型', width: 120, align: 'center', sort: true},
                        {field: 'identityNo', title: '身份证号码', width: 200, align: 'center'},
                        {field: 'mobileNo', title: '电话号码', width: 150, align: 'center'},
                        {field: 'age', title: '年龄', width: 80, align: 'center', sort: true},
                        {
                            field: 'inDate',
                            title: '入职时间',
                            sort: true,
                            align: 'center',
                            width: 120,
                            templet: function (d) {
                                if (d.inDate != '') {
                                    return d.inDate.substr(0, 10)
                                }
                            }
                        },
                        {field: 'companyAge', title: '司龄', width: 80, sort: true, align: 'center'},
                        {field: 'jobRoleName', title: '岗位', width: 150, align: 'center'},
                        {field: 'jobRoleNatureName', title: '岗位性质', width: 150, align: 'center'},
                        {field: 'discountRate', title: '折算比例', width: 150, align: 'center'},
                        {field: 'parentEmployeeName', title: '上级', width: 100, align: 'center'},
                        {field: 'orgPath', title: '部门', width: 100, align: 'center'},
                        {field: 'jobAge', title: '工作年限', width: 120, sort: true, align: 'center'},
                        {
                            field: 'status',
                            title: '状态',
                            width: 100,
                            align: 'center',
                            fixed: 'right',
                            templet: function (d) {
                                if (d.employeeStatus == 0) {
                                    return '在职';
                                } else if (d.employeeStatus == 3) {
                                    return '离职';
                                } else if (d.employeeStatus == 1) {
                                    return '退休';
                                }
                            }
                        },
                    ]
                ],
                page: true,
                text: {none: '暂无相关数据，请检查查询条件。'},
            });


            $('#queryEmployee').on('click', function () {
                table.reload('employee_table', {
                    page: {
                        curr: 1
                    },
                    loading: true,
                    url: 'api/organization/employee/queryEmployeeList4Page',
                    where: {
                        name: $("input[name='name']").val(),
                        sex: $("select[name='sex']").val(),
                        orgId: $("input[name='orgId']").val(),
                        mobileNo: $("input[name='mobileNo']").val(),
                        employeeStatus: $("select[name='employeeStatus']").val(),
                        type: $("select[name='type']").val(),
                        isBlackList: $("#isBlackList").val(),
                        otherStatus: $('#otherStatus').val(),
                        jobRole: $("select[name='jobRole']").val(),
                        jobRoleNature: $("select[name='jobRoleNature']").val(),
                        discountRate: $("select[name='discountRate']").val(),
                        jobYearStart: $('#jobYearStart').val(),
                        jobYearEnd: $('#jobYearEnd').val(),
                        ageStart: $('#ageStart').val(),
                        ageEnd: $('#ageEnd').val(),
                        inDateStart: $('#inDateStart').val(),
                        inDateEnd: $('#inDateEnd').val(),
                        destroyDateStart: $('#destroyDateStart').val(),
                        destroyDateEnd: $('#destroyDateEnd').val(),
                        companyAgeStart: $('#companyAgeStart').val(),
                        companyAgeEnd: $('#companyAgeEnd').val(),
                    }
                })
            });


            //监听工具栏按钮
            table.on('toolbar(employee_table)', function (obj) {
                var checkStatus = table.checkStatus(obj.config.id); //获取选中行状态
                var data = checkStatus.data;
                var event = obj.event;

                if ((event != 'create' && event != 'export' && event != 'LAYTABLE_COLS') && data.length <= 0) {
                    layer.msg('请选中一条数据,再进行操作。');
                    return;
                }

                if (event === 'create') {
                    layui.redirect.open('openUrl?url=modules/organization/employee/employee_archive&operType=create', '编辑员工资料');
                } else if (event === 'remove') {
                    if (data[0].employeeStatus == 1 || data[0].employeeStatus == 3) {
                        layer.msg('员工已为离职状态,不允许做离职操作。');
                        return;
                    } else {
                        employee.destroy(data[0]);
                    }
                } else if (event === 'edit') {
                    if (data[0].employeeStatus == 1 || data[0].employeeStatus == 3) {
                        layer.msg('员工已为离职状态,不允许再编辑。');
                        return;
                    } else {
                        employee.edit(data[0]);
                    }
                } else if (event === 'holiday') {
                    if (data[0].employeeStatus == 1 || data[0].employeeStatus == 3) {
                        layer.msg('员工已为离职状态,不允许再做休假。');
                        return;
                    } else {
                        employee.holiday(data[0]);
                    }
                } else if (event === 'transOrg') {
                    if (data[0].employeeStatus == 1 || data[0].employeeStatus == 3) {
                        layer.msg('员工已为离职状态,不允许办理调动操作。');
                        return;
                    } else {
                        employee.transOrg(data[0]);
                    }
                } else if (event === 'contract') {
                    employee.contractManager(data[0]);
                } else if (event === 'export') {
                    employee.export();
                }else if(event === 'applyBlackList'){
                    if (data[0].employeeStatus == 0 ) {
                        layer.msg('员工为正常状态,如需永不录用，请操作员工离职。');
                        return;
                    } else {
                        employee.applyBlackList(data[0]);
                    }
                }
            });


        },

        destroy: function (data) {
            var index = layer.open({
                type: 2,
                title: '员工离职',
                content: 'openUrl?url=modules/organization/employee/destroy_employee',
                maxmin: true,
                btn: ['确定', '取消'],
                area: ['550px', '700px'],
                skin: 'layui-layer-molv',
                success: function (layero, index) {
                    var body = layer.getChildFrame('body', index);
                    body.find('#employeeId').val(data.employeeId);
                    body.find('#name').val(data.name);
                    body.find('#identityNo').val(data.identityNo);
                    body.find('#mobileNo').val(data.mobileNo);
                    form.render();
                },
                yes: function (index, layero) {
                    var body = layer.getChildFrame('body', index);
                    var submit = body.find("#employee-remove-submit");
                    submit.click();
                }
            });
            layer.full(index);
        },

        holiday: function (data) {
            var sexName = (data.sex == 1) ? '男' : '女';

            var param = '&employee_id=' + data.employeeId + '&name=' + data.name + '&mobileNo=' + data.mobileNo + '&orgName=' + data.orgPath + '&sex=' + sexName + '&jobRoleName=' + data.jobRoleName +
                '&identityNo=' + data.identityNo + '&inDate=' + data.inDate.substr(0, 10);
            layui.redirect.open('openUrl?url=modules/organization/employee/employeeholiday_manager' + param, '员工休假管理');
        },

        transOrg: function (data) {
            var sexName = (data.sex == 1) ? '男' : '女';

            var param = '&employee_id=' + data.employeeId + '&name=' + data.name + '&mobileNo=' + data.mobileNo + '&orgName=' + data.orgPath + '&sex=' + sexName + '&jobRoleName=' + data.jobRoleName +
                '&identityNo=' + data.identityNo + '&inDate=' + data.inDate.substr(0, 10);
            var url = encodeURI('openUrl?url=modules/organization/employee/employee_trans_manager' + param);
            layui.redirect.open(url, '员工调动管理');
        },

        contractManager: function (data) {
            var sexName = (data.sex == 1) ? '男' : '女';

            var param = '&employee_id=' + data.employeeId + '&name=' + data.name + '&mobileNo=' + data.mobileNo + '&orgName=' + data.orgPath + '&sex=' + sexName + '&jobRoleName=' + data.jobRoleName +
                '&identityNo=' + data.identityNo + '&inDate=' + data.inDate.substr(0, 10) + '&jobRole=' + data.jobRole;
            var url = encodeURI('openUrl?url=modules/organization/contract/employee_contract_manager' + param);
            layui.redirect.open(url, '员工合同·协议管理');
        },

        selectOrg: function () {
            layui.orgTree.init('orgTree', 'orgId', 'orgPath', true,false);
        },

        edit: function (data) {
            layui.redirect.open('openUrl?url=modules/organization/employee/employee_archive&operType=edit&employeeId=' + data.employeeId, '编辑员工资料');
        },

        applyBlackList:function (data) {
            let employeeId=data.employeeId;
            layer.prompt({formType: 2,
                title: '申请永不录用原因',
                area: ['300px', '200px'] //自定义文本域宽高
        },function(val, index){
                let param='employeeId='+employeeId+'&reason='+val;
                layui.ajax.post('api/organization/employee/applyEmployeeBlackList', param);
            });
        },

        export: function () {
            var param = '?name=' + $("input[name='name']").val() + '&orgId=' + $("input[name='orgId']").val() + '&sex=' + $("select[name='sex']").val() + '&type=' + $("select[name='type']").val() +
                '&mobile=' + $("input[name='mobileNo']").val() + '&employeeStatus=' + $("select[name='employeeStatus']").val() + '&isBlackList=' + $("#isBlackList").val() + '&otherStatus=' + $("select[name='otherStatus']").val() +
                '&jobRole=' + $("select[name='jobRole']").val() + '&jobRoleNature=' + $("select[name='jobRoleNature']").val() + '&discountRate=' + $("select[name='discountRate']").val() +
                '&jobYearStart=' + $("input[name='jobYearStart']").val() + '&jobYearEnd=' + $("input[name='jobYearEnd']").val() + '&ageStart=' + $("input[name='ageStart']").val() + '&ageEnd=' + $("input[name='ageEnd']").val() +
                '&companyAgeStart=' + $("input[name='companyAgeStart']").val() + '&companyAgeEnd=' + $("input[name='companyAgeEnd']").val() + '&inDateStart=' + $("input[name='inDateStart']").val() + '&inDateEnd=' + $("input[name='inDateEnd']").val() +
                '&destroyDateStart=' + $("input[name='destroyDateStart']").val() + '&destroyDateEnd=' + $("input[name='destroyDateEnd']").val();

            window.location.href = "api/organization/employee/queryEmployeeList4Export" + param;
        },

        loadEmployeeArchive: function (id, name) {
            layui.redirect.open('openUrl?url=modules/organization/employee/my_archive&employeeId=' + id, name + '的档案');
        },

        moreCondition: function (type) {
            if (type == 'appear') {
                $("#more").show();
                $("#conceal").show();
                $("#appear").hide();
            } else {
                $("#more").hide();
                $("#conceal").hide();
                $("#appear").show();
            }
        }

    };
    exports('employee', employee);
});