layui.extend({
    index: "{/}/js/index"
}).define(['ajax', 'tree', 'layer', 'index'], function(exports){
    var $ = layui.$;
    var obj = {
        init : function(employeeDivId, searchTextId, valueControlId, displayControlId, showCheckbox) {
            var employeeDiv = $("#"+employeeDivId);
            var content = employeeDiv.html();

            if (content == null || content.trim() == '' || typeof(content) == "undefined" || content.trim().length == 0) {
                var html = [];
                html.push("<div class=\"layadmin-caller\">");
                html.push("<div class=\"layui-form caller-seach\">");
                html.push("<input type=\"text\" name=\""+searchTextId+"\" id=\""+searchTextId+"\" required=\"\" lay-verify=\"required\" placeholder=\"请输入员工姓名或者员工手机号码\" autocomplete=\"off\" class=\"layui-input\"/>");
                html.push("<i class=\"layui-icon layui-icon-search caller-dump-icon caller-icon\" onclick=\"layui.selectEmployee.search('"+searchTextId+"','"+valueControlId+"','"+displayControlId+"')\"></i>");
                html.push("</div>");
                html.push("<div class=\"caller-contar\" id=\""+searchTextId+"_items\">");
                html.push("</div>");
                html.push("</div>");

                employeeDiv.html(html.join(""));
            }
            this.open(employeeDivId, showCheckbox, valueControlId, displayControlId);
        },

        open : function(employeeDivId, needConfirmBtn, valueControlId, displayControlId) {
            if (needConfirmBtn) {
                layui.admin.popup({
                    title: '请选择员工',
                    area: ['40%', '60%'],
                    content: $("#"+employeeDivId),
                    skin: 'layui-layer-admin',
                    btn: ['确定'],
                    yes: function(index, layero) {
                        alert('aa');
                    }
                });
            } else {
                layui.admin.popup({
                    title: '请选择员工',
                    area: ['40%', '60%'],
                    content: $("#"+employeeDivId),
                    skin: 'layui-layer-admin'
                });
            }

        },

        search : function(searchTextId, valueControlId, displayControlId) {
            var searchText = $("#"+searchTextId).val();
            if (searchText == null || searchText.trim().length <= 0) {
                return;
            }

            layui.ajax.get('/api/organization/employee/selectEmployee', 'searchText='+searchText, function (data) {
                var employees = data.rows;
                if (employees == null || employees.length <= 0) {
                    return;
                }
                $("#"+searchTextId+"_items").empty();
                var length = employees.length;
                var html = [];
                for (var i=0;i<length;i++) {
                    var employee = employees[i];
                    html.push("<div class=\"caller-item\" style='cursor:pointer' onclick='layui.selectEmployee.back(\""+employee.employeeId+"\",\""+employee.name+"\",\""+valueControlId+"\",\""+displayControlId+"\")'>");
                    var sex = employee.sex;
                    if (sex == "1") {
                        html.push("<img src=\"/img/male.jpg\" alt=\"\" class=\"caller-img caller-fl\">");
                    } else {
                        html.push("<img src=\"/img/female.jpg\" alt=\"\" class=\"caller-img caller-fl\">");
                    }

                    html.push("<div class=\"caller-main caller-fl\">");
                    html.push("<p><strong>"+employee.name+"</strong></p>");
                    html.push("<p class=\"caller-adds\"><i class=\"layui-icon layui-icon-cellphone\"></i>"+employee.mobileNo+"</p>");
                    var orgName = '';
                    var jobRoleName = '';
                    if (employee.employeeJobRole != null) {
                        orgName = employee.employeeJobRole.orgName;
                        jobRoleName = employee.employeeJobRole.jobRoleName;
                    }
                    html.push("<p class=\"caller-adds\"><i class=\"layui-icon layui-icon-location\"></i>"+orgName+"</p>");
                    html.push("</div>");
                    html.push("<button class=\"layui-btn layui-btn-sm caller-fr\">");

                    html.push(jobRoleName);
                    html.push("</button>");

                    html.push("</div>");
                }

                $("#"+searchTextId+"_items").html(html.join(""));

                //parent.layer.closeAll('loading');
            });
        },

        back : function(employeeId, name, valueControlId, displayControlId) {
            $("#"+valueControlId).val(employeeId);
            $("#"+displayControlId).val(name);
            layui.layer.closeAll('page');
        },

        confirm : function(treeDivId, valueControlId, displayControlId) {

        }
    };
    exports('selectEmployee', obj);
});