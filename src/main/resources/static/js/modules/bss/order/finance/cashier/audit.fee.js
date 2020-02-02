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

                custOrder: [],

                display:'display:block',
                feeTableData: [{ receiptNumber:'1',feeCategory: '1', periods: '',designFeeDetails:'3',ShopName:'邵阳店', fee: '1',collectDate:new Date(),designer:'设计师测试1',designerLevel:'初级审计师', accountManager: '客户代表测试1' }],
                defaultSex: '2',
                defaultStandard: '1',

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

        methods: {
            onSubmit: function() {
                alert(this.id);
                ajax.get('api/organization/employee/searchEmployee?searchText=金', null, function(responseData){
                    vm.custOrder = responseData;
                });
            },
            getEmployee : function() {
                axios.get('api/organization/employee/loadEmployeeArchive?employeeId=1').then(function(responseData){
                    vm.customer = responseData.data.rows;
                }).catch(function(error){
                    console.log(error);
                });
            },
            changeStandard: function(newVal) {
                alert(newVal);
                alert(this.defaultStandard);
            },
            changeSex: function(newVal) {
                alert(newVal);
                alert(this.defaultSex);
            }
        }
    });

    return vm;
})