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

                defaultSex: '2',

                id: util.getRequestParam('id')
            }
        },

        methods: {
            onSubmit: function() {
                alert(this.id);
                ajax.get('api/organization/employee/searchEmployee?searchText=金', null, function(responseData){
                    vm.custOrder = responseData;
                });
            },

            changeSex: function(newVal) {
                alert(newVal);
                alert(this.defaultSex);
            }
        }
    });

    return vm;
})