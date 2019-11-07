layui.extend({
}).define(['ajax', 'form', 'layer', 'element', 'laydate', 'select'], function (exports) {
    var $ = layui.$;
    var form = layui.form;
    var layer = layui.layer;
    var laydate = layui.laydate;
    var createEmployeeContract = {
        init: function () {

            layui.select.init('contractType','CONTRACT_TYPE','',false);


            var startTime=laydate.render({
                elem: '#contractStartTime',
            });

            laydate.render({
                elem: '#contractSignTime',
            });

            var endTime=laydate.render({
                elem: '#contractEndTime'
            });

            form.on('select(contractType)',function (data) {
                if(data.value==5 || data.value==4){
                    $("#start").hide();
                    $("#end").hide();
                    $("#contractEndTime").removeAttr("lay-verify");
                    $("#contractStartTime").removeAttr("lay-verify");
                }else{
                    $("#end").show();
                    $("#start").show();
                    $("#contractEndTime").attr("lay-verify","required");
                    $("#contractStartTime").attr("lay-verify","required");
                }
                form.render('select', 'contractType');
            });

            form.on('submit(create-employeeContract-submit)', function (data) {
                var field = data.field; //获取提交的字段
                var index = parent.layer.getFrameIndex(window.name);
                $.ajax({
                    url: 'api/organization/employee-contract/createEmployeeContract',
                    type: 'POST',
                    data: field,
                    success: function (data) {
                        if (data.code == 0) {
                            layer.confirm('操作成功，点击确定按钮刷新本页面，点击关闭按钮关闭本界面？', {
                                btn: ['确定', '关闭'],
                                closeBtn: 0,
                                icon: 6,
                                title: '提示信息',
                                shade: [0.5, '#fff'],
                                skin: 'layui-layer-admin layui-anim'
                            }, function () {
                                parent.layui.table.reload('employeeContractTable'); //重载表格
                                parent.layer.close(index); //再执行关闭
                            }, function () {
                                top.layui.admin.closeThisTabs();
                            });
                        } else {
                            parent.layer.msg("提交失败:"+data.message, {icon: 5});
                        }
                    }
                });
            });
        },

    };
    exports('createEmployeeContract', createEmployeeContract);
});