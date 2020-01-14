layui.extend({
    orgTree: 'org',
}).define(['ajax', 'table', 'element', 'orgTree', 'layer', 'form', 'select', 'redirect', 'laydate'], function (exports) {
    let $ = layui.$;
    let table = layui.table;
    let layer = layui.layer;
    let form = layui.form;
    let laydate = layui.laydate;

    let createPreparation = {
        init: function () {

            layui.select.init('houseMode', 'HOUSE_MODE', '', true);

            let now= new Date();
            let year=now.getFullYear();
            let month = now.getMonth();
            let date = now.getDate();
            let hour = now.getHours();
            let minu = now.getMinutes();//得到分钟
            let sec = now.getSeconds();//得到秒
            $('#custNo').val(''+year+month+date+hour+minu+sec+'');

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

            form.on('select(custType)',function (data) {
                if(data.value=='2'){
                    document.getElementById("oldPlace").disabled=false;
                    document.getElementById("oldCustName").disabled=false;
                    document.getElementById("oldCustMobile").disabled=false;
                }else{
                    document.getElementById("oldPlace").disabled=true;
                    document.getElementById("oldCustName").disabled=true;
                    document.getElementById("oldCustMobile").disabled=true;
                    $('#oldPlace').val('');
                    $('#oldCustName').val('');
                    $('#oldCustMobile').val('');
                }
            });

                    let ins = table.render({
                elem: "#employee_table",
                height: 200,
                toolbar: '#toolbar',
                defaultToolbar: [''],
                loading:false,
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
                        {field: 'name', title: '申报人', fixed: 'left', align: 'center',},
                        {field: 'sex', title: '申报时间', align: 'center'},
                        {field: 'typeName', title: '上门时间', align: 'center'},
                        {field: 'identityNo', title: '报备状态', align: 'center'},
                        {field: 'mobileNo', title: '录入人',  align: 'center'},
                        {field: 'age', title: '楼盘地址', align: 'center'},
                        {field: 'companyAge', title: '备注', align: 'center'},
                    ]
                ],
                page: false,
                text: {none: '暂无相关数据，请检查查询条件。'},
            });

        },

        selectOrg: function () {
            layui.orgTree.init('orgTree', 'orgId', 'orgPath', false, false);
        },

    };
    exports('createPreparation', createPreparation);
});