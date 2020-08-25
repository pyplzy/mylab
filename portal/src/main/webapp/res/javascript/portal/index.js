$(function(){
	
	//由于在弹窗中使用easydropdown无法显示完整的下拉框内容（可能是受系统样式影响），做以下操作解决
	$("#dropdownGender").click(function(){
		//如果下拉框展开，尝试设置高度
		if($(this).children("div").hasClass("open")){
			$(this).children("div").children("div").delay(200).css("height","84px");
		}
		
	});
	//由于在弹窗中使用easydropdown无法显示完整的下拉框内容（可能是受系统样式影响），做以下操作解决
	$("#dropdownUserCata").click(function(){
		//如果下拉框展开，尝试设置高度
		if($(this).children("div").hasClass("open")){
			$(this).children("div").children("div").delay(200).css("height","56px");
		}
	});
	//通讯录搜索框点击及回车事件
	$("#schcontent").click(function(){
		//模拟placeholder（有插件可有，首页为了减少http请求，不使用）
		if($.trim($(this).val())=='通讯录搜索'){
			$(this).val('');
		}
		$(this).addClass("borderclor");	
	}).keyup(function(event) {
		//回车（建议用event.which判断）
		if(event.which == 13) {
			$("#schcontent").addClass("bbff");
			doAddressBookSearch();
		}
	}).blur(function(){
		//模拟placeholder（有插件可有，首页为了减少http请求，不使用）
		if($.trim($(this).val())==''){
			$(this).val('通讯录搜索');
		}
	});
	
	//通讯录搜索框放大镜点击事件
	$('#schbtn').click(function() {
		//设置样式
		$(".schInput").addClass("bbff");
		doAddressBookSearch();
	});
	
	//点击屏幕其他地方弹窗收起
	$(document).mouseup(function(e){
	  var _con = $(".schInput,.searchcont");   // 设置目标区域
	  if(!_con.is(e.target) && _con.has(e.target).length === 0){ // Mark 1
		  $(".searchcont").hide();	
		  //宽度变回来
		  $(".searchcont").css("width","280px");
		  $(".pdetail").hide();
		  $(".schInput").removeClass("bbff");
	  }
	});

	//【通讯录-新建联系人】保存按钮点击事件
	$('#btnSave').click(function(){
		//姓名
		var name = $.trim($('#newabName').val());
		if(name==''){
			showTipDiag("姓名不能为空！");
			return;
		}
		//手机
		var mobileNo = $.trim($('#newabMobileNo').val()); 
		if(mobileNo!=''&&(mobileNo.length!=11||!isint(mobileNo))){
			showTipDiag("请输入正确的手机号！");
			return;
		}
		//固话
		var fixedLineTel = $.trim($('#newabFixedLineTel').val());
		if(fixedLineTel!=''&&!istell(fixedLineTel)){
			showTipDiag("请输入正确的固话号码！");
			return;
		}
		//邮箱
		var email = $.trim($('#newabEmail').val());
		if(email!=''&&!isemail(email)){
			showTipDiag("请输入正确的邮箱！");
			return;
		}
		//ajax执行保存
		$.ajax({
			type: 'POST',
			url: 'ab/menu_ab_addABrec.do',
			async: false,//因为要实时改变分页数据，只能同步请求
			dataType:"text",//返回数据类型为json时（controller使用@ResponseBody）必须设置返回的数据类型为json
			data:{  
				'name':name,
				'title':$.trim($('#newabTitle').val()),
				'administrativeLevel':$.trim($('#newabAdministrativeLevel').val()),
				'deparment':$.trim($('#newabDeparment').val()),
				'email':email,
				'gender':$("#newabGender").val(),
				'company':$.trim($('#newabCompany').val()),
				'mobileNo':mobileNo,
				'fixedLineTel':fixedLineTel,
				'userCata':$("#newabUserCata").val(),
				'headImg':$('#newimgPath').val()
			},
			success : function(data) {
				if(data=='success'){
					showTipDiag("保存成功！");
					//关闭【通讯录-新建联系人】弹窗
					closeDiv9();
				}else{
					showTipDiag("保存失败！");
				}
			},//success回调函数结束
			error:function(XMLHttpRequest, textStatus, errorThrown){
				showTipDiag("保存失败！");
			}
		});//ajax结束
	});
	
	//重置按钮点击事件
	$('#btnReset').click(function(){
		//设置头像
		$('#newabHeadImg').attr('src','res/images/photo1.png');
		//姓名
		$('#newabName').val('');
		//清空头像路径
		$('newimgPath').val('');
		//设置公司
		$('#newabCompany').val('');
		//设置部门
		$('#newabDeparment').val('');		
		//设置性别
		$("#newabGender").easyDropDown('select',0);
		//设置手机
		$('#newabMobileNo').val('');		
		//设置固话
		$('#newabFixedLineTel').val('');	
		//设置邮箱
		$('#newabEmail').val('');	
		//设置职务
		$('#newabTitle').val('');	
		//设置级别
		$('#newabAdministrativeLevel').val('');
		//设置类型
		$("#newabUserCata").easyDropDown('select',0);
	});	
	
    //头像上传
    $('#fileupload').fileupload({
		dataType: 'json',
	    add: function (e, data) {
	    	var size = data.files[0].size;
	    	if (size > 10 * 1024 * 1024) { // 10mb
	    		showTipDiag('您上传的文件超过大小限制！');
                return;
            }
    		//文件名
    		var fileName = data.files[0].name;
    		//点的位置
    		var lastDotIndex = fileName.lastIndexOf(".");
    		//后缀名
    		var suffix = fileName.substring(lastDotIndex);
    		if(suffix!='.gif'&&suffix!='.png'&&suffix!='.jpg'&&suffix!='.jpeg'){
	    		showTipDiag('请上传图片文件！');
                return;
    		}
    		//隐藏按钮
    		$('#rChangeHeadBtn').hide();
    		$('#lChangeHeadBtn').hide();
    		//直接上传
    		data.formData =  [
      		    {name:'groupCode',value:'headImage'},
    		    {name:'relationId',value:'0'},//传0，使用自动生成的文件名
    		    {name:'userId',value:''}//传空字符串，使用自动生成的文件名
    		];
    		data.submit();
	    },
	    done: function (e, data) {
    		//显示按钮
    		$('#rChangeHeadBtn').show();
    		$('#lChangeHeadBtn').show();
    		
            $.each(data.result, function (index, file) {
            	if(file.fileSize=="-1"){
            		showTipDiag('您上传的文件超过大小限制！');
            	}else if(file.fileSize=="-2"){
            		showTipDiag('请上传图片文件，提示参见头像下方！');
            	}else{
            		var fileName = file.fileName;
            		//用上传后的图片地址显示头像，并在隐藏区域赋值（为了保存）
            		$('#newabHeadImg').attr('src','ab/menu_ab_getHeadImg.do?imgPath='+fileName+'&timestamp='+Math.random());
            		$('#newimgPath').val(fileName);
            	}
            });
        }
	 });//头像上传结束
	
});//$function结束



//执行通讯录搜索
function doAddressBookSearch(){
	//搜索框的值
	var searchValue = $.trim($('#schcontent').val());
	//清空搜索结果
	$('#schresultul').empty();//搜索结果
	$('#schcnt').empty();//搜索记录条数
	//如果没有搜索内容
	if(searchValue==""){
		$('#schcnt').append('(<b>没有符合搜索条件的记录</b>)');
	}else{
		//ajax请求搜索数据
		$.ajax({
			type: 'POST',
			url: 'ab/menu_ab_doSearch.do',
			async: false,//因为要实时改变分页数据，只能同步请求
			dataType:"json",//返回数据类型为json时（controller使用@ResponseBody）必须设置返回的数据类型为json
			data:{  
				'searchValue':searchValue,
				'page':1,
				'rows':100//最大查100条
			},
			success : function(data) {
				var str="";
				//有记录
				if(data.pageObj.total>0) {
					$.each(data.personLst,function(i,v) {
						str+='<li value="'+v.addressBookId+'"><div class="liwrap"><span class="name">'+v.name;
						//防止机构人员同步过来的数据deparment为null
						if(v.deparment){
							str+='</span><span class="department">（'+v.deparment+'）</span></div></li>';
						}else{
							str+='</span><span class="department"></span></div></li>';
						}
					});
					$("#schresultul").append(str);	
					$('#schcnt').append('(<b>'+data.pageObj.total+'</b>)');
					//记录渲染样式、添加点击事件
					$(".liwrap").mouseenter(function(){
						$(this).addClass("liwraphov");
					}).mouseleave(function(){
						$(this).removeClass("liwraphov");
					}).click(function(){
						$(this).toggleClass("liactive");
						$(this).parent().siblings().find(".liwrap").removeClass("liactive");
					});
					//记录上点击
					$(".pcont li").click(function(){
						//ajax请求个人详细通讯录信息
						$.ajax({
							type: 'POST',
							url: 'ab/menu_ab_getABdetail.do',
							async: false,//因为要实时改变分页数据，只能同步请求
							dataType:"json",//返回数据类型为json时（controller使用@ResponseBody）必须设置返回的数据类型为json
							data:{  
								'addressBookId':$(this).val()
							},
							success : function(data) {
								//有记录，填充详情界面
								if(data) {
									//头像
									if(data.headImg&&$.trim(data.headImg)!=""){
										$('#abHeadImg').attr('src','ab/menu_ab_getHeadImg.do?imgPath='+data.headImg);
									}else{//默认头像
										$('#abHeadImg').attr('src','res/images/photo1.png');
									}
									//头像下面的姓名
									$('#abName').text(data.name);
									//防止机构人员同步过来的数据company为null
									if(data.company){
										//头像下面的公司
										$('#abCompany1').text(data.company);
									}else{
										$('#abCompany1').text("");
									}
									//设置公司
									$('#abCompany2').val(data.company);	
									//设置部门
									$('#abDeparment').val(data.deparment);		
									//设置性别
									$('#abGender').val(data.gender);	
									//设置手机
									$('#abMobileNo').val(data.mobileNo);		
									//设置固话
									$('#abFixedLineTel').val(data.fixedLineTel);	
									//设置邮箱
									$('#abEmail').val(data.email);	
									//设置职务
									$('#abTitle').val(data.title);	
									//设置级别
									$('#abAdministrativeLevel').val(data.administrativeLevel);
									//设置类型
									$('#abUserCata').val(data.userCata);										
								}
							}//success回调函数结束
						});//ajax结束
						$(".searchcont").css("width","940px");
						$(".pdetail").show();	
					});
				
				}else{//没记录
					$('#schcnt').append('(<b>没有符合搜索条件的记录</b>)');
				}
			}//success回调函数结束
		});//ajax结束
	}
	//展示搜索结果
	$(".searchcont").show();
}

//通讯录-新增联系人弹窗
function showDivNewLinkMan() {
	var $abHeadImg = $('#abHeadImg');
	if($abHeadImg.attr('src')=='##'){
		$abHeadImg.attr('src',ctx+'/res/images/photo1.png');
	}
	//初始化下弹窗（把数据清掉，避免再次打开看见上次的内容）
	$('#btnReset').click();
	var bg = document.getElementById("mask9"); 
	var con = document.getElementById("searchnew");  
	var w = document.body.scrollWidth; //网页正文全文宽 
	var h = document.body.scrollHeight; //网页正文全文高 
	bg.style.display = "block"; 
	bg.style.width = w + "px"; 
	bg.style.height = h + "px"; 
	con.style.display = "block"; 
}; 
//关闭通讯录-新增联系人弹窗
function closeDiv9() { 
	var bg = document.getElementById("mask9"); 
	var con = document.getElementById("searchnew"); 
	bg.style.display = "none"; 
	con.style.display = "none"; 
};

//关闭对话框
function closeMsgbox() {
	$('#confirm').hide();
}
//生成提示对话框
function showTipDiag(tipMsg){
	$('#label33').html('').html(tipMsg);
	$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox();">确&nbsp;定</div>');
	$('#confirm').show();
}
//判断输入是否是一个整数
function isint(str){
var result=str.match(/^(-|\+)?\d+$/);
if(result==null) return false;
return true;
}
//判断输入是否是有效的电子邮件
function isemail(str){
var result=str.match(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/);
if(result==null) return false;
return true;
}
//匹配国内电话号码(0511-4405222 或 021-87888822)
function istell(str){
var result=str.match(/\d{3}-\d{8}|\d{4}-\d{7}/);
if(result==null) return false;
return true;
}
//返回字符串的实际长度, 一个汉字算2个长度
function strlen(str){
return str.replace(/[^\x00-\xff]/g, "**").length;
}
