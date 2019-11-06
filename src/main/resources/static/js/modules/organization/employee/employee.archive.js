layui.extend({
    orgTree: 'org',
    citypicker: 'city-picker/city-picker',
    selectEmployee: 'employee',
    time : 'time'
}).define(['ajax', 'select', 'form', 'layer', 'laydate', 'laytpl', 'element', 'orgTree', 'citypicker', 'selectEmployee', 'redirect','time'],function(exports){
    var $ = layui.$;
    var form = layui.form;
    var layer = layui.layer;
    var laydate = layui.laydate;
    var employee = {
        currentTab : 1,
        workExpIndex : 0,

        init : function() {
            if ($("#operType").val() == "create") {
                $("#operTypeArea").css("display", "");
            } else {
                $("#operTypeArea").css("display", "none");
            }
            layui.select.init('createType', 'CREATE_EMPLOYEE_TYPE', '1', false);
            layui.select.init('type', 'EMPLOYEE_TYPE', '1', false);
            layui.select.init('birthdayType', 'BIRTHDAY_TYPE', '1', false);
            layui.select.init('jobRoleNature', 'JOB_NATURE', '1', false);
            layui.select.init('jobRole', 'JOB_ROLE', null, true, '请选择或搜索岗位');
            layui.select.init('firstEducationLevel', 'EDUCATION_LEVEL', '3', false);
            layui.select.init('educationLevel', 'EDUCATION_LEVEL', '3', false);
            layui.select.init('schoolType', 'SCHOOL_TYPE', '1', false);

            form.on('select(employeeJobRole.jobRoleNature)', function(data) {
                layui.employee.calculateDiscountRate();
            });

            form.on('select(createTypeFilter)', function(data) {
                var createType = $("#createType").val();
                if (createType == "3") {
                    $("#type").val("3");
                    form.render('select', 'type');
                }
            });

            layui.element.on('tab(employeeTab)', function(data){
                layui.employee.currentTab = data.index + 1;
                if (layui.employee.currentTab <= 1) {
                    $("#previous").css("display", "none");
                } else {
                    $("#previous").css("display", "");
                }
            });

            laydate.render({
                elem: '#birthday'
            });

            laydate.render({
                elem : '#inDate',
                value: new Date(),
                done : function(value, date, endDate) {
                    var regularDate = layui.time.addMonth(value, 3);
                    $("#regularDate").val(regularDate);
                }
            });

            var regularDate = layui.time.addMonth(layui.time.format(new Date()), 3);

            laydate.render({
                elem : '#regularDate',
                value : regularDate
            });

            laydate.render({
                elem : '#jobDate',
                done : function(value, date, endDate) {
                    layui.employee.calculateJobYear(value);
                }
            });

            laydate.render({
                elem: '#workStartDate_0',
                type: 'month'
            });

            laydate.render({
                elem: '#workEndDate_0',
                type: 'month'
            });


            var registerPicker = new layui.citypicker("#city-picker", {
                provincename:"nativeProv",
                cityname:"nativeCity",
                districtname: "nativeRegion",
                level: 'nativeRegion',// 级别
            });
            registerPicker.setValue("湖南省/长沙市/天心区");

            var homePicker = new layui.citypicker("#home-city-picker", {
                provincename:"homeProv",
                cityname:"homeCity",
                districtname: "homeRegion",
                level: 'homeRegion',// 级别
            });
            homePicker.setValue("湖南省/长沙市/天心区");

            form.on('submit(btnSubmit)', function(data){
                layui.employee.create(data.field);
                return false;
            });

            var employeeId = $("#employeeId").val();
            if (employeeId != '') {
                this.loadEmployee(employeeId);
            }
        },

        verifyMobileNo : function() {
            var mobileNo = $("#mobileNo").val();
            if (mobileNo.length != 11) {
                return;
            }

            layui.ajax.post('api/organization/employee/verifyMobileNo','&mobileNo='+mobileNo, function(data){});
        },

        selectOrg : function() {
            layui.orgTree.init('orgTree', 'employeeJobRole.orgId', 'orgPath', false);
        },

        selectParentEmployee : function() {
            layui.selectEmployee.init('parentEmployeeList', 'employeeSearch', 'employeeJobRole.parentEmployeeId', 'parentEmployeeName', false);
        },

        previous : function() {
            this.currentTab--;
            if (this.currentTab <= 1) {
                $("#previous").css("display", "none");
            }

            layui.element.tabChange('employeeTab', this.currentTab);
            return false;
        },

        next : function() {
            this.currentTab++;
            if (this.currentTab > 1) {
                $("#previous").css("display", "");
            }
            layui.element.tabChange('employeeTab', this.currentTab);
            return false;
        },

        analyzeIdentityNo : function() {
            var identityNo = $("#identityNo").val();
            if (identityNo == null || identityNo == "" || identityNo.length != 18) {
                return;
            }
            var birthday = identityNo.substring(6, 10) + "-" + identityNo.substring(10, 12) + "-" + identityNo.substring(12, 14);

            var year = layui.time.getYearDiff(birthday);
            $("")
            laydate.render({
                elem: '#birthday',
                value: birthday
            });

            var sex = parseInt(identityNo.substring(16, 1)) % 2;
            if (sex == 1) {
                $("#male").prop("checked", "true");
                form.render("radio");
            } else {
                $("#female").prop("checked", "true");
                form.render("radio");
            }

            this.verifyIdentityNo();
        },

        verifyIdentityNo : function() {
            var identityNo = $("#identityNo").val();
            if (identityNo.length != 18) {
                return;
            }

            var createType = $("#createType").val();

            layui.ajax.post('api/organization/employee/verifyIdentityNo', '&createType='+createType+'&identityNo='+identityNo, function(data){
                var employee = data.rows;
                if (employee == null) {
                    return;
                }
                var status = employee.status;
                if (status == "3") {
                    layui.employee.renderCreateType("该证件号码的员工"+employee.name+"已离职，是否做复职处理？", "2", employee.employeeId);
                } else if (status == "1") {
                    layui.employee.renderCreateType("该证件号码的员工"+employee.name+"已退休，是否做返聘处理？", "3", employee.employeeId);
                }
            });
        },

        verifyMobileNo : function() {
            var mobileNo = $("#mobileNo").val();
            if (mobileNo == null || mobileNo.trim().length <= 0) {
                return;
            }

            layui.ajax.post('api/organization/employee/verifyMobileNo', '&mobileNo='+mobileNo, function(data){

            });
        },

        renderCreateType : function(tips, createType, employeeId) {
            layer.confirm(tips, {
                btn : ['确定','关闭'],
                closeBtn : 0,
                icon : 6,
                title : '提示信息',
                shade : [0.5, '#fff'],
                skin : 'layui-layer-admin layui-anim'
            },function(index) {
                $("#createType").val(createType);
                form.render('select', 'createType');
                layui.employee.loadEmployee(employeeId);
                layer.close(index);
            });
        },

        loadEmployee : function(employeeId) {
            layui.ajax.post('api/organization/employee/loadAbnormal', '&employeeId='+employeeId, function(data){
                var employee = data.rows;
                layui.employee.refreshEmployee(employee);
                if (createType == "3") {
                    $("#type").val("3");
                    form.render('select', 'type');
                }
            });
        },

        refreshEmployee : function(employee) {

            form.val('employee_form', employee)
            var inDate = employee.inDate;
            var regularDate = employee.regularDate;
            if (inDate != null) {
                $("#inDate").val(inDate.substring(0,10));
            }
            if (regularDate != null) {
                $("#regularDate").val(regularDate);
            }


            var employeeJobRole = employee.employeeJobRole;

            var orgPath = employeeJobRole.orgPath;
            if (orgPath != null) {
                $("#orgPath").val(orgPath);
            }

            var orgId = employeeJobRole.orgId;
            if (orgId != null) {
                $(document.getElementById("employeeJobRole.orgId")).val(orgId);
            }

            var jobRole = employeeJobRole.jobRole;
            if (jobRole != null) {
                $("#jobRole").val(jobRole);
                form.render('select', 'jobRole');
            }

            var jobRoleNature = employeeJobRole.jobRoleNature;
            if (jobRoleNature != null) {
                $("#jobRoleNature").val(jobRoleNature);
                form.render('select', 'jobRoleNature');
            }

            var discountRate = employeeJobRole.discountRate;
            if (discountRate != null) {
                $(document.getElementById("employeeJobRole.discountRate")).val(discountRate);
            }

            var parentEmployeeName = employeeJobRole.parentEmployeeName;
            if (parentEmployeeName != null) {
                $("#parentEmployeeName").val(parentEmployeeName);
            }

            var jobGrade = employeeJobRole.jobGrade;
            if (jobGrade != null) {
                $(document.getElementById("employeeJobRole.jobGrade")).val(jobGrade);
            }


            if (this.workExpIndex > 0) {
                for (var i=this.workExpIndex; i > 0;i--) {
                    $("#workExp_"+i).remove();
                }
                this.workExpIndex = 0;
            }

            var employeeWorkExperiences = employee.employeeWorkExperiences;
            if (employeeWorkExperiences != null && employeeWorkExperiences.length > 0) {
                var length = employeeWorkExperiences.length
                for (var i=0; i<length; i++) {
                    var employeeWorkExperience = employeeWorkExperiences[i];
                    if (i > 0) {
                        this.addWorkExp();
                    }
                    $("#workStartDate_"+i).val(employeeWorkExperience.startDate);
                    $("#workEndDate_"+i).val(employeeWorkExperience.endDate);
                    $("#workContent_"+i).val(employeeWorkExperience.content);
                }
            }
        },

        calculateJobYear : function(jobDate) {
            if (jobDate.length < 10) {
                return;
            }
            var year = layui.time.getYearDiff(jobDate);
            $("#jobYear").val(year);
        },

        calculateDiscountRate : function(data) {
            var jobRoleNature = $(document.getElementById("jobRoleNature")).val();
            var orgId = $(document.getElementById("employeeJobRole.orgId")).val();
            if (orgId == null || orgId == "") {
                this.selectOrg();
                return;
            } else {
                layui.ajax.post('api/organization/employee/calculateDiscountRate', '&orgId='+orgId+"&jobRoleNature="+jobRoleNature, function(data){
                    $(document.getElementById("employeeJobRole.discountRate")).val(data.rows.discountRate);
                });
            }
        },

        addWorkExp : function() {
            this.workExpIndex++;
            var template = document.getElementById("workExp").innerHTML;
            var index = this.workExpIndex;
            layui.laytpl(template).render(index, function(html){
                $("#workExp_0").parent().append(html);
                laydate.render({
                    elem: '#workStartDate_'+index,
                    value: $("#workStartDate_"+index).val(),
                    type: 'month'
                });

                laydate.render({
                    elem: '#workEndDate_'+index,
                    value: $("#workEndDate_"+index).val(),
                    type: 'month'
                });
            });
        },

        delWorkExp : function(index) {
            $("#workExp_"+index).remove();
        },

        create : function(formData) {
            layui.ajax.post('api/organization/employee/create', formData);
        }
    };
    exports('employee', employee);
});