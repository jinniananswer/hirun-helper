layui.extend({
}).define(['ajax','echarts'],function(exports){
    let $ = layui.$;
    let echarts = layui.echarts;

    let map = {
        draw : function(containerId, mapName, areaType) {
            layui.ajax.get("api/system/map/mapByShopNum", '&areaType='+areaType, function(datas) {
                let rows = datas.rows;
                if (rows != null && rows.length > 0) {
                    let length = rows.length;
                    for (let i=0;i<length;i++) {
                        let data = rows[i];
                        data.itemStyle = {
                            normal: {
                                areaStyle: {color:'#FF5722'},
                                label: {
                                    show:true,
                                    formatter: '{a}\n{b}',
                                    textStyle:{color:"#333"}
                                }
                            }
                        };
                    }
                }
                let option = {
                    title : {
                        text: '门店分布',
                        subtext: ''
                    },
                    tooltip : {
                        trigger: 'item'
                    },
                    dataRange: {
                        orient: 'vertical',
                        min: 0,
                        max: 5,
                        text:['高','低'],
                        splitNumber:0
                    },
                    series : [
                        {
                            name: '门店分布',
                            type: 'map',
                            mapType: mapName,
                            itemStyle:{
                                normal:{
                                    label:{show:false},
                                    borderWidth: 1,//设置外层边框
                                    borderColor:'#666666',
                                    areaStyle:{color:'#ffffff'}
                                },
                                emphasis:{label:{show:true}}
                            },
                            data:rows
                        }
                    ]
                };
                let mapView = null;
                let elemDataView = $('#'+containerId).children('div')
                let renderDataView = function(){
                    mapView = echarts.init(elemDataView[0], layui.echartsTheme);
                    mapView.setOption(option);
                    mapView.on('click', function (param) {
                        let name = param.name;
                        areaType++;
                        if (mapName != 'china') {
                            name = 'china';
                            areaType = 1;
                        }
                        layui.map.draw(containerId, name, areaType);
                    });
                    window.onresize = mapView.resize;
                };

                renderDataView();
            });
        }

    };
    exports('map', map);
});