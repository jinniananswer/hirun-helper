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
        'xe-utils': 'static/vxe/js/xe-utils',
        'vxe-table': 'static/vxe/js/vxe-table',
        'moment': 'static/js/framework/date/moment.min',
        'axios': 'static/js/framework/axios/axios.min',
        'qs': 'static/js/framework/axios/qs.min',
        'ajax': 'static/js/framework/extend/ajax',
        'vueselect': 'static/js/framework/extend/select',
        'util': 'static/js/framework/extend/util',
        'cust-info': 'static/js/modules/bss/component/cust.info',
        'order-info': 'static/js/modules/bss/component/order.info',
        'order-payment': 'static/js/modules/bss/component/order.payment',
        'order-worker': 'static/js/modules/bss/component/order.worker',
        'order-selectemployee': 'static/js/modules/bss/component/order.selectemployee',
        'order-selectdecorator': 'static/js/modules/bss/component/order.selectdecorator',
        'cust-visit': 'static/js/modules/bss/component/cust.visit',
        'upload-file': 'static/js/modules/system/component/upload.file',
        'order-file-upload': 'static/js/modules/bss/component/order.file.upload',
        'house-select': 'static/js/modules/bss/component/house.select',
        'order-search-employee':'static/js/modules/bss/component/order.search.employee',
        'order-file-upload':'static/js/modules/bss/component/order.file.upload',
        'order-discount-item':'static/js/modules/bss/component/order.discount.item',
        'org-orgtree':'static/js/modules/organization/component/org.orgtree'

    },
    shim: {
        'ELEMENT': {
            deps: ['vue', 'css!static/element-ui/css/index.css']
        },
        'vxe-table': {
            deps: ['vue', 'xe-utils', 'css!static/vxe/css/index.css']
        }
    }
});

require(['vue', 'ELEMENT', 'vue-router'], function(Vue, element, VueRouter) {
    element.install(Vue);
    Vue.use(VueRouter);
});