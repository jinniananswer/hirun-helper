let vm = new Vue({
    el: '#app',
    data() {
        return {
            employeeOptions: [],
            roleOptions: [],
            selectedEmployee: '',
            selectedRole: '',
            revoke: false,
            employeeTableData: [],
            roleTableData: [],
            employeeDialogVisible: false,
            employeeDialogTitle: '',
            haveRoleTableData: [],
            roleDetailDialogVisible: false,
            menuData: [],
            menuCheckedKeys: [],
            defaultProps: {
                children: 'children',
                label: 'title'
            },
            haveFuncTableData: [],
        }
    },
    methods: {
        loadEmployee: function () {
            axios.get('api/organization/employee/loadEmployee').then(function (responseData) {
                vm.employeeOptions = responseData.data.rows;
            }).catch(function (error) {
                console.log(error);
            });
        },

        selectEmployeeChange: function (item) {
            let spans = item.split('-');
            let isExist = false;
            vm.employeeTableData.forEach(function (element, index, array) {
                if (spans[0] == element.userId) {
                    isExist = true;
                }
            });
            if (!isExist) {
                vm.employeeTableData.push({
                    userId: spans[0],
                    employeeName: spans[1],
                    employeeMobileNo: spans[2],
                });
            }
        },

        handleEmployeeTableDelete: function (index) {
            vm.employeeTableData.splice(index, 1);
        },

        handleEmployeeRoleView: function (index, row) {
            vm.employeeDialogTitle = '已分配给【' + row.employeeName + '】的角色';
            axios.get('api/user/user-role/listRole', {params: {userId: row.userId}}).then(function (responseData) {
                vm.haveRoleTableData = responseData.data.rows;
            }).catch(function (error) {
                console.log(error);
            });
            vm.employeeDialogVisible = true;
        },

        loadRole: function () {
            axios.get('api/user/role/loadRole').then(function (responseData) {
                vm.roleOptions = responseData.data.rows;
            }).catch(function (error) {
                console.log(error);
            });

        },

        selectRoleChange: function (item) {
            let spans = item.split('-');
            let isExist = false;
            vm.roleTableData.forEach(function (element, index, array) {
                if (spans[0] == element.roleId) {
                    isExist = true;
                }
            });

            if (!isExist) {
                vm.roleTableData.push({
                    roleId: spans[0],
                    roleName: spans[1],
                });
            }
        },

        handleRoleTableDelete: function (index) {
            vm.roleTableData.splice(index, 1);
        },

        handleRoleFuncView: function (index, row) {

            // 查角色能看到的菜单Id
            axios.get('api/system/menu/role-menu', {params: {roleId: row.roleId}}).then(function (responseData) {
                vm.menuCheckedKeys = responseData.data.rows;
            }).catch(function (error) {
                console.log(error);
            });

            // 查全量菜单信息用于展示
            axios.get('api/system/menu/all-menu', {params: {roleId: row.roleId}}).then(function (responseData) {
                vm.menuData = responseData.data.rows;
            }).catch(function (error) {
                console.log(error);
            });

            // 查角色可用的功能权限信息
            axios.get('api/system/func/func-list', {params: {roleId: row.roleId}}).then(function (responseData) {
                vm.haveFuncTableData = responseData.data.rows;
            }).catch(function (error) {
                console.log(error);
            });

            vm.roleDetailDialogVisible = true;
        },

        prepareSubmitData: function (userIds, roleIds) {
            vm.employeeTableData.map((e, index) => {
                userIds.push(e.userId);
            });
            if (userIds.length <= 0) {
                this.$message({
                    message: '请至少选择一个员工！',
                    type: 'warning'
                });
                return false;
            }

            vm.roleTableData.map((e, index) => {
                roleIds.push(e.roleId);
            });

            if (roleIds.length <= 0) {
                this.$message({
                    message: '请至少选择一个角色！',
                    type: 'warning'
                });
                return false;
            }

            return true;
        },

        grantRoleBtn: function () {
            let userIds = [];
            let roleIds = [];
            if (vm.prepareSubmitData(userIds, roleIds)) {

                axios({
                    method: 'post',
                    url: 'api/user/user-role/grantRole',
                    data: {
                        "userIds": userIds,
                        "roleIds": roleIds,
                    }
                }).then(function (responseData) {
                    if (0 == responseData.data.code) {

                        Vue.prototype.$message({
                            message: '角色分配成功！',
                            type: 'success'
                        });
                    }
                }).catch(function (error) {
                    console.log(error);
                    Vue.prototype.$message({
                        message: '角色分配失败！',
                        type: 'warning'
                    });
                });
            }
        },

        revokeRoleBtn: function () {
            let userIds = [];
            let roleIds = [];
            if (vm.prepareSubmitData(userIds, roleIds)) {
                axios({
                    method: 'post',
                    url: 'api/user/user-role/revokeRole',
                    data: {
                        "userIds": userIds,
                        "roleIds": roleIds,
                    }
                }).then(function (responseData) {
                    if (0 == responseData.data.code) {

                        Vue.prototype.$message({
                            message: '角色回收成功！',
                            type: 'success'
                        });
                    }
                }).catch(function (error) {
                    console.log(error);
                    Vue.prototype.$message({
                        message: '角色回收失败！',
                        type: 'warning'
                    });
                });
            }
        },

    },
    created: function () {
        this.loadEmployee();
        this.loadRole();
    },
});
