layui.extend({

}).define(['ajax', 'table', 'element', 'layer', 'form', 'select', 'redirect'], function (exports) {
    var $ = layui.$;
    var table = layui.table;
    var layer = layui.layer;
    var form = layui.form;
    var message = {
        init: function () {

            var ins = table.render({
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
                    {type: 'checkbox', fixed: 'left'},
                    {field: 'id', title: 'ID'},
                    {field: 'userId', title: '标题内容'},
                    {field: 'createTime', title: '时间', sort: true, align: 'center', fixed: 'right'}
                ]]
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