layui.use(['ajax', 'layer', 'layedit', 'tag'], function () {
    let $ = layui.$;
    let layedit = layui.layedit;
    let layer = layui.layer;
    let tag = layui.tag;

    // 构建一个默认的编辑器
    let index = layedit.build('LAY_announce-textarea', {
        tool: ['strong', 'italic', 'underline', 'del', '|', 'left', 'center', 'right', '|', 'face', 'link', 'unlink'],
        height: 180
    });

    // 定时
    setInterval(function () {
        let index = parent.layer.getFrameIndex(window.name);
        if ($("#orgCarts").children().length > 0) {
            $("#orgCarts").children().each(function () {
                if ("122" == $(this).attr("lay-id")) {
                    parent.layer.title("公告", index);
                    return;
                }
            });
            parent.layer.title("通知", index);
        } else {
            parent.layer.title("公告", index);
        }
    }, 5000);

    // 编辑器外部操作
    let active = {
        createAnnounce: function () {

            let orgs = [];
            $("#orgCarts").children().each(function () {
                orgs.push($(this).attr("lay-id"));
            });

            let content = layedit.getContent(index); // 获取编辑器内容

            let data = new Object();
            data.orgs = orgs;
            data.content = content;

            $.ajax({
                type: "post",
                url: "api/system/notify/sendAnnounce",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(data),
                success: function (obj) {
                    layer.msg("操作成功！");
                    layedit.setContent(index, "", false);
                },
                error: function (obj) {
                    layer.alert("操作失败！");
                }
            });
        },

        orgAdd: function () {

            let index = layer.open({
                type: 2,
                title: '部门',
                content: 'openUrl?url=modules/system/org-select',
                maxmin: false,
                area: ['400px', '400px'],
                skin: 'layui-layer-molv',
                btn: ['确定', '取消'],
                yes: function (index, layero) {
                    $(layero).find('iframe').contents().find('#collectBtn').click();
                    let ids = $(layero).find('iframe').contents().find('#orgIds').val();
                    ids = eval(ids);
                    let rawIds = ids.map(function (org) {
                        return org.id;
                    });

                    $.ajax({
                        type: "post",
                        url: "api/organization/org/filter",
                        dataType: "json",
                        contentType: "application/json",
                        data: JSON.stringify(rawIds),
                        success: function (data) {
                            if (0 == data.code) {
                                data = data.rows;

                                for (let i = 0; i < ids.length; i++) {
                                    if (data.indexOf(ids[i].id) > -1) {
                                        tag.delete('orgCarts', ids[i].id);
                                        tag.add('orgCarts', {text: ids[i].title, id: ids[i].id});
                                    }
                                }

                            }
                        },
                        error: function (obj) {
                            layer.alert("操作失败！");
                        }
                    });

                    layer.close(index);
                },
                no: function (index, layero) {
                    console.log('click button no');
                    layer.close(index);
                },
            });


        },

        orgDeleteAll: function () {
            $("#orgCarts").children().each(function () {
                tag.delete('orgCarts', $(this).attr("lay-id"));
            });
        },

        peopleAdd: function () {
            tag.add('peopleCarts', {
                text: '个人:' + (Math.random() * 1000 | 0),
                id: new Date().getTime()
            })
        },

        peopleDeleteAll: function () {
            $("#peopleCarts").children().each(function () {
                tag.delete('peopleCarts', $(this).attr("lay-id"));
            });
        },

        tagDelete: function (othis) {
            tag.delete('targetCarts', '44'); //删除：“商品管理”
            othis.addClass('layui-btn-disabled');
        },

        targetCartsCheckout: function () {

            let orgs = [];
            $("#orgCarts").children().each(function () {
                orgs.push($(this).attr("lay-id"));
            });

            let peoples = [];
            $("#peopleCarts").children().each(function () {
                peoples.push($(this).attr("lay-id"));
            });

            alert(JSON.stringify(orgs));
            alert(JSON.stringify(peoples));

        },
    };

    $('.site-demo-layedit').on('click', function () {
        let type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });

});