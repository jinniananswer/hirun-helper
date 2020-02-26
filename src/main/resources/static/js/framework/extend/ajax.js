define(['vue','ELEMENT','axios', 'qs'], function(Vue,element,axios, QS){
    let vm = new Vue({});
    let ajax = {
        get: function(url, request, successFunc, errFunc, needConfirm) {
            this.execute('get', url, request, successFunc, errFunc, needConfirm);
        },

        post: function(url, request, successFunc, errFunc, needConfirm) {
            this.execute('post', url, request, successFunc, errFunc, needConfirm);
        },

        execute: function(methodType, url, request, successFunc, errFunc, needConfirm) {

            let loading = vm.$loading({
                lock: true,
                text: '拼命加载中……',
                spinner: 'el-icon-loading',
                background: 'rgba(0, 0, 0, 0.7)'
            });

            let successMethod = function(info) {
                let data = info.data;
                let code = data.code;
                if (code != 0) {
                    vm.$confirm(code+":"+data.message, '操作失败', {
                        confirmButtonText: '确定',
                        type: 'error',
                        center: true
                    }).then(() => {

                    }).catch(() => {

                    });
                } else if (successFunc == null || typeof(successFunc) == "undefined") {
                    if (needConfirm) {
                        vm.$confirm('点击确定按钮刷新本页面，点击关闭按钮关闭本界面', '操作成功', {
                            confirmButtonText: '确定',
                            type: 'success',
                            center: true
                        }).then(() => {
                            document.location.reload();
                        }).catch(() => {
                            top.layui.admin.closeThisTabs();
                        });
                    } else {
                        vm.$alert('点击确定关闭本页面', '操作成功', {
                            confirmButtonText: '确定',
                            callback: action => {
                                top.layui.admin.closeThisTabs();
                            }
                        });
                    }

                } else {
                    successFunc(data.rows);
                }

                if (loading != null) {
                    loading.close();
                }
            }

            if (errFunc == null || typeof(errFunc) == "undefined") {
                errFunc = function(data) {
                    if (loading != null) {
                        loading.close();
                    }
                    vm.$message.error('对不起，我们的系统出错了，请赶紧联系管理员报告错误吧，'+data.statusText+'我们将第一时间解决您的问题');
                }

            }

            if (methodType == 'get') {
                axios.get(url,{
                    params: request
                })
                .then(successMethod)
                .catch(errFunc);
            } else if (methodType == 'post') {
                let data = QS.stringify(request);
                axios.post(url, data).then(successMethod).catch(errFunc);
            }
        }
    }
    return ajax;
})