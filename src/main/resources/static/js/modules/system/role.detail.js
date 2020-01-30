layui.extend({}).define(['ajax', 'table', 'element', 'layer', 'tree'], function (exports) {
    let $ = layui.$;
    let table = layui.table;
    let layer = layui.layer;
    let tree = layui.tree;

    // 让表格根据属性 checked 来判断是否勾选 checkbox
    table = $.extend(table, {config: {checkName: 'checked'}});

    // 全局变量

    let roleDetail = {

        init: function () {
            let roleId = $('#roleId').val();
            // 初始化
            roleDetail.initMenuTree(roleId);
            roleDetail.initFuncTable(roleId);
        },

        initMenuTree: function (roleId) {

            layui.ajax.get('api/system/menu/list-all?roleId=' + roleId, '', function (data) {
                let json = eval(data);
                let menus = json.rows;

                // 根据选择的角色，刷新菜单权限数据
                tree.render({
                    elem: '#menuTree',
                    id: 'menuTree',
                    data: menus,
                    showCheckbox: true,
                });

            });
        },

        initFuncTable: function (roleId) {
            table.render({
                id: "funcTableId",
                elem: '#func-table',
                method: 'get',
                loading: true,
                page: false,
                url: 'api/system/func/func-list?roleId=' + roleId,
                response: {
                    msgName: 'message',
                    countName: 'total',
                    dataName: 'rows'
                },
                cols: [[
                    {
                        type: 'checkbox',
                    }, {
                        field: 'funcId',
                        title: 'ID'
                    }, {
                        field: 'funcCode',
                        title: '权限编码'
                    }, {
                        field: 'funcDesc',
                        title: '权限说明'
                    },
                ]],
                done: function (res, curr, count) {
                    for (let i = 0; i < res.rows.length; i++) {
                        $(".layui-table tr[data-index=" + i + "] input[type='checkbox']").prop('disabled', true);
                    }
                }
            });
        },
    };

    exports('roleDetail', roleDetail);
})