layui.define(['ajax', 'form', 'layer'],function(exports){
    var $ = layui.$;
    var form = layui.form;
    var layer = layui.layer;
    var staff = {
        init : function() {
            form.on('submit(btnSubmit)', function(data){
                layui.staff.create(data.field);
                return false;
            });
        },
        create : function(formData) {
            layui.ajax.post('/api/hr/staff/save', formData);
        }
    };
    exports('staff', staff);
});