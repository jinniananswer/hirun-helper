layui.extend({

}).define(['ajax', 'laytpl', 'element', 'redirect'],function(exports){
    var $ = layui.$;
    var form = layui.form;
    var layer = layui.layer;
    var laydate = layui.laydate;
    var employee = {
        init : function() {
            layui.ajax.post('api/organization/employee/loadEmployeeArchive?employeeId='+$('#employeeId').val(), '', function(data){
                var archive = data.rows;

                var main = document.getElementById("employeeMainInfo").innerHTML;
                var mainArea = document.getElementById("employeeMain")
                layui.laytpl(main).render(archive, function(html){
                    mainArea.innerHTML = html;
                });

                var tech = document.getElementById("employeeTechInfo").innerHTML;
                var techArea = document.getElementById("employeeTech")
                layui.laytpl(tech).render(archive, function(html){
                    techArea.innerHTML = html;
                });

                var base = document.getElementById("employeeBaseInfo").innerHTML;
                var baseArea = document.getElementById("employeeBase")
                layui.laytpl(base).render(archive, function(html){
                    baseArea.innerHTML = html;
                });

                var job = document.getElementById("employeeJobInfo").innerHTML;
                var jobArea = document.getElementById("employeeJob")
                layui.laytpl(job).render(archive, function(html){
                    jobArea.innerHTML = html;
                });

                var education = document.getElementById("employeeEducationInfo").innerHTML;
                var educationArea = document.getElementById("employeeEducation")
                layui.laytpl(education).render(archive, function(html){
                    educationArea.innerHTML = html;
                });

                var socialSecurity = document.getElementById("employeeSocialSecurityInfo").innerHTML;
                var socialSecurityArea = document.getElementById("employeeSocialSecurity")
                layui.laytpl(socialSecurity).render(archive, function(html){
                    socialSecurityArea.innerHTML = html;
                });

                var children = document.getElementById("employeeChidrenInfo").innerHTML;
                var childrenArea = document.getElementById("employeeChildren")
                layui.laytpl(children).render(archive, function(html){
                    childrenArea.innerHTML = html;
                });

                var workExperience = document.getElementById("employeeWorkExperienceInfo").innerHTML;
                var workExperienceArea = document.getElementById("employeeWorkExperience")
                layui.laytpl(workExperience).render(archive, function(html){
                    workExperienceArea.innerHTML = html;
                });

                var history = document.getElementById("employeeHistoryInfo").innerHTML;
                var historyArea = document.getElementById("employeeHistory")
                layui.laytpl(history).render(archive, function(html){
                    historyArea.innerHTML = html;
                });
            });
        },

        transUndefined : function(object) {
            if (typeof(object) == "undefined") {
                return "";
            } else {
                return object;
            }
        }
    };
    exports('employee', employee);
});