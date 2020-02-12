require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util', 'cust-info', 'order-info', 'order-worker', 'order-selectemployee', 'cust-visit'], function (Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee, custVisit) {
    let vm = new Vue({
            el: '#app',
            data: function () {
                return {
                    FeeDetails: [],
                    custQueryCond: {
                        name: '',
                        sex: '',
                        wechatNicName: '',
                        mobileNo: ''
                    },

                    collectedFeeTable: {
                        receiptNumber: '',
                        collectionDate: new Date(),
                        payee: '',
                        designEmployeeId: '',
                        collectedFee: '',
                        cash: '',
                        industrialBankCard: '',
                        pudongDevelopmentBankCard: '',
                        constructionBankBasic: '',
                        constructionBank3797: '',
                        iCBC3301: '',
                        iCBCInstallment: '',
                        aBCInstallment: '',
                        summary: '',
                    },
                    feeTableData: [],
                    defaultStandard: '1',
                    custOrder: [],
                    display: 'display:block',
                    multipleSelection: [],
                    designEmployeeId: '',
                    id: util.getRequest('id'),
                    customerDefault: 'base',
                    progress: [-10, 70],
                    activeTab: 'orderInfo',
                    capitalizationAmount: '',

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
            // mounted(){
            //     this.$nextTick(()=>{
            //         this.init();
            //     })
            // },
            methods: {
                loadDesignFeeDetail: function () {
                    axios.get('api/system/fee-item-cfg/getFeeItemDatas?codeType=1').then(function (responseData) {
                        vm.FeeDetails = responseData.data.rows;
                    }).catch(function (error) {
                        console.log(error);
                    });
                },
                checkMoney: function () {
                    var money = this.collectedFeeTable.collectedFee;
                    if (money.indexOf('.') != -1) {
                        alert("请输入正确的金额,单位为元");
                    }
                    var reg = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/;
                    if (reg.test(money)) {
                        this.capitalizationAmount = this.MoneyToChinese(money);
                    } else {
                        alert("请输入正确的金额");
                    }
                },
                MoneyToChinese: function (money) {
                    //汉字的数字
                    var cnNums = new Array('零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖');
                    //基本单位
                    var cnIntRadice = new Array('', '拾', '佰', '仟');
                    //对应整数部分扩展单位
                    var cnIntUnits = new Array('', '万', '亿', '兆');
                    //对应小数部分单位
                    var cnDecUnits = new Array('角', '分', '毫', '厘');
                    //整数金额时后面跟的字符
                    var cnInteger = '整';
                    //整型完以后的单位
                    var cnIntLast = '元';
                    //最大处理的数字
                    var maxNum = 999999999999999.9999;
                    //金额整数部分
                    var integerNum;
                    //金额小数部分
                    var decimalNum;
                    //输出的中文金额字符串
                    var chineseStr = '';
                    //分离金额后用的数组，预定义
                    var parts;
                    if (money == '') {
                        return '';
                    }
                    money = parseFloat(money);
                    if (money >= maxNum) {
                        //超出最大处理数字
                        return '';
                    }
                    if (money == 0) {
                        chineseStr = cnNums[0] + cnIntLast + cnInteger;
                        return chineseStr;
                    }
                    //转换为字符串
                    money = money.toString();
                    if (money.indexOf('.') == -1) {
                        integerNum = money;
                        decimalNum = '';
                    } else {
                        parts = money.split('.');
                        integerNum = parts[0];
                        decimalNum = parts[1].substr(0, 4);
                    }
                    //获取整型部分转换
                    if (parseInt(integerNum, 10) > 0) {
                        var zeroCount = 0;
                        var IntLen = integerNum.length;
                        for (var i = 0; i < IntLen; i++) {
                            var n = integerNum.substr(i, 1);
                            var p = IntLen - i - 1;
                            var q = p / 4;
                            var m = p % 4;
                            if (n == '0') {
                                zeroCount++;
                            } else {
                                if (zeroCount > 0) {
                                    chineseStr += cnNums[0];
                                }
                                //归零
                                zeroCount = 0;
                                chineseStr += cnNums[parseInt(n)] + cnIntRadice[m];
                            }
                            if (m == 0 && zeroCount < 4) {
                                chineseStr += cnIntUnits[q];
                            }
                        }
                        chineseStr += cnIntLast;
                    }
                    //小数部分
                    if (decimalNum != '') {
                        var decLen = decimalNum.length;
                        for (var i = 0; i < decLen; i++) {
                            var n = decimalNum.substr(i, 1);
                            if (n != '0') {
                                chineseStr += cnNums[Number(n)] + cnDecUnits[i];
                            }
                        }
                    }
                    if (chineseStr == '') {
                        chineseStr += cnNums[0] + cnIntLast + cnInteger;
                    } else if (decimalNum == '') {
                        chineseStr += cnInteger;
                    }
                    return chineseStr;
                },

                //处理表格多选表头初始化方法
                // init(){
                //     document.querySelector(".el-checkbox__inner").style.display="none";
                //     document.querySelector(".cell").innerHTML = '是否交齐';
                //
                // },
                designFeeSubmit(collectedFeeTable) {
                    this.$refs.collectedFeeTable.validate((valid) => {
                        if (valid) {
                            axios({
                                method: 'post',
                                url: 'api/bss/order/order-fee/addDesignFee',
                                data: this.collectedFeeTable
                            }).then(function (responseData) {
                                if (0 == responseData.data.code) {
                                    Vue.prototype.$message({
                                        message: '缴费成功！',
                                        type: 'success'
                                    });
                                }
                            });
                        }
                    })
                },
                downPaymentSubmit(collectedFeeTable) {
                     this.$refs.collectedFeeTable.validate((valid) => {
                        if (valid) {
                            axios({
                                method: 'post',
                                url: 'api/bss/order/order-fee/addDownPayment',
                                data: this.collectedFeeTable
                            }).then(function (responseData) {
                                if (0 == responseData.data.code) {
                                    Vue.prototype.$message({
                                        message: '缴费成功！',
                                        type: 'success'
                                    });
                                }
                            });
                        }
                    })
                },
                getEmployee: function () {
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

                handleSelectionChange(val) {
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
    vm.loadDesignFeeDetail();
    return vm;
})