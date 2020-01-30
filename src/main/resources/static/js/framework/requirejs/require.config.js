require.config({
    map: { //map告诉RequireJS在任何模块之前，都先载入这个css模块
        '*': {
            css: 'static/js/framework/requirejs/css.min.js'
        }
    },
    paths: {
        'vue': 'static/js/framework/vue/vue.min',
        'vue-router': 'static/js/framework/vue/vue-router',
        'ELEMENT': 'static/element-ui/js/index',
        'axios': 'static/js/framework/axios/axios.min',
        'qs': 'static/js/framework/axios/qs.min',
        'ajax': 'static/js/framework/extend/ajax',
        'vueselect': 'static/js/framework/extend/select',
        'util': 'static/js/framework/extend/util'
    },
    shim: {
        'ELEMENT': {
            deps: ['vue', 'css!static/element-ui/css/index.css']
        }
    }
});

require(['vue', 'ELEMENT', 'vue-router'], function(Vue, element, VueRouter){
    element.install(Vue);
    Vue.use(VueRouter);
});