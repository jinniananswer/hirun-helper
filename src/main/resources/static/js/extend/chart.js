layui.extend({
    setter: 'config' //配置模块
    ,admin: 'admin' //核心模块
    ,view: 'view' //视图渲染模块
}).define(['ajax', 'carousel', 'echarts', 'setter', 'admin'],function(exports){
    var admin = layui.admin;
    var $ = layui.$;
    var carousel = layui.carousel;
    var echarts = layui.echarts;
    var setter = layui.setter;

    layui.use(['admin', 'carousel'], function(){
        var $ = layui.$
            ,admin = layui.admin
            ,carousel = layui.carousel
            ,element = layui.element
            ,device = layui.device();

        //轮播切换
        $('.layadmin-carousel').each(function(){
            var othis = $(this);
            carousel.render({
                elem: this
                ,width: '100%'
                ,arrow: 'none'
                ,interval: othis.data('interval')
                ,autoplay: othis.data('autoplay') === true
                ,trigger: (device.ios || device.android) ? 'click' : 'hover'
                ,anim: othis.data('anim')
            });
        });

        element.render('progress');

    });

    var chart = {
        drawPie : function(containerId, pieData, rate) {
            if (rate == null) {
                rate = 75;
            }
            var chartPie = null
            var pie = {
                    title : {
                        text: pieData.title,
                        x: 'center',
                        textStyle: {
                            fontSize: 14
                        }
                    },
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        orient : 'vertical',
                        x : 'left',
                        data: pieData.itemNames
                    },
                    series : [{
                        name: pieData.itemTitle,
                        type:'pie',
                        radius : rate + '%',
                        center: ['50%', '50%'],
                        data:pieData.datas
                    }]
                };
            var elementPie = $('#'+containerId).children('div');
            var renderPie = function() {
                chartPie = echarts.init(elementPie[0], layui.echartsTheme);
                chartPie.setOption(pie);
                window.onresize = chartPie.resize;
            };
            renderPie();
        }
    };
    exports('chart', chart);
});