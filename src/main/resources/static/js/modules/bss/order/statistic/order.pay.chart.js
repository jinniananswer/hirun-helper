require(['vue', 'ELEMENT', 'vxe-table', 'axios', 'echarts', 'org-orgtree', 'ajax','order-search-employee','util'], function (Vue, element, vxetable, axios, echarts, orgTree, ajax,orderSearchEmployee,util) {
    Vue.use(echarts)
    let vm = new Vue({
        el: '#app',
        data: function () {
            return {
                queryCond: {},
                pays: [],
                pickerOptions: {
                    shortcuts: [{
                        text: '最近一周',
                        onClick(picker) {
                            const end = util.getNowDate();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                            let begin = util.formatDate(start, "YYYY-MM-DD");
                            picker.$emit('pick', [begin, end]);
                        }
                    }, {
                        text: '最近一个月',
                        onClick(picker) {
                            const end = util.getNowDate();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                            let begin = util.formatDate(start, "YYYY-MM-DD");
                            picker.$emit('pick', [begin, end]);
                        }
                    }, {
                        text: '最近三个月',
                        onClick(picker) {
                            const end = util.getNowDate();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                            let begin = util.formatDate(start, "YYYY-MM-DD");
                            picker.$emit('pick', [begin, end]);
                        }
                    }]
                }
            }
        },

        methods: {
            init: function() {

            },

            query: function() {
                let that = this;
                ajax.post('api/bss/order/order-pay-no/queryPayTrend', this.queryCond, function(responseData){
                    if (responseData) {
                        that.drawEcharts(responseData);
                    }

                });
            },

            drawEcharts : function(data) {
                let option = {
                    title: {
                        text: data.title
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data: data.legend
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    toolbox: {
                        feature: {
                            saveAsImage: {}
                        }
                    },
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        data: data.xaxises
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: data.datas
                };

                let myChart = echarts.init(document.getElementById('echarts_box'));

                myChart.setOption(option)
            }
        },

        mounted() {

        }

    });
})