layui.extend({
    orgTree: 'org',
    citypicker: 'city-picker/city-picker',
    selectEmployee: 'employee'
}).define(['ajax', 'form', 'layer', 'element', 'laydate', 'select','orgTree','selectEmployee','citypicker'], function (exports) {
    var $ = layui.$;
    var form = layui.form;
    var layer = layui.layer;
    var laydate = layui.laydate;
    var transReturnConfirm = {
        init: function () {


            layui.ajax.post('api/organization/employee-trans-detail/queryDetail4Confirm?relPendingId='+$('#relPendingId').val(), '', function(data) {
                var detail = data.rows;
                layui.select.init('jobRoleNature', 'JOB_NATURE', detail.sourceJobRoleNature, false);
                layui.select.init('jobRole', 'JOB_ROLE', detail.sourceJobRole, true, '请选择或搜索岗位');
                layui.select.init('jobGrade', 'JOB_GRADE', detail.sourceJobGrade,true,'');

                $('#orgId').val(detail.sourceOrgId);
                $('#orgPath').val(detail.sourceOrgPath);
                $('#discountRate').val(detail.sourceDiscountRate);
                $('#parentEmployeeName').val(detail.sourceParentEmployeeName);
                $('#parentEmployeeId').val(detail.sourceParentEmployeeId);
                $('#id').val(detail.id);
                $('#employeeId').val(detail.employeeId);
            });


            form.on('select(jobRoleNature)', function(data) {
                layui.transPendingConfirm.calculateDiscountRate();
            });

            var homePicker = new layui.citypicker("#home-city-picker", {
                provincename:"homeProv",
                cityname:"homeCity",
                districtname: "homeRegion",
                level: 'homeRegion',// 级别
            });
            homePicker.setValue("湖南省/长沙市/天心区");

            form.on('submit(confirm-submit)', function (data) {
                var field = data.field; //获取提交的字段
                var index = parent.layer.getFrameIndex(window.name);
                $.ajax({
                    url: 'api/organization/employee-trans-detail/confirmReturnDetail',
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
                                parent.layui.table.reload('pending_table'); //重载表格
                                parent.layer.close(index); //再执行关闭
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

        selectOrg : function() {
            layui.orgTree.init('orgTree', 'orgId', 'orgPath', false);
        },

        selectParentEmployee : function() {
            layui.selectEmployee.init('parentEmployeeList', 'employeeSearch', 'parentEmployeeId', 'parentEmployeeName', false);
        },

        calculateDiscountRate : function(data) {
            var jobRoleNature = $(document.getElementById("jobRoleNature")).val();
            var orgId = $(document.getElementById("orgId")).val();
            console.log(jobRoleNature,orgId);
            if (orgId == null || orgId == "") {
                this.selectOrg();
                return;
            } else {
                layui.ajax.post('api/organization/employee/calculateDiscountRate', '&orgId='+orgId+"&jobRoleNature="+jobRoleNature, function(data){
                    $(document.getElementById("discountRate")).val(data.rows.discountRate);
                });
            }
        },

    };
    exports('transReturnConfirm', transReturnConfirm);
});