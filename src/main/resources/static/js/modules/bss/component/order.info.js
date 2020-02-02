define(['vue','ELEMENT','ajax'], function(Vue,element,ajax){
    Vue.component('order-info', {
        props: ['order-id'],

        data : function(){
            return {
                order:{
                    base:{},
                    product:{},
                    fee:{},
                    contract:{}
                },
                stages:{
                    0: '酝酿',
                    10: '初选',
                    30: '初步决策',
                    50: '决策',
                    60: '施工',
                    95: '维护'
                },
                activeTab:'orderInfo'
            }
        },

        template: `
            <el-card class="box-card" shadow="hover">
                <el-tabs v-model="activeTab">
                    <el-tab-pane label="订单信息" name="orderInfo">
                        <div class="text item" style="margin-left: 18px;padding-bottom: 10px;width:95%" >
                            <el-slider
                                    v-model="order.base.stage"
                                    range
                                    :marks="stages">
                            </el-slider>
                        </div>

                        <el-row :gutter="5">
                            <el-col :span="8">
                                <div class="text">
                                    订单状态：<template v-if="order.base.statusName != null && order.base.statusName != ''"><el-tag type="danger">{{order.base.statusName}}</el-tag></template>
                                </div>
                            </el-col>
                            <el-col :span="8">
                                <div class="text item">
                                    所属楼盘：<template v-if="order.base.housesName != null && order.base.housesName != ''"><el-tag>{{order.base.housesName}}</el-tag></template>
                                </div>
                            </el-col>
                            <el-col :span="8">
                                <div class="text item">
                                    装修地址：<template v-if="order.base.decorateAddress != null && order.base.decorateAddress != ''"><el-tag>{{order.base.decorateAddress}}</el-tag></template>
                                </div>
                            </el-col>
                        </el-row >
                        <el-row :gutter="5">
                            <el-col :span="8">
                                <div class="text">
                                    建筑面积：<template v-if="order.base.floorage != null && order.base.floorage != ''"><el-tag>{{order.base.floorage}}平米</el-tag></template>
                                </div>
                            </el-col>
                            <el-col :span="8">
                                <div class="text">
                                    套内面积：<template v-if="order.base.indoorArea != null && order.base.indoorArea != ''"><el-tag>{{order.base.indoorArea}}平米</el-tag></template>
                                </div>
                            </el-col>
                            <el-col :span="8">
                                <div class="text">
                                    户型：<template v-if="order.base.houseLayoutName != null && order.base.houseLayoutName != ''"><el-tag>{{order.base.houseLayoutName}}</el-tag></template>
                                </div>
                            </el-col>

                        </el-row >
                    </el-tab-pane>
                    <el-tab-pane label="订单费用" name="fee">
                        订单费用
                    </el-tab-pane>
                    <el-tab-pane label="所选产品" name="product">
                        <div class="text item">
                            <h3>风格蓝图：写意白描</h3>
                            <el-alert
                                title="以抽象概括的设计手法，从中国传统图案或构件中提取线条，并运用于整套设计中表现文士审美追求的设计系列"
                                type="info"
                                :closable="false"
                                show-icon>
                            </el-alert>
                        </div>
                        <div class="text item">
                            <h3>功能蓝图</h3>
                        </div></el-tab-pane>
                    <el-tab-pane label="合同信息" name="contract">合同信息</el-tab-pane>
                </el-tabs>
            </el-card>
            `,

        methods: {
            init() {
                let that = this;
                ajax.get('api/bss.order/order-base/getOrderInfo', {orderId:this.orderId}, function(data) {
                    let stage = data.stage;
                    data.stage = [];
                    data.stage.push(-10);
                    data.stage.push(stage);
                    that.order.base = data;

                })
            }
        },

        watch: {

        },

        mounted () {
            this.init();
        }
    });


})