define(['vue', 'ELEMENT', 'ajax'], function (Vue, element, ajax) {
    Vue.component('cust-visit', {
        props: ['cust-id'],

        data: function () {
            return {
                customerVisit: []
            }
        },

        template: `
            <el-card class="box-card">
                <div>
                    <el-divider content-position="left"><span style="font-weight: 700">客户回访信息</span></el-divider>
                </div>
                <template>
                    <el-table
                            :data="customerVisit"
                            border="true"
                            style="width: 100%">
                        <el-table-column
                                prop="visitTypeName"
                                label="回访类型"
                                fixed
                                align="center"
                                width="150"
                                >
                        </el-table-column>
                        <el-table-column
                                prop="visitObject"
                                label="回访对象"
                                align="center"
                                width="150">
                        </el-table-column>
                        <el-table-column
                                prop="visitEmployeeName"
                                label="回访人"
                                align="center"
                                width="120">
                        </el-table-column>
                        <el-table-column
                                prop="visitTime"
                                align="center"
                                label="回访时间"
                                width="180">
                        </el-table-column>
                        <el-table-column
                                prop="visitWay"
                                align="center"
                                label="回访方式">
                        </el-table-column>
                        <el-table-column
                                prop="visitContent"
                                align="center"
                                label="回访内容"
                                width="400">
                        </el-table-column>
                    </el-table>
                </template>
            </el-card>
            `,

        methods: {
            init() {
                let that = this;
                ajax.get('api/customer/party-visit/queryCustVisit', {custId: this.custId}, function (data) {
                    that.customerVisit = data;
                })
            }
        },

        watch: {},

        mounted() {
            this.init();
        }
    });


})