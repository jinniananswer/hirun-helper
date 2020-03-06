require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util', 'cust-info', 'order-info', 'order-worker', 'order-selectemployee', 'cust-visit'], function (Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee, custVisit) {
    let vm = new Vue({
            el: '#app',
            data: function () {
                return {
                    FeeDetails: [],
                    payItems: [],
                    payments: [],

                    display: 'display:block',
                    multipleSelection: [],
                    designEmployeeId: '',
                    id: util.getRequest('id'),
                    customerDefault: 'base',
                    progress: [-10, 70],
                    parentFeeItemId: '设计费',
                    activeTab: 'orderInfo',


                    requirement: {
                        title: '客户需求信息',
                        style: '白色简约',
                        func: '功能列表'
                    },
                    rules: {
                        feeItemId: [
                            {required: true, message: '请选择对应小类', trigger: 'blur'},
                        ],
                    },

                    avatarUrl: 'static/img/male.jpg'
                }
            },
            mounted: function() {
                let data = {
                    orderId : 7//测试用
                }
                ajax.post('api/bss/order/order-fee/loadDesignFeeInfo',data, (responseData)=>{
                    Object.assign(this.collectedDesignFee, responseData);
                });
            },
            methods: {
                checkMoney: function () {
                    var money=this.collectedDesignFee.collectedFee;
                    var oldmoney=this.collectedDesignFee.oldCollectedFee;
                    if (money.indexOf('.') != -1)
                    {
                        alert("请输入正确的金额,单位为元");
                        this.collectedDesignFee.collectedFee=oldmoney;
                    }
                    var reg = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/;
                    if (!reg.test(money)) {
                        alert("请输入正确的金额");
                    }
                },

                //处理表格多选表头初始化方法
                // init(){
                //     document.querySelector(".el-checkbox__inner").style.display="none";
                //     document.querySelector(".cell").innerHTML = '是否交齐';
                //
                // },
                auditSubmit(collectedDesignFee)
                {
                    this.$refs.collectedDesignFee.validate((valid) => {
                        if (valid) {
                            axios({
                                method: 'post',
                                url: 'api/bss/order/order-fee/addOrderFee',
                                data: this.collectedDesignFee
                            }).then(function (responseData) {
                                if (0 == responseData.data.code) {
                                    Vue.prototype.$message({
                                        message: '审核成功！',
                                        type: 'success'
                                    });
                                }
                            });
                        }
                    })
                },
                auditUpdate(collectedDesignFee)
                {
                    let url = 'api/bss/order/order-fee/auditUpdate';
                   ajax.post(url, this.collectedDesignFee);
                    // this.$refs.collectedDesignFee.validate((valid) => {
                    //     if (valid) {
                    //         axios({
                    //             method: 'post',
                    //             url: 'api/bss/order/order-fee/auditUpdate',
                    //             data: this.collectedDesignFee
                    //         }).then(function (responseData) {
                    //             if (0 == responseData.data.code) {
                    //                 Vue.prototype.$message({
                    //                     message: '修改并审核成功！',
                    //                     type: 'success'
                    //                 });
                    //             }
                    //         });
                    //     }
                    // })
                },
                submit(collectedDesignFee)
                {
                    this.$refs.collectedDesignFee.validate((valid) => {
                        if (valid) {
                            axios({
                                method: 'post',
                                url: 'api/bss/order/order-fee/addOrderFee',
                                data: this.collectedDesignFee
                            }).then(function (responseData) {
                                if (0 == responseData.data.code) {
                                    Vue.prototype.$message({
                                        message: '审核不通过！',
                                        type: 'success'
                                    });
                                }
                            });
                        }
                    })
                },
                getEmployee : function () {
                    axios.get('api/organization/employee/loadEmployeeArchive?employeeId=1').then(function (responseData) {
                        vm.customer = responseData.data.rows;
                    }).catch(function (error) {
                        console.log(error);
                    });
                }
                ,
                handleFeeTableDelete: function (index) {
                    vm.employeeTableData.splice(index, 1);
                }
                ,
                // addRow: function(feeTableData,event){//新增一行
                //     //之前一直想不到怎么新增一行空数据，最后幸亏一位朋友提示：表格新增一行，其实就是源数据的新增，从源数据入手就可以实现了，于是 恍然大悟啊！
                //     feeTableData.push({ feeCategory: '1', periods: '',designFeeDetails:'3',ShopName:'邵阳店', fee: '',collectDate:new Date(),designer:'设计师测试1',designerLevel:'初级审计师',cabinetDesigner:'橱柜设计师测试1', accountManager: '客户代表测试1',projectManager:'项目经理测试1',remarks:'' })
                // },

                handleSelectionChange(val)
                {
                    this.multipleSelection = val;
                }
                ,
                handleFeeTableDelete: function (index) {
                    vm.feeTableData.splice(index, 1);
                    console.log(JSON.stringify(this.feeTableData))
                }
            }
        })
    ;
    return vm;
})