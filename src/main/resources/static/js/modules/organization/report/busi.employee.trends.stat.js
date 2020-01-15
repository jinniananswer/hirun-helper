layui.extend({
    orgTree: 'org'
}).define(['ajax', 'table', 'element', 'orgTree', 'laydate', 'form','select'], function (exports) {
    var $ = layui.$;
    var table = layui.table;
    var laydate = layui.laydate;

    var busiEmployeeTrendsStat = {
        init: function () {
            layui.select.init('orgNature', 'ORG_NATURE', '', true);

            laydate.render({
                elem: '#queryTime',
                type: 'year',
                value: new Date()
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
                        {field: 'org_nature_name', title: '部门',rowspan:2, width: 150, fixed: 'left', align: 'center',totalRowText:'合计: '},
                        {field: 'job_role_name', title: '现岗位',rowspan:2, width: 150, fixed: 'left', align: 'center'},
                        {field: '', title: '在编人数',colspan: 12, align: 'center'},
                        {field: '', title: '入职人数',colspan: 12, align: 'center'},
                        {field: '', title: '离职人数',colspan: 12, align: 'center'},
                    ],
                    [
                        {field: 'employee_num_1', title: '1月', width: 80, align: 'center',totalRow:true},
                        {field: 'employee_num_2', title: '2月', width: 80,  align: 'center',totalRow:true},
                        {field: 'employee_num_3', title: '3月', width: 80,  align: 'center',totalRow:true},
                        {field: 'employee_num_4', title: '4月', width: 80, align: 'center',totalRow:true},
                        {field: 'employee_num_5', title: '5月', width: 80, align: 'center',totalRow:true},
                        {field: 'employee_num_6', title: '6月', width: 80, align: 'center',totalRow:true},
                        {field: 'employee_num_7', title: '7月', width: 80, align: 'center',totalRow:true},
                        {field: 'employee_num_8', title: '8月', width: 80, align: 'center',totalRow:true},
                        {field: 'employee_num_9', title: '9月', width: 80, align: 'center',totalRow:true},
                        {field: 'employee_num_10', title: '10月', width: 80, align: 'center',totalRow:true},
                        {field: 'employee_num_11', title: '11月', width: 80, align: 'center',totalRow:true},
                        {field: 'employee_num_12', title: '12月', width: 80, align: 'center',totalRow:true},
                        {field: 'employee_entry_num_1', title: '1月', width: 80, align: 'center',totalRow:true},
                        {field: 'employee_entry_num_2', title: '2月', width: 80,  align: 'center',totalRow:true},
                        {field: 'employee_entry_num_3', title: '3月', width: 80,  align: 'center',totalRow:true},
                        {field: 'employee_entry_num_4', title: '4月', width: 80, align: 'center',totalRow:true},
                        {field: 'employee_entry_num_5', title: '5月', width: 80, align: 'center',totalRow:true},
                        {field: 'employee_entry_num_6', title: '6月', width: 80, align: 'center',totalRow:true},
                        {field: 'employee_entry_num_7', title: '7月', width: 80, align: 'center',totalRow:true},
                        {field: 'employee_entry_num_8', title: '8月', width: 80, align: 'center',totalRow:true},
                        {field: 'employee_entry_num_9', title: '9月', width: 80, align: 'center',totalRow:true},
                        {field: 'employee_entry_num_10', title: '10月', width: 80, align: 'center',totalRow:true},
                        {field: 'employee_entry_num_11', title: '11月', width: 80, align: 'center',totalRow:true},
                        {field: 'employee_entry_num_12', title: '12月', width: 80, align: 'center',totalRow:true},
                        {field: 'employee_destroy_num_1', title: '1月', width: 80, align: 'center',totalRow:true},
                        {field: 'employee_destroy_num_2', title: '2月', width: 80,  align: 'center',totalRow:true},
                        {field: 'employee_destroy_num_3', title: '3月', width: 80,  align: 'center',totalRow:true},
                        {field: 'employee_destroy_num_4', title: '4月', width: 80, align: 'center',totalRow:true},
                        {field: 'employee_destroy_num_5', title: '5月', width: 80, align: 'center',totalRow:true},
                        {field: 'employee_destroy_num_6', title: '6月', width: 80, align: 'center',totalRow:true},
                        {field: 'employee_destroy_num_7', title: '7月', width: 80, align: 'center',totalRow:true},
                        {field: 'employee_destroy_num_8', title: '8月', width: 80, align: 'center',totalRow:true},
                        {field: 'employee_destroy_num_9', title: '9月', width: 80, align: 'center',totalRow:true},
                        {field: 'employee_destroy_num_10', title: '10月', width: 80, align: 'center',totalRow:true},
                        {field: 'employee_destroy_num_11', title: '11月', width: 80, align: 'center',totalRow:true},
                        {field: 'employee_destroy_num_12', title: '12月', width: 80, align: 'center',totalRow:true},
                    ]
                ],
                page: false,
                text: {none: '暂无相关数据，请检查查询条件。'},
            });


            $('#queryEmployee').on('click', function () {
                table.reload('employee_transition_table', {
                    loading: true,
                    url: 'api/organization/stat-employee-quantity-month/busiCountByOrgNatureAndJobRole',
                    where: {
                        queryTime: $("input[name='queryTime']").val(),
                        orgId: $("input[name='orgId']").val(),
                        orgNature: $("select[name='orgNature']").val(),
                    }
                })
            });


        },

        selectOrg: function () {
            layui.orgTree.init('orgTree', 'orgId', 'orgPath', true,false);
        },


    };
    exports('busiEmployeeTrendsStat', busiEmployeeTrendsStat);
});