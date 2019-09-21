layui.use('table', function () {
    var table = layui.table;
    table.render({
        elem: '#employee-table',
        url: '/api/user/user/list',
        response: {
            msgName: 'message',
            countName: 'total',
            dataName: 'rows'
        },
        page: true,
        cols: [[
            {field: 'userId', title: 'ID', width: 80, sort: true, fixed: 'center'},
            {field: 'username', title: '用户名', width: 177},
            {field: 'mobileNo', title: '手机号码', width: 177, sort: true},
            {field: 'role', title: '角色', width: 177},
            {field: 'status', title: '状态', width: 177},
            {field: 'createDate', title: '创建时间', width: 177},
            {field: 'removeDate', title: '离职时间', width: 177},
            {field: 'updateTime', title: '更新时间', width: 177}
        ]]
    });

});