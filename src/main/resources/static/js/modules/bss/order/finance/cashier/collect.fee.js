require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util'], function(Vue, element, axios, ajax, vueselect, util) {
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                custQueryCond: {
                    name: '',
                    sex: '',
                    wechatNicName: '',
                    mobileNo: ''
                },
                feeTableData: [],
                custOrder: [],
                display:'display:block',
                multipleSelection: [],
                defaultSex: '1',

                id: util.getRequest('id'),
                customerDefault: 'base',

                progress: [-10,70],

                activeTab:'orderInfo',



                marks: {
                    0: '酝酿',
                    10: '初选',
                    30: '初步决策',
                    50: '决策',
                    60: '施工',
                    95: '维护'
                },

                requirement : {
                    title : '客户需求信息',
                    style : '白色简约',
                    func : '功能列表'
                },

                workers: [
                    {
                        job: '家装顾问',
                        name: '左金虎'
                    },
                    {
                        job: '客户代表',
                        name: '张晓梅'
                    },
                    {
                        job: '设计师',
                        name: '罗厚石'
                    },
                    {
                        job: '项目经理',
                        name: '彭帅'
                    },
                    {
                        job: '工程助理',
                        name: '彭帅'
                    },
                    {
                        job: '工程文员',
                        name: '彭帅'
                    },
                    {
                        job: '出纳',
                        name: '彭帅'
                    },
                    {
                        job: '财务',
                        name: '彭帅'
                    },
                    {
                        job: '售后人员',
                        name: '彭帅'
                    }
                ],

                avatarUrl: 'static/img/male.jpg'
            }
        },
        mounted(){
            this.$nextTick(()=>{
                this.init();
            })
        },
        methods: {
            onSubmit: function() {
                alert(this.id);
                ajax.get('api/organization/employee/searchEmployee?searchText=金', null, function(responseData){
                    vm.custOrder = responseData;
                });
            },
            init(){
                document.querySelector(".el-checkbox__inner").style.display="none";
                document.querySelector(".cell").innerHTML = '是否交齐'
            },
            getEmployee : function() {
                axios.get('api/organization/employee/loadEmployeeArchive?employeeId=1').then(function(responseData){
                    vm.customer = responseData.data.rows;
                }).catch(function(error){
                    console.log(error);
                });
            },
            handleFeeTableDelete: function (index) {
                vm.employeeTableData.splice(index, 1);
            },
            addRow: function(feeTableData,event){//新增一行
                //之前一直想不到怎么新增一行空数据，最后幸亏一位朋友提示：表格新增一行，其实就是源数据的新增，从源数据入手就可以实现了，于是 恍然大悟啊！
                feeTableData.push({ feeCategory: '1', periods: '',designFeeDetails:'3',ShopName:'邵阳店', fee: '',collectDate:new Date(),designer:'设计师测试1',designerLevel:'初级审计师',cabinetDesigner:'橱柜设计师测试1', accountManager: '客户代表测试1',projectManager:'项目经理测试1',remarks:'' })
            },
            changeSex: function(newVal) {
                alert(newVal);
                alert(this.defaultSex);
            },
            changeStandard: function(newVal) {
                alert(newVal);
                alert(this.defaultSex);
            },
            changeFeeCategory: function(newVal) {
                alert(newVal);
            },
            changeDesignFeeDetails: function(newVal) {
                alert(newVal);
            },
            handleFeeTableEdit(index,row ) {
                row.show = row.show ? false : true
                Vue.set(this.feeTableData, index, row)
                //this.$set(this.tabledatas, index, row)
                // 其他操作
                console.log(JSON.stringify(this.feeTableData))
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            handleFeeTableDelete: function (index) {
                vm.feeTableData.splice(index, 1);
                console.log(JSON.stringify(this.feeTableData))
            }
        }
    });

    return vm;
})