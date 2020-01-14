layui.extend({
    orgTree: 'org',
}).define(['ajax', 'table', 'element', 'orgTree', 'layer', 'form', 'select', 'redirect', 'laydate'], function (exports) {
    let $ = layui.$;
    let table = layui.table;
    let layer = layui.layer;
    let form = layui.form;
    let laydate = layui.laydate;

    let createCustVisit = {
        init: function () {
            laydate.render({
                elem: '#preparTime',
                value: new Date()
            });

/*            form.verify({
                verifyTypeValue: function (value) {
                    if (typeof(value)!='number' ) {
                        return '输入不合法';
                    }
                },
            });*/

            /* 监听提交 */
            form.on('submit(save)', function(data){
                parent.layer.alert(JSON.stringify(data.field), {
                    title: '最终的提交信息'
                })
                return false;
            });

                let ins = table.render({
                elem: "#employee_table",
                height: 200,
                toolbar: '#toolbar',
                defaultToolbar: [''],
                loading:false,
                cols: [
                    [
                        {field: 'name', title: '回访类型', fixed: 'left', align: 'center',},
                        {field: 'sex', title: '回访对象', align: 'center'},
                        {field: 'typeName', title: '回访人', align: 'center'},
                        {field: 'identityNo', title: '回访时间', align: 'center'},
                        {field: 'mobileNo', title: '回访方式',  align: 'center'},
                        {field: 'age', title: '回访内容', align: 'center'},
                    ]
                ],
                page: false,
                data:[{
                    "name": "出平面图时回访"
                    ,"sex": "鸿助手测试100"
                    ,"typeName": "设计师1"
                    ,"identityNo": "2020/01/02 12:12:34"
                    ,"mobileNo": "电话"
                    ,"age": "测试"
                }],
                text: {none: '暂无相关数据，请检查查询条件。'},
            });

        },

        selectOrg: function () {
            layui.orgTree.init('orgTree', 'orgId', 'orgPath', false, false);
        },

    };
    exports('createCustVisit', createCustVisit);
});