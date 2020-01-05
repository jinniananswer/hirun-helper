layui.extend({
    setter: 'config', //配置模块
    admin: 'admin', //核心模块
    view: 'view' //视图渲染模块
}).define(['ajax', 'map', 'carousel', 'echarts', 'setter', 'admin', 'chart', 'layer', 'table'], function (exports) {
    /*
      下面通过 layui.use 分段加载不同的模块，实现不同区域的同时渲染，从而保证视图的快速呈现
    */
    let $ = layui.$;
    let admin = layui.admin;
    let carousel = layui.carousel;
    let echarts = layui.echarts;
    let setter = layui.setter;
    let table = layui.table;

    //区块轮播切换
    layui.use(['admin', 'carousel'], function () {
        let $ = layui.$;
        let admin = layui.admin;
        let carousel = layui.carousel;
        let element = layui.element;
        let device = layui.device();

        //轮播切换
        $('.layadmin-carousel').each(function () {
            var othis = $(this);
            carousel.render({
                elem: this,
                width: '100%',
                arrow: 'none',
                interval: othis.data('interval'),
                autoplay: othis.data('autoplay') === true,
                trigger: (device.ios || device.android) ? 'click' : 'hover',
                anim: othis.data('anim')
            });
        });


    });

    let console = {
        init: function () {
            layui.map.draw('LAY-index-mapview', 'china', 1);
            layui.ajax.post('api/organization/employee-statistic/countInAndDestroyOneYear', '', function (data) {
                let bar = data.rows;
                layui.chart.drawBar('LAY-index-dataview', bar);
            });
            this.drawBirthdayWish();
            this.drawPending();
            this.drawHotMenus();
            this.drawMessage();
        },

        /** 公告/通知/私信 */
        drawMessage: function () {

            let messageTable = table.render({
                elem: '#message-table',
                url: 'api/system/notify-queue/query-unread-all',
                fitColumns: true,
                text: {none: '无未读信息'},
                // cellMinWidth: 70,
                response: {
                    msgName: 'message',
                    countName: 'total',
                    dataName: 'rows'
                },
                page: false,
                cols: [[
                    {
                        field: 'readed', title: '类型', width: 70, align: 'center', templet: function (d) {
                            if (d.notifyType === 1) {
                                return '<span class="layui-badge layui-bg-orange">公告</span>';
                            } else if (d.notifyType === 2) {
                                return '<span class="layui-badge layui-bg-green">通知</span>';
                            } else if (d.notifyType === 3) {
                                return '<span class="layui-badge layui-bg-blue">私信</span>';
                            } else {
                                return '';
                            }
                        }
                    },
                    {field: 'content', title: '内容', align: 'left'},
                ]],
                done: function () {
                    // $('th').hide();
                }
            });
            //监听行单击事件（双击事件为：rowDouble）
            table.on('row(message-table)', function(obj) {
                let data = obj.data;
                let index = layer.open({
                    type: 2,
                    title: '查看详情',
                    content: 'openUrl?url=modules/system/message-detail',
                    maxmin: true,
                    btn: ['我知道了'],
                    area: ['550px', '400px'],
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
                                layer.msg("操作成功！");
                                table.reload('message-table');
                            },
                            error: function (obj) {
                                layer.alert("操作失败！");
                            }
                        });
                        layer.close(index);
                    }
                });

                //标注选中样式
                obj.tr.addClass('layui-table-click').siblings().removeClass('layui-table-click');
            });
        },

        /** 常用菜单 */
        drawHotMenus: function () {
            layui.ajax.get('api/system/menu-click/hostMenus', '', function (data) {
                let rows = data.rows;
                let html = [];
                for (let i = 0; i < rows.length; i++) {
                    html.push("    <li class=\"layui-col-xs3\">\n");
                    html.push("        <a lay-href=\"" + rows[i].menuUrl + "\" title=\"" + rows[i].title + "\">\n");
                    html.push("            <i class=\"layui-icon " + rows[i].iconfont +"\"></i>\n");
                    html.push("            <cite>" + rows[i].title + "</cite>\n");
                    html.push("        </a>\n");
                    html.push("    </li>\n");
                }

                $("#hostMenusPage1").empty();
                $("#hostMenusPage1").html(html.join(""));
            });
        },

        drawBirthdayWish: function () {
            layui.ajax.get('api/organization/employee/showBirthdayWish', '', function (data) {
                let employee = data.rows;
                if (employee == null) {
                    return;
                }

                let name = employee.name;
                let day = employee.day;

                if (name == null) {
                    return;
                }

                let html = [];
                html.push("<div class=\"layui-carousel\" id=\"birthdayDiv\">");
                html.push("<div carousel-item>");
                html.push("<div style='background: url(\"static/img/birthdaybg.jpg\") no-repeat;background-size: cover;'><div style='position:absolute;top:175px;padding:20px;width:100%;text-align: center;'><h1 style='font-size: 50px'>HI," + name + "：<span style='width:100%;font-size: 50px;color:#e20b2a'><strong>生日快乐！</strong></span></h1></div></div>");
                html.push("<div style='background: url(\"static/img/birthdaybg.jpg\") no-repeat;background-size: cover;'><div style='position: absolute;top:150px;padding:20px;line-height: 40px;'><h2>亲爱的" + name + "，今天是你的生日，因为你的降临，今天成了一个美丽的日子，从此世界便多出了一抹绚丽的色彩</h2></div></div>");
                html.push("<div style='background: url(\"static/img/birthdaybg.jpg\") no-repeat;background-size: cover;'><div style='position: absolute;top:150px;padding:20px;line-height: 40px;'><h2>不负韶光，鸿扬与你也已经携手走过了<strong style='color:#e20b2a'>" + day + "</strong>个日夜，过去的日子里，公司的发展离不开你的付出与努力，同时也离不开你家人的支持与奉献，在此向你们表示衷心感谢!</h2></div></div>");
                html.push("<div style='background: url(\"static/img/birthdaybg.jpg\") no-repeat;background-size: cover;'><div style='position: absolute;top:150px;padding:20px;line-height: 40px;'><h2>在这个特别的日子里我们谨代表学富五车、英俊潇洒的董事长祝贺你生日快乐，希望你身体倍儿棒，吃嘛嘛儿香！</h2></div></div>");
                html.push("<div style='background: url(\"static/img/birthdaybg.jpg\") no-repeat;background-size: cover;'><div style='position: absolute;top:150px;padding:20px;line-height: 40px;'><h2>虽然只是一个小小的问候，但那是我的挂念；虽然只是一句轻轻的祝福，但那是我的期盼。人生旅程的又一个起点，等待着你继续扬帆起航！</h2></div></div>");
                html.push("</div>");
                html.push("</div>");

                let birthdayDiv = $("#birthdayWish");
                birthdayDiv.html(html.join(""));
                layui.use('carousel', function () {
                    let carousel = layui.carousel;
                    //建造实例
                    carousel.render({
                        elem: '#birthdayDiv'
                        , width: '800px' //设置容器宽度
                        , height: '457px'
                        , arrow: 'always' //始终显示箭头
                        //,anim: 'updown' //切换动画方式
                    });
                });
                layui.layer.open({
                    title: false,
                    type: 1,
                    area: '800px',
                    content: $("#birthdayWish") //这里content是一个普通的String
                });
            });
        },

        drawPending: function () {
            layui.ajax.post('api/organization/hr-pending/countPending', '', function (datas) {
                var data = datas.rows;
                if (data == null || data.length <= 0) {
                    return;
                }
                $("#pending").empty();

                var length = data.length;
                var html = [];
                for (var i = 0; i < length; i++) {
                    var pendingData = data[i];
                    var pendingType = pendingData.name;
                    var pendingNum = pendingData.num;
                    if(i==2){
                        html.push('<li class="layui-col-xs12">');
                    }else{
                        html.push('<li class="layui-col-xs6">');
                    }
                    html.push('<a lay-href="openUrl?url=modules/organization/hr/pending_manager" class="layadmin-backlog-body">');
                    html.push('<h3>' + pendingType + '</h3>');
                    html.push('<p><cite>' + pendingNum + '</cite></p>');
                    html.push('</a></li>');
                }
                $("#pending").html(html.join(""));
            });
        }

    }

    exports('console', console);
});