layui.define(['ajax', 'tree', 'layer'], function(exports){
    var $ = layui.$;
    var obj = {
        init : function(treeDivId, valueControlId, displayControlId) {
            layui.ajax.get('/api/organization/org/listWithTree', '', function (data) {
                layui.tree.render({
                    elem: "#"+treeDivId,
                    data: data.rows,
                    click: function(obj) {
                        $("#"+valueControlId).val(obj.data.id);
                        $("#"+displayControlId).val(obj.data.path);
                        layer.closeAll('page');
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