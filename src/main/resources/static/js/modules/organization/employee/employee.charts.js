layui.extend({
}).define(['ajax', 'chart'],function(exports){
    var charts = {
        init : function() {
            layui.ajax.post('api/organization/employee-statistic/countBySex','', function(data){
                var pie = data.rows;
                layui.chart.drawPie('LAY-index-sexPie', pie);
            });

            layui.ajax.post('api/organization/employee-statistic/countByAge','', function(data){
                var pie = data.rows;
                layui.chart.drawPie('LAY-index-agePie', pie, 70);
            });

            layui.ajax.post('api/organization/employee-statistic/countByJobRoleNature','', function(data){
                var pie = data.rows;
                layui.chart.drawPie('LAY-index-jobRoleNature', pie, 60);
            });

            layui.ajax.post('api/organization/employee-statistic/countByCompanyAge','', function(data){
                var pie = data.rows;
                layui.chart.drawPie('LAY-index-companyAge', pie, 70);
            });

            layui.ajax.post('api/organization/employee-statistic/countByEducationLevel','', function(data){
                var pie = data.rows;
                layui.chart.drawPie('LAY-index-educationLevel', pie, 60);
            });

            layui.ajax.post('api/organization/employee-statistic/countByType','', function(data){
                var pie = data.rows;
                layui.chart.drawPie('LAY-index-type', pie, 70);
            });
        }
    };
    exports('charts', charts);
});