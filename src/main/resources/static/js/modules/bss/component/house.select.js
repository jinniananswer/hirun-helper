define(['vue','ELEMENT','ajax'], function(Vue,element,ajax){
    Vue.component('house-select', {
        props: ['value', 'disabled'],

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
                    :key="item.housesId"
                    :label="item.name"
                    :value="item.housesId">
                    <span style="float: left">{{ item.name }}</span>
                    <span style="float: right; color: #8492a6; font-size: 13px">{{ item.remark }}</span>
                </el-option>
            </el-select>
            `,

        methods: {
            init() {
                let that = this;
                ajax.get('api/house/houses/queryHouse','', function(data) {
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