layui.extend({
  setter: 'config' //配置模块
  ,admin: 'admin' //核心模块
  ,view: 'view' //视图渲染模块
}).define(['ajax', 'map', 'carousel', 'echarts', 'setter', 'admin', 'chart', 'layer'], function(exports){
  /*
    下面通过 layui.use 分段加载不同的模块，实现不同区域的同时渲染，从而保证视图的快速呈现
  */
  let admin = layui.admin;
  let $ = layui.$;
  let carousel = layui.carousel;
  let echarts = layui.echarts;
  let setter = layui.setter;


  //区块轮播切换
  layui.use(['admin', 'carousel'], function(){
    let $ = layui.$
    ,admin = layui.admin
    ,carousel = layui.carousel
    ,element = layui.element
    ,device = layui.device();

    //轮播切换
    $('.layadmin-carousel').each(function(){
      var othis = $(this);
      carousel.render({
        elem: this
        ,width: '100%'
        ,arrow: 'none'
        ,interval: othis.data('interval')
        ,autoplay: othis.data('autoplay') === true
        ,trigger: (device.ios || device.android) ? 'click' : 'hover'
        ,anim: othis.data('anim')
      });
    });


  });

  let console = {
      init : function() {
          layui.map.draw('LAY-index-mapview', 'china', 1);
          layui.ajax.post('api/organization/employee-statistic/countInAndDestroyOneYear','', function(data){
              let bar = data.rows;
              layui.chart.drawBar('LAY-index-dataview', bar);
          });
          this.drawBirthdayWish();
      },

      drawBirthdayWish : function() {
          layui.ajax.get('api/organization/employee/showBirthdayWish','', function(data){
              let employee = data.rows;
              if (employee == null) {
                  return;
              }

              let name = employee.name;
              let day = employee.day;

              if (name == null) {
                  return;
              }

              let html = [];
              html.push("<div class=\"layui-carousel\" id=\"birthdayDiv\">");
              html.push("<div carousel-item>");
              html.push("<div style='background: url(\"static/img/birthdaybg.jpeg\") no-repeat;background-size: cover;'><div style='position:absolute;top:400px;padding:20px'><h1 style='font-size: 50px'>HI,"+name+"</h1><br><h1 style='font-size: 50px;color:#e20b2a'><strong>生日快乐！</strong></h1></div></div>");
              html.push("<div style='background: url(\"static/img/birthdaybg.jpeg\") no-repeat;background-size: cover;'><div style='position: absolute;top:400px;padding:20px'><h2>亲爱的"+name+"，今天是你的生日，因为你的降临，今天成了一个美丽的日子，从此世界便多出了一抹绚丽的色彩</h2></div></div>");
              html.push("<div style='background: url(\"static/img/birthdaybg.jpeg\") no-repeat;background-size: cover;'><div style='position: absolute;top:400px;padding:20px'><h2>不负韶光，鸿扬与你也已经携手走过了<strong style='color:#e20b2a'>"+day+"</strong>个日夜，过去的日子里，公司的发展离不开你的付出与努力，同时也离不开你家人的支持与奉献，在此向你们表示衷心感谢!</h2></div></div>");
              html.push("<div style='background: url(\"static/img/birthdaybg.jpeg\") no-repeat;background-size: cover;'><div style='position: absolute;top:400px;padding:20px'><h2>在这个特别的日子里我们谨代表学富五车、英俊潇洒的董事长祝贺你生日快乐，希望你身体倍儿棒，吃嘛嘛儿香！</h2></div></div>");
              html.push("<div style='background: url(\"static/img/birthdaybg.jpeg\") no-repeat;background-size: cover;'><div style='position: absolute;top:400px;padding:20px'><h2>虽然只是一个小小的问候，但那是我的挂念；虽然只是一句轻轻的祝福，但那是我的期盼。人生旅程的又一个起点，等待着你继续扬帆起航！</h2></div></div>");
              html.push("</div>");
              html.push("</div>");

              let birthdayDiv = $("#birthdayWish");
              birthdayDiv.html(html.join(""));
              layui.use('carousel', function(){
                  let carousel = layui.carousel;
                  //建造实例
                  carousel.render({
                      elem: '#birthdayDiv'
                      ,width: '400px' //设置容器宽度
                      ,height: '712px'
                      ,arrow: 'always' //始终显示箭头
                      //,anim: 'updown' //切换动画方式
                  });
              });
              layui.layer.open({
                  title: false,
                  type: 1,
                  area: '400px',
                  content: $("#birthdayWish") //这里content是一个普通的String
              });
          });
      }

  }
  
  exports('console', console);
});