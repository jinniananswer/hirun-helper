define(['vue','ELEMENT','ajax'], function(Vue,element,ajax){
    Vue.component('order-selectemployee', {
        props: ['role-id', 'value', 'disabled'],

        data : function(){
            return {
                options: [],
                sValue: this.value
            }
        },

        template: `
            <el-select v-model="sValue" filterable :disabled="this.disabled===true?true:false" placeholder="请选择" style="width:100%" @change="handle">
                <el-option
                    v-for="item in options"
                    :key="item.employeeId"
                    :label="item.name"
                    :value="item.employeeId">
                </el-option>
            </el-select>
            `,

        methods: {
            init() {
                let that = this;
                ajax.get('api/bss.order/order-base/selectRoleEmployee', {roleId:this.roleId}, function(data) {
                    that.options = data;
                })
            },

            handle() {
                this.$emit( 'change', this.sValue);
            }
        },

        watch: {
            value(val) {
                this.sValue = val;
            },

            sValue(val, oldValue) {
                if (val != oldValue) {
                    this.$emit("input", val);
                }
            }
        },

        mounted () {
            this.init();
        }
    });


})