layui.define(['ajax', 'tree', 'layer'], function(exports){
    let $ = layui.$;
    let obj = {
        afterFunc : null,
        init : function(employeeDivId, searchTextId, valueControlId, displayControlId, showCheckbox, func) {
            let employeeDiv = $("#"+employeeDivId);
            let content = employeeDiv.html();
            if (func != null && typeof(func) != "undefined") {
                this.afterFunc = func;
            }

            if (content == null || content.trim() == '' || typeof(content) == "undefined" || content.trim().length == 0) {
                let html = [];
                html.push("<div class=\"layadmin-caller\">");
                html.push("<div class=\"layui-form caller-seach\">");
                html.push("<input type=\"text\" name=\""+searchTextId+"\" id=\""+searchTextId+"\" required=\"\" lay-verify=\"required\" placeholder=\"请输入员工姓名或者员工手机号码\" autocomplete=\"off\" class=\"layui-input\"/>");
                html.push("<i class=\"layui-icon layui-icon-search caller-dump-icon caller-icon\" onclick=\"layui.selectEmployee.search('"+searchTextId+"','"+valueControlId+"','"+displayControlId+"')\"></i>");
                html.push("</div>");
                html.push("<div class=\"caller-contar\" id=\""+searchTextId+"_items\">");
                html.push("</div>");
                html.push("</div>");

                employeeDiv.html(html.join(""));

                $('#'+searchTextId).bind('keyup', function(event) {
                    if (event.keyCode == "13") {
                        //回车执行查询
                        layui.selectEmployee.search(searchTextId, valueControlId, displayControlId);
                    }
                });
            }
            this.open(employeeDivId, showCheckbox, valueControlId, displayControlId);
        },

        open : function(employeeDivId, needConfirmBtn, valueControlId, displayControlId, afterFunc) {
            if (needConfirmBtn) {
                layui.layer.open({
                    title: '请选择员工',
                    area: ['40%', '60%'],
                    content: $("#"+employeeDivId),
                    skin: 'layui-layer-admin',
                    btn: ['确定'],
                    yes: function(index, layero) {

                    }
                });
            } else {
                this.popup({
                    title: '请选择员工',
                    area: ['40%', '60%'],
                    content: $("#"+employeeDivId)
                });
            }

        },

        search : function(searchTextId, valueControlId, displayControlId) {
            let searchText = $("#"+searchTextId).val();
            if (searchText == null || searchText.trim().length <= 0) {
                return;
            }

            layui.ajax.get('api/organization/employee/searchEmployee', 'searchText='+searchText, function (data) {
                let employees = data.rows;
                $("#"+searchTextId+"_items").empty();
                if (employees == null || employees.length <= 0) {
                    return;
                }

                let length = employees.length;
                let html = [];
                for (let i=0;i<length;i++) {
                    let employee = employees[i];
                    html.push("<div class=\"caller-item\" style='cursor:pointer' onclick='layui.selectEmployee.back(\""+employee.employeeId+"\",\""+employee.name+"\",\""+valueControlId+"\",\""+displayControlId+"\")'>");
                    let sex = employee.sex;
                    if (sex == "1") {
                        html.push("<img src=\"static/img/male.jpg\" alt=\"\" class=\"caller-img caller-fl\">");
                    } else {
                        html.push("<img src=\"static/img/female.jpg\" alt=\"\" class=\"caller-img caller-fl\">");
                    }

                    html.push("<div class=\"caller-main caller-fl\">");
                    html.push("<p><strong>"+employee.name+"</strong></p>");
                    html.push("<p class=\"caller-adds\"><i class=\"layui-icon layui-icon-cellphone\"></i>"+employee.mobileNo+"</p>");
                    html.push("<p class=\"caller-adds\"><i class=\"layui-icon layui-icon-location\"></i>"+employee.orgPath+"</p>");
                    html.push("</div>");
                    html.push("<button class=\"layui-btn layui-btn-sm caller-fr\">");

                    html.push(employee.jobRoleName);
                    html.push("</button>");

                    html.push("</div>");
                }

                $("#"+searchTextId+"_items").html(html.join(""));

                //parent.layer.closeAll('loading');
            });
        },

        popup : function(options) {
            let success = options.success;
            let skin = options.skin;

            delete options.success;
            delete options.skin;

            return layui.layer.open(layui.$.extend({
                type: 1
                ,title: '提示'
                ,content: ''
                ,id: 'LAY-system-view-popup'
                ,skin: 'layui-layer-admin' + (skin ? ' ' + skin : '')
                ,shadeClose: true
                ,closeBtn: false
                ,success: function(layero, index){
                    var elemClose = $('<i class="layui-icon" close>&#x1006;</i>');
                    layero.append(elemClose);
                    elemClose.on('click', function(){
                        layer.close(index);
                    });
                    typeof success === 'function' && success.apply(this, arguments);
                }
            }, options));

        },

        back : function(employeeId, name, valueControlId, displayControlId) {
            $(document.getElementById(valueControlId)).val(employeeId);
            $(document.getElementById(displayControlId)).val(name);
            layui.layer.closeAll('page');

            if (this.afterFunc != null && typeof(this.afterFunc) == "function") {
                this.afterFunc(employeeId, name, valueControlId);
            }
        },

        confirm : function(treeDivId, valueControlId, displayControlId) {

        }
    };
    exports('selectEmployee', obj);
});