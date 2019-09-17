layui.define(['ajax', 'tree'], function(exports){
    var $ = layui.$;
    var obj = {
        init : function(treeDivId, valueControlId, displayControlId) {
            layui.ajax.get('/api/organization/org/listWithTree', '', function (data) {
                layui.tree.render({
                    elem: "#"+treeDivId,
                    data: data.rows,
                    click: function(obj) {

                    }
                });

                layer.open({
                    type: 1,
                    title: '请选择部门',
                    area: ['40%', '60%'],
                    content: $("#"+treeDivId),
                    skin: 'layui-layer-admin'
                });
            });
        }
    };
    exports('orgTree', obj);
});