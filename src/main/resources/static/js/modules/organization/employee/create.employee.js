layui.extend({
    orgTree: 'org'
}).define(['ajax', 'select', 'form', 'layer', 'laydate', 'element', 'orgTree'],function(exports){
    var $ = layui.$;
    var form = layui.form;
    var layer = layui.layer;
    var laydate = layui.laydate;
    var employee = {
        init : function() {
            layui.select.init('educationLevel', 'SEX', '2', false);
            layui.select.init('birthdayType', 'BIRTHDAY_TYPE', '1', false);

            layui.element.on('tab(employeeTab)', function(data){

            });

            laydate.render({
                elem: '#birthday'
            });

            form.on('submit(btnSubmit)', function(data){
                layui.employee.create(data.field);
                return false;
            });
        },

        selectOrg : function() {
            layui.orgTree.init('orgTree');

        },

        create : function(formData) {
            layui.ajax.post('/api/hr/staff/save', formData);
        }
    };
    exports('employee', employee);
});