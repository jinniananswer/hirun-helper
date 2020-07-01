define(['vue','ELEMENT','ajax'], function(Vue,element,ajax){
    Vue.component('vue-select', {
        props: ['code-type', 'value', 'disabled', 'disable-value'],

        data : function(){
            return {
                options: [],
                sValue: this.value
            }
        },

        template: `
            <el-select v-model="sValue" placeholder="请选择" clearable filterable :disabled="this.disabled===true?true:false" style="width:100%" @change="handle">
                <el-option
                    v-for="item in options"
                    :key="item.codeValue"
                    :label="item.codeName"
                    :value="item.codeValue"
                    :disabled="item.codeValue==this.disableValue">
                </el-option>
            </el-select>
            `,

        methods: {
            init() {
                let that = this;
                ajax.get('api/system/static-data/getByCodeType?codeType='+this.codeType, null, function(data) {
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