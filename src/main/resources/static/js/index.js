
layui.extend({
    setter: 'config' //配置模块
    ,admin: 'admin' //核心模块
    ,view: 'view' //视图渲染模块
}).define(['setter', 'admin'], function(exports){
    var setter = layui.setter
        ,element = layui.element
        ,admin = layui.admin
        ,tabsPage = admin.tabsPage
        ,view = layui.view

        //打开标签页
        ,openTabsPage = function(url, text){
            //遍历页签选项卡
            var matchTo
                ,tabs = $('#LAY_app_tabsheader>li')
                ,path = url.replace(/(^http(s*):)|(\?[\s\S]*$)/g, '');

            tabs.each(function(index){
                var li = $(this)
                    ,layid = li.attr('lay-id');

                if(layid === url){
                    matchTo = true;
                    tabsPage.index = index;
                }
            });

            text = text || '新标签页';

            if(setter.pageTabs){
                //如果未在选项卡中匹配到，则追加选项卡
                if(!matchTo){
                    $(APP_BODY).append([
                        '<div class="layadmin-tabsbody-item layui-show">'
                        ,'<iframe src="'+ url +'" frameborder="0" class="layadmin-iframe"></iframe>'
                        ,'</div>'
                    ].join(''));
                    tabsPage.index = tabs.length;
                    element.tabAdd(FILTER_TAB_TBAS, {
                        title: '<span>'+ text +'</span>'
                        ,id: url
                        ,attr: path
                    });
                }
            } else {
                var iframe = admin.tabsBody(admin.tabsPage.index).find('.layadmin-iframe');
                iframe[0].contentWindow.location.href = url;
            }

            //定位当前tabs
            element.tabChange(FILTER_TAB_TBAS, url);
            admin.tabsBodyChange(tabsPage.index, {
                url: url
                ,text: text
            });
        }

        ,APP_BODY = '#LAY_app_body', FILTER_TAB_TBAS = 'layadmin-layout-tabs'
        ,$ = layui.$, $win = $(window);

    //初始
    if(admin.screen() < 2) admin.sideFlexible();

    view().autoRender();

    //对外输出
    exports('index', {
        openTabsPage: openTabsPage
    });
});

layui.define(['ajax', 'element'],function(exports){
 	var $ = layui.$;
 	var index = {
 		loadMenus : function() {
			layui.ajax.get('/api/system/menu/list', '', function (data) {
                var json = eval(data);
                var menus = json.rows;
                var html = [];
                $.each(menus, function (i, item) {
                    //循环获取数据
                    var name = item.text;
                    var icon = item.icon;
                    html.push("<li  class=\"layui-nav-item\">");
                    html.push("<a href=\"javascript:;\">");
                    if(icon) {
                        html.push("<i class=\"layui-icon "+icon+"\"></i>");
                    }
                    html.push("<cite>"+name+"</cite>");
                    html.push("</a>");
                    var children = item.children;
                    index.recursiveChildren(children, html);
                    html.push("</li>");
                });

                $("#LAY-system-side-menu").html(html.join(""));
                layui.element.render('nav', 'layadmin-system-side-menu');
            });
	 	},

		recursiveChildren : function(children, html) {
 			if(children && children.length > 0) {
                html.push("<dl class=\"layui-nav-child\">");
                $.each(children, function(i, t){
                    html.push("<dd>");
                    var url = t.url;
                    var icon = t.icon;
                    if(url == null || url == "" || url == "undfined") {
                        html.push("<a>"+t.text+"</a>");
                    }
                    else {
                        html.push("<a lay-href=\"" + t.url + "\">" + t.text + "</a>");
                    }
                    index.recursiveChildren(t.children, html);
                    html.push("</dd>");
                });
                html.push("</dl>");
			}
		}
	};
	exports('index', index);
 });