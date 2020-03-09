define(['vue','ELEMENT','ajax'], function(Vue,element,ajax){
    Vue.component('order-selectdecorator', {
        props: ['type', 'value', 'disabled'],

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
                    :key="item.decoratorId"
                    :label="item.name"
                    :value="item.decoratorId">
                </el-option>
            </el-select>
            `,

        methods: {
            init() {
                let that = this;
                ajax.get('api/bss.order/decorator/selectTypeDecorator', {type:this.type}, function(data) {
                    that.options = data;
                    // if(that.self){
                    //     that.sValue=data[0].decoratorId;
                    // }
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