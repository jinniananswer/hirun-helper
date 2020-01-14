layui.extend({
    orgTree: 'org',
    time : 'time'
}).define(['ajax', 'select', 'form', 'layer', 'laydate', 'laytpl', 'element', 'orgTree', 'table', 'redirect','time'],function(exports){
    let $ = layui.$;
    let form = layui.form;
    let layer = layui.layer;
    let table = layui.table;
    let laydate = layui.laydate;
    let charge = {
        currentTab : 1,
        workExpIndex : 0,
        childrenIndex : 0,
        registerPicker : null,
        homePicker : null,
        init : function() {

            charge.initRoleTable();

            form.on('submit(btnSubmit)', function(data){
                layui.charge.create(data.field);
                return false;
            });

            laydate.render({
                elem: '#contractsigningTime'
            });
            laydate.render({
                elem: '#contractendTime'
            });
            laydate.render({
                elem: '#collectionDate'
            });

        },
        initRoleTable: function () {
            table.render({
                elem: '#role-table',
                toolbar: '#roleToolbar',
                defaultToolbar: ['filter'],
                height: 'full-20',
                url: 'api/user/role/role-list',
                fitColumns: true,
                response: {
                    msgName: 'message',
                    countName: 'total',
                    dataName: 'rows'
                },
                cols: [[
                    {field: 'roleId', title: '款项', width: 60, align: 'center'},
                    {field: 'roleName', title: '期数', width: 80,align: 'center'},
                    {field: 'roleId', title: '品牌', width: 60, align: 'center'},
                    {field: 'roleName', title: '店名', align: 'center'},
                    {field: 'roleId', title: '是否收齐', width: 60, align: 'center'},
                    {field: 'roleName', title: '金额', align: 'center'},
                    {field: 'roleId', title: '是否收齐', width: 60, align: 'center'},
                    {field: 'roleName', title: '设计师', align: 'center'},
                    {field: 'roleId', title: '设计师级别', width: 60, align: 'center'},
                    {field: 'roleName', title: '橱柜设计师', align: 'center'},
                    {field: 'roleId', title: '客户代表', width: 60, align: 'center'},
                    {field: 'roleName', title: '项目经理', align: 'center'},
                    {field: 'remark', title: '备注', },
                    {fixed: 'right', align: 'center', title: '操作', width: 150, fixed: 'right', toolbar: '#roleBar'}
                ]],
                page: false,
                text: {none: '暂无相关数据，请检查查询条件。'}
            });
        },
    };
    exports('charge', charge);
});