layui.extend({
}).define(['ajax', 'form', 'layer', 'element', 'laydate', 'select'], function (exports) {
    var $ = layui.$;
    var form = layui.form;
    var layer = layui.layer;
    var laydate = layui.laydate;
    var ajax = layui.ajax;
    var measureHouse = {
        init: function () {
            laydate.render({
                elem : '#measureDate'
            });

            form.on('submit(submit)', function(data) {
                measureHouse.submit(data.field);
            })
            // $('#submit').bind('click', function() {
            //     measureHouse.submit();
            // })
        },

        submit: function (data) {
            ajax.post('api/demo/demo/testAwx', data);
        }

    };
    exports('measureHouse', measureHouse);
});