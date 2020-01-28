require.config({
    map: { //map告诉RequireJS在任何模块之前，都先载入这个css模块
        '*': {
            css: 'static/js/framework/requirejs/css.min.js'
        }
    },
    paths: {
        'vue': 'static/js/framework/vue/vue.min',
        'ELEMENT': 'static/element-ui/js/index',
        'axios': 'static/js/framework/axios/axios.min'
    },
    shim: {
        'ELEMENT': {
            deps: ['vue', 'css!static/element-ui/css/index.css']
        }
    }
});

require(['vue', 'ELEMENT'], function(Vue, element){
    element.install(Vue);
});