layui.extend({}).define(['ajax', 'table', 'element', 'layer', 'form', 'select', 'redirect'], function (exports) {
    let $ = layui.$;
    let table = layui.table;
    let layer = layui.layer;
    let form = layui.form;
    let message = {
        init: function () {

            let ins = table.render({
                elem: '#message-table',
                toolbar: '#toolbar',
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
                    {field: 'userId', title: '标题内容', align: 'left'},
                    {field: 'createTime', title: '时间', align: 'center'}
                ]]
            });

            table.on('tool(message-table)', function (obj) {
                let data = obj.data;
                if (obj.event === 'rowClick') {
                    layer.msg('查看消息！');
                }
            });

            table.on('toolbar(message-table)', function (obj) {
                let checkStatus = table.checkStatus(obj.config.id); // 获取选中行状态
                let data = checkStatus.data;
                let event = obj.event;

                if (data.length <= 0) {
                    layer.msg('请选中一条数据，再进行操作。');
                    return;
                }

                switch (event) {
                    case 'delete':
                        layer.msg('删除: ' + data[0].createTime);
                        break;
                    case 'markReaded':
                        let param = data.map(item => item.id);
                        console.log(param);
                        // layui.ajax.post('api/system/notify-queue/markReaded', param, function (data) {
                        //
                        // });

                        $.ajax({
                            type: "post",
                            url: "api/system/notify-queue/markReaded",
                            dataType: "json",
                            contentType: "application/json",
                            data: JSON.stringify(param),
                            success: function (obj) {
                                alert(obj.description);
                            },
                            error: function (obj) {
                                alert("操作出错");
                                return false;
                            }
                        });

                        layer.msg('标记已读: ' + data[0].createTime);
                        break;
                    case 'markReadedAll':
                        layer.msg('全部已读: ' + data[0].createTime);
                        break;
                }
                ;
            });
        },

        deleteMessage: function (data) {

        },

    };
    exports('message', message);
});