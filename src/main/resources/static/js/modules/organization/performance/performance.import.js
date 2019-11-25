layui.extend({}).define(['ajax', 'upload'], function (exports) {
    var $ = layui.jquery;
    var upload = layui.upload;

    var performanceImport = {
        init: function () {
            var listView = $('#uploadFileList')
                , uploadListIns = upload.render({
                elem: '#upload-list'
                , url: 'api/organization/employee-performance/importEmployeePerformance'
                , accept: 'file'
                , multiple: false
                , exts: 'xls|xlsx'
                , field: 'fileUpload'
                , auto: false
                , bindAction: '#upload-listAction'
                , choose: function (obj) {
                    var files = this.files = obj.pushFile(); //将每次选择的文件追加到文件队列

                    //读取本地文件
                    obj.preview(function (index, file, result) {
                        $('#uploadFileList').empty();

                        var tr = $(['<tr id="upload-' + index + '">'
                            , '<td>' + file.name + '</td>'
                            , '<td>' + (file.size / 1014).toFixed(1) + 'kb</td>'
                            , '<td>等待上传</td>'
                            , '<td>'
                            , '<button class="layui-btn layui-btn-mini layui-btn-danger upload-delete">删除</button>'
                            , '</td>'
                            , '</tr>'].join(''));

                        //删除
                        tr.find('.upload-delete').on('click', function () {
                            delete files[index]; //删除对应的文件
                            tr.remove();
                            uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                        });

                        listView.append(tr);
                    });
                }
                , done: function (res, index, upload) {
                    if (res.code == 0) { //上传成功
                        var tr = listView.find('tr#upload-' + index)
                            , tds = tr.children();
                        tds.eq(2).html('<span style="color: #5FB878;">上传成功</span>');
                        tds.eq(3).html(''); //清空操作
                        return delete this.files[index]; //删除文件队列已经上传成功的文件
                    } else {
                        var batchId = res.message;
                        $('#batchId').val(batchId);
                        var tr = listView.find('tr#upload-' + index)
                            , tds = tr.children();
                        tds.eq(2).html('<span style="color: #FF5722;">上传失败</span>');
                        tds.eq(3).html(''); //清空操作
                        tds.eq(3).html('<button class="layui-btn layui-btn-mini layui-btn-danger upload-error" onclick="layui.performanceImport.exportsError();">失败详情下载</button>');
                    }
                }
            });
        },

        exportsError: function () {
            var batchId=$('#batchId').val();
            window.location.href = "api/organization/employee-performance/download-error?batchId=" + batchId;
        }

    };
    exports('performanceImport', performanceImport);
});