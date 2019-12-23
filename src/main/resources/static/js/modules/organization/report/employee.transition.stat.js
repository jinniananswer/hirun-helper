layui.extend({
    orgTree: 'org'
}).define(['ajax', 'table', 'element', 'orgTree', 'laydate', 'form'], function (exports) {
    var $ = layui.$;
    var table = layui.table;
    var laydate = layui.laydate;

    var employeeTransitionStat = {
        init: function () {

            laydate.render({
                elem: '#queryTime',
                type: 'month'
            });

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
                        {field: 'orgName', title: '部门', width: 324, fixed: 'left', align: 'center'},
                        {field: 'year', title: '年份', width: 89, fixed: 'left', align: 'center'},
                        {field: 'month', title: '月份', width: 89, fixed: 'left', align: 'center'},
                        {field: 'employeeEntryQuantity', title: '入职人数', width: 89, align: 'center',totalRow:true,totalRowText:"合计"},
                        {field: 'entryEmployeeName', title: '入职员工', width: 89, align: 'center'},
                        {field: 'employeeDestroyQuantity', title: '离职人数', width: 89, align: 'center',totalRow:true,totalRowText:"合计"},
                        {field: 'destroyEmployeeName', title: '离职员工', width: 89, align: 'center'},
                        {field: 'employeeHolidayQuantity', title: '休假人数', width: 89, align: 'center',totalRow:true,totalRowText:"合计"},
                        {field: 'holidayEmployeeName', title: '休假员工', width: 89, align: 'center'},
                        {field: 'employeeTransOutQuantity', title: '调出人数', width: 89, align: 'center',totalRow:true,totalRowText:"合计"},
                        {field: 'transOutEmployeeName', title: '调出员工', width: 89, align: 'center'},
                        {field: 'employeeTransInQuantity', title: '调入人数', width: 89, align: 'center',totalRow:true,totalRowText:"合计"},
                        {field: 'transInEmployeeName', title: '调入员工', width: 89, align: 'center'},
                        {field: 'employeeBorrowInQuantity', title: '借调(入)人数', width: 89, align: 'center',totalRow:true,totalRowText:"合计"},
                        {field: 'borrowInEmployeeName', title: '借调员工', width: 89, align: 'center'},
                        {field: 'employeeBorrowOutQuantity', title: '借调(出)人数', width: 89, align: 'center',totalRow:true,totalRowText:"合计"},
                        {field: 'borrowOutEmployeeName', title: '借调员工', width: 89, align: 'center'},
                    ]
                ],
                page: false,
                text: {none: '暂无相关数据，请检查查询条件。'},
            });


            $('#queryEmployee').on('click', function () {
                table.reload('employee_transition_table', {
                    loading: true,
                    url: 'api/organization/stat-employee-transition/queryEmployeeTransitionStat',
                    where: {
                        queryTime: $("input[name='queryTime']").val(),
                        orgId: $("input[name='orgId']").val(),
                    }
                })
            });


        },

        selectOrg: function () {
            layui.orgTree.init('orgTree', 'orgId', 'orgPath', false);
        },


    };
    exports('employeeTransitionStat', employeeTransitionStat);
});