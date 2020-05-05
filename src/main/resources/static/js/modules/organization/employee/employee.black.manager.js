layui.extend({
}).define(['ajax', 'table', 'element', 'layer', 'form', 'select'], function (exports) {
    let $ = layui.$;
    let table = layui.table;
    let form = layui.form;
    let employeeBlackManager = {
        init: function () {


            table.render({
                elem: "#employeeBlackTable",
                height: 550,
                loading:false,
                defaultToolbar: ['filter'],
                toolbar: '#operateBar',
                parseData: function (res) { //res 即为原始返回的数据
                    return {
                        "code": res.code, //解析接口状态
                        "msg": res.message, //解析提示文本
                        "count": res.total, //解析数据长度
                        "data": res.rows //解析数据列表
                    };
                },
                cols: [
                    [
                        {type: 'radio'},
                        {field: 'id', title: '编号', width: 150,align: 'center'},
                        {field: 'name', title: '姓名', width: 150,align: 'center'},
                        {field: 'identityNo', title: '证件号码', align: 'center'},
                        {field: 'remark', title: '备注', align: 'center'},
                    ]
                ],
                page: true,
                text: {
                    none: '暂无相关数据，请检查查询条件。'
                }
            });


            $('#query').on('click', function () {
                let name =$('#employee_name').val();
                let identityNo=$('#identity_no').val();
                if(name==''&&identityNo==''){
                    layer.msg('姓名或者证件号码不能同时为空');
                    return;
                }
                table.reload('employeeBlackTable', {
                    page: {
                        curr: 1
                    },
                    url: 'api/organization/employee-blacklist/queryEmployeeBlackList',
                    where: {
                        employeeName: $("input[name='employee_name']").val(),
                        identityNo: $("input[name='identity_no']").val(),
                    }
                })
            });

            table.on('toolbar(employeeBlackTable)', function (obj) {
                let checkStatus = table.checkStatus(obj.config.id); //获取选中行状态
                let data = checkStatus.data;
                let event = obj.event;

                if (data.length <= 0) {
                    layer.msg('请选中一条数据，再进行操作。');
                    return;
                }

                switch (event) {
                    case 'delete':
                        employeeBlackManager.delete(data[0]);
                        break;
                }
                ;
            });

        },


        delete: function (data) {
                let id = data.id;
                layer.prompt({
                    formType: 2,
                    title: '释放黑名单原因',
                    area: ['300px', '200px'] //自定义文本域宽高
                }, function (val, index) {
                    let param = 'id=' + id + '&remark=' + val;
                    layui.ajax.post('api/organization/employee-blacklist/deleteEmployeeBlackList', param);
                });
        },

    };
    exports('employeeBlackManager', employeeBlackManager);
});