layui.extend({
    orgTree: 'org'
}).define(['ajax', 'table', 'element', 'layer', 'form','select'], function (exports) {
    var $ = layui.$;
    var table = layui.table;
    var layer = layui.layer;
    var form = layui.form;
    var pendingManager = {
        init: function () {

            layui.select.init('pendingType', 'PENDING_TYPE', '', true);

            table.render({
                elem: "#pending_table",
                height: 550,
                url: 'api/organization/hr-pending/queryPendingByExecuteId',
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
                        {field: 'employeeId', title: '姓名', width: 120,fixed :'left'},
                        {field: 'pendingType', title: '待办类型', width: 120,templet : function (d) {
                                if(d.pendingType==1){
                                    return '借调';
                                }else if(d.pendingType==2) {
                                    return '调出';
                                }
                        }},
                        {field: 'startTime', title: '开始时间', width: 200},
                        {field: 'endTime', title: '结束时间', width: 200},
                        {field: 'pendingStatus', title: '待办状态', width: 100, templet: function (d) {
                                if (d.pendingStatus == 1) {
                                    return '未处理';
                                } else if (d.pendingStatus == 2) {
                                    return '已处理';
                                } else if (d.pendingStatus == 3) {
                                    return '删除';
                                }
                            }
                        },
                        {field: 'pendingCreateId', title: '创建人', width: 150},
                        {field: 'createTime', title: '创建时间', width: 200},
                        {field: 'pendingExecuteId', title: '执行人', width: 150},
                        {field: 'remark', title: '备注', width: 150},
                        {align: 'center', title: '操作',width: 150, fixed: 'right',templet:'#operateTmp'}
                    ]
                ],
                page: true,
                text: {
                    none: '暂无相关数据，请检查查询条件。'
                }
            });


            $('#queryPending').on('click', function () {
                table.reload('pending_table', {
                    page: {
                        curr: 1
                    },
                    where: {
                        pendingType: $("select[name='pendingType']").val(),
                        pendingStatus: $("select[name='pendingStatus']").val(),
                    }
                })
            });

            table.on('tool(pending_table)', function (obj) {
                var data = obj.data;//获取当前行数据
                var layEvent = obj.event;
                if (layEvent === 'operate') {
                    pendingManager.operate(data);
                }
            });

            //监听工具栏新增按钮
            $('.layui-btn-container .layui-btn').on('click', function () {
               layui.redirect.open('openUrl?url=modules/organization/employee/create_employee','新建员工档案');
            });

        },

        operate: function (data) {
            var index=layer.open({
                type: 2,
                title: '员工调动确认',
                content: 'openUrl?url=/modules/organization/hr/transorg_pending_confirm',
                maxmin: true,
                btn: ['确定', '取消'],
                area: ['550px', '700px'],
                skin: 'layui-layer-molv',
                success: function (layero, index) {
                    var body = layer.getChildFrame('body', index);
                    body.find('#employeeId').val(data.employeeId);
                    body.find('#name').val(data.employeeId);
                    body.find('#id').val(data.id);
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
    exports('pendingManager', pendingManager);
});