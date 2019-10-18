layui.extend({
    orgTree: 'org'
}).define(['ajax', 'table', 'element', 'orgTree', 'layer', 'form','select'], function (exports) {
    var $ = layui.$;
    var table = layui.table;
    var layer = layui.layer;
    var form = layui.form;
    var employee = {
        init: function () {

            layui.select.init('sex', 'SEX', '', true);

            table.render({
                elem: "#employee_table",
                height: 312,
                url: '/api/organization/employee/employeeList',
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
                        {field: 'employeeId', title: 'ID', width: 80, sort: true, fixed: 'left'},
                        {field: 'name', title: '姓名', width: 120},
                        {
                            field: 'sex', title: '性别', width: 80, sort: true, templet: function (d) {
                                if (d.sex == 1) {
                                    return '男'
                                } else {
                                    return '女'
                                }
                            }
                        },
                        {field: 'identityNo', title: '身份证号码', width: 200},
                        {field: 'mobileNo', title: '电话号码', width: 150},
                        {field: 'inDate', title: '入职时间', width: 200},
                        {
                            field: 'status', title: '状态', width: 100, templet: function (d) {
                                if (d.status == 0) {
                                    return '正常';
                                } else if (d.status == 1) {
                                    return '离职';
                                } else if (d.status == 3) {
                                    return '实习';
                                }
                            }
                        },
                        {align: 'center', title: '操作', fixed: 'right', toolbar: '#bar'}
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
                        mobile: $("input[name='mobile']").val(),
                        status: $("select[name='employeestatus']").val(),

                    }
                })
            });

            table.on('tool(employee_table)', function (obj) {
                var data = obj.data;//获取当前行数据
                var layEvent = obj.event;
                if (layEvent === 'remove') {
                    employee.destroy(data);
                } else if (layEvent === 'callout') {

                } else if (layEvent === 'holiday') {

                } else if (layEvent === 'callout') {

                } else if (layEvent === 'second') {

                }
            });

            //监听工具栏新增按钮
            $('.layui-btn-container .layui-btn').on('click', function () {
                window.location.href = 'openUrl?url=modules/organization/employee/create_employee'
            });

        },

        destroy: function (data) {
            layer.open({
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
        },

        selectOrg : function() {
            layui.orgTree.init('orgTree', 'orgId', 'orgPath', false);
        },


    };
    exports('employee', employee);
});