layui.extend({}).define(['ajax', 'form', 'layer', 'element', 'laydate', 'select'], function (exports) {
    var $ = layui.$;
    var form = layui.form;
    var layer = layui.layer;
    var laydate = layui.laydate;
    var updateEmployeeHoliday = {
        init: function () {

            layui.select.init('holidayType', 'HOLIDAY_TYPE', $('#holidayTypeValue').val(), true);

            laydate.render({
                elem: '#startTime',
                type: 'datetime',
                done: function(value,date){
                    this.dateTime.hours=0;
                    this.dateTime.minutes=0;
                    this.dateTime.seconds=0;
                },
            });

            laydate.render({
                elem: '#endTime',
                type: 'datetime',
                format: 'yyyy-MM-dd HH:mm:ss',
                done: function(value,date){
                    this.dateTime.hours=23;
                    this.dateTime.minutes=59;
                    this.dateTime.seconds=59;
                },
            });

            form.on('submit(update-employeeholiday-submit)', function (data) {
                var field = data.field; //获取提交的字段
                var index = parent.layer.getFrameIndex(window.name);
                $.ajax({
                    url: 'api/organization/employee-holiday/updateEmployeeHoliday',
                    type: 'POST',
                    data: field,
                    success: function (data) {
                        if (data.code == 0) {
                            layer.confirm('操作成功，点击确定按钮刷新本页面，点击关闭按钮关闭本界面？', {
                                btn: ['确定', '关闭'],
                                closeBtn: 0,
                                icon: 6,
                                title: '提示信息',
                                shade: [0.5, '#fff'],
                                skin: 'layui-layer-admin layui-anim'
                            }, function () {
                                parent.layui.table.reload('employeeholiday_table'); //重载表格
                                parent.layer.close(index); //再执行关闭
                            }, function () {
                                top.layui.admin.closeThisTabs();
                            });
                        } else {
                            parent.layer.msg("提交失败", {icon: 5});
                        }
                    }
                });
            });
        },

    };
    exports('updateEmployeeHoliday', updateEmployeeHoliday);
});