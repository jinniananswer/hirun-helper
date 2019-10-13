layui.extend({
    orgTree: 'org',
    citypicker: 'city-picker/city-picker',
    selectEmployee: 'employee'
}).define(['ajax', 'select', 'form', 'layer', 'laydate', 'laytpl', 'element', 'orgTree', 'citypicker', 'selectEmployee'],function(exports){
    var $ = layui.$;
    var form = layui.form;
    var layer = layui.layer;
    var laydate = layui.laydate;
    var employee = {
        currentTab : 1,
        workExpIndex : 0,

        init : function() {
            layui.select.init('createType', 'CREATE_EMPLOYEE_TYPE', '1', false);
            layui.select.init('educationLevel', 'SEX', '1', false);
            layui.select.init('birthdayType', 'BIRTHDAY_TYPE', '1', false);
            layui.select.init('workNature', 'JOB_NATURE', '1', false);
            layui.select.init('jobRole', 'JOB_ROLE', '1', true);
            layui.select.init('firstEducationLevel', 'EDUCATION_LEVEL', '3', false);
            layui.select.init('highestEducationLevel', 'EDUCATION_LEVEL', '3', false);
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
                elem: '#jobDate',
                type: 'month'
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
            registerPicker.setValue("湖南省/长沙市/");

            var homePicker = new layui.citypicker("#home-city-picker", {
                provincename:"homeProv",
                cityname:"homeCity",
                districtname: "homeRegion",
                level: 'homeRegion',// 级别
            });
            homePicker.setValue("湖南省/长沙市/");

            form.on('submit(btnSubmit)', function(data){
                layui.employee.create(data.field);
                return false;
            });
        },

        selectOrg : function() {
            layui.orgTree.init('orgTree', 'orgId', 'orgPath', false);
        },

        selectParentEmployee : function() {
            layui.selectEmployee.init('parentEmployeeList', 'employeeSearch', 'parentEmployeeId', 'parentEmployeeName', false);
        },

        next : function() {
            this.currentTab++;
            layui.element.tabChange('employeeTab', this.currentTab);
            return false;
        },

        analyzeIdentityNo : function() {
            var identityNo = $("#identityNo").val();
            if (identityNo == null || identityNo == "" || identityNo.length != 18) {
                return;
            }
            var birthday = identityNo.substring(6, 10) + "-" + identityNo.substring(10, 12) + "-" + identityNo.substring(12, 14);
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
            layui.ajax.post('/api/organization/employee/create', formData);
        }
    };
    exports('employee', employee);
});