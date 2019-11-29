layui.extend({}).define(['ajax', 'layer'], function (exports) {
    let $ = layui.$;
    let layer = layui.layer;

    let websocketObj = {
        init: function () {
            let employeeId = $("#spanEmployeeId").text();
            let url = "ws://" + document.location.host + "/hirun/websocket/" + employeeId;
            let websocket = null;

            if ('WebSocket' in window) {
                websocket = new WebSocket(url);
            } else {
                alert('当前浏览器不支持 WebSocket，建议升级浏览器！')
            }

            websocket.onopen = function () {
                // layer.msg('WebSocket 连接成功！');
            }

            websocket.onmessage = function (event) {
                let message = event.data;
                let data = JSON.parse(message);
                layer.msg(data.content, {
                    icon: 0,
                    time: 10000,
                    offset: 'rb',
                    btn: ['查看'],
                    yes: function () {
                        let index = layer.open({
                            type: 2,
                            title: '查看详情',
                            content: 'openUrl?url=/modules/system/message-detail',
                            maxmin: true,
                            btn: ['我知道了'],
                            area: ['550px', '700px'],
                            skin: 'layui-layer-molv',
                            success: function (layero, index) {
                                let body = layer.getChildFrame('body', index);
                                body.find('#senderName').html('<h1>来自【' + data.name + '】的' + data.notifyTypeDesc + '</h1>');
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

                                    },
                                    error: function (obj) {

                                    }
                                });
                                layer.close(index);
                            }
                        });
                        layer.full(index);
                    }
                });
            }

            websocket.onerror = function () {
                layer.msg('WebSocket 连接发生错误！');
            };

            // 连接关闭的回调方法
            websocket.onclose = function () {
                layer.msg('WebSocket 连接关闭！');
            }

            // 监听窗口关闭事件，当窗口关闭时，主动去关闭 websocket 连接，防止连接还没断开就关闭窗口，server 端会抛异常。
            window.onbeforeunload = function () {
                websocket.close();
            }

        },
    };

    exports('websocketObj', websocketObj);
})