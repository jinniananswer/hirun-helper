layui.extend({}).define(['ajax', 'table', 'element', 'layer', 'tree'], function (exports) {
    let $ = layui.$;
    let table = layui.table;
    let layer = layui.layer;
    let tree = layui.tree;

    // 让表格根据属性 checked 来判断是否勾选 checkbox
    table = $.extend(table, {config: {checkName: 'checked'}});

    // 全局变量
    let currRoleId = null;
    let currRoleName = null;
    let oldMenus = null;
    let oldFuncs = null;

    let roleManager = {
        init: function () {

            // 初始化
            roleManager.initMenuTree();
            roleManager.initRoleTable();
            roleManager.initFuncTable();

            // 事件绑定
            table.on('tool(role-table)', function (obj) {
                let data = obj.data;

                if (obj.event === 'selectRole') {
                    currRoleId = data.roleId;
                    currRoleName = data.roleName;
                    layui.ajax.get('api/system/menu/list-all?roleId=' + data.roleId, '', function (data) {
                        let json = eval(data);
                        let menus = json.rows;

                        // 根据选择的角色，刷新菜单权限数据
                        tree.render({
                            elem: '#menuTree',
                            id: 'menuTree',
                            data: menus,
                            showCheckbox: true
                        });

                        oldMenus = roleManager.getSelectedMenus();
                    });

                    // 根据选择的角色，刷新操作权限数据
                    table.reload('func-table', {
                        id: 'funcTableId',
                        loading: true,
                        page: false,
                        url: 'api/system/func/func-list',
                        where: {
                            roleId: data.roleId,
                        },
                        done: function(res, curr, count){
                            oldFuncs = table.checkStatus('funcTableId').data;
                        }
                    });

                } else if (obj.event === 'editRole') {
                    console.log('editRole: ' + data.roleId);
                    roleManager.editRole(data);
                } else if (obj.event === 'deleteRole') {
                    console.log('deleteRole: ' + data.roleId);
                    roleManager.deleteRole(data);
                }
            });

            table.on('toolbar(role-table)', function (obj) {
                let checkStatus = table.checkStatus(obj.config.id); // 获取选中行状态
                let data = checkStatus.data;

                let event = obj.event;
                if (event === 'createRole') {
                    let index = layer.open({
                        type: 2,
                        title: '新角色',
                        content: 'openUrl?url=modules/system/role-create',
                        maxmin: false,
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
                let event = obj.event;

                if (event === 'updateFuncRole') {
                    if (!currRoleId) {
                        layer.alert('请先选择一个角色，再进行操作！');
                        return;
                    }

                    let checkStatus = table.checkStatus(obj.config.id); // 获取选中行状态
                    let curFuncs = checkStatus.data;
                    let revoked = roleManager.funcMinus(oldFuncs, curFuncs);
                    let granted = roleManager.funcMinus(curFuncs, oldFuncs);

                    let msg1 = "";
                    revoked.map((e) => {
                        msg1 += "【" + e + "】</br>"
                    });

                    let msg2 = "";
                    granted.map((e) => {
                        msg2 += "【" + e + "】</br>"
                    });

                    layer.confirm('<fieldset class="layui-elem-field">\n' +
                        '  <legend>回收操作权限</legend>\n' +
                        '  <div class="layui-field-box">\n' +
                        msg1 +
                        '  </div>\n' +
                        '</fieldset>' +
                        '<fieldset class="layui-elem-field">\n' +
                        '  <legend>下放操作权限</legend>\n' +
                        '  <div class="layui-field-box">\n' +
                        msg2 +
                        '  </div>\n' +
                        '</fieldset>', {icon: 3, title: '请核对信息，当前角色：' + currRoleName, area:['650px','400px']}, function (index) {

                        let ids = [];
                        curFuncs.map((e) => {
                            ids.push(e.funcId)
                        });
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

                }
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
                roleManager.initMenuTree();
                roleManager.initFuncTable();
            });

            $('#updateMenuRole').on('click', function () {
                if (!currRoleId) {
                    layer.alert('请先选择一个角色再进行操作！');
                    return;
                }

                let curMenus = roleManager.getSelectedMenus();
                let revoked = roleManager.menuMinus(oldMenus, curMenus);
                let granted = roleManager.menuMinus(curMenus, oldMenus);

                let msg1 = "";
                revoked.map((e) => {
                    msg1 += "【" + e + "】</br>"
                });

                let msg2 = "";
                granted.map((e) => {
                    msg2 += "【" + e + "】</br>"
                });

                layer.confirm('<fieldset class="layui-elem-field">\n' +
                    '  <legend>回收菜单</legend>\n' +
                    '  <div class="layui-field-box">\n' +
                    msg1 +
                    '  </div>\n' +
                    '</fieldset>' +
                    '<fieldset class="layui-elem-field">\n' +
                    '  <legend>下放菜单</legend>\n' +
                    '  <div class="layui-field-box">\n' +
                    msg2 +
                    '  </div>\n' +
                    '</fieldset>', {icon: 3, title: '请核对信息，当前角色：' + currRoleName, area:['650px','400px']}, function (index) {

                    let ids = [];
                    curMenus.map((e) => {
                        ids.push(e.id)
                    });

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
                });

            });

        },

        initMenuTree: function () {
            currRoleId = null;
            currRoleName = null;
            oldMenus = null;
            oldFuncs = null;
            layui.ajax.get('api/system/menu/list-all', '', function (data) {
                let json = eval(data);
                let menus = json.rows;

                tree.render({
                    elem: '#menuTree',
                    id: 'menuTree',
                    data: menus,
                    showCheckbox: true
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
                    roleManager.getLeaf(node.children, ids);
                } else {
                    ids.push({"id": node.id, "title": node.title});
                }
            }
        },

        getSelectedMenus: function () {
            let checkedData = tree.getChecked('menuTree'); // 获取选中节点的数据
            let nodeList = eval(checkedData)

            let ids = [];
            roleManager.getLeaf(nodeList, ids);
            return ids;
        },

        menuMinus: function (a, b) {
            let rtn = [];
            for (let i = 0; i < a.length; i++) {
                let j = 0;
                while (j < b.length) {
                    if (a[i].id === b[j].id) {
                        break;
                    }
                    j++;
                }
                if (j == b.length) {
                    rtn.push(a[i].title);
                }
            }
            return rtn;
        },

        funcMinus: function(a, b) {
            let rtn = [];
            for (let i = 0; i < a.length; i++) {
                let j = 0;
                while (j < b.length) {
                    if (a[i].funcId === b[j].funcId) {
                        break;
                    }
                    j++;
                }
                if (j == b.length) {
                    rtn.push("权限编码：" + a[i].funcCode + "，权限说明：" + a[i].funcDesc);
                }
            }
            return rtn;
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
            layer.confirm('真的要删除【' + data.roleName + '】吗?', {icon: 3, title: '提示'}, function (index) {
                layui.ajax.get('api/user/role/delete-role/' + data.roleId, '', function (data) {
                    if (0 == data.code) {
                        layer.msg("删除成功！", {time: 3000, icon: 6});
                        roleManager.initRoleTable();
                        roleManager.initMenuTree();
                        roleManager.initFuncTable();
                    } else {
                        layer.alert("删除失败！" + data.message);
                    }
                });
                layer.close(index);
            });

        },
    };

    exports('roleManager', roleManager);
})