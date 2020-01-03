layui.extend({
    orgTree: 'org'
}).define(['ajax', 'table', 'element', 'orgTree', 'laydate', 'form','select'], function (exports) {
    var $ = layui.$;
    var table = layui.table;
    var laydate = layui.laydate;

    var employeeTrendsStat = {
        init: function () {

            laydate.render({
                elem: '#queryTime',
                type: 'month',
                value: new Date()
            });


            layui.select.init('orgNature', 'ORG_NATURE', '', true);

            table.render({
                elem: "#employee_transition_table",
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
                        {field: 'org_nature_name', title: '部门', rowspan: 2, width: 150, fixed: 'left', align: 'center'},
                        {field: 'job_role_name', title: '现岗位', rowspan: 2, width: 150, fixed: 'left', align: 'center'},
                        {field: '', title: '本月员工盘点', colspan: 3, align: 'center'},
                        {field: '', title: '本月员工异动', colspan: 14, align: 'center'},
                    ],
                    [
                        {field: 'employee_num', title: '在编人数', width: 140, align: 'center',totalRow: true},
                        {field: 'less_month_num', title: '入职9个月内人数', width: 150, align: 'center', sort: true,totalRow: true},
                        {field: 'more_month_num', title: '入职9个月以上人数', width: 170, align: 'center', sort: true,totalRow: true},
                        {
                            field: 'employee_entry_quantity',
                            title: '入职人数',
                            width: 110,
                            align: 'center',
                            totalRow: true,
                            sort: true
                        },
                        {field: 'entryEmployeeName', title: '入职员工', width: 110, align: 'center'},
                        {
                            field: 'employee_destroy_quantity',
                            title: '离职人数',
                            width: 110,
                            align: 'center',
                            totalRow: true,
                            sort: true
                        },
                        {field: 'destroyEmployeeName', title: '离职员工', width: 110, align: 'center'},
                        {
                            field: 'employee_holiday_quantity',
                            title: '休假人数',
                            width: 110,
                            align: 'center',
                            totalRow: true,
                            sort: true
                        },
                        {field: 'holidayEmployeeName', title: '休假员工', width: 110, align: 'center'},
                        {
                            field: 'employee_trans_out_quantity',
                            title: '调出人数',
                            width: 110,
                            align: 'center',
                            totalRow: true,
                            sort: true
                        },
                        {field: 'transOutEmployeeName', title: '调出员工', width: 110, align: 'center'},
                        {
                            field: 'employee_trans_in_quantity',
                            title: '调入人数',
                            width: 110,
                            align: 'center',
                            totalRow: true,
                            sort: true
                        },
                        {field: 'transInEmployeeName', title: '调入员工', width: 110, align: 'center'},
                        {
                            field: 'employee_borrow_in_quantity',
                            title: '借调(入)人数',
                            width: 110,
                            align: 'center',
                            totalRow: true,
                            sort: true
                        },
                        {field: 'borrowInEmployeeName', title: '借调员工', width: 110, align: 'center'},
                        {
                            field: 'employee_borrow_out_quantity',
                            title: '借调(出)人数',
                            width: 110,
                            align: 'center',
                            totalRow: true,
                            sort: true
                        },
                        {field: 'borrowOutEmployeeName', title: '借调员工', width: 110, align: 'center'},
                    ]
                ],
                page: false,
                text: {none: '暂无相关数据，请检查查询条件。'},
            });


            $('#queryEmployee').on('click', function () {
                table.reload('employee_transition_table', {
                    loading: true,
                    url: 'api/organization/stat-employee-quantity-month/queryEmployeeTrendsStat',
                    where: {
                        queryTime: $("input[name='queryTime']").val(),
                        orgId: $("input[name='orgId']").val(),
                        orgNature: $("select[name='orgNature']").val(),
                    }
                })
            });


        },

        selectOrg: function () {
            layui.orgTree.init('orgTree', 'orgId', 'orgPath', false, false);
        },


    };
    exports('employeeTrendsStat', employeeTrendsStat);
});