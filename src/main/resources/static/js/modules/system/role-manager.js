layui.extend({}).define(['ajax', 'table', 'element', 'layedit', 'laydate', 'layer', 'tree'], function (exports) {
    let $ = layui.$;
    let table = layui.table;
    let layedit = layui.layedit;
    let layer = layui.layer;
    let element = layui.element;
    let laydate = layui.laydate;
    let tree = layui.tree;

    laydate.render({
        elem: '#start-end-date',
        type: 'date',
        range: true
    });

    let roleObj = {
        init: function () {

            element.on('nav(nav-ul)', function (elem) {
                layer.msg(elem.text());
            });

            // 菜单树
            tree.render({
                elem: '#menuTree',
                data: [{
                    title: '一级1'
                    , id: 1
                    , field: 'name1'
                    , checked: true
                    , spread: true
                    , children: [{
                        title: '二级1-1 可允许跳转'
                        , id: 3
                        , field: 'name11'
                        , href: 'https://www.layui.com/'
                        , children: [{
                            title: '三级1-1-3'
                            , id: 23
                            , field: ''
                            , children: [{
                                title: '四级1-1-3-1'
                                , id: 24
                                , field: ''
                                , children: [{
                                    title: '五级1-1-3-1-1'
                                    , id: 30
                                    , field: ''
                                }, {
                                    title: '五级1-1-3-1-2'
                                    , id: 31
                                    , field: ''
                                }]
                            }]
                        }, {
                            title: '三级1-1-1'
                            , id: 7
                            , field: ''
                            , children: [{
                                title: '四级1-1-1-1 可允许跳转'
                                , id: 15
                                , field: ''
                                , href: 'https://www.layui.com/doc/'
                            }]
                        }, {
                            title: '三级1-1-2'
                            , id: 8
                            , field: ''
                            , children: [{
                                title: '四级1-1-2-1'
                                , id: 32
                                , field: ''
                            }]
                        }]
                    }, {
                        title: '二级1-2'
                        , id: 4
                        , spread: true
                        , children: [{
                            title: '三级1-2-1'
                            , id: 9
                            , field: ''
                            , disabled: true
                        }, {
                            title: '三级1-2-2'
                            , id: 10
                            , field: ''
                        }]
                    }, {
                        title: '二级1-3'
                        , id: 20
                        , field: ''
                        , children: [{
                            title: '三级1-3-1'
                            , id: 21
                            , field: ''
                        }, {
                            title: '三级1-3-2'
                            , id: 22
                            , field: ''
                        }]
                    }]
                }, {
                    title: '一级2'
                    , id: 2
                    , field: ''
                    , spread: true
                    , children: [{
                        title: '二级2-1'
                        , id: 5
                        , field: ''
                        , spread: true
                        , children: [{
                            title: '三级2-1-1'
                            , id: 11
                            , field: ''
                        }, {
                            title: '三级2-1-2'
                            , id: 12
                            , field: ''
                        }]
                    }, {
                        title: '二级2-2'
                        , id: 6
                        , field: ''
                        , children: [{
                            title: '三级2-2-1'
                            , id: 13
                            , field: ''
                        }, {
                            title: '三级2-2-2'
                            , id: 14
                            , field: ''
                            , disabled: true
                        }]
                    }]
                }, {
                    title: '一级3'
                    , id: 16
                    , field: ''
                    , children: [{
                        title: '二级3-1'
                        , id: 17
                        , field: ''
                        , fixed: true
                        , children: [{
                            title: '三级3-1-1'
                            , id: 18
                            , field: ''
                        }, {
                            title: '三级3-1-2'
                            , id: 19
                            , field: ''
                        }]
                    }, {
                        title: '二级3-2'
                        , id: 27
                        , field: ''
                        , children: [{
                            title: '三级3-2-1'
                            , id: 28
                            , field: ''
                        }, {
                            title: '三级3-2-2'
                            , id: 29
                            , field: ''
                        }]
                    }]
                }],
                showCheckbox: true,
                id: 'demoId1',
                isJump: true, // 是否允许点击节点时弹出新窗口跳转
                click: function (obj) {
                    var data = obj.data;  //获取当前点击的节点数据
                    layer.msg('状态：' + obj.state + '<br>节点数据：' + JSON.stringify(data));
                }
            });

            let announce = table.render({
                elem: '#role-table',
                toolbar: '#roleToolbar',
                defaultToolbar: ['filter'],
                height: 'full-20',
                fitColumns: true,
                response: {
                    msgName: 'message',
                    countName: 'total',
                    dataName: 'rows'
                },
                cols: [[
                    {field: 'roleId', title: 'ID', width: 60, align: 'center'},
                    {field: 'roleName', title: '角色名', align: 'center'},
                    {field: 'status', title: '状态', align: 'left'},
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
                })
            });

            table.on('tool(role-table)', function (obj) {
                let data = obj.data;

                if (obj.event === 'editRole') {
                    console.log('editRole: ' + data.id);
                    roleObj.editRole(data);
                } else if (obj.event === 'deleteRole') {
                    console.log('deleteRole: ' + data.id);
                    roleObj.deleteRole([data]);
                }
            });

            table.on('toolbar(role-table)', function (obj) {
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

        editRole: function (data) {
            var index = layer.open({
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