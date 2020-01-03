layui.extend({
    orgTree: 'org'
}).define(['ajax', 'table', 'element', 'orgTree', 'laydate', 'form'], function (exports) {
    var $ = layui.$;
    var table = layui.table;
    var laydate = layui.laydate;

    var companyEmployeeTrendsStat = {
        init: function () {

            laydate.render({
                elem: '#queryTime',
                type: 'year'
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
                        {field: 'org_name', title: '分公司',rowspan:3, width: 150, fixed: 'left', align: 'center'},
                        {field: '', title: '四大业务类',colspan: 24, align: 'center'},
                        {field: '', title: '所有岗位员工',colspan: 24, align: 'center'},
                    ],
                    [
                        {field: '', title: '入职人数',colspan: 12, align: 'center'},
                        {field: '', title: '离职人数',colspan: 12, align: 'center'},
                        {field: '', title: '入职人数',colspan: 12, align: 'center'},
                        {field: '', title: '离职人数',colspan: 12, align: 'center'},
                    ],
                    [
                        {field: 'busi_employee_entry_1', title: '1月', width: 80, align: 'center',totalRow:true},
                        {field: 'busi_employee_entry_2', title: '2月', width: 80,  align: 'center',totalRow:true},
                        {field: 'busi_employee_entry_3', title: '3月', width: 80,  align: 'center',totalRow:true},
                        {field: 'busi_employee_entry_4', title: '4月', width: 80, align: 'center',totalRow:true},
                        {field: 'busi_employee_entry_5', title: '5月', width: 80, align: 'center',totalRow:true},
                        {field: 'busi_employee_entry_6', title: '6月', width: 80, align: 'center',totalRow:true},
                        {field: 'busi_employee_entry_7', title: '7月', width: 80, align: 'center',totalRow:true},
                        {field: 'busi_employee_entry_8', title: '8月', width: 80, align: 'center',totalRow:true},
                        {field: 'busi_employee_entry_9', title: '9月', width: 80, align: 'center',totalRow:true},
                        {field: 'busi_employee_entry_10', title: '10月', width: 80, align: 'center',totalRow:true},
                        {field: 'busi_employee_entry_11', title: '11月', width: 80, align: 'center',totalRow:true},
                        {field: 'busi_employee_entry_12', title: '12月', width: 80, align: 'center',totalRow:true},
                        {field: 'busi_employee_destroy_1', title: '1月', width: 80, align: 'center',totalRow:true},
                        {field: 'busi_employee_destroy_2', title: '2月', width: 80,  align: 'center',totalRow:true},
                        {field: 'busi_employee_destroy_3', title: '3月', width: 80,  align: 'center',totalRow:true},
                        {field: 'busi_employee_destroy_4', title: '4月', width: 80, align: 'center',totalRow:true},
                        {field: 'busi_employee_destroy_5', title: '5月', width: 80, align: 'center',totalRow:true},
                        {field: 'busi_employee_destroy_6', title: '6月', width: 80, align: 'center',totalRow:true},
                        {field: 'busi_employee_destroy_7', title: '7月', width: 80, align: 'center',totalRow:true},
                        {field: 'busi_employee_destroy_8', title: '8月', width: 80, align: 'center',totalRow:true},
                        {field: 'busi_employee_destroy_9', title: '9月', width: 80, align: 'center',totalRow:true},
                        {field: 'busi_employee_destroy_10', title: '10月', width: 80, align: 'center',totalRow:true},
                        {field: 'busi_employee_destroy_11', title: '11月', width: 80, align: 'center',totalRow:true},
                        {field: 'busi_employee_destroy_12', title: '12月', width: 80, align: 'center',totalRow:true},
                        {field: 'all_employee_entry_1', title: '1月', width: 80, align: 'center',totalRow:true},
                        {field: 'all_employee_entry_2', title: '2月', width: 80,  align: 'center',totalRow:true},
                        {field: 'all_employee_entry_3', title: '3月', width: 80,  align: 'center',totalRow:true},
                        {field: 'all_employee_entry_4', title: '4月', width: 80, align: 'center',totalRow:true},
                        {field: 'all_employee_entry_5', title: '5月', width: 80, align: 'center',totalRow:true},
                        {field: 'all_employee_entry_6', title: '6月', width: 80, align: 'center',totalRow:true},
                        {field: 'all_employee_entry_7', title: '7月', width: 80, align: 'center',totalRow:true},
                        {field: 'all_employee_entry_8', title: '8月', width: 80, align: 'center',totalRow:true},
                        {field: 'all_employee_entry_9', title: '9月', width: 80, align: 'center',totalRow:true},
                        {field: 'all_employee_entry_10', title: '10月', width: 80, align: 'center',totalRow:true},
                        {field: 'all_employee_entry_11', title: '11月', width: 80, align: 'center',totalRow:true},
                        {field: 'all_employee_entry_12', title: '12月', width: 80, align: 'center',totalRow:true},
                        {field: 'all_employee_destroy_1', title: '1月', width: 80, align: 'center',totalRow:true},
                        {field: 'all_employee_destroy_2', title: '2月', width: 80,  align: 'center',totalRow:true},
                        {field: 'all_employee_destroy_3', title: '3月', width: 80,  align: 'center',totalRow:true},
                        {field: 'all_employee_destroy_4', title: '4月', width: 80, align: 'center',totalRow:true},
                        {field: 'all_employee_destroy_5', title: '5月', width: 80, align: 'center',totalRow:true},
                        {field: 'all_employee_destroy_6', title: '6月', width: 80, align: 'center',totalRow:true},
                        {field: 'all_employee_destroy_7', title: '7月', width: 80, align: 'center',totalRow:true},
                        {field: 'all_employee_destroy_8', title: '8月', width: 80, align: 'center',totalRow:true},
                        {field: 'all_employee_destroy_9', title: '9月', width: 80, align: 'center',totalRow:true},
                        {field: 'all_employee_destroy_10', title: '10月', width: 80, align: 'center',totalRow:true},
                        {field: 'all_employee_destroy_11', title: '11月', width: 80, align: 'center',totalRow:true},
                        {field: 'all_employee_destroy_12', title: '12月', width: 80, align: 'center',totalRow:true},
                    ]
                ],
                page: false,
                text: {none: '暂无相关数据，请检查查询条件。'},
            });


            $('#queryEmployee').on('click', function () {
                table.reload('employee_transition_table', {
                    loading: true,
                    url: 'api/organization/stat-employee-quantity-month/busiAndAllCountTrend',
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
    exports('companyEmployeeTrendsStat', companyEmployeeTrendsStat);
});