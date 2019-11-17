layui.extend({
}).define(['ajax', 'table', 'element','layer', 'form','select','redirect'], function (exports) {
    var $ = layui.$;
    var table = layui.table;
    var layer = layui.layer;
    var form = layui.form;
    var employeePenaltyManager = {
        init: function () {

            layui.select.init('penaltyType', 'PENALTY_TYPE', '', true);

            table.render({
                elem: "#penalty_table",
                height: 550,
                url: 'api/organization/employee-penalty/queryPenaltyList',
                loading: true,
                toolbar: '#toolbar',
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
                        {field: 'employeeName', title: '姓名',fixed: 'left', width: 120,fixed :'left',align: 'center'},
                        {field: 'orgPath', title: '部门',align: 'center'},
                        {field: 'jobRoleName', title: '岗位',align: 'center'},
                        {field: 'penaltyType', title: '奖惩类型', width: 120,align: 'center',templet:function (d) {
                                if(d.penaltyType==1){return '奖励';}else{return "惩罚"}
                            }},
                        {field: 'penaltyTime', title: '事件时间',align: 'center'},
                        {field: 'penaltyScore', title: '事件分数',align: 'center'},
                        {field: 'penaltyContent', title: '事件内容',align: 'center'},
                        {field: 'remark', title: '备注',align: 'center'},
                        {align: 'center', title: '操作', fixed: 'right',width:220,templet:'#operateTmp'}
                    ]
                ],
                page: true,
                text: {
                    none: '暂无相关数据，请检查查询条件。'
                }
            });


            $('#queryEmployeePenalty').on('click', function () {
                table.reload('penalty_table', {
                    page: {
                        curr: 1
                    },
                    where: {
                        employeeName: $("input[name='employeeName']").val(),
                        penaltyType: $("select[name='penaltyType']").val(),
                    }
                })
            });

            table.on('tool(penalty_table)', function (obj) {
                var data = obj.data;//获取当前行数据
                var layEvent = obj.event;
                if (layEvent === 'delete') {
                    employeePenaltyManager.delete(data);
                }else if(layEvent==='edit'){
                    employeePenaltyManager.edit(data);
                }
            });

            //监听工具栏新增按钮
            table.on('toolbar(penalty_table)',function (obj) {
                var event = obj.event;
                if(event==='create'){
                    employeePenaltyManager.add();
                }
            });
        },

        add :function () {
            var index = layer.open({
                type: 2,
                title: '新增员工奖惩',
                content: 'openUrl?url=/modules/organization/penalty/create_employee_penalty',
                maxmin: true,
                btn: ['保存', '取消'],
                skin: 'layui-layer-molv',
                yes: function (index, layero) {
                    var body = layer.getChildFrame('body', index);
                    var submit = body.find("#add-employeePenalty-submit");
                    submit.click();
                }
            });
            layer.full(index);
        },

        delete: function (data) {
            layer.confirm('是否确定删除',
                {btn: ['确定', '取消']}, function (index, layero) {
                    $.ajax({
                        url: 'api/organization/employee-penalty/deleteEmployeePenalty',
                        type: 'POST',
                        data: 'id=' + data.id,
                        success: function (data) {
                            if (data.code == 0) {
                                layer.confirm('操作成功，点击确定按钮刷新本页面，点击关闭按钮关闭本界面？', {
                                    btn: ['确定', '关闭'],
                                    closeBtn: 0,
                                    icon: 6,
                                    title: '提示信息',
                                    shade: [0.5, '#fff'],
                                    skin: 'layui-layer-admin layui-anim'
                                }, function (index) {
                                    table.reload('penalty_table'); //重载表格
                                    layer.close(index);
                                }, function () {
                                    top.layui.admin.closeThisTabs();
                                });
                            } else {
                                parent.layer.msg("提交失败", {icon: 5});
                            }
                        }
                    });
                });
        },

        edit: function (data) {
            var index = layer.open({
                type: 2,
                title: '员工奖惩修改',
                content: 'openUrl?url=/modules/organization/penalty/update_employee_penalty',
                maxmin: true,
                btn: ['确定', '取消'],
                area: ['550px', '700px'],
                skin: 'layui-layer-molv',
                success: function (layero, index) {
                    var body = layer.getChildFrame('body', index);
                    body.find('#employeeId').val(data.employeeId);
                    body.find('#penaltyTime').val(data.penaltyTime.substr(0, 10));
                    body.find('#penaltyScore').val(data.penaltyScore);
                    body.find('#remark').val(data.remark);
                    body.find('#penaltyContent').val(data.penaltyContent);
                    body.find('#id').val(data.id);
                    body.find('#employeeName').val(data.employeeName);
                    body.find('#penaltyTypeValue').val(data.penaltyType);
                    form.render();
                },
                yes: function (index, layero) {
                    var body = layer.getChildFrame('body', index);
                    var submit = body.find("#update-employeePenalty-submit");
                    submit.click();
                }
            });
            layer.full(index);
        },

    };
    exports('employeePenaltyManager', employeePenaltyManager);
});