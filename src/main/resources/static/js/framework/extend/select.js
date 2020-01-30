define(['vue','ELEMENT','ajax'], function(Vue,element,ajax){
    Vue.component('vue-select', {
        props: ['code-type', 'value'],

        model: {
            prop: 'value',
            event: 'change'
        },

        data : function(){
            return {
                options: [],
                value: ''
            }
        },

        template: '<el-select v-bind:value="value" v-on:change="$emit(\'change\', $event.target.checked)" placeholder="请选择" style="width:100%">' +
            '<el-option' +
            '      v-for="item in options"' +
            '      :key="item.codeValue"' +
            '      :label="item.codeName"' +
            '      :value="item.codeValue">' +
            '</el-option>' +
            '</el-select>',

        methods: {
            init() {
                let that = this;
                ajax.get('api/system/static-data/getByCodeType?codeType='+this.codeType, null, function(data) {
                    that.options = data;
                })
            }
        },

        mounted () {
            this.init();
        }
    });


})