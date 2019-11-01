layui.define(['ajax', 'tree', 'layer'], function(exports){
    var $ = layui.$;
    var obj = {
        init : function(treeDivId, valueControlId, displayControlId, showCheckbox) {
            var content = $("#"+treeDivId).html();
            if (content == null || content.trim() == '' || typeof(content) == "undefined" || content.trim().length == 0) {
                layui.ajax.get('api/organization/org/listWithTree', '', function (data) {
                    layui.tree.render({
                        elem: "#"+treeDivId,
                        data: data.rows,
                        showCheckbox: showCheckbox,
                        id: "orgTree_"+treeDivId,
                        click: function(obj) {
                            if (showCheckbox) {
                                return;
                            }
                            $(document.getElementById(valueControlId)).val(obj.data.id);
                            $(document.getElementById(displayControlId)).val(obj.data.path);
                            var children = obj.data.children;
                            if (children == null || typeof(children) == "undefined" || children.length <= 0) {
                                layer.closeAll('page');
                            }
                        }
                    });
                    if (showCheckbox) {
                        obj.open(treeDivId, true, valueControlId, displayControlId)
                    } else {
                        obj.open(treeDivId);
                    }
                });
            } else{
                this.open(treeDivId, showCheckbox, valueControlId, displayControlId);
            }

        },

        open : function(treeDivId, needConfirmBtn, valueControlId, displayControlId) {
            if (needConfirmBtn) {
                layer.open({
                    type: 1,
                    title: '请选择部门',
                    area: ['40%', '60%'],
                    content: $("#"+treeDivId),
                    skin: 'layui-layer-admin',
                    btn: ['确定'],
                    yes: function(index, layero) {
                        layui.orgTree.confirm(treeDivId, valueControlId, displayControlId);
                    }
                });
            } else {
                layer.open({
                    type: 1,
                    title: '请选择部门',
                    area: ['40%', '60%'],
                    content: $("#"+treeDivId),
                    skin: 'layui-layer-admin'
                });
            }

        },

        confirm : function(treeDivId, valueControlId, displayControlId) {
            var checkData = layui.tree.getChecked("orgTree_"+treeDivId);
            if (checkData == null || checkData == '') {
                return;
            }

            var root = checkData[0];
            var value = '';
            var text = '';

            var allData = this.recursionChild(root);

            var idTexts = allData.split(",");
            var length = idTexts.length;
            for (var i=0; i<length; i++) {
                var idText = idTexts[i].split("$");
                value += idText[0] + ",";
                text += idText[1] + ",";
            }

            $(document.getElementById(valueControlId)).val(value.substring(0, value.length - 1));
            $(document.getElementById(displayControlId)).val(text.substring(0, text.length - 1));
            layer.closeAll('page');
        },

        recursionChild : function(node) {
            var result = '';
            var children = node.children;

            if (children == null || children.length <= 0) {
                return node.id + "$" + node.title;
            } else {
                var length = children.length;
                for (var i=0; i<length; i++) {
                    result += this.recursionChild(children[i]) + ",";
                }
                return result.substring(0, result.length - 1);
            }
        }
    };
    exports('orgTree', obj);
});