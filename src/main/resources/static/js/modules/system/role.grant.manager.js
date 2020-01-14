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
                let userId = $(this).closest('tr').find('td:eq(0)').text();
                selectedRoleIds.delete(userId);
                // 删除当前按钮所在的tr
                $(this).closest('tr').remove();
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
                    '  <td>' + employeeName + '</td>' +
                    '  <td>' + employeeMobileNo + '</td>' +
                    '  <td> <a class="layui-btn layui-btn-danger layui-btn-xs del"><i class="layui-icon layui-icon-delete"></i>删除</a> </td>' +
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
                    '  <td>' + data.value + '</td>' +
                    '  <td>' + roleName + '</td>' +
                    '  <td> <a class="layui-btn layui-btn-danger layui-btn-xs del"><i class="layui-icon layui-icon-delete"></i>删除</a> </td>' +
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