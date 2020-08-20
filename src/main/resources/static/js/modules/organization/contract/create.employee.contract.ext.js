layui.extend({
}).define(['ajax', 'form', 'layer', 'element', 'laydate', 'select'], function (exports) {
    var $ = layui.$;
    var form = layui.form;
    var layer = layui.layer;
    var laydate = layui.laydate;
    var createEmployeeContractExt = {
        init: function () {

            layui.select.init('agreementType','AGREEMENT_TYPE',null,true,'请选择协议类型');
            //layui.select.init('probation','PROBATION',null,true);


            laydate.render({
                elem: '#contractStartTime',
            });

            laydate.render({
                elem: '#contractSignTime',
            });

            laydate.render({
                elem: '#contractEndTime'
            });

            form.on('select(agreementType)',function (data) {
                if(data.value==6){
                    createEmployeeContractExt.timeComponentsController('show');
                    $("#probationDiv").hide();
                    $("#probation").removeAttr("lay-verify");
                }else if(data.value==9){
                    createEmployeeContractExt.timeComponentsController('show');
                    //$("#probationDiv").show();
                    //$("#probationDiv").hide();
                    //$("#probation").removeAttr("lay-verify");
                } else{
                    createEmployeeContractExt.timeComponentsController('hide');
                    //$("#probationDiv").hide();
                    //$("#probation").removeAttr("lay-verify");
                }
                form.render('select', 'contractType');
            });

            form.on('submit(create-employeeContract-submit)', function (data) {
                var field = data.field; //获取提交的字段

                 if(field.contractType==7 ||field.contractType==8 ||field.contractType==10){
                    field.contractStartTime='';
                    field.contractEndTime='';
                }
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

        timeComponentsController: function (type) {
            if(type=='show'){
                $("#end").show();
                $("#start").show();
                $("#contractEndTime").attr("lay-verify","required");
                $("#contractStartTime").attr("lay-verify","required");
            }else{
                $("#start").hide();
                $("#end").hide();
                $("#contractEndTime").removeAttr("lay-verify");
                $("#contractStartTime").removeAttr("lay-verify");
            }
        }

    };
    exports('createEmployeeContractExt', createEmployeeContractExt);
});