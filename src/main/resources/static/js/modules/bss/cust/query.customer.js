require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util','order-selectemployee','vue-router'], function(Vue, element, axios, ajax, vueselect, util,orderSelectEmployee,vueRouter) {
    const Foo = { template: '<div>foo</div>' };
    const Bar = { template: '<div>bar</div>' };
    const routes = [
        { path: '/foo', component: Foo },
        { path: '/bar', component: Bar }
    ];
    let vm = new Vue({
        el: '#app',
        router:new vueRouter({routes}),
        data: function() {
            return {
                custQueryCond: {
                    name: '',
                    designEmployeeId:'',
                    counselorEmployeeId:'',
                    reportEmployeeId:'',
                    informationSource:'',
                    customerStatus:'',
                    customerType:'',
                    reportEmployeeId:'',
                    timeType:'',
                    startTime:'',
                    endTime:'',
                    houseMode:''
                },
                custId:'',
                customerInfo: [],
                checked: null,
                display:'display:block',

                id: util.getRequest('id'),

            }
        },

        methods: {
            onSubmit: function() {
                ajax.get('api/organization/employee/searchEmployee', {searchText:'金'}, function(responseData){
                    vm.customerInfo = responseData;
                });
            },

            handleClick(row) {
                console.log(row);
            },

            getTemplateRow(index,row){                                 //获取选中数据
                this.templateSelection = row;
                this.custId=row.employeeId;
            },

            customerVisit(){
                alert(this.custId);
                const that=this;
                that.$router.push({ path: '/a' }).catch(err => {err});
            }

        }
    });

    return vm;
})