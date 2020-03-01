define(['vue','ELEMENT','ajax'], function(Vue,element,ajax){
    Vue.component('cust-info', {
        props: ['cust-id', 'order-id'],

        data : function(){
            return {
                sCustId: this.custId,
                sOrderId: this.orderId,
                customer:{}
            }
        },

        template: `
            <el-card class="box-card" shadow="hover">
                <div slot="header" class="clearfix">
                    <span>客户信息</span>
                </div>
                <el-row :gutter="12">
                    <el-col :span="12">
                        <el-avatar :size="60" :src="customer.headUrl"></el-avatar>
                    </el-col>
                    <el-col :span="12">
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
                <el-collapse accordion>
                    <el-collapse-item title="更多信息" name="1">
                        <div class="item">年龄：<template v-if="customer.age != null && customer.age != ''"><el-tag>{{customer.age}}</el-tag></template><template v-else>未录入</template></div>
                        <div class="item">学历：<template v-if="customer.educational != null && customer.educational != ''"><el-tag>{{customer.educational}}</el-tag></template><template v-else>未录入</template></div>
                        <div class="item">家人数量：<template v-if="customer.familyMembersCount != null && customer.familyMembersCount != ''"><el-tag>{{customer.familyMembersCount}}</el-tag></template><template v-else>未录入</template></div>
                        <div>客户兴趣爱好：<template v-if="customer.hobby != null && customer.hobby != ''"><el-tag type="warning">{{customer.hobby}}</el-tag></template><template v-else>未录入</template></div>
                    </el-collapse-item>
                </el-collapse>
            </el-card>
            `,

        methods: {
            init() {
                let that = this;
                ajax.get('api/customer/cust-base/getCustInfo', {custId:this.sCustId, orderId:this.sOrderId}, function(data) {
                    that.customer = data;
                })
            }
        },

        watch: {
            custId(val) {
                this.sCustId = val;
            },

            sCustId(val, oldValue) {
                if (val != oldValue) {
                    this.init();
                }
            },

            orderId(val) {
                this.sOrderId = val;
            },

            sCustId(val, oldValue) {
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