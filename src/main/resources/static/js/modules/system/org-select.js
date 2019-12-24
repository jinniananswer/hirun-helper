layui.extend({}).define(['ajax', 'element', 'tree'], function (exports) {
    let $ = layui.$;
    let tree = layui.tree;

    let orgSelectManager = {
        init: function () {
            orgSelectManager.initOrgTree();
            $(document).ready(function() {
                $("#collectBtn").click(function () {
                    let ids = orgSelectManager.getSelectedOrgs();
                    $("#orgIds").val(JSON.stringify(ids));
                });
            });
        },

        initOrgTree: function () {
            layui.ajax.get('api/organization/org/listWithTree', '', function (data) {
                let json = eval(data);
                let orgs = json.rows;

                tree.render({
                    elem: '#orgTree',
                    id: 'orgTree',
                    data: orgs,
                    showCheckbox: true
                });
            });
        },

        getNodes: function (nodeList, ids) {
            for (let i = 0; i < nodeList.length; i++) {
                let node = nodeList[i];
                if (node.children) {
                    ids.push({"id": node.id, "title": node.path});
                    orgSelectManager.getNodes(node.children, ids);
                } else {
                    ids.push({"id": node.id, "title": node.path});
                }
            }
        },

        getSelectedOrgs: function () {
            let checkedData = tree.getChecked('orgTree'); // 获取选中节点的数据
            let nodeList = eval(checkedData);
            let ids = [];
            orgSelectManager.getNodes(nodeList, ids);
            return ids;
        },

    };

    exports('orgSelectManager', orgSelectManager);
})