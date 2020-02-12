layui.extend({}).define(['ajax', 'form', 'element'], function (exports) {
    var $ = layui.$;
    var form = layui.form;
    var transPendingDetail = {
        init: function () {

            layui.ajax.get('api/organization/hr-pending/getDetail', 'id=' + $('#id').val(), function (data) {
                var detail = data.rows;
                if (detail == null) {
                    return;
                }
                form.val("detail-form", {
                    "sourceOrgPath": detail.sourceOrgPath,
                    "orgPath": detail.orgPath,
                    "sourceJobRoleName": detail.sourceJobRoleName,
                    "jobRoleName": detail.jobRoleName,
                    "sourceJobGradeName": detail.sourceJobGradeName,
                    "jobGradeName": detail.jobGradeName,
                    "sourceJobRoleNatureName": detail.sourceJobRoleNatureName,
                    "jobRoleNatureName": detail.jobRoleNatureName,
                    "sourceDiscountRate": detail.sourceDiscountRate,
                    "discountRate": detail.discountRate,
                    "sourceParentEmployeeName": detail.sourceParentEmployeeName,
                    "parentEmployeeName": detail.parentEmployeeName,
                    "sourceHomeAddress":detail.sourceHomeArea+"/"+detail.sourceHomeAddress,
                    "homeAddress":detail.homeArea+"/"+detail.homeAddress
                });
            });
        },

    };
    exports('transPendingDetail', transPendingDetail);
});