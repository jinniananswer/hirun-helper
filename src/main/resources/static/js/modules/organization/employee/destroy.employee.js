layui.extend({
    selectEmployee: 'employee'
}).define(['ajax', 'form', 'layer', 'element', 'laydate', 'select', 'selectEmployee'], function (exports) {
    var $ = layui.$;
    var form = layui.form;
    var layer = layui.layer;
    var laydate = layui.laydate;
    var destroyemployee = {
        init: function () {

            layui.ajax.post('api/organization/employee/queryExtendCondition4Destroy', 'employeeId=' + $('#employeeId').val(), function (data) {
                var datas = data.rows;
                if (datas.hasChildEmployee=='NO') {
                    $("#newParent").hide();
                    $("#remind").hide();
                    $("#parentEmployeeName").removeAttr("lay-verify");
                } else {
                    $("#newParent").show();
                    $("#remind").show();
                    $("#parentEmployeeName").attr("lay-verify", "required");
                }
                $("#destroyTimes option[value='"+datas.destroyTimes+"']").prop("selected",true);
                $("#destroyTimes").attr("disabled",true);

                if(datas.destroyTimes>=2){
                    $("#isBlackList option[value='1']").prop("selected",true);
                    $("#isBlackList").attr("disabled",true);
                }
                form.render("select");
            });


            layui.select.init('destroyWay', 'DESTROY_WAY', '1', false);
            layui.select.init('isBlackList', 'YES_NO', '2', false);


            laydate.render({
                elem: '#destroyDate',
            });

            laydate.render({
                elem: '#socialSecurityEnd',
            });

            //根据选择离职次数决定员工是否永不录取
            form.on('select(destroyTimes)', function (data) {
                if (data.value == 2) {
                    $("#isBlackList option[value='1']").prop("selected",true);
                    $("#isBlackList").attr("disabled",true);
                } else {
                    $("#isBlackList option[value='2']").prop("selected",true);
                    $("#isBlackList").removeAttr("disabled");
                }
                form.render("select");
            });

            form.on('submit(employee-remove-submit)', function (data) {
                var field = data.field; //获取提交的字段
                var index = parent.layer.getFrameIndex(window.name);
                $.ajax({
                    url: 'api/organization/employee/destroyEmployee',
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
                                parent.layui.table.reload('employee_table'); //重载表格
                                parent.layer.close(index); //再执行关闭
                            }, function () {
                                top.layui.admin.closeThisTabs();
                            });
                        } else {
                            parent.layer.msg("提交失败" + data.message, {icon: 5});
                        }
                    }
                });

            });
        },

        selectNewParent: function () {
            layui.selectEmployee.init('employeeList', 'employeeSearch', 'newParentEmployeeId', 'parentEmployeeName', false);
        },


    };
    exports('destroyemployee', destroyemployee);
});