layui.define(['ajax', 'tree', 'layer'], function(exports){
    let $ = layui.$;
    let obj = {
        init : function(treeDivId, valueControlId, displayControlId, showCheckbox, onlyLeaf) {
            if (onlyLeaf == null || typeof(onlyLeaf) == "undefined") {
                onlyLeaf = true;
            }
            let content = $("#"+treeDivId).html();
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
                            $(document.getElementById(displayControlId)).trigger("propertychange");
                            let children = obj.data.children;
                            if (onlyLeaf) {
                                if (children == null || typeof(children) == "undefined" || children.length <= 0) {
                                    layer.closeAll('page');
                                }
                            } else {
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
                this.popup({
                    title: '请选择部门',
                    area: ['40%', '60%'],
                    content: $("#"+treeDivId),
                    btn: ['确定'],
                    yes: function(index, layero) {
                        layui.orgTree.confirm(treeDivId, valueControlId, displayControlId);
                    }
                });
            } else {
                this.popup({
                    title: '请选择部门',
                    area: ['40%', '60%'],
                    content: $("#"+treeDivId)
                });
            }

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

        confirm : function(treeDivId, valueControlId, displayControlId) {
            let checkData = layui.tree.getChecked("orgTree_"+treeDivId);
            if (checkData == null || checkData == '') {
                return;
            }

            let root = checkData[0];
            let value = '';
            let text = '';

            let allData = this.recursionChild(root);

            let idTexts = allData.split(",");
            let length = idTexts.length;
            for (let i=0; i<length; i++) {
                let idText = idTexts[i].split("$");
                value += idText[0] + ",";
                text += idText[1] + ",";
            }

            $(document.getElementById(valueControlId)).val(value.substring(0, value.length - 1));
            $(document.getElementById(displayControlId)).val(text.substring(0, text.length - 1));
            layer.closeAll('page');
        },

        recursionChild : function(node) {
            let result = '';
            let children = node.children;

            if (children == null || children.length <= 0) {
                return node.id + "$" + node.title;
            } else {
                let length = children.length;
                for (let i=0; i<length; i++) {
                    result += this.recursionChild(children[i]) + ",";
                }
                return result.substring(0, result.length - 1);
            }
        }
    };
    exports('orgTree', obj);
});