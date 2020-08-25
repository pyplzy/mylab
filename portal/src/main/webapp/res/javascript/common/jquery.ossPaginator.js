;(//插件定义开始：为了更好的兼容性，开始前有个分号
	function($){//此处将$作为匿名函数的形参
		    $.fn.extend({
        ossPaginator: function (options) {
            var settings = $.extend({
                totalrecords: 0,//总记录数
                recordsperpage: 0,//每页记录数
                length: 10,//分页按钮显示个数（不包括首页，上一页，下一页，尾页）
                first: '首页',
                last: '尾页',                
                next: '下一页',
                prev: '上一页',
                go: '确定',
                display: 'double',//取值为double（显示去第几页）|single（不显示去第几页），默认double
                initval: 1,//初始化哪一页被选中
                onchange: null,//回调函数
                controlsalways: false//是否一直显示首页，上一页，下一页，尾页
            }, options);
            return this.each(function () {
                var currentPage = 0;
                var startPage = 0;
                var totalpages = parseInt(settings.totalrecords / settings.recordsperpage);//总页数
                if (settings.totalrecords % settings.recordsperpage > 0) totalpages++;
                var initialized = false;//是否初始化好的标识
                var container = $(this).addClass('page').addClass('clearfix');//分页容器
                //清空容器里的内容
                container.find('p').remove();
                container.find('span').remove();
								
				//除确定外所有按钮的容器（左浮动）
				var allButtonsWrapper = $('<p/>').addClass('left');
				//首页（左浮动）
				var btnFirst = $('<button/>').text(settings.first).addClass('left').click(function () { if ($(this).hasClass('disabled')) return false; currentPage = 0; navigate(0); });
                //尾页（左浮动）
                var btnLast = $('<button/>').text(settings.last).addClass('left').click(function () { if ($(this).hasClass('disabled')) return false; currentPage = totalpages - 1; navigate(currentPage); });                
                //上一页（左浮动）
                var btnPrev = $('<button/>').text(settings.prev).addClass('left').click(function () { if ($(this).hasClass('disabled')) return false; currentPage = parseInt(digitBtnsWrapper.find('.on').text()) - 1; navigate(--currentPage); });
                //下一页（左浮动）
                var btnNext = $('<button/>').text(settings.next).addClass('left').click(function () { if ($(this).hasClass('disabled')) return false; currentPage = parseInt(digitBtnsWrapper.find('.on').text()); navigate(currentPage); });
                //四大按钮中间的数字按钮（左浮动）
                var digitBtnsWrapper = $('<p/>').addClass('left');
                                
                //共多少页
                var spanTotalPage = $('<span/>').text('共'+totalpages+'页').addClass('totalnum').addClass('left');
                //去第几页
                var spanGo = $('<span/>').text('去第').addClass('totalnum').addClass('right');
                //输入页数文本框容器
                var inputPageWrapper = $('<p/>').addClass('jumpwrap').addClass('right');
                //输入页数的文本框
                var inputPage = $('<input/>').attr('type', 'text').addClass('jump')
                		.focus(function(){
											$(this).addClass("jumphov");
											$(this).parent().prev(".pagebtn_wrap").animate({"width":"50px"},"slow");
										}).blur(function(){
											$(this).removeClass("jumphov");
											$(this).parent().prev(".pagebtn_wrap").animate({"width":"0"},"slow");
										})
                		.keydown(function (e) {
                    if (isTextSelected(inputPage)) inputPage.val('');
                    if (e.which >= 48 && e.which < 58) {//数字0-9的keycode
                        var value = parseInt(inputPage.val() + (e.which - 48));
                        if (!(value > 0 && value <= totalpages)) e.preventDefault();
                    
                    } else if (!(e.which == 8 || e.which == 46)) e.preventDefault();//keycode 8 = BackSpace BackSpace;keycode 46 = Delete
                });
                //包装输入页数的文本框
                inputPageWrapper.append(inputPage);
                
                //确定按钮容器
                var btnGoWrapper = $('<p/>').addClass('pagebtn_wrap').addClass('right');
                //确定按钮
                var btnGo = $('<a/>').attr('href', '#').addClass('pagebtn').html(settings.go).click(function () { if (inputPage.val() == '') return false; else { currentPage = parseInt(inputPage.val()) - 1; navigate(currentPage); } });
								//包装确定按钮
                btnGoWrapper.append(btnGo);
                
                //尾部的页
                var spanLast = $('<span/>').text('页').addClass('totalnum').addClass('right');
                
                //包装除确定外的按钮
                allButtonsWrapper.append(btnFirst).append(btnPrev);
                allButtonsWrapper.append(digitBtnsWrapper);
                allButtonsWrapper.append(btnNext).append(btnLast);
                //包装分页全部元素
                container.append(allButtonsWrapper).append(spanTotalPage).append(spanLast).append(btnGoWrapper).append(inputPageWrapper).append(spanGo);
                
                //如果是single 去第几页|输入页数文本框|确定按钮|尾部的页，都不显示
                if (settings.display == 'single') {
                    spanGo.css('display', 'none');
                    inputPageWrapper.css('display', 'none');
                    btnGoWrapper.css('display', 'none');
                    spanLast.css('display', 'none');
                }
                buildNavigation(startPage);
                if (settings.initval == 0) settings.initval = 1;
                currentPage = settings.initval - 1;
                navigate(currentPage);
                initialized = true;
                
                //展示记录数（暂时未用到）
                function showLabels(pageIndex) {
                    container.find('span').remove();
                    var upper = (pageIndex + 1) * settings.recordsperpage;
                    if (upper > settings.totalrecords) upper = settings.totalrecords;
                    container.append($('<span/>').append($('<b/>').text(pageIndex * settings.recordsperpage + 1)))
                                             .append($('<span/>').text('-'))
                                             .append($('<span/>').append($('<b/>').text(upper)))
                                             .append($('<span/>').text('of'))
                                             .append($('<span/>').append($('<b/>').text(settings.totalrecords)));
                }
                
                
                //构建导航
                function buildNavigation(startPage) {
                	digitBtnsWrapper.find('button').remove();
                    if (settings.totalrecords <= settings.recordsperpage){
                    	 return;
                   	}
                    for (var i = startPage; i < startPage + settings.length; i++) {
                        if (i == totalpages) break;
                        
                        var $btn = $('<button/>');
            			$btn.attr('id', (i + 1))
                        .text(i + 1)
                        .click(function () {
                        		//$(this).closest('button')最近的button，如果自身是，返回，不是往父元素找，.prevAll()所有前面的同辈元素
                            currentPage = startPage + $(this).closest('button').prevAll().length;
                            navigate(currentPage);
                        });
                        //包装页数数字按钮
                        digitBtnsWrapper.append($btn);
                    }
                    //showLabels(startPage);
                    //输入页数文本框赋值
                    inputPage.val((startPage + 1));
                    //清除选中样式
                    digitBtnsWrapper.find('button').removeClass('on');
                    //将第一个加选中
                    digitBtnsWrapper.find('button:eq(0)').addClass('on');
                    //set width of paginator
                    //var sW = list.find('li:eq(0) a').outerWidth() + (parseInt(list.find('li:eq(0)').css('margin-left')) * 2);
                    //var width = sW * list.find('li').length;
                    //list.css({ width: width });
                    
                    //确定四大按钮显示情况
                    showRequiredButtons(startPage);
                }
                
                //导航
                function navigate(topage) {
                    //make sure the page in between min and max page count
                    var index = topage;
                    var mid = settings.length / 2;
                    if (settings.length % 2 > 0) mid = (settings.length + 1) / 2;
                    var startIndex = 0;
                    if (topage >= 0 && topage < totalpages) {
                        if (topage >= mid) {
                            if (totalpages - topage > mid)
                                startIndex = topage - (mid - 1);
                            else if (totalpages > settings.length)
                                startIndex = totalpages - settings.length;
                        }
                        buildNavigation(startIndex); 
                        //showLabels(currentPage);
                        digitBtnsWrapper.find('button').removeClass('on');
                        inputPage.val(currentPage + 1);
                        digitBtnsWrapper.find('button[id="' + (index + 1) + '"]').addClass('on');
                        var recordStartIndex = currentPage * settings.recordsperpage;
                        var recordsEndIndex = recordStartIndex + settings.recordsperpage;
                        if (recordsEndIndex > settings.totalrecords)
                            recordsEndIndex = settings.totalrecords % recordsEndIndex;
                        if (initialized) {//初始化完成
                            if (settings.onchange != null) {//回调函数不为空，调用回调函数，传递页数，记录开始下标，记录结束下标
                                settings.onchange((currentPage + 1), recordStartIndex, recordsEndIndex);
                            }
                        }
                        //showRequiredButtons();
                    }
                    showRequiredButtons();
                }
                //显示必要的四大按钮（首页、上一页、下一页、尾页）
                function showRequiredButtons() {
                	//总页数大于数字按钮的长度
                    if (totalpages > settings.length) {

                        if (currentPage > 0) {
                            if (!settings.controlsalways) {
                                btnPrev.css('display', '');
                            }
                            else {
                                btnPrev.css('display', '').removeClass('disabled');
                            }
                        }
                        else {
                            if (!settings.controlsalways) {
                                btnPrev.css('display', 'none');
                            }
                            else {
                                btnPrev.css('display', '').addClass('disabled');
                            }
                        }
                        if (currentPage > settings.length / 2 - 1) {
                            if (!settings.controlsalways) {
                                btnFirst.css('display', '');
                            }
                            else {
                                btnFirst.css('display', '').removeClass('disabled');
                            }
                        }
                        else {
                            if (!settings.controlsalways) {
                                btnFirst.css('display', 'none');
                            }
                            else {
                                btnFirst.css('display', '').addClass('disabled');
                            }
                        }

                        if (currentPage == totalpages - 1) {
                            if (!settings.controlsalways) {
                                btnNext.css('display', 'none');
                            }
                            else {
                                btnNext.css('display', '').addClass('disabled');
                            }
                        }
                        else {
                            if (!settings.controlsalways) {
                                btnNext.css('display', '');
                            }
                            else {
                                btnNext.css('display', '').removeClass('disabled');
                            }
                        }
                        if (totalpages > settings.length && currentPage < (totalpages - (settings.length / 2)) - 1) {
                            if (!settings.controlsalways) {
                                btnLast.css('display', '');
                            }
                            else {
                                btnLast.css('display', '').removeClass('disabled');
                            }
                        }
                        else {
                            if (!settings.controlsalways) {
                                btnLast.css('display', 'none');
                            }
                            else {
                                btnLast.css('display', '').addClass('disabled');
                            }
                        };
                    }
                    //总页数小于数字按钮的长度
                    else {
                    	  //如果controlsalways=false，把四大按钮隐藏
                        if (!settings.controlsalways) {
                            btnFirst.css('display', 'none');
                            btnPrev.css('display', 'none');
                            btnNext.css('display', 'none');
                            btnLast.css('display', 'none');
                        }
                        else {//如果controlsalways=true，把四大按钮置为不可用
                            btnFirst.css('display', '').addClass('disabled');
                            btnPrev.css('display', '').addClass('disabled');
                            btnNext.css('display', '').addClass('disabled');
                            btnLast.css('display', '').addClass('disabled');
                        }
                    }
                }
                //判断文字选中
                function isTextSelected(el) {
                    var startPos = el.get(0).selectionStart;
                    var endPos = el.get(0).selectionEnd;
                    var doc = document.selection;//IE支持
                    if (doc && doc.createRange().text.length != 0) {//doc.createRange()选中的文本对象
                        return true;
                    } else if (!doc && el.val().substring(startPos, endPos).length != 0) {
                        return true;
                    }
                    return false;
                }
                
            });
        }
    });

})(jQuery);//插件定义结束：将jQuery作为实参传递给匿名函数