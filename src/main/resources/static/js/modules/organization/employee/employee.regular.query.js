layui.extend({
    orgTree: 'org',
    time: 'time'
}).define(['ajax', 'table', 'element', 'orgTree', 'laydate', 'form','select', 'time'], function (exports) {
    var $ = layui.$;
    var table = layui.table;
    var laydate = layui.laydate;

    var employeeRegularQuery = {
        init: function () {

            laydate.render({
                elem: '#queryTime',
                type: 'date',
                value: layui.time.addMonth(new Date(), -3)
            });



            table.render({
                elem: "#employee_regular_table",
                height: 550,
                loading: false,
                toolbar: '#toolbar',
                totalRow: true,
                parseData: function (res) { //res 即为原始返回的数据
                    return {
                        "code": res.code, //解析接口状态
                        "msg": res.message, //解析提示文本
                        "count": res.rows.total, //解析数据长度
                        "data": res.rows //解析数据列表
                    };
                },
                cols: [
                    [
                        {
                            field: 'name',
                            title: '姓名',
                            width: 120,
                            fixed: 'left',
                            align: 'center',
                            templet: '#templetArchive'
                        },
                        {field: 'orgPath', title: '部门', width: 300, align: 'center'},
                        {field: 'jobRoleName', title: '岗位', width: 200, align: 'center', sort: true},
                        {field: 'jobRoleNatureName', title: '岗位性质', width: 200, align: 'center', sort: true},
                        {
                            field: 'inDate',
                            title: '入职时间',
                            sort: true,
                            align: 'center',
                            templet: function (d) {
                                if (d.inDate != '') {
                                    return d.inDate.substr(0, 10)
                                }
                            }
                        },
                        {
                            field: 'regularDate',
                            title: '转正时间',
                            sort: true,
                            align: 'center',
                            templet: function (d) {
                                if (d.regularDate != '') {
                                    return d.regularDate.substr(0, 10)
                                }
                            }
                        },
                        {field: 'hrEmployeeName', title: '人资人员', align: 'center'},
                    ]
                ],
                page: false,
                text: {none: '暂无相关数据，请检查查询条件。'},
            });


            $('#queryEmployee').on('click', function () {
                table.reload('employee_regular_table', {
                    loading: true,
                    url: 'api/organization/employee/queryEmployeeRegularInfo',
                    where: {
                        queryTime: $("input[name='queryTime']").val(),
                        orgLine: $("input[name='orgId']").val(),
                        isSign: $("select[name='isSign']").val(),
                    }
                })
            });


        },

        selectOrg: function () {
            layui.orgTree.init('orgTree', 'orgId', 'orgPath', true, false);
        },


    };
    exports('employeeRegularQuery', employeeRegularQuery);
});