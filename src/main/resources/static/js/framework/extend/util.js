define(['moment'], function(moment){
    let util = {
        getRequest: function(name) {
            return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ""])[1].replace(/\+/g, '%20')) || null;
        },

        getNowDate: function() {
            return moment(new Date()).add('year',0).format("YYYY-MM-DD");
        },

        getNowTime: function() {
            return moment(new Date()).add('year',0).format("YYYY-MM-DD HH:mm:ss");
        },

        openPage: function(url, title) {
            let topLayui = parent === self ? layui : top.layui;
            topLayui.index.openTabsPage(url, title);
        },

        redirect: function(url) {
            document.location.href = url;
        }
    }

    return util;
})