require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util', 'cust-info', 'order-info', 'order-worker', 'order-selectemployee', 'cust-visit'], function (Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee, custVisit) {
    let vm = new Vue({
        el: '#app',
        data: function () {
            return {
                workerSalary: {
                    hydropowerSalary: '',
                    hydropowerRemark: '',
                    woodworkerSalary: '',
                    woodworkerRemark:'',
                    tilerSalary: '',
                    tilerRemark: '',
                    painterSalary:'',
                    painterRemark:'',
                    wallworkerSalary:'',
                    wallworkerRemark:'',
                    orderId:'',
                    id:'',
                    custId:'',
                },
                progress: [-10, 70],
                activeTab: 'orderInfo',
                orderId:util.getRequest("orderId"),
                custId:util.getRequest("custId"),

                rules: {
                    hydropowerSalary: [
                        {required: true, message: '请填写水电工人工资', trigger: 'blue'}
                    ],
                    woodworkerSalary: [
                        {required: true, message: '请填写木工工人工资', trigger: 'change'}
                    ],
                    tilerSalary: [
                        {required: true, message: '请填写泥工工人工资', trigger: 'change'}
                    ],
                    painterSalary: [
                        {required: true, message: '请填写油漆工人工资', trigger: 'change'}
                    ],
                    wallworkerSalary: [
                        {required: true, message: '请填写敲墙工人工资', trigger: 'change'}
                    ],
                },

                marks: {
                    0: '酝酿',
                    10: '初选',
                    30: '初步决策',
                    50: '决策',
                    60: '施工',
                    95: '维护'
                },

                requirement: {
                    title: '客户需求信息',
                    style: '白色简约',
                    func: '功能列表'
                },

                avatarUrl: 'static/img/male.jpg'
            }
        },

        methods: {
            init:function(){
                let that = this;
                that.workerSalary.orderId=that.orderId;
                ajax.get('api/bss.order/order-worker-salary/queryWorkerSalary', {orderId:this.workerSalary.orderId}, function(data) {
                    Object.assign(that.workerSalary, data);
                })
            },

            save() {
                this.$refs.workerSalary.validate((valid) => {
                    if (valid) {
                        ajax.post('api/bss.order/order-worker-salary/saveWorkerSalary', this.workerSalary,null,null,true);
                    }
                })
            },

            closeWorkerSalary() {
                this.$refs.workerSalary.validate((valid) => {
                    if (valid) {
                        this.$confirm('执行操作【】, 是否继续?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            ajax.post('api/bss.order/order-worker-salary/closeWorkerSalary', this.workerSalary);
                        })
                    }
                })
            },

        },

        mounted () {
            this.init();
        }
    });

    return vm;
})