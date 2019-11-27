layui.use(['ajax', 'layer', 'layedit'], function () {
    let $ = layui.$;
    let layedit = layui.layedit;
    let layer = layui.layer;

    // 构建一个默认的编辑器
    let index = layedit.build('LAY_announce-textarea', {
        tool: ['strong', 'italic', 'underline', 'del', '|', 'left', 'center', 'right', '|', 'face', 'link', 'unlink'],
        height: 100
    });

    // 编辑器外部操作
    let active = {
        createAnnounce: function () {
            let content = layedit.getContent(index); // 获取编辑器内容
            $.ajax({
                type: "post",
                url: "api/system/notify/sendAnnounce",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(content),
                success: function (obj) {
                    layer.msg("操作成功！");
                    layedit.setContent(index, "", false);
                },
                error: function (obj) {
                    layer.alert("操作失败！");
                }
            });
        },
    };

    $('.site-demo-layedit').on('click', function () {
        let type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });


});