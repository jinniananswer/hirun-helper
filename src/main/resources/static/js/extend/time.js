layui.define([], function(exports){
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
        },

        format : function(date) {
            var year = date.getFullYear();
            var month = date.getMonth() + 1;
            month = month.length > 1 ? month : "0"+month;

            var day = date.getDate();
            day = day.length > 1 ? day : "0"+day;

            return year + "-" + month + "-" + day;
        },

        addMonth : function(date, num) {
            num = parseInt(num);
            var sDate = new Date(date);

            var sYear = sDate.getFullYear();
            var sMonth = sDate.getMonth() + 1;
            var sDay = sDate.getDate();

            var eYear = sYear;
            var eMonth = sMonth + num;
            var eDay = sDay;
            while (eMonth > 12) {
                eYear++;
                eMonth -= 12;
            }

            var eDate = new Date(eYear, eMonth - 1, eDay);

            while (eDate.getMonth() != eMonth - 1) {
                eDay--;
                eDate = new Date(eYear, eMonth - 1, eDay);
            }

            return this.format(eDate);
        }
    };
    exports('time', obj);
});