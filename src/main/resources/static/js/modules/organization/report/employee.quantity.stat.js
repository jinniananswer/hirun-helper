layui.extend({
    orgTree: 'org'
}).define(['ajax', 'table', 'element', 'orgTree', 'laydate', 'form'], function (exports) {
    var $ = layui.$;
    var table = layui.table;
    var laydate = layui.laydate;

    var employeeQuantityStat = {
        init: function () {

            laydate.render({
                elem: '#year',
                type: 'year'
            });

            table.render({
                elem: "#employee_quantity_table",
                height: 550,
                loading: false,
                toolbar: '#toolbar',
                totalRow:true,
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
                        {field: 'orgId', title: '部门', width: 120, fixed: 'left', align: 'center'},
                        {field: 'year', title: '年份',width: 180, align: 'center'},
                        {field: 'month', title: '月份',width: 300, align: 'center'},
                        {field: 'employeeQuantity', title: '员工数量',width: 300, align: 'center',totalRow:true},
                    ]
                ],
                page: true,
                text: {none: '暂无相关数据，请检查查询条件。'},
            });


            $('#queryEmployee').on('click', function () {
                table.reload('employee_quantity_table', {
                    page: {
                        curr: 1
                    },
                    loading:true,
                    url :'api/organization/stat-employee-quantity-month/queryEmployeeQuantityStat',
                    where: {
                        year: $("input[name='year']").val(),
                        orgId: $("input[name='orgId']").val(),
                    }
                })
            });


        },

        selectOrg: function () {
            layui.orgTree.init('orgTree', 'orgId', 'orgPath', false);
        },


    };
    exports('employeeQuantityStat', employeeQuantityStat);
});