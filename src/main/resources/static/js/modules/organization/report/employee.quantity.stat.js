layui.extend({
    orgTree: 'org'
}).define(['ajax', 'table', 'element', 'orgTree', 'laydate', 'form'], function (exports) {
    var $ = layui.$;
    var table = layui.table;
    var laydate = layui.laydate;
    var form = layui.form;


    var employeeQuantityStat = {
        init: function () {

            laydate.render({
                elem: '#year',
                type: 'year',
                value:new Date()
            });

            table.render({
                elem: "#employee_quantity_table",
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
                        {field: 'orgName', title: '部门',rowspan:2, width: 324, fixed: 'left', align: 'center',totalRowText:'合计: '},
                        {field: 'janurayCount', title: '年份',colspan: 12, align: 'center'},
                    ],
                    [
                        {field: 'janurayCount', title: '1月', width: 89, align: 'center',totalRow:true},
                        {field: 'februaryCount', title: '2月', width: 89, align: 'center',totalRow:true},
                        {field: 'marchCount', title: '3月', width: 89, align: 'center',totalRow:true},
                        {field: 'aprilCount', title: '4月', width: 89, align: 'center',totalRow:true},
                        {field: 'mayCount', title: '5月', width: 89, align: 'center',totalRow:true},
                        {field: 'juneCount', title: '6月', width: 89, align: 'center',totalRow:true},
                        {field: 'julyCount', title: '7月', width: 89, align: 'center',totalRow:true},
                        {field: 'augustCount', title: '8月', width: 89, align: 'center',totalRow:true},
                        {field: 'septemberCount', title: '9月', width: 89, align: 'center',totalRow:true},
                        {field: 'octoberCount', title: '10月', width: 89, align: 'center',totalRow:true},
                        {field: 'novemberCount', title: '11月', width: 89, align: 'center',totalRow:true},
                        {field: 'decemberCount', title: '12月', width: 89, align: 'center',totalRow:true},
                    ]
                ],
                page: false,
                text: {none: '暂无相关数据，请检查查询条件。'},
            });


            form.on('submit(queryEmployee)', function (data) {
                table.reload('employee_quantity_table', {
                    loading: true,
                    url: 'api/organization/stat-employee-quantity-month/queryEmployeeQuantityStat',
                    where: {
                        year: $("input[name='year']").val(),
                        orgId: $("input[name='orgId']").val(),
                    }
                })
            });

            $('#reloadCount').on('click', function () {
                layui.ajax.post('api/organization/stat-employee-quantity-month/reloadCount','', function(data){});
            });

        },

        selectOrg: function () {
            layui.orgTree.init('orgTree', 'orgId', 'orgPath', true,false);
        },


    };
    exports('employeeQuantityStat', employeeQuantityStat);
});