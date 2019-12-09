layui.use(['ajax', 'layer', 'element'], function () {
    let $ = layui.$;
    let layer = layui.layer;

    $('#editRoleBtn').on('click', function () {

        let roleId = $('#roleId').val();
        let roleName = $('#roleName').val();
        let remark = $('#remark').val();
        let content = {"roleId": roleId,"roleName": roleName, "remark": remark};

        $.ajax({
            type: "post",
            url: "api/user/role/edit",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(content),
            success: function (data) {
                if (0 == data.code) {
                    layer.msg("保存成功！", {
                        time: 1500, icon: 6, end: function () {
                            let index = parent.layer.getFrameIndex(window.name); // 得到当前iframe层的索引
                            parent.layer.close(index);
                            parent.layui.$('#queryRole').click();
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