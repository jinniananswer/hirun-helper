layui.extend({

}).define(['ajax', 'echarts'],function(exports){
    let $ = layui.$;

    let chart = {
        drawPie : function(containerId, pieData, rate) {
            if (rate == null) {
                rate = 75;
            }
            let chartPie = null;
            let pie = {
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
            let elementPie = $('#'+containerId).children('div');
            let renderPie = function() {
                chartPie = echarts.init(elementPie[0], layui.echartsTheme);
                chartPie.setOption(pie);
                window.onresize = chartPie.resize;
            };
            renderPie();
        },

        drawBar : function(containerId, data) {
            let values = data.yaxis;
            if (values != null && values.length > 0) {
                let length = values.length;
                for (let i=0;i<length;i++) {
                    let value = values[i];
                    value.type = "bar";
                    value.markPoint = {
                        data : [
                            {type : 'max', name: '最大值'},
                            {type : 'min', name: '最小值'}
                        ]
                    };
                    value.markLine = {
                        data : [{type : 'average', name: '平均值'}]
                    };
                }
            }
            let yearBar = {
                title : {
                    text: data.title,
                    subtext: data.subtitle
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data: data.legend
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : data.xaxis
                    }
                ],
                yAxis : [
                    {
                        type : 'value'
                    }
                ],
                series : values
            }
            let chartBar = null;
            let elementBar = $('#'+containerId).children('div');
            let renderBar = function() {
                chartBar = echarts.init(elementBar[0], layui.echartsTheme);
                chartBar.setOption(yearBar);
                window.onresize = chartBar.resize;
            };
            renderBar();
        }
    };
    exports('chart', chart);
});