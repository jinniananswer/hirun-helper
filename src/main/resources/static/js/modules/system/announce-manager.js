layui.extend({}).define(['ajax', 'table', 'element', 'layedit', 'layer', 'form', 'select', 'redirect'], function (exports) {
    let $ = layui.$;
    let table = layui.table;
    let layedit = layui.layedit;
    let layer = layui.layer;
    let form = layui.form;
    let element = layui.element;

    let announceObj = {
        init: function () {

            element.on('nav(nav-ul)', function (elem) {
                layer.msg(elem.text());
            });

            let announce = table.render({
                elem: '#announce-table',
                toolbar: '#announceToolbar',
                defaultToolbar: ['filter'],
                height: 'full-20',
                url: 'api/system/notify-queue/announce-list',
                fitColumns: true,
                response: {
                    msgName: 'message',
                    countName: 'total',
                    dataName: 'rows'
                },
                page: false,
                cols: [[
                    {type: 'checkbox'},
                    {field: 'id', title: 'ID', width: 60, align: 'center'},
                    {field: 'content', title: '公告内容', align: 'left'},
                    {field: 'name', title: '发送者', width: 80, align: 'center'},
                    {field: 'createTime', title: '时间', width: 160, align: 'center'},
                    {
                        field: 'readed', title: '状态', width: 80, align: 'center', templet: function (d) {
                            if (d.readed) {
                                return '<span class="layui-btn layui-btn-primary layui-btn-xs">已读</span>';
                            } else {
                                return '<span class="layui-btn layui-btn-normal layui-btn-xs">未读</span>';
                            }
                        }
                    },
                    {align: 'center', title: '操作', width: 80, fixed: 'right', toolbar: '#announceBar'}
                ]]
            });

            table.on('tool(announce-table)', function (obj) {
                let data = obj.data;
                if (obj.event === 'rowClick') {
                    layer.msg('查看消息！');
                }
                if (obj.event === 'seeDetail') {
                    // announceObj.seeDetail(data, '公告');
                }
            });

            table.on('toolbar(announce-table)', function (obj) {
                let checkStatus = table.checkStatus(obj.config.id); // 获取选中行状态
                let data = checkStatus.data;
                let event = obj.event;

                switch (event) {

                    case 'newAnnounce':
                        let index = layer.open({
                            type: 2,
                            title: '编辑新公告',
                            content: 'openUrl?url=/modules/system/announce-create',
                            maxmin: true,
                            area: ['550px', '700px'],
                            skin: 'layui-layer-molv',
                            success: function (layero, index) {

                            },
                            yes: function (index, layero) {

                                layer.close(index);
                            }
                        });
                        layer.full(index);
                        break;
                    case 'editAnnounce':

                        let idxEdit = layer.open({
                            type: 2,
                            title: '修改公告',
                            content: 'openUrl?url=/modules/system/announce-create',
                            maxmin: true,
                            area: ['550px', '700px'],
                            skin: 'layui-layer-molv',
                            success: function (layero, index) {
                                layedit.setContent(index, "原始内容", false);
                            },
                            yes: function (index, layero) {

                                layer.close(index);
                            }
                        });
                        layer.full(idxEdit);
                        break;
                    case 'deleteAnnounce':
                        if (data.length <= 0) {
                            layer.msg('请选中一条数据，再进行操作。');
                            return;
                        }
                        let param = data.map(item => item.id);
                        $.ajax({
                            type: "post",
                            url: "api/system/notify/deleteAnnounce",
                            dataType: "json",
                            contentType: "application/json",
                            data: JSON.stringify(param),
                            success: function (obj) {
                                layer.msg("操作成功！");
                                table.reload('announce-table');
                            },
                            error: function (obj) {
                                layer.alert("操作失败！");
                            }
                        });
                        break;
                }
                ;
            });

        },
    };

    exports('announceObj', announceObj);
})