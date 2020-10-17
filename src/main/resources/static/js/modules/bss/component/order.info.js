define(['vue','ELEMENT','ajax'], function(Vue,element,ajax){
    Vue.component('order-info', {
        props: ['order-id'],

        data : function(){
            return {
                sOrderId: this.orderId,
                order:{

                },
                customer: {

                },
                stages:{
                    0: '酝酿',
                    10: '初选',
                    30: '初步决策',
                    50: '决策',
                    60: '实现',
                    95: '维护'
                },
                defaultProps: {
                    children: 'children',
                    label: 'title'
                },
                dialogTableVisible: false,
                funcDialogVisibleA:false,
                funcDialogVisibleB:false,
                funcDialogVisibleC:false,
                funcDialogVisibleAll:false,
                disabledA:true,
                disabledB:true,
                disabledC:true,
                activeTab:'orderInfo',
                subActiveTab: ''
            }
        },

        template: `
            <el-card class="box-card" shadow="hover">
                <el-row :gutter="20">
                    <el-col :span="4">
                        <el-row :gutter="12">
                            <el-col :span="8">
                                <el-avatar :size="60" v-if="customer.headUrl != null" :src="customer.headUrl"></el-avatar>
                                <el-avatar :size="60" v-else>{{customer.custName!=null?customer.custName.substring(0,1):''}}</el-avatar>
                            </el-col>
                            <el-col :span="16">
                                <h3 v-if="customer.custName != null && customer.custName != ''">{{customer.custName}}</h3>
                                <h3 v-else>未录入</h3>
                            </el-col>
                        </el-row>
                        <el-divider>基本资料</el-divider>
                        <div class="text item">
                            客户编码：<template v-if="customer.custNo != null && customer.custNo != ''"><el-tag>{{customer.custNo}}</el-tag></template><template v-else>未录入</template>
                        </div>
                        <div class="text item">
                            性别：<template v-if="customer.sexName != null && customer.sexName != ''"><el-tag>{{customer.sexName}}</el-tag></template><template v-else>未录入</template>
                        </div>
                        <div class="text item">
                            电话：<template v-if="customer.mobileNo != null && customer.mobileNo != ''"><el-tag>{{customer.mobileNo}}</el-tag></template><template v-else>未录入</template>
                        </div>
                    </el-col>
                    <el-col :span="20">
                        <el-tabs v-model="activeTab">
                            <el-tab-pane label="客户信息" name="customerInfo">
                                <el-row :gutter="5">
                                    <el-col :span="8">
                                        <div class="text">
                                            客户姓名：<template v-if="customer.custName != null && customer.custName != ''"><el-tag type="danger">{{customer.custName}}</el-tag></template>
                                        </div>
                                    </el-col>
                                    <el-col :span="8">
                                        <div class="text item">
                                            客户电话：<template v-if="customer.mobileNo != null && customer.mobileNo != ''"><el-tag>{{customer.mobileNo}}</el-tag></template>
                                        </div>
                                    </el-col>
                                    <el-col :span="8">
                                        <div class="text item">
                                            咨询时间：<template v-if="customer.consultTime != null && customer.consultTime != ''"><el-tag>{{customer.consultTime}}</el-tag></template>
                                        </div>
                                    </el-col>
                                </el-row >  
                                <el-row :gutter="5">
                                    <el-col :span="8">
                                        <div class="text">
                                            客户类型：<template v-if="customer.custTypeName != null && customer.custTypeName != ''"><el-tag type="danger">{{customer.custTypeName}}</el-tag></template>
                                        </div>
                                    </el-col>
                                    <el-col :span="8">
                                        <div class="text item">
                                            职业：<template v-if="customer.profession != null && customer.profession != ''"><el-tag>{{customer.profession}}</el-tag></template>
                                        </div>
                                    </el-col>
                                    <el-col :span="8">
                                        <div class="text item">
                                            学历：<template v-if="customer.educational != null && customer.educational != ''"><el-tag>{{customer.educational}}</el-tag></template>
                                        </div>
                                    </el-col>
                                </el-row >                                           
                            </el-tab-pane>
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
                            <el-tab-pane label="工作人员" name="workerInfo">
                                <el-table
                                    :data="order.orderWorkers"
                                    stripe="true"
                                    border
                                    style="width: 100%">
                                    <el-table-column
                                            label="员工姓名"
                                            prop="name"
                                            width="140">
                                    </el-table-column>
                                    <el-table-column
                                            prop="roleName"
                                            width="140"
                                            label="角色名称">
                                    </el-table-column>
                                    <el-table-column
                                            prop="statusName"
                                            label="员工状态"
                                            width="140">
                                    </el-table-column>
                                    <el-table-column
                                            prop="startDate"
                                            label="加入时间"
                                            width="200">
                                    </el-table-column>
                                    <el-table-column
                                            prop="workerStatus"
                                            label="当前状态">
                                    </el-table-column>
                                </el-table>
                            </el-tab-pane>
                            <el-tab-pane label="订单款项" name="fee">
                                <el-tabs v-model="subActiveTab" type="border-card">
                                    <el-tab-pane label="应收款项" name="feeInfo">
                                        <el-table
                                            :data="order.orderFees"
                                            stripe="true"
                                            border
                                            row-key="rowKey"
                                            :row-class-name="feeRowClassName"
                                            :tree-props="{children:'children', hasChildren: 'hasChildren'}"
                                            style="width: 100%">
                                            <el-table-column
                                                    label="应收项"
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
                                            <el-table-column
                                                    prop="needPay"
                                                    label="应付（元）"
                                                    width="140"
                                                    align="right">
                                            </el-table-column>
                                            <el-table-column
                                                    prop="pay"
                                                    label="实付（元）"
                                                    width="140"
                                                    align="right">
                                            </el-table-column>
                                            <el-table-column
                                                    prop="isEquals"
                                                    label="是否付齐"
                                                    width="140"
                                                    align="right">
                                            </el-table-column>
                                        </el-table>
                                    </el-tab-pane>
                                    <el-tab-pane label="实收款项" name="payInfo">
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
                                                                label="实收项"
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
                                                                label="付款类型"
                                                                prop="paymentTypeName"
                                                                fixed>
                                                        </el-table-column>
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
                                            <el-table-column
                                                    prop="auditStatusName"
                                                    label="审核状态" 
                                                    width="140">
                                            </el-table-column>
                                            <el-table-column
                                                    prop="auditEmployeeName"
                                                    label="审核员工" 
                                                    width="140">
                                            </el-table-column>
                                        </el-table>
                                    </el-tab-pane>
                                </el-tabs>
                            </el-tab-pane>
                            <el-tab-pane label="中间产品" name="product">
                                <div class="grid-content bg-purple-light">
                                    <!--
                                    <el-collapse value="1" accordion>
                                        <el-card class="box-card" shadow="hover">
                                            <div>
                                                <el-divider content-position="left"><span style="font-weight: 700">蓝图指导书</span>
                                                </el-divider>
                                            </div>
                                            <el-table
                                                    :data="order.ltzdsInfo"
                                                    border
                                                    stripe="true"
                                                    style="width: 100%">
                                                <el-table-column
                                                        prop="commitTime"
                                                        align="center"
                                                        label="推送时间">
                                                </el-table-column>
                                                <el-table-column
                                                        prop="commitEmployeeName"
                                                        align="center"
                                                        label="推送人">
                                                </el-table-column>
                                            </el-table>
                                        </el-card>
                                    </el-collapse>
                                    -->
                                    <el-card class="box-card" shadow="hover">
                                        <div>
                                            <el-divider content-position="left"><span style="font-weight: 700">需求蓝图一</span>
                                            </el-divider>
                                        </div>
                                        <el-table
                                                :data="order.xqltyInfo"
                                                border
                                                stripe="true"
                                                style="width: 100%">
                                            <el-table-column
                                                    prop="actionTypeName"
                                                    label="蓝图一类型"
                                                    align="center"
                                                    width="180">
                                            </el-table-column>
                                            <el-table-column
                                                    prop="style"
                                                    label="风格蓝图"
                                                    align="center"
                                                    width="200">
                                            </el-table-column>
                                            <el-table-column
                                                    prop="func"
                                                    label="功能蓝图"
                                                    align="center"
                                                    width="200">
                                            </el-table-column>
                                            <el-table-column
                                                    prop="modeTime"
                                                    align="center"
                                                    label="推送时间">
                                            </el-table-column>
                                            <el-table-column
                                                    prop="pushEmployeeName"
                                                    align="center"
                                                    label="推送人">
                                            </el-table-column>
                                        </el-table>
                                    </el-card>
                                    <el-card class="box-card" shadow="hover">
                                        <div>
                                            <el-divider content-position="left"><span style="font-weight: 700">风格蓝图二</span>
                                                <el-button type="text" @click="dialogTableVisible = true">图片详情</el-button>
                                            </el-divider>
                                        </div>
                                        <template v-if="order.xqlteInfo != null">
                                            <div class="text item" v-for="(styleInfo, index) in order.xqlteInfo.styleInfo">
                                                <h3>{{styleInfo.name}}</h3>
                                                <el-alert
                                                        :title="styleInfo.content"
                                                        type="info"
                                                        :closable="false"
                                                        show-icon>
                                                </el-alert>
                                            </div>
                                        </template>
                                    </el-card>
                                    
                                    <el-card class="box-card" shadow="hover">
                                        <div>
                                            <el-divider content-position="left"><span style="font-weight: 700">功能蓝图二</span>
                                                <el-button type="text" @click="funcDialogVisibleAll = true" style="color: green">
                                                    蓝图比对
                                                </el-button>
                                            </el-divider>
                                        </div>
                                        <el-button type="text" @click="funcDialogVisibleA = true" :disabled="disabledA"><h3>
                                            功能蓝图_A</h3></el-button>
                                        <el-button type="text" @click="funcDialogVisibleB = true" :disabled="disabledB"><h3>
                                            功能蓝图_B</h3></el-button>
                                        <el-button type="text" @click="funcDialogVisibleC = true" :disabled="disabledC"><h3>
                                            功能蓝图_C</h3></el-button>
                                    </el-card>
                                </div>
            
                                <el-dialog title="风格蓝图照片" :visible.sync="dialogTableVisible">
                                    <template v-if="order.xqlteInfo != null">
                                        <el-carousel :interval="4000" type="card" height="200px">
                                            <el-carousel-item v-for="url in order.xqlteInfo.styleUrl">
                                                <el-image
                                                        style="width: 300px; height: 300px"
                                                        :src="url"></el-image>
                                            </el-carousel-item>
                                        </el-carousel>
                                    </template>
                                </el-dialog>
                
                                <el-dialog title="功能蓝图" :visible.sync="funcDialogVisibleA" append-to-body height="300px">
                                    <div style="height: 50vh;overflow: auto;">
                                        <template v-if="order.xqlteInfo != null">
                                            <el-tree :data="order.xqlteInfo.funcInfo.FUNC_A"
                                                     :props="defaultProps"
                                                     node-key="id"
                                                     ref="funcA"
                                                     default-expand-all>
                                            </el-tree>
                                        </template>
                                    </div>
                                </el-dialog>
                
                                <el-dialog title="功能蓝图" :visible.sync="funcDialogVisibleB" append-to-body height="300px">
                                    <div style="height: 50vh;overflow: auto;">
                                        <template v-if="order.xqlteInfo != null">
                                            <el-tree :data="order.xqlteInfo.funcInfo.FUNC_B"
                                                     :props="defaultProps"
                                                     node-key="id"
                                                     ref="funcB"
                                                     default-expand-all></el-tree>
                                        </template>
                                    </div>
                                </el-dialog>
                
                                <el-dialog title="功能蓝图" :visible.sync="funcDialogVisibleC" append-to-body height="300px">
                                    <div style="height: 50vh;overflow: auto;">
                                        <template v-if="order.xqlteInfo != null">
                                            <el-tree :data="order.xqlteInfo.funcInfo.FUNC_C"
                                                     :props="defaultProps"
                                                     node-key="id"
                                                     ref="funcC"
                                                     default-expand-all></el-tree>
                                        </template>
                                    </div>
                                </el-dialog>
            
                                <el-dialog title="功能蓝图对比" :visible.sync="funcDialogVisibleAll" append-to-body width="1200px" height="300px">
                                    <el-row :gutter="12">
                                        <el-col :span="8">
                                            <div class="grid-content bg-purple">
                                                <el-card class="box-card" shadow="hover">
                                                    <div slot="header" class="clearfix">
                                                        <span>功能蓝图A</span>
                                                    </div>
                                                    <div style="height: 100vh;overflow: auto;">
                                                        <template v-if="order.xqlteInfo != null">
                                                            <el-tree :data="order.xqlteInfo.funcInfo.FUNC_A"
                                                                     :props="defaultProps"
                                                                     node-key="id"
                                                                     ref="funcA"
                                                                     default-expand-all></el-tree>
                                                        </template>
                                                    </div>
                                                </el-card>
                                            </div>
                                        </el-col>
                                        <el-col :span="8">
                                            <div class="grid-content bg-purple">
                                                <el-card class="box-card" shadow="hover">
                                                    <div slot="header" class="clearfix">
                                                        <span>功能蓝图B</span>
                                                    </div>
                                                    <div style="height: 100vh;overflow: auto;">
                                                        <template v-if="order.xqlteInfo != null">
                                                            <el-tree :data="order.xqlteInfo.funcInfo.FUNC_B"
                                                                     :props="defaultProps"
                                                                     node-key="id"
                                                                     ref="funcB"
                                                                     default-expand-all></el-tree>
                                                        </template>
                                                    </div>
                                                </el-card>
                                            </div>
                                        </el-col>
                                        <el-col :span="8">
                                            <div class="grid-content bg-purple">
                                                <el-card class="box-card" shadow="hover">
                                                    <div slot="header" class="clearfix">
                                                        <span>功能蓝图C</span>
                                                    </div>
                                                    <div style="height: 100vh;overflow: auto;">
                                                        <template v-if="order.xqlteInfo != null">
                                                            <el-tree :data="order.xqlteInfo.funcInfo.FUNC_C"
                                                                     :props="defaultProps"
                                                                     node-key="id"
                                                                     ref="funcC"
                                                                     default-expand-all
                                                            ></el-tree>
                                                        </template>
                                                    </div>
                                                </el-card>
                                            </div>
                                        </el-col>
                                    </el-row>
                
                                </el-dialog>
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
                            <el-tab-pane label="合同信息" name="contract">
                                <el-table
                                    :data="order.orderContracts"
                                    stripe="true"
                                    border
                                    style="width: 100%">
                                    <el-table-column
                                            label="合同类型"
                                            prop="contractTypeName"
                                            width="140">
                                    </el-table-column>
                                    <el-table-column
                                            prop="signDate"
                                            width="140"
                                            label="签订日期">
                                    </el-table-column>
                                    <el-table-column
                                            prop="busiLevelName"
                                            width="120"
                                            label="业务级别">
                                    </el-table-column>
                                    <el-table-column
                                            prop="environmentalTestingAgencyName"
                                            label="环保机构">
                                    </el-table-column>
                                    <el-table-column
                                            prop="chargeSecondFeeDate"
                                            width="200"
                                            label="二期款时间">
                                    </el-table-column>
                                    <el-table-column
                                            prop="startDate"
                                            label="开始时间"
                                            width="200">
                                    </el-table-column>
                                    <el-table-column
                                            prop="endDate"
                                            label="结束时间"
                                            width="200">
                                    </el-table-column>
                                    
                                </el-table>
                            </el-tab-pane>
                            <el-tab-pane label="优惠项目" name="discount">
                                <el-table
                                    :data="order.orderDiscountItems"
                                    stripe="true"
                                    border
                                    style="width: 100%">
                                    <el-table-column
                                            label="优惠项目"
                                            prop="discountItemName"
                                            width="140">
                                    </el-table-column>
                                    <el-table-column
                                            prop="employeeName"
                                            width="140"
                                            label="录入人">
                                    </el-table-column>
                                    <el-table-column
                                            prop="createTime"
                                            width="200"
                                            label="录入时间">
                                    </el-table-column>
                                    <el-table-column
                                            prop="contractDiscountFee"
                                            width="200"
                                            label="合同优惠金额">
                                    </el-table-column>
                                    <el-table-column
                                            prop="结算优惠金额"
                                            width="160"
                                            label="结算优惠金额">
                                    </el-table-column>
                                    <el-table-column
                                            prop="approveEmployeeName"
                                            label="优惠审批人"
                                            width="140">
                                    </el-table-column>
                                    <el-table-column
                                            prop="statusName"
                                            label="状态"
                                            width="140">
                                    </el-table-column>
                                    <el-table-column
                                            prop="remark"
                                            label="备注"
                                            width="200">
                                    </el-table-column>
                                    
                                </el-table>
                            </el-tab-pane>
                        </el-tabs>
                    </el-col>
                </el-row>
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
                        that.customer = data.customer;

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
                        if (data.xqlteInfo != null) {
                            if(data.xqlteInfo.funcInfo != null && data.xqlteInfo.funcInfo.FUNC_A != null){
                                that.disabledA = false;
                            }

                            if(data.xqlteInfo.funcInfo != null && data.xqlteInfo.funcInfo.FUNC_B != null){
                                that.disabledB = false;
                            }

                            if(data.xqlteInfo.funcInfo != null && data.xqlteInfo.funcInfo.FUNC_C != null){
                                that.disabledC = false;
                            }
                        }

                    });
                }
            },

            feeRowClassName : function({row, rowIndex}) {
                if (row.isEquals == "未付齐") {
                    return 'warning-row';
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

});
