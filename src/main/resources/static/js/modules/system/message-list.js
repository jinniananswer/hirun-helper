layui.extend({}).define(['ajax', 'table', 'element', 'layer', 'form', 'select', 'redirect'], function (exports) {
    let $ = layui.$;
    let table = layui.table;
    let layer = layui.layer;
    let form = layui.form;
    let element = layui.element;

    let messageObj = {
        init: function () {

            element.on('nav(nav-ul)', function (elem) {
                layer.msg(elem.text());
            });

            let announce = table.render({
                elem: '#announce-table',
                toolbar: '#announceToolbar',
                defaultToolbar: ['filter'],
                height: 'full-20',
                url: 'api/system/notify-queue/announce-list',
                fitColumns: true,
                response: {
                    msgName: 'message',
                    countName: 'total',
                    dataName: 'rows'
                },
                page: false,
                cols: [[
                    {type: 'checkbox'},
                    {field: 'id', title: 'ID', width: 60, align: 'center'},
                    {field: 'content', title: '公告内容', align: 'left'},
                    {field: 'name', title: '发送者', width: 80, align: 'center'},
                    {field: 'createTime', title: '时间', width: 160, align: 'center'},
                    {
                        field: 'readed', title: '状态', width: 60, align: 'center', templet: function (d) {
                            if (d.readed) {
                                return '<span class="layui-badge-dot layui-bg-gray"></span>';
                            } else {
                                return '<span class="layui-badge-dot"></span>';
                            }
                        }
                    },
                    {align: 'center', title: '操作', width: 80, fixed: 'right', toolbar: '#announceBar'}
                ]]
            });

            let notice = table.render({
                elem: '#notice-table',
                toolbar: '#noticeToolbar',
                defaultToolbar: ['filter'],
                height: 'full-20',
                url: 'api/system/notify-queue/notice-list',
                fitColumns: true,
                response: {
                    msgName: 'message',
                    countName: 'total',
                    dataName: 'rows'
                },
                page: false,
                cols: [[
                    {type: 'checkbox'},
                    {field: 'id', title: 'ID', width: 60, align: 'center'},
                    {field: 'content', title: '通知内容', align: 'left'},
                    {field: 'name', title: '发送者', width: 80, align: 'center'},
                    {field: 'createTime', title: '时间', width: 160, align: 'center'},
                    {
                        field: 'readed', title: '状态', width: 60, align: 'center', templet: function (d) {
                            if (d.readed) {
                                return '<span class="layui-badge-dot layui-bg-gray"></span>';
                            } else {
                                return '<span class="layui-badge-dot"></span>';
                            }
                        }
                    },
                    {align: 'center', title: '操作', width: 80, fixed: 'right', toolbar: '#announceBar'}
                ]]
            });

            let message = table.render({
                elem: '#message-table',
                toolbar: '#messageToolbar',
                defaultToolbar: ['filter'],
                height: 'full-20',
                url: 'api/system/notify-queue/message-list',
                fitColumns: true,
                response: {
                    msgName: 'message',
                    countName: 'total',
                    dataName: 'rows'
                },
                page: false,
                cols: [[
                    {type: 'checkbox'},
                    {field: 'id', title: 'ID', width: 60, align: 'center'},
                    {field: 'content', title: '私信内容', align: 'left'},
                    {field: 'name', title: '发送者', width: 80, align: 'center'},
                    {field: 'createTime', title: '时间', width: 160, align: 'center'},
                    {
                        field: 'readed', title: '状态', width: 60, align: 'center', templet: function (d) {
                            if (d.readed) {
                                return '<span class="layui-badge-dot layui-bg-gray"></span>';
                            } else {
                                return '<span class="layui-badge-dot"></span>';
                            }
                        }
                    },
                    {align: 'center', title: '操作', width: 80, fixed: 'right', toolbar: '#messageBar'}
                ]]
            });

            table.on('tool(announce-table)', function (obj) {
                let data = obj.data;
                if (obj.event === 'rowClick') {
                    layer.msg('查看消息！');
                }
                if (obj.event === 'seeDetail') {
                    messageObj.seeDetail(data, '公告');
                }
            });
            table.on('tool(notice-table)', function (obj) {
                let data = obj.data;
                if (obj.event === 'rowClick') {
                    layer.msg('查看消息！');
                }
                if (obj.event === 'seeDetail') {
                    messageObj.seeDetail(data, '通知');
                }
            });
            table.on('tool(message-table)', function (obj) {
                let data = obj.data;
                if (obj.event === 'rowClick') {
                    layer.msg('查看消息！');
                }
                if (obj.event === 'seeDetail') {
                    messageObj.seeDetail(data, '私信');
                }
            });

            table.on('toolbar(announce-table)', function (obj) {
                let checkStatus = table.checkStatus(obj.config.id); // 获取选中行状态
                let data = checkStatus.data;
                let event = obj.event;

                switch (event) {
                    case 'markReaded':
                        if (data.length <= 0) {
                            layer.msg('请选中一条数据，再进行操作。');
                            return;
                        }
                        let param = data.map(item => item.id);
                        $.ajax({
                            type: "post",
                            url: "api/system/notify-queue/markReaded",
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
                        break;
                    case 'markReadedAll':

                        layer.confirm('要全部标记已读？', {
                            btn: ['Yes', 'No'],
                            closeBtn: 0,
                            icon: 6,
                            title: '提示信息',
                            shade: [0.5, '#fff'],
                            skin: 'layui-layer-admin layui-anim'
                        }, function (index) {
                            layui.ajax.get('api/system/notify-queue/markReadedAll/1', '', function (data) {
                                layer.msg('操作成功！');
                                table.reload('announce-table');
                            });
                        }, function () {

                        });

                        break;
                }
                ;
            });

            table.on('toolbar(notice-table)', function (obj) {
                let checkStatus = table.checkStatus(obj.config.id); // 获取选中行状态
                let data = checkStatus.data;
                let event = obj.event;

                switch (event) {
                    case 'markReaded':
                        if (data.length <= 0) {
                            layer.msg('请选中一条数据，再进行操作。');
                            return;
                        }
                        let param = data.map(item => item.id);
                        $.ajax({
                            type: "post",
                            url: "api/system/notify-queue/markReaded",
                            dataType: "json",
                            contentType: "application/json",
                            data: JSON.stringify(param),
                            success: function (obj) {
                                layer.msg("操作成功！");
                                table.reload('notice-table');
                            },
                            error: function (obj) {
                                layer.alert("操作失败！");
                            }
                        });
                        break;
                    case 'markReadedAll':

                        layer.confirm('要全部标记已读？', {
                            btn: ['Yes', 'No'],
                            closeBtn: 0,
                            icon: 6,
                            title: '提示信息',
                            shade: [0.5, '#fff'],
                            skin: 'layui-layer-admin layui-anim'
                        }, function (index) {
                            layui.ajax.get('api/system/notify-queue/markReadedAll/1', '', function (data) {
                                layer.msg('操作成功！');
                                table.reload('notice-table');
                            });
                        }, function () {

                        });

                        break;
                }
                ;
            });

            table.on('toolbar(message-table)', function (obj) {
                let checkStatus = table.checkStatus(obj.config.id); // 获取选中行状态
                let data = checkStatus.data;
                let event = obj.event;

                switch (event) {
                    case 'markReaded':
                        if (data.length <= 0) {
                            layer.msg('请选中一条数据，再进行操作。');
                            return;
                        }
                        let param = data.map(item => item.id);
                        $.ajax({
                            type: "post",
                            url: "api/system/notify-queue/markReaded",
                            dataType: "json",
                            contentType: "application/json",
                            data: JSON.stringify(param),
                            success: function (obj) {
                                layer.msg("操作成功！");
                                table.reload('message-table');
                            },
                            error: function (obj) {
                                layer.alert("操作失败！");
                            }
                        });
                        break;
                    case 'markReadedAll':

                        layer.confirm('要全部标记已读？', {
                            btn: ['Yes', 'No'],
                            closeBtn: 0,
                            icon: 6,
                            title: '提示信息',
                            shade: [0.5, '#fff'],
                            skin: 'layui-layer-admin layui-anim'
                        }, function (index) {
                            layui.ajax.get('api/system/notify-queue/markReadedAll/3', '', function (data) {
                                layer.msg('操作成功！');
                                table.reload('message-table');
                            });
                        }, function () {

                        });

                        break;
                }
                ;
            });
        },

        seeDetail: function (data, notifyType) {
            var index = layer.open({
                type: 2,
                title: '查看详情',
                content: 'openUrl?url=modules/system/message-detail',
                maxmin: true,
                btn: ['我知道了'],
                area: ['550px', '400px'],
                skin: 'layui-layer-molv',
                success: function (layero, index) {
                    let body = layer.getChildFrame('body', index);
                    body.find('#senderName').html('<h1>来自【' + data.name + '】的' + notifyType + '</h1>');
                    body.find('#createTime').html('<span>' + data.createTime + '</span>');
                    body.find('#content').html('<div class="layadmin-text">' + data.content + '</div>');
                },
                yes: function (index, layero) {
                    $.ajax({
                        type: "post",
                        url: "api/system/notify-queue/markReaded",
                        dataType: "json",
                        contentType: "application/json",
                        data: JSON.stringify([data.id]),
                        success: function (obj) {
                            layer.msg("操作成功！");
                            table.reload('announce-table');
                            table.reload('notice-table');
                            table.reload('message-table');
                        },
                        error: function (obj) {
                            layer.alert("操作失败！");
                        }
                    });
                    layer.close(index);
                }
            });

        },
    };

    exports('messageObj', messageObj);
});