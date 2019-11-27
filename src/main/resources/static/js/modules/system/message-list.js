layui.extend({}).define(['ajax', 'table', 'element', 'layer', 'form', 'select', 'redirect'], function (exports) {
    let $ = layui.$;
    let table = layui.table;
    let layer = layui.layer;
    let form = layui.form;
    let element = layui.element;

    let messageObj = {
        init: function () {

            element.on('nav(nav-ul)', function (elem) {
                console.log(elem)
                layer.msg(elem.text());
            });

            let announce = table.render({
                elem: '#announce-table',
                toolbar: '#announceToolbar',
                height: 'full-20',
                url: 'api/system/notify-queue/announce-list',
                fitColumns: true,
                response: {
                    msgName: 'message',
                    countName: 'total',
                    dataName: 'rows'
                },
                page: true,
                cols: [[
                    {type: 'checkbox'},
                    {field: 'id', title: 'ID', width: 60, align: 'center'},
                    {field: 'content', title: '公告内容', align: 'left'},
                    {field: 'createTime', title: '时间', width: 180, align: 'center'},
                    {align: 'center', title: '操作', width: 150, fixed: 'right', toolbar: '#announceBar'}
                ]]
            });

            let message = table.render({
                elem: '#message-table',
                toolbar: '#messageToolbar',
                height: 'full-20',
                url: 'api/system/notify-queue/message-list',
                fitColumns: true,
                response: {
                    msgName: 'message',
                    countName: 'total',
                    dataName: 'rows'
                },
                page: true,
                cols: [[
                    {type: 'checkbox'},
                    {field: 'id', title: 'ID', width: 60, align: 'center'},
                    {field: 'content', title: '私信内容', align: 'left'},
                    {field: 'createTime', title: '时间', width: 180, align: 'center'},
                    {align: 'center', title: '操作', width: 150, fixed: 'right', toolbar: '#messageBar'}
                ]]
            });

            table.on('tool(announce-table)', function (obj) {
                let data = obj.data;
                if (obj.event === 'rowClick') {
                    layer.msg('查看消息！');
                }
                if (obj.event === 'seeDetail') {
                    messageObj.seeDetail(data);
                }
            });
            table.on('tool(message-table)', function (obj) {
                let data = obj.data;
                if (obj.event === 'rowClick') {
                    layer.msg('查看消息！');
                }
                if (obj.event === 'seeDetail') {
                    messageObj.seeDetail(data);
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
                            layui.ajax.get('api/system/notify-queue/markReadedAll', '', function (data) {
                                layer.msg('操作成功！');
                                table.reload('announce-table');
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
                            layui.ajax.get('api/system/notify-queue/markReadedAll', '', function (data) {
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

        seeDetail: function (data) {
            var index = layer.open({
                type: 2,
                title: '查看详情',
                content: 'openUrl?url=/modules/system/message-detail',
                maxmin: true,
                btn: ['我知道了'],
                area: ['550px', '700px'],
                skin: 'layui-layer-molv',
                success: function (layero, index) {
                    let body = layer.getChildFrame('body', index);
                    body.find('#createTime').html('<span>' + data.createTime + '</span>')
                    body.find('#content').html('<div class="layadmin-text">' + data.content +'</div>');
                },
                yes: function (index, layero) {
                    layer.close(index);
                }
            });
            layer.full(index);
        },
    };

    exports('messageObj', messageObj);
});