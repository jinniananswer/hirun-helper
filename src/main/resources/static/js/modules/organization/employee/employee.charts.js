layui.extend({
    orgTree: 'org'
}).define(['ajax', 'chart', 'orgTree'],function(exports){
    let $ = layui.$;
    let charts = {
        init : function() {

            $('#orgPath').bind('input propertychange', function(event) {
                layui.charts.query();
            });

            layui.charts.query();

        },

        query : function() {
            layui.ajax.post('api/organization/employee-statistic/countBySex','&orgId='+$("#orgId").val(), function(data){
                let pie = data.rows;
                if (pie != null) {
                    layui.chart.drawPie('LAY-index-sexPie', pie);
                }
            });

            layui.ajax.post('api/organization/employee-statistic/countByAge','&orgId='+$("#orgId").val(), function(data){
                let pie = data.rows;
                if (pie != null) {
                    layui.chart.drawPie('LAY-index-agePie', pie, 70);
                }
            });

            layui.ajax.post('api/organization/employee-statistic/countByJobRoleNature','&orgId='+$("#orgId").val(), function(data){
                let pie = data.rows;
                if (pie != null) {
                    layui.chart.drawPie('LAY-index-jobRoleNature', pie, 60);
                }
            });

            layui.ajax.post('api/organization/employee-statistic/countByCompanyAge','&orgId='+$("#orgId").val(), function(data){
                let pie = data.rows;
                if (pie != null) {
                    layui.chart.drawPie('LAY-index-companyAge', pie, 70);
                }
            });

            layui.ajax.post('api/organization/employee-statistic/countByEducationLevel','&orgId='+$("#orgId").val(), function(data){
                let pie = data.rows;
                if (pie != null) {
                    layui.chart.drawPie('LAY-index-educationLevel', pie, 60);
                }
            });

            layui.ajax.post('api/organization/employee-statistic/countByType','&orgId='+$("#orgId").val(), function(data){
                let pie = data.rows;
                if (pie != null) {
                    layui.chart.drawPie('LAY-index-type', pie, 70);
                }
            });
        }
    };
    exports('charts', charts);
});