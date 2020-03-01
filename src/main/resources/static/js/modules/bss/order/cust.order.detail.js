require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util','cust-info', 'order-info', 'order-worker', 'order-selectemployee','cust-visit','order-search-employee', 'order-payment'], function(Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee,custVisit,orderSearchEmployee,payment) {
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                customer: {
                    title : '客户基本信息',
                    name : '刘慧',
                    birthday : '1989-01-01',
                    mobileNo : '18573140029',

                },

                designEmployeeId : 7,
                orderId: util.getRequest('orderId'),
                custId: util.getRequest('custId'),
                sex: '2',

                sexDisable: false,

                customerDefault: 'base',

                progress: [-10,70],

                activeTab:'orderInfo',

                eid:null,
                employeeName:'',



                marks: {
                    0: '酝酿',
                    10: '初选',
                    30: '初步决策',
                    50: '决策',
                    60: '施工',
                    95: '维护'
                },

                datas:[],

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

                avatarUrl: 'static/img/male.jpg',

                pays: []
            }
        },

        methods: {
            submit : async function() {
                // let errMap = this.$refs.paymethods.$refs.xTable.validate().catch(errMap => errMap);
                // let errMap2 = this.$refs.paymethods2.$refs.xTable.validate().catch(errMap2 => errMap2);
                let isValid = await this.$refs.paymethods.valid().then(isValid=>isValid);
                console.log(isValid);
                // alert(JSON.stringify(this.pays));
                // alert(JSON.stringify(this.$refs.paymethods2.payments));
            }
        }
    });

    return vm;
})