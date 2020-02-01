let vm = new Vue({
    el: '#app',
    data: function () {
        return {
            activeName: 'announce',
            dialogVisible: false,
            dialogTitle: '公告',
            currentRow: {},
            announceTableData: [],
            noticeTableData: [],
            messageTableData: [],
            selectedIds: [],
            notifyType: 0,
        }
    },

    methods: {

        /* 加载公告数据 */
        loadAnnounceTableData: function () {
            axios.get('api/system/notify-queue/announce-list').then(function (res) {
                vm.announceTableData = res.data.rows;
            }).catch(function (error) {
                console.log(error);
            });
        },

        /* 加载通知数据 */
        loadNoticeTableData: function () {
            axios.get('api/system/notify-queue/notice-list').then(function (res) {
                vm.noticeTableData = res.data.rows;
            }).catch(function (error) {
                console.log(error);
            });
        },

        /* 加载私信数据 */
        loadMessageTableData: function () {
            axios.get('api/system/notify-queue/message-list').then(function (res) {
                vm.messageTableData = res.data.rows;
            }).catch(function (error) {
                console.log(error);
            });
        },

        /* 打开对话框，显示详情 */
        showDetail(row) {
            //alert("showDetail: " + JSON.stringify(row));
            vm.currentRow = row;
            vm.notifyType = row.notifyType;
            vm.selectedIds.length = 0;
            vm.selectedIds.push(vm.currentRow.id);
            if (1 == vm.notifyType) {
                vm.dialogTitle = "来自【" + row.name + "】的公告";
            } else if (2 == vm.notifyType) {
                vm.dialogTitle = "来自【" + row.name + "】的通知";
            } else if (3 == vm.notifyType) {
                vm.dialogTitle = "来自【" + row.name + "】的私信";
            }

            vm.dialogVisible = true;
        },

        markReaded() {
            axios({
                method: 'post',
                url: 'api/system/notify-queue/markReaded',
                data: vm.selectedIds
            }).then(function (res) {
                if (0 == res.data.code) {
                    if (1 == vm.notifyType) {
                        vm.loadAnnounceTableData();
                    } else if (2 == vm.notifyType) {
                        vm.loadNoticeTableData();
                    } else if (3 == vm.notifyType) {
                        vm.loadMessageTableData();
                    }
                }
            }).catch(function (error) {
                console.log(error);
                Vue.prototype.$message({
                    message: '标记消息已读失败！Id:' + vm.currentRow.id,
                    type: 'warning'
                });
            });
            vm.dialogVisible = false;
        },

        markReadedAll(notifyType) {
            this.$confirm('要全部标记已读？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                axios.get('api/system/notify-queue/markReadedAll/' + notifyType).then(function (res) {
                    if (0 == res.data.code) {
                        // 刷新数据
                        if (1 == notifyType) {
                            vm.loadAnnounceTableData();
                        } else if (2 == notifyType) {
                            vm.loadNoticeTableData();
                        } else if (3 == notifyType) {
                            vm.loadMessageTableData();
                        }

                        Vue.prototype.$message({
                            message: '操作成功！',
                            type: 'success'
                        });
                    }
                }).catch(function (error) {
                    console.log(error);
                    this.$message({
                        type: 'info',
                        message: '操作失败！'
                    });
                });

            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消'
                });
            });
        },

        handleSelectionChange (rows) {
            vm.selectedIds.length = 0;
            rows.map(e => {
                vm.selectedIds.push(e.id);
                vm.notifyType = e.notifyType;
            });
        }
    }
});

vm.loadAnnounceTableData();
vm.loadNoticeTableData();
vm.loadMessageTableData();