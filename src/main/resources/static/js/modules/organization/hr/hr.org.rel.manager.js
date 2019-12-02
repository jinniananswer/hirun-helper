layui.extend({
    orgTree: 'org',
    selectEmployee: 'employee'
}).define(['ajax', 'table', 'element', 'layer', 'form', 'select','selectEmployee','orgTree'], function (exports) {
    var $ = layui.$;
    var table = layui.table;
    var form = layui.form;
    var orgHrRelManager = {
        init: function () {


            table.render({
                elem: "#orgHrRel_table",
                height: 550,
                url: 'api/organization/org-hr-rel/queryOrgHrRelList',
                loading: true,
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
                        {field: 'orgPath', title: '部门', width: 432,align: 'center'},
                        {field: 'archiveManagerEmployeeName', title: '档案管理人资', align: 'center'},
                        {field: 'relationManagerEmployeeName', title: '劳动关系管理人资', align: 'center'},
                        {field: 'startTime', title: '开始时间', align: 'center'},
                        {field: 'endTime', title: '结束时间',  align: 'center'},
                    ]
                ],
                page: true,
                text: {
                    none: '暂无相关数据，请检查查询条件。'
                }
            });


            $('#queryOrgHrRel').on('click', function () {
                table.reload('orgHrRel_table', {
                    page: {
                        curr: 1
                    },
                    where: {
                        employeeId: $("input[name='employeeId']").val(),
                        orgSet: $("input[name='orgSet']").val(),
                    }
                })
            });

            table.on('toolbar(orgHrRel_table)', function (obj) {
                let checkStatus = table.checkStatus(obj.config.id); // 获取选中行状态
                let data = checkStatus.data;
                let event = obj.event;

                switch (event) {
                    case 'update':
                        if (data.length <= 0) {
                            layer.msg('请选中一条数据，再进行操作。');
                            return;
                        }
                        let param = data.map(item => item.id);
                        orgHrRelManager.update(param);
                        break;
                }
                ;
            });

        },

        selectEmployee : function() {
            layui.selectEmployee.init('employeeList', 'employeeSearch', 'employeeId', 'employeeName', false);
        },

        selectOrg : function() {
            layui.orgTree.init('orgTree', 'orgSet', 'orgPath', true);
        },

        update: function (data) {
            var index = layer.open({
                type: 2,
                title: '部门人资关系维护',
                content: 'openUrl?url=modules/organization/hr/update_hr_org_rel',
                maxmin: true,
                btn: ['确定', '取消'],
                area: ['550px', '700px'],
                skin: 'layui-layer-molv',
                success: function (layero, index) {
                    var body = layer.getChildFrame('body', index);
                    body.find('#ids').val(data);
                    form.render();
                },
                yes: function (index, layero) {
                    var body = layer.getChildFrame('body', index);
                    var submit = body.find("#confirm-submit");
                    submit.click();
                }
            });
            layer.full(index);
        },
    };
    exports('orgHrRelManager', orgHrRelManager);
});