define(['vue','ELEMENT','ajax'], function(Vue,element,ajax){
    Vue.component('org-selectemployee', {
        props: ['role-id', 'value', 'disabled', 'mode', 'multi'],

        data : function(){
            return {
                options: [],
                sValue: this.value
            }
        },

        template: `
            <el-select v-model="sValue" :multiple="this.multi==true?true:false" clearable filterable :disabled="this.disabled===true?true:false" placeholder="请选择" style="width:100%" @change="handle">
                <el-option
                    v-for="item in options"
                    :key="item.employeeId"
                    :label="item.name"
                    :value="item.employeeId">
                    <span style="float: left">{{ item.name }}</span>
                    <span style="float: right; color: #8492a6; font-size: 13px">{{ item.jobRoleName }}</span>
                </el-option>
            </el-select>
            `,

        methods: {
            init() {
                let that = this;
                if (this.mode == null) {
                    this.mode = 'priv';
                }
                ajax.get('api/organization/employee/queryEmployeeBySelectMode', {roleId:this.roleId,mode:this.mode}, function(data) {
                    that.options = data;
                    if(that.self){
                        that.sValue=data[0].employeeId;
                    }
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