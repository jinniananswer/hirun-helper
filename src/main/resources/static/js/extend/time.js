layui.define(null, function(exports){
    var $ = layui.$;
    var obj = {
        getYearDiff : function(startDate, endDate) {
            if (endDate == null) {
                endDate = new Date();
            }
            startDate = new Date(startDate);
            //总秒数
            var millisecond = Math.floor((endDate.getTime() - startDate.getTime())/1000);

            //总天数
            var allDay = Math.floor(millisecond/(24*60*60));

            //注意同getYear的区别
            var startYear = startDate.getFullYear();
            var currentYear = endDate.getFullYear();

            //闰年个数
            var leapYear = 0;
            for(var i=startYear;i<currentYear;i++){
                if(this.isLeapYear(i)){
                    leapYear++;
                }
            }

            //年数
            var year = Math.floor((allDay - leapYear*366)/365 + leapYear);
            return year;
        },

        isLeapYear : function(year) {
            if((year%4==0 && year%100!=0)||(year%100==0 && year%400==0)){
                return true;
            }
            return false;
        }
    };
    exports('time', obj);
});