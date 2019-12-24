layui.extend({}).define(['ajax', 'table', 'element', 'layedit', 'laydate', 'util', 'layer', 'form', 'select', 'redirect'], function (exports) {
    let $ = layui.$;
    let table = layui.table;
    let layedit = layui.layedit;
    let layer = layui.layer;
    let form = layui.form;
    let element = layui.element;
    let laydate = layui.laydate;
    let util = layui.util;

    laydate.render({
        elem: '#startTime',
        type: 'datetime'
    });

    laydate.render({
        elem: '#endTime',
        type: 'datetime'
    });

    $('#startTime').val(util.toDateString(new Date(new Date() - 1000 * 60 * 60 * 24 * 10), 'yyyy-MM-dd HH:mm:ss'));
    $('#endTime').val(util.toDateString(new Date(), 'yyyy-MM-dd HH:mm:ss'));

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
                fitColumns: true,
                response: {
                    msgName: 'message',
                    countName: 'total',
                    dataName: 'rows'
                },
                cols: [[
                    {type: 'checkbox'},
                    {field: 'id', title: 'ID', width: 60, align: 'center'},
                    {field: 'content', title: '公告内容', align: 'left'},
                    {field: 'name', title: '发送者', width: 80, align: 'center'},
                    {field: 'createTime', title: '时间', width: 160, align: 'center'},
                    {fixed: 'right', align: 'center', title: '操作', width: 150, fixed: 'right', toolbar: '#announceBar'}
                ]],
                page: false,
                text: {none: '暂无相关数据，请检查查询条件。'}
            });

            $('#queryAnnounce').on('click', function () {
                table.reload('announce-table', {
                    page: {
                        curr: 1
                    },
                    loading: true,
                    page: false,
                    url: 'api/system/notify/announce-list-all',
                    where: {
                        startTime: $("input[id='startTime']").val(),
                        endTime: $("input[id='endTime']").val()
                    }
                })
            });

            table.on('tool(announce-table)', function (obj) {
                let data = obj.data;

                if (obj.event === 'seeDetails') {
                    announceObj.seeDetails(data);
                } else if (obj.event === 'deleteAnnounce') {
                    announceObj.deleteAnnounce([data]);
                    console.log('deleteAnnounce: ' + data.id);
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
                            content: 'openUrl?url=modules/system/announce-create',
                            maxmin: true,
                            //area: ['600px', '450px'],
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
                            content: 'openUrl?url=modules/system/announce-create',
                            maxmin: true,
                            area: ['550px', '400px'],
                            skin: 'layui-layer-molv',
                            success: function (layero, index) {
                                layedit.setContent(index, "原始内容", false);
                            },
                            yes: function (index, layero) {

                                layer.close(index);
                            }
                        });
                        break;
                    case 'deleteAnnounce':
                        announceObj.deleteAnnounce(data);
                        break;
                }
                ;
            });

        },

        seeDetails: function(data) {
            var index = layer.open({
                type: 2,
                title: '查看详情',
                content: 'openUrl?url=modules/system/message-detail',
                maxmin: true,
                btn: ['关闭'],
                area: ['550px', '400px'],
                skin: 'layui-layer-molv',
                success: function (layero, index) {
                    let body = layer.getChildFrame('body', index);
                    body.find('#senderName').html('<h1>来自【' + data.name + '】的公告' + '</h1>');
                    body.find('#createTime').html('<span>' + data.createTime + '</span>');
                    body.find('#content').html('<div class="layadmin-text">' + data.content + '</div>');
                },
                yes: function (index, layero) {
                    layer.close(index);
                }
            });

        },

        editAnnounce: function(data) {

        },

        deleteAnnounce: function(data) {
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

        },
    };

    exports('announceObj', announceObj);
})