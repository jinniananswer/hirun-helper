layui.extend({
    orgTree: 'org'
}).define(['ajax', 'table', 'element', 'orgTree', 'laydate', 'form','select'], function (exports) {
    var $ = layui.$;
    var table = layui.table;
    var laydate = layui.laydate;

    var employeeCompanyStat = {
        init: function () {
            layui.select.init('orgNature', 'ORG_NATURE', '', true);
            layui.select.init('jobRole', 'JOB_ROLE', null, true);

            laydate.render({
                elem: '#queryTime',
                type: 'month',
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
                        {field: 'org_nature_name', title: '部门',rowspan:2, width: 150, fixed: 'left', align: 'center'},
                        {field: 'job_role_name', title: '现岗位',rowspan:2, width: 150, fixed: 'left', align: 'center'},
                        {field: 'employee_sum', title: '在编总人数',rowspan:2, align: 'center',width: 150,totalRow: true},
                        {field: '', title: '分公司',colspan: 14, align: 'center'},
                    ],
                    [
                        {field: 'city_count_2', title: '长沙', width: 110, align: 'center',totalRow: true},
                        {field: 'city_count_3', title: '株洲', width: 110,  align: 'center',totalRow: true},
                        {field: 'city_count_4', title: '湘潭', width: 110,  align: 'center',totalRow: true},
                        {field: 'city_count_5', title: '衡阳', width: 110, align: 'center',totalRow: true},
                        {field: 'city_count_6', title: '武汉', width: 110, align: 'center',totalRow: true},
                        {field: 'city_count_7', title: '荆州', width: 110, align: 'center',totalRow: true},
                        {field: 'city_count_8', title: '娄底', width: 110, align: 'center',totalRow: true},
                        {field: 'city_count_9', title: '岳阳', width: 110, align: 'center',totalRow: true},
                        {field: 'city_count_10', title: '怀化', width: 110, align: 'center',totalRow: true},
                        {field: 'city_count_11', title: '常德', width: 110, align: 'center',totalRow: true},
                        {field: 'city_count_12', title: '郴州', width: 110, align: 'center',totalRow: true},
                        {field: 'city_count_13', title: '浏阳', width: 110, align: 'center',totalRow: true},
                        {field: 'city_count_14', title: '宁乡', width: 110, align: 'center',totalRow: true},
                        {field: 'city_count_16', title: '萍乡', width: 110, align: 'center',totalRow: true},
                    ]
                ],
                page: false,
                text: {none: '暂无相关数据，请检查查询条件。'},
            });


            $('#queryEmployee').on('click', function () {
                table.reload('employee_transition_table', {
                    loading: true,
                    url: 'api/organization/stat-employee-quantity-month/queryEmployeeCompanyStat',
                    where: {
                        queryTime: $("input[name='queryTime']").val(),
                        orgNature: $("select[name='orgNature']").val(),
                        jobRole: $("select[name='jobRole']").val(),
                    }
                })
            });


        },

        selectOrg: function () {
            layui.orgTree.init('orgTree', 'orgId', 'orgPath', false);
        },


    };
    exports('employeeCompanyStat', employeeCompanyStat);
});