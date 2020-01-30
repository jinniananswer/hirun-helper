layui.extend({
    orgTree: 'org',
    selectEmployee: 'employee'
}).define(['ajax', 'table', 'element', 'layer', 'form', 'select','selectEmployee','orgTree'], function (exports) {
    let $ = layui.$;
    let table = layui.table;
    let form = layui.form;
    let employeeBatchChange = {
        init: function () {


            table.render({
                elem: "#employeeBatchChange_table",
                height: 550,
                loading:false,
                defaultToolbar: ['filter'],
                toolbar: '#operateBar',
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
                        {type: 'checkbox'},
                        {field: 'name', title: '姓名', width: 150,align: 'center'},
                        {field: 'orgPath', title: '部门', width: 432,align: 'center'},
                        {field: 'jobRoleName', title: '岗位', align: 'center'},
                        {field: 'jobRoleNatureName', title: '岗位性质', align: 'center'},
                        {field: 'parentEmployeeName', title: '上级员工', align: 'center'},
                    ]
                ],
                page: true,
                text: {
                    none: '暂无相关数据，请检查查询条件。'
                }
            });


            $('#queryOrgHrRel').on('click', function () {
                table.reload('employeeBatchChange_table', {
                    page: {
                        curr: 1
                    },
                    url: 'api/organization/employee/queryEmployee4BatchChange',
                    where: {
                        parentEmployeeId: $("input[name='employeeId']").val(),
                        orgId: $("input[name='orgSet']").val(),
                    }
                })
            });

            table.on('toolbar(employeeBatchChange_table)', function (obj) {
                let checkStatus = table.checkStatus(obj.config.id); // 获取选中行状态
                let data = checkStatus.data;
                let event = obj.event;

                switch (event) {
                    case 'update':
                        if (data.length <= 0) {
                            layer.msg('请选中一条数据，再进行操作。');
                            return;
                        }
                        let param = data.map(item => item.jobRoleId);
                        console.log(param);
                        employeeBatchChange.update(param);
                        break;
                }
                ;
            });

        },

        selectEmployee : function() {
            layui.selectEmployee.init('employeeList', 'employeeSearch', 'employeeId', 'employeeName', false);
        },

        selectOrg : function() {
            layui.orgTree.init('orgTree', 'orgSet', 'orgPath', false,false);

        },

        update: function (data) {
            let index = layer.open({
                type: 2,
                title: '批量变更员工上级',
                content: 'openUrl?url=modules/organization/employee/batch_change_leader',
                maxmin: true,
                btn: ['确定', '取消'],
                area: ['550px', '700px'],
                skin: 'layui-layer-molv',
                success: function (layero, index) {
                    let body = layer.getChildFrame('body', index);
                    body.find('#ids').val(data);
                    form.render();
                },
                yes: function (index, layero) {
                    let body = layer.getChildFrame('body', index);
                    let submit = body.find("#confirm-submit");
                    submit.click();
                }
            });
            layer.full(index);
        },
    };
    exports('employeeBatchChange', employeeBatchChange);
});