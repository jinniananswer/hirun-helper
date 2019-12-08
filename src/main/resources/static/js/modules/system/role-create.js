layui.use(['ajax', 'layer', 'element'], function () {
    let $ = layui.$;
    let layer = layui.layer;

    $('#createRoleBtn').on('click', function () {

        let roleName = $('#roleName').val();
        let remark = $('#remark').val();
        let content = {"roleName": roleName, "remark": remark};

        $.ajax({
            type: "post",
            url: "api/user/role/create-role",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(content),
            success: function (data) {
                if (0 == data.code) {
                    layer.msg("保存成功！", {
                        time: 3000, icon: 6, end: function () {
                            let index = parent.layer.getFrameIndex(window.name); // 得到当前iframe层的索引
                            parent.layer.close(index);
                            parent.$('#queryRole').click();
                        }
                    });
                } else {
                    layer.alert("保存失败！" + data.message);
                }
            },
            error: function (data) {
                layer.alert("保存失败！");
            },

        });
    });

});