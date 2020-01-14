layui.extend({
    orgTree: 'org',
}).define(['ajax', 'table', 'element', 'orgTree', 'layer', 'form', 'select', 'redirect', 'laydate'], function (exports) {
    let $ = layui.$;
    let table = layui.table;
    let layer = layui.layer;
    let form = layui.form;
    let laydate = layui.laydate;


    exports('planeSketch', planeSketch);
});