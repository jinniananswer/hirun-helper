layui.extend({
    orgTree: 'org',
}).define(['ajax', 'table', 'element', 'orgTree', 'layer', 'form', 'select', 'redirect', 'laydate'], function (exports) {
    var $ = layui.$;
    var table = layui.table;
    var layer = layui.layer;
    var form = layui.form;
    var laydate = layui.laydate;

    var queryCustomer = {
        init: function () {

            laydate.render({
                elem: '#startDate',
                value:new Date(),
            });

            laydate.render({
                elem: '#endDate',
                value:new Date(),
            });

            var ins = table.render({
                elem: "#employee_table",
                height: 550,
                toolbar: '#toolbar',
                defaultToolbar: ['filter'],
                cols: [
                    [
                        {type: 'radio', fixed: 'left'},
                        {field: 'name', title: '客户编码', width: 120, fixed: 'left', align: 'center', templet: '#templetArchive'},
                        {field: 'sex', title: '客户姓名', width: 80, sort: true, align: 'center',},
                        {field: 'typeName', title: '楼盘地址', width: 120, align: 'center', sort: true},
                        {field: 'identityNo', title: '客户代表', width: 200, align: 'center'},
                        {field: 'mobileNo', title: '设计师', width: 150, align: 'center'},
                        {field: 'age', title: '客户电话', width: 80, align: 'center', sort: true},
                        {field: 'inDate', title: '家装顾问', sort: true, align: 'center', width: 120,},
                        {field: 'companyAge', title: '咨询时间', width: 80, sort: true, align: 'center'},
                        {field: 'jobRoleName', title: '申报时间', width: 150, align: 'center'},
                        {field: 'jobRoleNatureName', title: '店名', width: 150, align: 'center'},
                        {field: 'discountRate', title: '客户类型', width: 150, align: 'center'},
                        {field: 'parentEmployeeName', title: '申报人', width: 100, align: 'center'},
                        {field: 'orgPath', title: '报备状态', width: 100, align: 'center'},
                        {field: 'jobAge', title: '裁定备注', width: 120, sort: true, align: 'center'},
                        {field: 'status', title: '信息来源', width: 100, align: 'center'},
                        {field: 'status', title: '活动类型', width: 100, align: 'center'},
                        {field: 'status', title: '活动时间', width: 100, align: 'center'},
                        {field: 'status', title: '客户状态', width: 100, align: 'center'},
                        {field: 'status', title: '电话咨询时间', width: 100, align: 'center'},
                        {field: 'status', title: '客户类型备注', width: 100, align: 'center'},
                        {field: 'status', title: '量房时间', width: 100, align: 'center'},
                        {field: 'status', title: '设计费时间', width: 100, align: 'center'},
                        {field: 'status', title: '看预算时间', width: 100, align: 'center'},
                        {field: 'status', title: '签单时间', width: 100, align: 'center'},
                        {field: 'status', title: '首付时间', width: 100, align: 'center'},
                        {field: 'status', title: '施工时间', width: 100, align: 'center'},
                        {field: 'status', title: '验收时间', width: 100, align: 'center'},
                        {field: 'status', title: '户型', width: 100, align: 'center'},
                        {field: 'status', title: '建筑面积', width: 100, align: 'center'},
                        {field: 'status', title: '回访次数', width: 100, align: 'center'},
                        {field: 'status', title: '最后一次回访时间', width: 100, align: 'center'},
                        {field: 'status', title: '回访类别', width: 100, align: 'center'},
                        {field: 'status', title: '回访内容', width: 100, align: 'center'},
                        {field: 'status', title: '录入时间', width: 100, align: 'center'},
                        {field: 'status', title: '录入人', width: 100, align: 'center'},
                    ]
                ],
                data:[{
                    "name": "2020014233524"
                    ,"sex": "鸿助手测试100"
                    ,"typeName": "长沙市岳麓区保利西海岸A3栋1803"
                    ,"identityNo": "客户代表1"
                    ,"mobileNo": "设计师1"
                    ,"age":"13723885094"
                    ,"inDate": "家装顾问1"
                    ,"companyAge":"2020/01/01 20:12:34"
                    ,"jobRoleName":"2020/01/01 20:12:34"
                    ,"jobRoleNatureName":"袁家岭店"
                }],
                page: true,
                text: {none: '暂无相关数据，请检查查询条件。'},
            });


            $('#queryEmployee').on('click', function () {
                table.reload('employee_table', {
                    page: {
                        curr: 1
                    },
                    loading: true,
                    url: 'api/organization/employee/queryEmployeeList4Page',
                    where: {
                        name: $("input[name='name']").val(),
                        sex: $("select[name='sex']").val(),
                        orgId: $("input[name='orgId']").val(),
                        mobileNo: $("input[name='mobileNo']").val(),
                        employeeStatus: $("select[name='employeeStatus']").val(),
                        type: $("select[name='type']").val(),
                        isBlackList: $("#isBlackList").val(),
                        otherStatus: $('#otherStatus').val(),
                        jobRole: $("select[name='jobRole']").val(),
                        jobRoleNature: $("select[name='jobRoleNature']").val(),
                        discountRate: $("select[name='discountRate']").val(),
                        jobYearStart: $('#jobYearStart').val(),
                        jobYearEnd: $('#jobYearEnd').val(),
                        ageStart: $('#ageStart').val(),
                        ageEnd: $('#ageEnd').val(),
                        inDateStart: $('#inDateStart').val(),
                        inDateEnd: $('#inDateEnd').val(),
                        destroyDateStart: $('#destroyDateStart').val(),
                        destroyDateEnd: $('#destroyDateEnd').val(),
                        companyAgeStart: $('#companyAgeStart').val(),
                        companyAgeEnd: $('#companyAgeEnd').val(),
                    }
                })
            });


            //监听工具栏按钮
            table.on('toolbar(employee_table)', function (obj) {
                var checkStatus = table.checkStatus(obj.config.id); //获取选中行状态
                var data = checkStatus.data;
                var event = obj.event;

                if (event === 'edit') {
                    queryCustomer.edit(data[0]);
                } else if (event === 'visit') {
                    layui.redirect.open('openUrl?url=modules/bss/cust/create_cust_visit', '客户回访');
                }
            });


        },

        edit: function (data) {
            var index = layer.open({
                type: 2,
                title: '客户编辑',
                content: 'openUrl?url=modules/bss/cust/cust_info',
                maxmin: true,
                btn: ['确定', '取消'],
                area: ['550px', '700px'],
                skin: 'layui-layer-molv',
                success: function (layero, index) {
                    var body = layer.getChildFrame('body', index);
                    body.find('#employeeId').val(data.employeeId);
                    body.find('#name').val(data.name);
                    body.find('#identityNo').val(data.identityNo);
                    body.find('#mobileNo').val(data.mobileNo);
                    form.render();
                },
                yes: function (index, layero) {
                    var body = layer.getChildFrame('body', index);
                    var submit = body.find("#employee-remove-submit");
                    submit.click();
                }
            });
            layer.full(index);
        },


        selectOrg: function () {
            layui.orgTree.init('orgTree', 'orgId', 'orgPath', false,false);
        },

    };
    exports('queryCustomer', queryCustomer);
});