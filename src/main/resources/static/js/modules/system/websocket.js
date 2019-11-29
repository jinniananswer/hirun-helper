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
                layer.msg('WebSocket 连接成功！');
            }

            websocket.onmessage = function (event) {
                layer.msg(event.data, {
                    icon: 0,
                    time: 15000,
                    offset: 'rb'
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