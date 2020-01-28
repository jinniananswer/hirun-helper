require(['vue','ELEMENT', 'axios'], function(Vue, element, axios) {
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

                display:'display:block'
            }
        },

        methods: {
            onSubmit: function() {
                axios.get('api/organization/employee/searchEmployee?searchText=金').then(function(responseData){
                    vm.custOrder = responseData.data.rows;
                }).catch(function(error){
                    console.log(error);
                });
            }
        }
    });

    return vm;
})