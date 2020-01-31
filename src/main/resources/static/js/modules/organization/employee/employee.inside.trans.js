layui.extend({
    orgTree: 'org',
    citypicker: 'city-picker/city-picker',
    selectEmployee: 'employee'
}).define(['ajax', 'form', 'layer', 'element', 'laydate', 'select','orgTree','selectEmployee','citypicker'], function (exports) {
    let $ = layui.$;
    let form = layui.form;
    let layer = layui.layer;
    let laydate = layui.laydate;
    let employeeInsideTrans = {
        init: function () {

            layui.select.init('jobRoleNature', 'JOB_NATURE', '1', false);
            layui.select.init('jobRole', 'JOB_ROLE', null, true, '请选择或搜索岗位');
            layui.select.init('jobGrade', 'JOB_GRADE', null, true);

            let homePicker = new layui.citypicker("#home-city-picker", {
                provincename:"homeProv",
                cityname:"homeCity",
                districtname: "homeRegion",
                level: 'homeRegion',// 级别
            });
            homePicker.setValue("湖南省/长沙市/天心区");

            layui.ajax.get('api/organization/employee/load', 'employeeId=' + $('#employeeId').val(), function (data) {
                var detail = data.rows;
                if (detail == null) {
                    return;
                }
                form.val("tranorg-confirm-form",detail);

                let home = detail.home;
                if (home != null) {
                    homePicker.setValue(home);
                }

                let orgPath = detail.employeeJobRole.orgPath;
                if (orgPath != null) {
                    $("#orgPath").val(orgPath);
                }

                let orgId = detail.employeeJobRole.orgId;
                if (orgId != null) {
                    $(document.getElementById("orgId")).val(orgId);
                }

                let jobRole = detail.employeeJobRole.jobRole;
                if (jobRole != null) {
                    $("#jobRole").val(jobRole);
                    form.render('select', 'jobRole');
                }

                let jobGrade = detail.employeeJobRole.jobGrade;
                if (jobRole != null) {
                    $("#jobGrade").val(jobGrade);
                    form.render('select', 'jobGrade');
                }

                let jobRoleNature = detail.employeeJobRole.jobRoleNature;
                if (jobRoleNature != null) {
                    $("#jobRoleNature").val(jobRoleNature);
                    form.render('select', 'jobRoleNature');
                }

                let discountRate = detail.employeeJobRole.discountRate;
                if (discountRate != null) {
                    $(document.getElementById("discountRate")).val(discountRate);
                }

                let parentEmployeeName = detail.employeeJobRole.parentEmployeeName;
                if (parentEmployeeName != null) {
                    $("#parentEmployeeName").val(parentEmployeeName);
                }

                let parentEmployeeId = detail.employeeJobRole.parentEmployeeId;
                if (parentEmployeeId != null) {
                    $(document.getElementById("parentEmployeeId")).val(parentEmployeeId);
                }
            });


            laydate.render({
                elem: '#startTime',
                type: 'datetime',
                done: function(value,date){
                    this.dateTime.hours=0;
                    this.dateTime.minutes=0;
                    this.dateTime.seconds=0;
                },
            });

            laydate.render({
                elem: '#endTime',
                type: 'datetime',
                format: 'yyyy-MM-dd HH:mm:ss',
                done: function(value,date){
                    this.dateTime.hours=23;
                    this.dateTime.minutes=59;
                    this.dateTime.seconds=59;
                },
            });

            form.on('select(transType)',function (data) {
                if(data.value==1){
                    $("#end").show();
                    $("#endTime").attr("lay-verify","required");
                    $("#endTime").removeAttr("disabled");
                }else{
                    $("#end").hide();
                    $("#endTime").attr("disabled","true");
                    $("#endTime").removeAttr("lay-verify");
                }
            });

            form.on('select(jobRoleNature)', function(data) {
                layui.employeeInsideTrans.calculateDiscountRate();
            });


            form.on('submit(confirm-submit)', function (data) {
                let field = data.field; //获取提交的字段
                let index = parent.layer.getFrameIndex(window.name);
                $.ajax({
                    url: 'api/organization/hr-pending/confirmTransPending',
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
                                parent.layui.table.reload('employeeTransTable'); //重载表格
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

        selectOrg : function() {
            layui.orgTree.init('orgTree', 'orgId', 'orgPath', false);
        },

        selectParentEmployee : function() {
            layui.selectEmployee.init('parentEmployeeList', 'employeeSearch', 'parentEmployeeId', 'parentEmployeeName', false);
        },

        calculateDiscountRate : function(data) {
            let jobRoleNature = $(document.getElementById("jobRoleNature")).val();
            let orgId = $(document.getElementById("orgId")).val();
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
    exports('employeeInsideTrans', employeeInsideTrans);
});