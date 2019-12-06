layui.extend({}).define(['ajax', 'table', 'element', 'laydate', 'layer', 'tree', 'util'], function (exports) {
    let $ = layui.$;
    let table = layui.table;
    let layer = layui.layer;
    let element = layui.element;
    let laydate = layui.laydate;
    let tree = layui.tree;
    let util = layui.util;

    laydate.render({
        elem: '#start-end-date',
        type: 'date',
        range: true
    });

    let roleObj = {
        init: function () {

            util.event('lay-event', {
                updateMenuRole: function () {
                    let checkedData = tree.getChecked('menuTree'); //获取选中节点的数据
                    let nodeList = eval(checkedData)

                    let ids = [];
                    roleObj.getLeaf(nodeList, ids);
                    let currRoleId = $('#currRoleId').val();
                    let idsJson = JSON.stringify(ids);
                    console.log("currRoleId: ", currRoleId);
                    console.log("ids: ", idsJson);

                    $.ajax({
                        type: "post",
                        url: "api/user/menu-role/updateMenuRole/" + currRoleId,
                        dataType: "json",
                        contentType: "application/json",
                        data: idsJson,
                        success: function (data) {
                            if (0 == data.code) {
                                layer.msg("保存成功！");
                            } else {
                                layer.alert("保存失败！" + data.message);
                            }
                        },
                        error: function (data) {
                            layer.alert("保存失败！");
                        }
                    });

                },

                setChecked: function () {
                    tree.setChecked('demoId1', [12, 16]); //勾选指定节点
                },

                reload: function () {
                    //重载实例
                    tree.reload('demoId1', {});

                }
            });

            element.on('nav(nav-ul)', function (elem) {
                layer.msg(elem.text());
            });

            roleObj.initMenuTree();

            let announce = table.render({
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
                    {
                        field: 'status', title: '状态', align: 'center', templet: function (d) {
                            if (d.status == '0') {
                                return '<span class="layui-badge layui-bg-orange">有效</span>';
                            } else {
                                return '<span class="layui-badge layui-bg-gray">失效</span>';
                            }
                        }
                    },
                    {field: 'createTime', title: '创建时间', width: 160, align: 'center'},
                    {fixed: 'right', align: 'center', title: '操作', width: 150, fixed: 'right', toolbar: '#roleBar'}
                ]],
                page: false,
                text: {none: '暂无相关数据，请检查查询条件。'}
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
            });

            table.on('tool(role-table)', function (obj) {
                let data = obj.data;

                if (obj.event === 'selectRole') {
                    $('#currRoleId').val(data.roleId);
                    layui.ajax.get('api/system/menu/list-all?roleId=' + data.roleId, '', function (data) {
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
                } else if (obj.event === 'editRole') {
                    console.log('editRole: ' + data.id);
                    roleObj.editRole(data);
                } else if (obj.event === 'deleteRole') {
                    console.log('deleteRole: ' + data.id);
                    roleObj.deleteRole([data]);
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

        },

        initMenuTree: function () {
            $('#currRoleId').val("");
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
            if (data.length <= 0) {
                layer.msg('请选中一条数据，再进行操作。');
                return;
            }
            let param = data.map(item => item.id);
            $.ajax({
                type: "post",
                url: "api/system/notify/deleteAnnounce",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(param),
                success: function (obj) {
                    layer.msg("操作成功！");
                    table.reload('announce-table');
                },
                error: function (obj) {
                    layer.alert("操作失败！");
                }
            });

        },
    };

    exports('roleObj', roleObj);
})