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
                    60: '实现',
                    95: '维护'
                },
                activeTab:'orderInfo',
                subActiveTab: ''
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
                        <el-tabs v-model="subActiveTab" type="border-card">
                            <el-tab-pane label="费用信息" name="feeInfo">
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
                            <el-tab-pane label="付款信息" name="payInfo">
                                <el-table
                                    :data="order.orderPays"
                                    stripe="true"
                                    border
                                    style="width: 100%">
                                    <el-table-column type="expand">
                                        <template slot-scope="props">
                                            <el-table
                                                :data="props.row.payItems"
                                                stripe="true"
                                                border
                                                style="width: 100%">
                                                <el-table-column
                                                        label="收款项"
                                                        prop="payItemName"
                                                        fixed>
                                                </el-table-column>
                                                <el-table-column
                                                        prop="money"
                                                        width="100"
                                                        align="right"
                                                        label="金额（元）">
                                                </el-table-column>
                                            </el-table>
                                            <br/>
                                            <el-table
                                                :data="props.row.payMonies"
                                                stripe="true"
                                                border
                                                style="width: 100%">
                                                <el-table-column
                                                        label="付款方式"
                                                        prop="paymentName"
                                                        fixed>
                                                </el-table-column>
                                                <el-table-column
                                                        prop="money"
                                                        width="100"
                                                        align="right"
                                                        label="金额（元）">
                                                </el-table-column>
                                            </el-table>
                                        </template>
                                    </el-table-column>
                                    <el-table-column
                                            label="收款日期"
                                            prop="payDate"
                                            width="120">
                                    </el-table-column>
                                    <el-table-column
                                            prop="employeeName"
                                            label="收款员工"
                                            width="100">
                                    </el-table-column>
                                    <el-table-column
                                            prop="shopName"
                                            label="收款店面" 
                                            width="140">
                                    </el-table-column>
                                    <el-table-column
                                            prop="totalMoney"
                                            align="right"
                                            label="收款总金额（元）">
                                    </el-table-column>
                                    
                                </el-table>
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
                        </div>
                    </el-tab-pane>
                    <el-tab-pane label="订单文件" name="file">
                        <el-table
                            :data="order.orderFiles"
                            stripe="true"
                            border
                            style="width: 100%">
                            <el-table-column
                                    prop="stageName"
                                    width="120"
                                    label="文件类型">
                            </el-table-column>
                            <el-table-column
                                    prop="fileName"
                                    label="文件名称">
                            </el-table-column>
                            <el-table-column
                                    align="center"
                                    width="100"
                                    label="文件下载">
                                    <template slot-scope="scope">
                                        <el-link type="primary" :href="'api/bss.order/order-file/download/'+scope.row.orderId+'/'+scope.row.stage">下载文件</el-link>
                                    </template>
                            </el-table-column>
                        </el-table>
                    </el-tab-pane>
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

                        if (data.tabShow) {
                            if (data.tabShow.indexOf(".") > 0) {
                                let array = data.tabShow.split(".");
                                that.activeTab = array[0];
                                that.subActiveTab = array[1];
                            } else {
                                that.activeTab = data.tabShow;
                                that.subActiveTab = 'feeInfo';
                            }
                        } else {
                            that.activeTab = 'orderInfo';
                            that.subActiveTab = 'feeInfo';
                        }
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