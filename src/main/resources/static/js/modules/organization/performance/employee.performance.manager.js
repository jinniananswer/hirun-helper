layui.extend({
    orgTree: 'org'
}).define(['ajax', 'table', 'element', 'orgTree', 'layer', 'form', 'select','laydate'], function (exports) {
    var $ = layui.$;
    var table = layui.table;
    var layer = layui.layer;
    var form = layui.form;
    var laydate = layui.laydate;

    var employeePerformanceManager = {
        init: function () {

            laydate.render({
                elem: '#year',
                type: 'year',
                value:new Date()
            });

            layui.select.init('performance', 'PERFORMANCE_LEVEL', null, true);
            layui.select.init('jobRoleNature', 'JOB_NATURE', null, true);


            table.render({
                elem: "#employee_performance_table",
                height: 550,
                loading: false,
                toolbar: '#toolbar',
                defaultToolbar: ['filter'],
                defaultToolbar: [''],
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
                        {field: 'employeeName', title: '姓名', width: 120, fixed: 'left', align: 'center'},
                        {field: 'jobRoleName', title: '岗位',width: 180, align: 'center'},
                        {field: 'orgPath', title: '部门',width: 300, align: 'center'},
                        {field: 'jobRoleNatureName', title: '岗位性质',width: 300, align: 'center'},
                        {field: 'year', title: '年份', width: 200, align: 'center'},
                        {field: 'performanceName', title: '绩效', width: 150, align: 'center'},
                        {field: 'remark', title: '备注',  align: 'center'},
                        {align: 'center', title: '操作', width: 150, fixed: 'right',toolbar: '#bar'}
                    ]
                ],
                page: true,
                text: {none: '暂无相关数据，请检查查询条件。'},
            });


            $('#queryEmployee').on('click', function () {
                table.reload('employee_performance_table', {
                    page: {
                        curr: 1
                    },
                    loading:true,
                    url :'api/organization/employee-performance/employeePerformanceList',
                    where: {
                        name: $("input[name='name']").val(),
                        orgSet: $("input[name='orgSet']").val(),
                        year: $("input[name='year']").val(),
                        performance: $("select[name='performance']").val(),
                        jobRoleNature: $("select[name='jobRoleNature']").val(),
                    }
                })
            });


            //监听工具栏按钮
            table.on('toolbar(employee_performance_table)', function (obj) {
                var checkStatus = table.checkStatus(obj.config.id); //获取选中行状态
                var data = checkStatus.data;
                var event = obj.event;

               if (event === 'importEmployeePerformance') {
                   employeePerformanceManager.importEmployeePerformance();
               } else if(event==='crateEmployeePerformance'){
                   employeePerformanceManager.add();
               }else if(event==='export'){
                   employeePerformanceManager.export();
               }

            });

            table.on('tool(employee_performance_table)', function (obj) {
                var data = obj.data;//获取当前行数据
                var layEvent = obj.event;
                if (layEvent === 'edit') {
                    employeePerformanceManager.edit(data);
                }
            });


        },

        importEmployeePerformance: function () {
             layer.open({
                type: 2,
                title: '员工绩效导入',
                content: 'openUrl?url=modules/organization/performance/performance_import',
                maxmin: true,
                 offset: ['150px', '350px'],
                 area: ['800px', '500px'],
                skin: 'layui-layer-molv',
            });
        },

        add :function () {
            var index = layer.open({
                type: 2,
                title: '新增员工绩效',
                content: 'openUrl?url=modules/organization/performance/create_employee_performance',
                maxmin: true,
                btn: ['保存', '取消'],
                skin: 'layui-layer-molv',
                yes: function (index, layero) {
                    var body = layer.getChildFrame('body', index);
                    var submit = body.find("#add-employeePerformance-submit");
                    submit.click();
                }
            });
            layer.full(index);
        },

        edit: function (data) {
            var index = layer.open({
                type: 2,
                title: '修改员工绩效',
                content: 'openUrl?url=modules/organization/performance/update_employee_performance',
                maxmin: true,
                btn: ['确定', '取消'],
                area: ['550px', '700px'],
                skin: 'layui-layer-molv',
                success: function (layero, index) {
                    var body = layer.getChildFrame('body', index);
                    body.find('#employeeId').val(data.employeeId);
                    body.find('#remark').val(data.remark);
                    body.find('#id').val(data.id);
                    body.find('#employeeName').val(data.employeeName);
                    body.find('#year').val(data.year);
                    body.find('#performanceValue').val(data.performance);

                    form.render();
                },
                yes: function (index, layero) {
                    var body = layer.getChildFrame('body', index);
                    var submit = body.find("#update-employeePerformance-submit");
                    submit.click();
                }
            });
            layer.full(index);
        },

        selectOrg: function () {
            layui.orgTree.init('orgTree', 'orgSet', 'orgPath', true);
        },


        export: function(){
            var param='?name='+$("input[name='name']").val()+'&orgSet='+$("input[name='orgSet']").val()+'&year='+$("input[name='year']").val()+'&performance='
                +$("select[name='performance']").val()+'&jobRoleNature='+$("select[name='jobRoleNature']").val();
            window.location.href = "api/organization/employee-performance/exportEmployee4ImportPerformance"+param;
        },


    };
    exports('employeePerformanceManager', employeePerformanceManager);
});