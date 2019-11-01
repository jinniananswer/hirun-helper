layui.extend({
    orgTree: 'org',
    citypicker: 'city-picker/city-picker',
    selectEmployee: 'employee'
}).define(['ajax', 'select', 'form', 'layer', 'laydate', 'laytpl', 'element', 'orgTree', 'citypicker', 'selectEmployee', 'redirect','time'],function(exports){
    var $ = layui.$;
    var form = layui.form;
    var layer = layui.layer;
    var laydate = layui.laydate;
    var employee = {
        currentTab : 1,
        workExpIndex : 0,

        init : function() {
            layui.select.init('createType', 'CREATE_EMPLOYEE_TYPE', '1', false);
            layui.select.init('birthdayType', 'BIRTHDAY_TYPE', '1', false);
            layui.select.init('jobNature', 'JOB_NATURE', '1', false);
            layui.select.init('jobRole', 'JOB_ROLE', null, true, '请选择或搜索岗位');
            layui.select.init('firstEducationLevel', 'EDUCATION_LEVEL', '3', false);
            layui.select.init('educationLevel', 'EDUCATION_LEVEL', '3', false);
            layui.select.init('schoolType', 'SCHOOL_TYPE', '1', false);

            layui.element.on('tab(employeeTab)', function(data){

            });

            laydate.render({
                elem: '#birthday'
            });

            laydate.render({
                elem: '#inDate',
                value: new Date()
            });

            laydate.render({
                elem: '#regularDate'
            });

            laydate.render({
                elem: '#jobDate'
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
                var status = employee.status;

                if (status == "3") {
                    this.renderCreateType("该证件号码的员工已离职，是否做返聘处理？", "2", employee.employeeId);
                } else if (status == "1") {
                    this.renderCreateType("该证件号码的员工已离职，是否做返聘处理？", "3", employee.employeeId);
                }
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

                layui.ajax.post('api/organization/employee/loadAbnormal', '&employeeId='+employeeId, function(data){
                    var employee = data.rows;

                });
                layer.close(index);
            });
        },

        refreshEmployee : function(employee) {
            $("#identityNo").val(employee.identityNo);
            $("#name").val(employee.name);
            $("#mobileNo").val(employee.mobileNo);

            var sex = employee.sex;
            if (sex == 1) {
                $("#male").prop("checked", "true");
                form.render("radio");
            } else {
                $("#female").prop("checked", "true");
                form.render("radio");
            }

            var birthdayType = employee.birthdayType;
            if (birthdayType != null) {
                $("#birthdayType").val(birthdayType);
                form.render('select', 'birthdayType');
            }

            var birthday = employee.birthday;
            if (birthday != null) {
                $("#birthday").val(birthday);
            }

            var nativeAddress = employee.nativeAddress;
            if (nativeAddress != null) {
                $("#nativeAddress").val(nativeAddress);
            }

            var homeAddress = employee.homeAddress;
            if (homeAddress != null) {
                $("#homeAddress").val(homeAddress);
            }

            var inDate = employee.inDate;
            if (inDate != null) {
                $("#inDate").val(inDate);
            }

            var regularDate = employee.regularDate;
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
                $(document.getElementById(employeeJobRole.orgId)).val(orgId);
            }

            var jobRole = employeeJobRole.jobRole;
            if (jobRole != null) {
                $("#jobRole").val(jobRole);
                form.render('select', 'jobRole');
            }

            var jobNature = employeeJobRole.jobNature;
            if (jobNature != null) {
                $("#jobNature").val(jobNature);
                form.render('select', 'jobNature');
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

            var firstEducationLevel = employee.firstEducationLevel;
            if (firstEducationLevel != null) {
                $("#firstEducationLevel").val(firstEducationLevel);
                form.render('select', "firstEducationLevel");
            }

            var educationLevel = employee.educationLevel;
            if (educationLevel != null) {
                $("#educationLevel").val(educationLevel);
                form.render('select', "educationLevel");
            }

            var schoolType = employee.schoolType;
            if (schoolType != null) {
                $("#schoolType").val(schoolType);
                form.render('select', "schoolType");
            }

            var school = employee.school;
            if (school != null) {
                $("#school").val(school);
            }

            var major = employee.major;
            if (major != null) {
                $("#major").val(major);
            }

            var certificateNo = employee.certificateNo;
            if (certificateNo != null) {
                $("#certificateNo").val(certificateNo);
            }

            var techTitle = employee.techTitle;
            if (techTitle != null) {
                $("#jobDate").val(techTitle);
            }

            var jobDate = employee.jobDate;
            if (jobDate != null) {
                $("#jobDate").val(jobDate);
            }

            var jobYear = employee.jobYear;
            if (jobYear != null) {
                $("#jobYear").val(jobYear);
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

        caculatorJobYear : function() {
            var jobDate = $("#jobDate").val();
            var year = layui.time.getYearDiff(jobDate);
            $("#jobYear").val(year);
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