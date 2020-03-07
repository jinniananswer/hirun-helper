define(['vue','ELEMENT','ajax'], function(Vue,element,ajax){
    Vue.component('order-info', {
        props: ['order-id'],

        data : function(){
            return {
                sOrderId: this.orderId,
                order:{

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
                                    v-model="order.stage"
                                    range
                                    :marks="stages">
                            </el-slider>
                        </div>
                        <el-divider></el-divider>
                        <el-row :gutter="5">
                            <el-col :span="8">
                                <div class="text">
                                    订单状态：<template v-if="order.statusName != null && order.statusName != ''"><el-tag type="danger">{{order.statusName}}</el-tag></template>
                                </div>
                            </el-col>
                            <el-col :span="8">
                                <div class="text item">
                                    所属楼盘：<template v-if="order.housesName != null && order.housesName != ''"><el-tag>{{order.housesName}}</el-tag></template>
                                </div>
                            </el-col>
                            <el-col :span="8">
                                <div class="text item">
                                    装修地址：<template v-if="order.decorateAddress != null && order.decorateAddress != ''"><el-tag>{{order.decorateAddress}}</el-tag></template>
                                </div>
                            </el-col>
                        </el-row >
                        <el-row :gutter="5">
                            <el-col :span="8">
                                <div class="text">
                                    建筑面积：<template v-if="order.floorage != null && order.floorage != ''"><el-tag>{{order.floorage}}平米</el-tag></template>
                                </div>
                            </el-col>
                            <el-col :span="8">
                                <div class="text">
                                    套内面积：<template v-if="order.indoorArea != null && order.indoorArea != ''"><el-tag>{{order.indoorArea}}平米</el-tag></template>
                                </div>
                            </el-col>
                            <el-col :span="8">
                                <div class="text">
                                    户型：<template v-if="order.houseLayoutName != null && order.houseLayoutName != ''"><el-tag>{{order.houseLayoutName}}</el-tag></template>
                                </div>
                            </el-col>

                        </el-row >
                    </el-tab-pane>
                    <el-tab-pane label="订单费用" name="fee">
                        <el-tabs type="border-card">
                            <el-tab-pane label="费用信息">
                                <el-table
                                    :data="order.orderFees"
                                    stripe="true"
                                    border
                                    row-key="rowKey"
                                    :tree-props="{children:'children', hasChildren: 'hasChildren'}"
                                    style="width: 100%">
                                    <el-table-column
                                            label="费用项"
                                            prop="typeName"
                                            fixed>
                                    </el-table-column>
                                    <el-table-column
                                            prop="periodName"
                                            width="100"
                                            label="期数">
                                    </el-table-column>
                                    <el-table-column
                                            prop="totalMoney"
                                            label="总金额（元）"
                                            width="140"
                                            align="right">
                                    </el-table-column>
                                </el-table>
                            </el-tab-pane>
                            <el-tab-pane label="付款信息">
                                
                            </el-tab-pane>
                        </el-tabs>
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
                if (this.orderId != null) {
                    ajax.get('api/bss.order/order-domain/getOrderDetail', {orderId:this.sOrderId}, function(data) {
                        let stage = data.stage;
                        data.stage = [];
                        data.stage.push(-10);
                        data.stage.push(stage);
                        that.order = data;
                    });
                }
            }
        },

        watch: {
            orderId(val) {
                this.sOrderId = val;
            },

            sOrderId(val, oldValue) {
                if (val != oldValue) {
                    this.init();
                }
            }
        },

        mounted () {
            this.init();
        }
    });


})