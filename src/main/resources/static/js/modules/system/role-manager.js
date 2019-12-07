layui.extend({}).define(['ajax', 'table', 'element', 'layer', 'tree', 'util'], function (exports) {
    let $ = layui.$;
    let table = layui.table;
    let layer = layui.layer;
    let element = layui.element;
    let tree = layui.tree;
    let util = layui.util;

    // 让表格根据属性 checked 来判断是否勾选 checkbox
    table = $.extend(table, {config: {checkName: 'checked'}});

    // 全局变量
    let currRoleId;

    let roleObj = {
        init: function () {

            roleObj.initMenuTree();
            roleObj.initRoleTable();
            roleObj.initFuncTable();

            util.event('lay-event', {
                updateMenuRole: function () {
                    let checkedData = tree.getChecked('menuTree'); //获取选中节点的数据
                    let nodeList = eval(checkedData)

                    let ids = [];
                    roleObj.getLeaf(nodeList, ids);
                    let idsJson = JSON.stringify(ids);

                    $.ajax({
                        type: "post",
                        url: "api/user/menu-role/updateMenuRole/" + currRoleId,
                        dataType: "json",
                        contentType: "application/json",
                        data: idsJson,
                        success: function (data) {
                            if (0 == data.code) {
                                layer.msg("保存成功！", {time: 3000, icon: 6});
                            } else {
                                layer.alert("保存失败！" + data.message);
                            }
                        },
                        error: function (data) {
                            layer.alert("保存失败！");
                        }
                    });
                },

            });

            $('#queryRole').on('click', function () {
                table.reload('role-table', {
                    page: {
                        curr: 1
                    },
                    loading: true,
                    page: false,
                    url: 'api/user/role/role-list',
                    where: {
                        rolename: $("input[id='rolename']").val(),
                        startEndDate: $("input[id='start-end-date']").val(),
                    }
                });
                roleObj.initMenuTree();
                roleObj.initFuncTable();
            });

            table.on('tool(role-table)', function (obj) {
                let data = obj.data;

                if (obj.event === 'selectRole') {
                    currRoleId = data.roleId;
                    layui.ajax.get('api/system/menu/list-all?roleId=' + data.roleId, '', function (data) {
                        let json = eval(data);
                        let menus = json.rows;

                        // 根据选择的角色，刷新菜单权限数据
                        tree.render({
                            elem: '#menuTree',
                            id: 'menuTree',
                            data: menus,
                            showCheckbox: true,
                            click: function (obj) {
                                let data = obj.data;  // 获取当前点击的节点数据
                                tree.setChecked('menuTree', [data.id]);
                            }
                        });
                    });

                    // 根据选择的角色，刷新操作权限数据
                    table.reload('func-table', {
                        loading: true,
                        page: false,
                        url: 'api/system/func/func-list',
                        where: {
                            roleId: data.roleId,
                        }
                    });

                } else if (obj.event === 'editRole') {
                    console.log('editRole: ' + data.roleId);
                    roleObj.editRole(data);
                } else if (obj.event === 'deleteRole') {
                    console.log('deleteRole: ' + data.roleId);
                    roleObj.deleteRole(data);
                }
            });

            table.on('toolbar(role-table)', function (obj) {
                let checkStatus = table.checkStatus(obj.config.id); // 获取选中行状态
                let data = checkStatus.data;

                let event = obj.event;
                if (event === 'newRole') {
                    let index = layer.open({
                        type: 2,
                        title: '新角色',
                        content: 'openUrl?url=modules/system/announce-create',
                        maxmin: true,
                        area: ['550px', '400px'],
                        skin: 'layui-layer-molv',
                        success: function (layero, index) {

                        },
                        yes: function (index, layero) {
                            layer.close(index);
                        }
                    });
                }

            });

            table.on('toolbar(func-table)', function (obj) {
                let checkStatus = table.checkStatus(obj.config.id); // 获取选中行状态
                let data = checkStatus.data;
                let ids = [];
                for (let i = 0; i < data.length; i++) {
                    ids.push(data[i].funcId);
                }
                let idsJson = JSON.stringify(ids);

                $.ajax({
                    type: "post",
                    url: "api/user/func-role/updateFuncRole/" + currRoleId,
                    dataType: "json",
                    contentType: "application/json",
                    data: idsJson,
                    success: function (data) {
                        if (0 == data.code) {
                            layer.msg("保存成功！", {time: 3000, icon: 6});
                        } else {
                            layer.alert("保存失败！" + data.message);
                        }
                    },
                    error: function (data) {
                        layer.alert("保存失败！");
                    }
                });

            });

        },

        initMenuTree: function () {
            currRoleId = "";
            layui.ajax.get('api/system/menu/list-all', '', function (data) {
                let json = eval(data);
                let menus = json.rows;

                tree.render({
                    elem: '#menuTree',
                    id: 'menuTree',
                    data: menus,
                    showCheckbox: true,
                    click: function (obj) {
                        let data = obj.data;  //获取当前点击的节点数据
                        layer.msg('状态：' + obj.state + '<br>节点数据：' + JSON.stringify(data));
                    }
                });
            });
        },

        initRoleTable: function () {
            table.render({
                elem: '#role-table',
                toolbar: '#roleToolbar',
                defaultToolbar: ['filter'],
                height: 'full-20',
                url: 'api/user/role/role-list',
                fitColumns: true,
                response: {
                    msgName: 'message',
                    countName: 'total',
                    dataName: 'rows'
                },
                cols: [[
                    {type: 'radio', fixed: 'left', event: 'selectRole'},
                    {field: 'roleId', title: 'ID', width: 60, align: 'center'},
                    {field: 'roleName', title: '角色名', align: 'center'},
                    // {
                    //     field: 'enabled', title: '状态', align: 'center', templet: function (d) {
                    //         if (d.enabled == 1) {
                    //             return '<span class="layui-badge layui-bg-orange">有效</span>';
                    //         } else {
                    //             return '<span class="layui-badge layui-bg-gray">失效</span>';
                    //         }
                    //     }
                    // },
                    {fixed: 'right', align: 'center', title: '操作', width: 150, fixed: 'right', toolbar: '#roleBar'}
                ]],
                page: false,
                text: {none: '暂无相关数据，请检查查询条件。'}
            });
        },

        initFuncTable: function () {
            table.render({
                elem: '#func-table',
                toolbar: '#funcToolbar',
                defaultToolbar: ['filter'],
                height: 'full-20',
                url: 'api/system/func/func-list',
                fitColumns: true,
                response: {
                    msgName: 'message',
                    countName: 'total',
                    dataName: 'rows'
                },
                cols: [[
                    {type: 'checkbox', fixed: 'left', event: 'selectRole'},
                    {field: 'funcId', title: 'ID', width: 60, align: 'center'},
                    {field: 'funcCode', title: '权限编码', align: 'center'},
                    {field: 'funcDesc', title: '权限说明', align: 'center'},
                ]],
                page: false,
                text: {none: '暂无相关数据，请检查查询条件。'}
            });
        },

        /** 获取叶子节点Id */
        getLeaf: function (nodeList, ids) {
            for (let i = 0; i < nodeList.length; i++) {
                let node = nodeList[i];
                if (node.children) {
                    roleObj.getLeaf(node.children, ids);
                } else {
                    ids.push(node.id);
                }
            }
        },

        editRole: function (data) {
            let index = layer.open({
                type: 2,
                title: '查看详情',
                content: 'openUrl?url=modules/system/message-detail',
                maxmin: true,
                btn: ['关闭'],
                area: ['550px', '400px'],
                skin: 'layui-layer-molv',
                success: function (layero, index) {
                    let body = layer.getChildFrame('body', index);
                    body.find('#senderName').html('<h1>来自【' + data.name + '】的公告' + '</h1>');
                    body.find('#createTime').html('<span>' + data.createTime + '</span>');
                    body.find('#content').html('<div class="layadmin-text">' + data.content + '</div>');
                },
                yes: function (index, layero) {
                    layer.close(index);
                }
            });

        },

        deleteRole: function (data) {
            layer.confirm('真的要删除角色【' + data.roleName + '】?', {icon: 3, title: '提示'}, function (index) {
                layui.ajax.get('api/user/role/delete-role/' + data.roleId, '', function (data) {
                    if (0 == data.code) {
                        layer.msg("删除成功！", {time: 3000, icon: 6});
                        roleObj.initRoleTable();
                        roleObj.initMenuTree();
                        roleObj.initFuncTable();
                    } else {
                        layer.alert("删除失败！" + data.message);
                    }
                });
                layer.close(index);
            });

        },
    };

    exports('roleObj', roleObj);
})