define(['vue','ELEMENT','ajax'], function(Vue,element,ajax){
    Vue.component('order-worker', {
        props: ['order-id'],

        data : function(){
            return {
                sOrderId: this.orderId,
                workers: []
            }
        },

        template: `
            <el-card class="box-card" shadow="hover">
                <div slot="header" class="clearfix">
                    <span>订单主流程工作人员</span>
                </div>
                <el-row :gutter="12">
                    <el-col :span="6">
                        <div style="padding:50px">

                        </div>
                    </el-col>
                    <el-col :span="18">
                        <el-steps direction="vertical" :space="80" align-center>
                            <el-step v-for="worker in workers" :title="worker.name" :status="worker.status" :description="worker.roleName" icon="el-icon-s-custom"></el-step>
                        </el-steps>
                    </el-col>
                </el-row>
            </el-card>
            `,

        methods: {
            init() {
                let that = this;
                ajax.get('api/bss.order/order-base/getOrderWorkers', {orderId:this.sOrderId}, function(data) {
                    that.workers = data;
                })
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