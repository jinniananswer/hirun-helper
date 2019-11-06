layui.extend({
    orgTree: 'org'
}).define(['ajax', 'table', 'element', 'orgTree', 'layer', 'form','select','redirect'], function (exports) {
    var $ = layui.$;
    var table = layui.table;
    var layer = layui.layer;
    var form = layui.form;
    var employee = {
        init: function () {

            layui.select.init('sex', 'SEX', '', true);

            table.render({
                elem: "#employee_table",
                height: 550,
                url: 'api/organization/employee/selectEmployeeList',
                loading: true,
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
                        {field: 'name', title: '姓名', width: 120,fixed :'left',align: 'center'},
                        {
                            field: 'sex', title: '性别', width: 80, sort: true,align: 'center', templet: function (d) {
                                if (d.sex == 1) {
                                    return '男'
                                } else {
                                    return '女'
                                }
                            }
                        },
                        {field: 'identityNo', title: '身份证号码', width: 200,align: 'center'},
                        {field: 'mobileNo', title: '电话号码', width: 150,align: 'center'},
                        {field: 'inDate', title: '入职时间',align: 'center',width: 120,templet:function (d) {
                                if(d.inDate!=''){
                                    return d.inDate.substr(0,10)
                                }
                            }},
                        {field: 'jobRoleName', title: '岗位', width: 120,align: 'center'},
                        {field: 'orgPath', title: '部门',align: 'center'},

                        {
                            field: 'status', title: '状态', width: 100,align: 'center', templet: function (d) {
                                if (d.employeeStatus == 0) {
                                    return '正常';
                                } else if (d.employeeStatus == 3) {
                                    return '离职';
                                }
                            }
                        },
                        {align: 'center', title: '操作', fixed: 'right',width:220,templet:'#operateTmp'}
                    ]
                ],
                page: true,
                text: {
                    none: '暂无相关数据，请检查查询条件。'
                }
            });


            $('#queryEmployee').on('click', function () {
                table.reload('employee_table', {
                    page: {
                        curr: 1
                    },
                    where: {
                        name: $("input[name='name']").val(),
                        sex: $("select[name='sex']").val(),
                        orgId: $("input[name='orgId']").val(),
                        mobileNo: $("input[name='mobileNo']").val(),
                        employeeStatus: $("select[name='employeeStatus']").val(),
                    }
                })
            });

            table.on('tool(employee_table)', function (obj) {
                var data = obj.data;//获取当前行数据
                var layEvent = obj.event;
                if (layEvent === 'remove') {
                    employee.destroy(data);
                } else if (layEvent === 'transOrg') {
                    employee.transOrg(data);
                } else if (layEvent === 'holiday') {
                    employee.holiday(data);
                } else if (layEvent == 'edit') {
                    employee.edit(data);
                }
            });

            //监听工具栏新增按钮
            $('.layui-btn-container .layui-btn').on('click', function () {
                layui.redirect.open('openUrl?url=modules/organization/employee/create_employee','新建员工档案');
            });

        },

        destroy: function (data) {
            var index=layer.open({
                type: 2,
                title: '员工离职',
                content: 'openUrl?url=/modules/organization/employee/destroy_employee',
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
            var sexName=(data.sex==1)?'男':'女';

            var param='&employee_id='+data.employeeId+'&name='+data.name+'&mobileNo='+data.mobileNo+'&orgName='+data.orgPath+'&sex='+sexName+'&jobRoleName='+data.jobRoleName+
                '&identityNo='+data.identityNo+'&inDate='+data.inDate.substr(0,10);
            layui.redirect.open('openUrl?url=modules/organization/employee/employeeholiday_manager'+param, '员工休假管理');
        },

        transOrg: function (data) {
            var sexName=(data.sex==1)?'男':'女';

            var param='&employee_id='+data.employeeId+'&name='+data.name+'&mobileNo='+data.mobileNo+'&orgName='+data.orgPath+'&sex='+sexName+'&jobRoleName='+data.jobRoleName+
                '&identityNo='+data.identityNo+'&inDate='+data.inDate.substr(0,10);
            var url=encodeURI('openUrl?url=modules/organization/employee/employee_trans_manager'+param);
            layui.redirect.open(url, '员工调动管理');
        },

        selectOrg : function() {
            layui.orgTree.init('orgTree', 'orgId', 'orgPath', false);
        },

        edit : function(data) {
            layui.redirect.open('openUrl?url=/modules/organization/employee/employee_archive&operType=edit&employeeId='+data.employeeId,'编辑员工资料');
        }


    };
    exports('employee', employee);
});