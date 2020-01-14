layui.extend({}).define(['ajax', 'table', 'element', 'layer', 'tree', 'form'], function (exports) {
    let $ = layui.$;
    let element = layui.element;
    let table = layui.table;
    let layer = layui.layer;
    let tree = layui.tree;
    let form = layui.form;

    form.render();

    // 让表格根据属性 checked 来判断是否勾选 checkbox
    table = $.extend(table, {config: {checkName: 'checked'}});

    // 全局变量
    let selectedEmployeeIds = new Set([]);
    let selectedRoleIds = new Set([]);

    let roleGrantManager = {

        init: function () {
            $('body').on('click', '.del', function () {
                let userId = $(this).closest('tr').find('td:eq(0)').text();
                selectedEmployeeIds.delete(userId);
                // 删除当前按钮所在的tr
                $(this).closest('tr').remove();
            });

            $('body').on('click', '.del', function () {
                let roleId = $(this).closest('tr').find('td:eq(0)').text();
                selectedRoleIds.delete(roleId);
                // 删除当前按钮所在的tr
                $(this).closest('tr').remove();
            });

            $('body').on('click', '.seeGrantedRoles', function () {
                let userId = $(this).closest('tr').find('td:eq(0)').text();
                let employeeName = $(this).closest('tr').find('td:eq(1)').text();
                layui.ajax.get('api/user/user-role/listRole', 'userId=' + userId, function (data) {
                    let roles = data.rows;
                    let index = layer.open({
                        type: 0,
                        id: 'Layer',
                        title: '已分配给【' + employeeName + '】的角色',
                        area: ['500px', '450px'],
                        shade: 0,
                        anim: -1,
                        content: '<div class="table"></div>',
                        btn: ['我知道了'],
                        skin: 'layui-layer-molv',
                        success: function (layero, index) {
                            let colsList = [[
                                {field: 'roleId', title: 'ID', width: 80, align: 'center'},
                                {field: 'roleName', title: '角色名'}
                            ]];
                            let datas = [];
                            for (let i = 0; i < roles.length; i++) {
                                datas.push({
                                    roleId: roles[i].roleId,
                                    roleName: roles[i].roleName,
                                });
                            }

                            table.render({
                                elem: layero.find('.table'),
                                id: 'Message',
                                data: datas,
                                cols: colsList,
                                limit: 8,
                                page: false,
                            });
                        },
                        yes: function (index, layero) {
                            layer.close(index);
                        }
                    });
                });

            });

            $('body').on('click', '.seeDetails', function () {
                let roleId = $(this).closest('tr').find('td:eq(0)').text();

                let index = layer.open({
                    type: 2,
                    title: '角色权限',
                    content: 'openUrl?url=modules/system/role_detail',
                    maxmin: false,
                    skin: 'layui-layer-molv',
                    success: function (layero, index) {
                        let body = layer.getChildFrame('body', index);
                        body.find('#roleId').val(roleId);
                    },
                    yes: function (index, layero) {
                        layer.close(index);
                    }
                });
                layer.full(index);
            });

            form.on('select(employeeOptions)', function (data) {
                if (-1 == data.value) {
                    return;
                }

                if (selectedEmployeeIds.has(data.value)) {
                    return;
                } else {
                    selectedEmployeeIds.add(data.value);
                }

                let text = $("#employeeOptions option:selected").text();
                let i = text.indexOf('-');
                let employeeName = text.substring(0, i);
                let employeeMobileNo = text.substring(i + 1);
                //你要添加的html
                let html =
                    '<tr>' +
                    '  <td style="display:none">' + data.value + '</td>' +
                    '  <td align="center">' + employeeName + '</td>' +
                    '  <td align="center">' + employeeMobileNo + '</td>' +
                    '  <td align="center"> ' +
                    '    <a class="layui-btn layui-btn-danger layui-btn-xs del"><i class="layui-icon layui-icon-delete"></i>删除</a> ' +
                    '    <a class="layui-btn layui-btn-normal layui-btn-xs seeGrantedRoles"><i class="layui-icon layui-icon-read"></i>查看</a>' +
                    '  </td>' +
                    '</tr>';

                //添加到表格最后
                $(html).appendTo($('#employee-table tbody:last'));
                form.render();

            });

            form.on('select(roleOptions)', function (data) {
                if (-1 == data.value) {
                    return;
                }

                if (selectedRoleIds.has(data.value)) {
                    return;
                } else {
                    selectedRoleIds.add(data.value);
                }

                let roleName = $("#roleOptions option:selected").text();

                let html =
                    '<tr>' +
                    '  <td align="center">' + data.value + '</td>' +
                    '  <td align="center">' + roleName + '</td>' +
                    '  <td align="center"> ' +
                    '    <a class="layui-btn layui-btn-danger layui-btn-xs del"><i class="layui-icon layui-icon-delete"></i>删除</a> ' +
                    '    <a class="layui-btn layui-btn-normal layui-btn-xs seeDetails"><i class="layui-icon layui-icon-read"></i>查看</a>' +
                    '  </td>' +
                    '</tr>';

                // 添加到表格最后
                $(html).appendTo($('#role-table tbody:last'));
                form.render();

            });

            // 加载员工数据
            layui.ajax.get('api/organization/employee/loadEmployee', '', function (data) {
                let employees = data.rows;
                $('#employeeOptions').empty();
                if (employees == null || employees.length <= 0) {
                    return;
                }

                let length = employees.length;
                let datas = '<option value="-1">输入姓名或手机号搜索</option>';
                for (let i = 0; i < length; i++) {
                    let employee = employees[i];
                    datas += "<option value='" + employee.userId + "'>" + (employee.name + '-' + employee.mobileNo) + "</option>";
                }
                $('#employeeOptions').append(datas);
                form.render('select');
            });

            // 加载角色数据
            layui.ajax.get('api/user/role/loadRole', '', function (data) {
                let roles = data.rows;
                $('#roleOptions').empty();
                if (roles == null || roles.length <= 0) {
                    return;
                }

                let length = roles.length;
                let datas = '<option value="-1">输入角色名搜索</option>';
                for (let i = 0; i < length; i++) {
                    let role = roles[i];
                    datas += "<option value='" + role.roleId + "'>" + role.roleName + "</option>";
                }
                $('#roleOptions').append(datas);
                form.render('select');
            });

        },

    };

    exports('roleGrantManager', roleGrantManager);
})