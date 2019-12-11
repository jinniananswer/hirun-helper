layui.extend({

}).define(['ajax', 'laytpl', 'element', 'redirect'],function(exports){
    let $ = layui.$;
    let form = layui.form;
    let layer = layui.layer;
    let laydate = layui.laydate;
    let employee = {
        init : function() {
            layui.ajax.post('api/organization/employee/loadEmployeeArchive?employeeId='+$('#employeeId').val(), '', function(data){
                let archive = data.rows;

                let main = document.getElementById("employeeMainInfo").innerHTML;
                let mainArea = document.getElementById("employeeMain");
                layui.laytpl(main).render(archive, function(html){
                    mainArea.innerHTML = html;
                });

                let tech = document.getElementById("employeeTechInfo").innerHTML;
                let techArea = document.getElementById("employeeTech");
                layui.laytpl(tech).render(archive, function(html){
                    techArea.innerHTML = html;
                });

                let base = document.getElementById("employeeBaseInfo").innerHTML;
                let baseArea = document.getElementById("employeeBase");
                layui.laytpl(base).render(archive, function(html){
                    baseArea.innerHTML = html;
                });

                let job = document.getElementById("employeeJobInfo").innerHTML;
                let jobArea = document.getElementById("employeeJob");
                layui.laytpl(job).render(archive, function(html){
                    jobArea.innerHTML = html;
                });

                let contact = document.getElementById("employeeContactInfo").innerHTML;
                let contactArea = document.getElementById("employeeContact");
                layui.laytpl(contact).render(archive, function(html){
                    contactArea.innerHTML = html;
                });

                let education = document.getElementById("employeeEducationInfo").innerHTML;
                let educationArea = document.getElementById("employeeEducation");
                layui.laytpl(education).render(archive, function(html){
                    educationArea.innerHTML = html;
                });

                let socialSecurity = document.getElementById("employeeSocialSecurityInfo").innerHTML;
                let socialSecurityArea = document.getElementById("employeeSocialSecurity");
                layui.laytpl(socialSecurity).render(archive, function(html){
                    socialSecurityArea.innerHTML = html;
                });

                let children = document.getElementById("employeeChidrenInfo").innerHTML;
                let childrenArea = document.getElementById("employeeChildren");
                layui.laytpl(children).render(archive, function(html){
                    childrenArea.innerHTML = html;
                });

                let workExperience = document.getElementById("employeeWorkExperienceInfo").innerHTML;
                let workExperienceArea = document.getElementById("employeeWorkExperience");
                layui.laytpl(workExperience).render(archive, function(html){
                    workExperienceArea.innerHTML = html;
                });

                let history = document.getElementById("employeeHistoryInfo").innerHTML;
                let historyArea = document.getElementById("employeeHistory");
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