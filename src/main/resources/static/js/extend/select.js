layui.define(['ajax', 'form'], function(exports){
    var $ = layui.$;
    var form = layui.form;
    var obj = {
        init : function(containerId, codeType, defaultVal, needTitle, title) {
            layui.ajax.get('/api/system/static-data/getByCodeType', '&codeType='+codeType, function(data) {
                obj.drawOption(containerId, defaultVal, needTitle, title, data.rows);
            });
        },

        drawOption : function(containerId, defaultVal, needTitle, title, datas) {
            var html = [];
            if (needTitle) {
                if (title == null || typeof(title) == "undefined") {
                    title = "请选择";
                }
                html.push("<option value=''>"+title+"</option>");
            }

            $.each(datas, function (i, item) {
                if (item.codeValue == defaultVal) {
                    html.push("<option value='"+item.codeValue+"' selected>"+item.codeName+"</option>");
                }
                else {
                    html.push("<option value='"+item.codeValue+"'>"+item.codeName+"</option>");
                }

            });

            $("#"+containerId).html(html.join(""));
            form.render('select', containerId);
        }
    };
    exports('select', obj);
});