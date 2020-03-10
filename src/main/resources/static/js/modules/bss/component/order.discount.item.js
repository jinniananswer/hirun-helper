define(['vue', 'ELEMENT', 'ajax','vxe-table','axios'], function (Vue, element, ajax,table,axios) {
    Vue.use(table);
    Vue.component('order-discount-item', {
        props: ['order-id'],

        data: function () {
            return {
                discountItemDetail:false,
                discountItemDetailList:[],
                approveEmployeeList : []
            }
        },

        template: `
            <div>
                <el-button type="primary" @click="discountItemDetail = true" style="width: 120px" >优惠项目录入</el-button>
                <el-dialog title="优惠明细" :visible.sync="discountItemDetail" @open="openDiscountItemDetailDialog" fullscreen="true">
                    <vxe-table
                    :data="discountItemDetailList"
                    size="small"
                    keep-source
                    stripe
                    highlight-current-row
                    highlight-hover-row
                    border
                    resizable
                    show-overflow
                    height="340"
                    :edit-config="{trigger: 'click', mode: 'cell'}"
                    show-footer
                    ref="discountItemTable">
                        <vxe-table-column align="center" field="employeeName" title="录入人"></vxe-table-column>
                        <vxe-table-column align="center" field="createTime" title="录入时间"></vxe-table-column>
                        <vxe-table-column align="center" field="discountItemName" title="优惠项目"></vxe-table-column>
                        <vxe-table-column align="center" field="contractDiscountFee" title="合同优惠金额" :edit-render="{name: 'input', attrs: {type: 'text'}}"></vxe-table-column>
                        <vxe-table-column align="center" field="settleDiscountFee" title="结算优惠金额" :edit-render="{name: 'input', attrs: {type: 'text'}}"></vxe-table-column>
                        <vxe-table-column align="center" field="approveEmployeeId" title="优惠审批人" :edit-render="{name: 'select', options: approveEmployeeList, events: {change: selectRowChangeEvent}}"></vxe-table-column>
                        <vxe-table-column align="center" field="approveDate" title="审批时间"></vxe-table-column>
                        <vxe-table-column align="center" field="statusName" title="状态"></vxe-table-column>
                        <vxe-table-column align="center" field="remark" title="备注" :edit-render="{name: 'input', attrs: {type: 'text'}}"></vxe-table-column>
                    </vxe-table>
                    <el-button type="primary" @click="saveDiscountItemList">保存</el-button>
            </el-dialog>
            </div>
            `,

        methods: {

            selectRowChangeEvent ({ row }, evnt) {
                debugger;
            },

            openDiscountItemDetailDialog : function() {
                let data = {
                    orderId : this.orderId
                }
                ajax.get('api/bss.order/order-discount-item/list', data, (responseData)=>{
                    for(let i = 0; i < responseData.length; i++) {
                        responseData[i].contractDiscountFee = responseData[i].contractDiscountFee / 100;
                        responseData[i].settleDiscountFee = responseData[i].settleDiscountFee / 100;
                    }
                    this.discountItemDetailList = responseData;
                });
            },

            saveDiscountItemList : function () {
                let updateRecords = this.$refs.discountItemTable.getUpdateRecords()
                console.log("updateRecords: " + updateRecords);

                axios({
                    method: 'post',
                    url: 'api/bss.order/order-discount-item/save',
                    data: updateRecords
                }).then(function (responseData) {
                    if (0 == responseData.data.code) {
                        Vue.prototype.$confirm('点击确定按钮刷新本页面，点击关闭按钮关闭本界面', '操作成功', {
                            confirmButtonText: '确定',
                            type: 'success',
                            center: true
                        }).then(() => {
                            document.location.reload();
                        }).catch(() => {
                            top.layui.admin.closeThisTabs();
                        });

                    }
                }).catch(function (error) {
                    console.log(error);
                    Vue.prototype.$message({
                        message: '保存失败！',
                        type: 'warning'
                    });
                });
            },


        },

        watch: {

        },

        mounted: function() {

            // let arr = this.approveEmployeeList;
            ajax.get('api/bss.order/order-base/selectRoleEmployee', {roleId:19,isSelf:false}, (responseData)=>{
                let arr = [];
                for(let i = 0; i < responseData.length; i++) {
                    arr.push({
                        label: responseData[i].name,
                        value: responseData[i].employeeId
                    });
                }
                this.approveEmployeeList = arr;
            })
        },
    });


})