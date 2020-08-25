//全局变量，存放左侧记录点击后返回的数据对象，保存或者删除后，置空。
var g_data =null;
//通讯录管理
$(function(){
	//隐藏新增 显示新增
	$(".shide").click(function(){
		var cont = $(this).parent().parent().find(".contb");
		if(cont.is(":hidden")){		
			$(this).find(".updown").addClass("chgupdown");
			$(this).find("span").text("隐藏新增");
			cont.show(function(){
				var bodyh = $("body").height();
				$(".rightWrap",top.document).height( bodyh );//刷新高度
			  });
		}else{
				$(this).find(".updown").removeClass("chgupdown");
				$(this).find("span").text("显示新增");
				cont.hide(function(){
					var bodyh = $("body").height();
					$(".rightWrap",top.document).height( bodyh );//刷新高度
				  });
			}
	});
	
	//新增单个联系人按钮点击事件
	$('#newABrec').click(function(){
		parent.window.showDivNewLinkMan();
	});
	
	//增量导入按钮绑定点击事件
	$('#btnStartImp').click(function(){
		//如果文件名为空，提示选择文件
		if($('#selectedFile').val()==""){
			showTipDiag('请选择文件！');
		}
	});
	
	//增量联系人导入
    $('#fileuploadExl').fileupload({
		dataType: 'json',
		
	    add: function (e, data) {
	    	//添加文件时，为按钮绑定一次性点击事件，用来上传
	    	data.context = $('#btnStartImp').one("click", function(){
	    		//文件名
	    		var fileName = data.files[0].name;
	    		//点的位置
	    		var lastDotIndex = fileName.lastIndexOf(".");
	    		//后缀名
	    		var suffix = fileName.substring(lastDotIndex);
	    		if(suffix=='.xls'||suffix=='.xlsx'){
	                $('#btnStartImp span.rf_btn').text('导入中...');
	                //增量导入按钮置为不可用
	                $(this).prop("disabled",true);
	                data.submit();
	    		}else{
	    			showTipDiag('请选择excel数据文件！');
	    			return;
	    		}
            });
	    },
	    change: function (e, data) {
	        $.each(data.files, function (index, file) {
	        	//显示选则的文件名
	        	$('#selectedFile').val(file.name);
	        });
	    },
	    done: function (e, data) {
	    	//增量导入按钮置为可用
	    	$('#btnStartImp').prop("disabled",false);
	    	
            $.each(data.result, function (index, file) {
            	if(file.fileSize>0){
            		showTipDiag('导入成功！');
            		$('#selectedFile').val("");
            		$('#btnStartImp span.rf_btn').text('增量导入');
            	}else if(file.fileSize=="0"){
            		showTipDiag('请不要上传空文件！');
            		$('#selectedFile').val("");
            		$('#btnStartImp span.rf_btn').text('增量导入');
            	}else if(file.fileSize=="-1"){
            		showTipDiag('您上传的文件超过大小限制！');
            		$('#selectedFile').val("");
            		$('#btnStartImp span.rf_btn').text('增量导入');
            	}else{
            		showTipDiag('导入失败！');
            		$('#selectedFile').val("");
            		$('#btnStartImp span.rf_btn').text('增量导入');
            	}
            });
        }
	 });//增量联系人导入结束
    
	
	//通讯录搜索框回车事件
	$('#schcontent').keyup(function(event) {
		//回车（建议用event.which判断）
		if(event.which == 13) {
			doAddressBookSearch();
		}
	});
	
	//通讯录搜索框放大镜点击事件
	$('#schbtn').click(function() {
		doAddressBookSearch();
	});
	
	//保存按钮点击事件
	$('#btnSave').click(function(){
		//主键
		var addressBookId = $('#abAddressBookId').val();
		//不需要更新
		if(addressBookId==''){
			showTipDiag("数据没有变动，不需要更新！");
			return;
		}
		//姓名
		var name = $.trim($('#abName').val());
		if(name==''){
			showTipDiag("姓名不能为空！");
			return;
		}
		//手机
		var mobileNo = $.trim($('#abMobileNo').val()); 
		if(mobileNo!=''&&(mobileNo.length!=11||!isint(mobileNo))){
			showTipDiag("请输入正确的手机号！");
			return;
		}
		//固话
		var fixedLineTel = $.trim($('#abFixedLineTel').val());
		if(fixedLineTel!=''&&!istell(fixedLineTel)){
			showTipDiag("请输入正确的固话号码！");
			return;
		}
		//邮箱
		var email = $.trim($('#abEmail').val());
		if(email!=''&&!isemail(email)){
			showTipDiag("请输入正确的邮箱！");
			return;
		}
		//部门
		var deparment = $.trim($('#abDeparment').val());
		//ajax执行保存
		$.ajax({
			type: 'POST',
			url: 'menu_ab_updateABrec.do',
			async: false,//因为要实时改变分页数据，只能同步请求
			dataType:"text",//返回数据类型为json时（controller使用@ResponseBody）必须设置返回的数据类型为json
			data:{  
				'addressBookId':addressBookId,
				'name':name,
				'title':$.trim($('#abTitle').val()),
				'administrativeLevel':$.trim($('#abAdministrativeLevel').val()),
				'deparment':deparment,
				'email':email,
				'gender':$("#abGender").val(),
				'company':$.trim($('#abCompany').val()),
				'mobileNo':mobileNo,
				'fixedLineTel':fixedLineTel,
				'userCata':$("#abUserCata").val(),
				'headImg':$('#imgPath').val(),
				'userId':$('#userId').val()
			},
			success : function(data) {
				if(data=='success'){
					showTipDiag("保存成功！");
					//修改左侧记录的姓名和部门
					$('#'+addressBookId).text(name);
					//如果部门有值
					if(deparment){
						$('#'+addressBookId).next('span').text('（'+deparment+'）');
					}
					//清空全局变量
					g_data=null;
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
		//g_data不为null
		if(g_data){
			//头像
			if(g_data.headImg&&$.trim(g_data.headImg)!=""){
				$('#abHeadImg').attr('src','menu_ab_getHeadImg.do?imgPath='+g_data.headImg+'&timestamp='+Math.random());
				$('imgPath').val(g_data.headImg);
			}else{//默认头像
				$('#abHeadImg').attr('src','../res/images/photo1.png');
				$('imgPath').val();
			}
			//姓名
			$('#abName').val(g_data.name);
			//把通讯录主键放在隐藏域
			$('#abAddressBookId').val(g_data.addressBookId);
			//设置userId
			$('#userId').val(g_data.userId);
			//设置公司
			if(g_data.company){
				$('#abCompany').val(g_data.company);
			}else{
				$('#abCompany').val("");
			}
			//设置部门
			$('#abDeparment').val(g_data.deparment);		
			//设置性别（easyDropDown下拉框选中要根据index）
			var tmpGender = g_data.gender;
			if(tmpGender=='未知'){
				$("#abGender").easyDropDown('select',0);
			}else if(tmpGender=='男'){
				$("#abGender").easyDropDown('select',1);
			}else{
				$("#abGender").easyDropDown('select',2);
			}
			//设置手机
			$('#abMobileNo').val(g_data.mobileNo);		
			//设置固话
			$('#abFixedLineTel').val(g_data.fixedLineTel);	
			//设置邮箱
			$('#abEmail').val(g_data.email);	
			//设置职务
			$('#abTitle').val(g_data.title);	
			//设置级别
			$('#abAdministrativeLevel').val(g_data.administrativeLevel);
			//设置类型
			var  tmpUserCata = g_data.userCata;
			if(tmpUserCata=='系统用户'){
				$("#abUserCata").easyDropDown('select',0);
			}else{
				$("#abUserCata").easyDropDown('select',1);
			}
		}
	});
	
	//删除按钮点击事件
	$('#btnDelete').click(function(){
		deleteABrecord();
	});
	
    //头像上传
    $('#fileupload').fileupload({
		dataType: 'json',
	    add: function (e, data) {
	    	var relationId = $('#abAddressBookId').val();
	    	var userId = $('#userId').val();
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
    		    {name:'relationId',value:relationId},
    		    {name:'userId',value:userId}
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
            		showTipDiag('请上传图片文件！');
            	}else{
            		var fileName = file.fileName;
            		//用上传后的图片地址显示头像，并在隐藏区域赋值（为了保存）
            		$('#abHeadImg').attr('src','menu_ab_getHeadImg.do?imgPath='+fileName+'&timestamp='+Math.random());
            		$('#imgPath').val(fileName);
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
	//如果没有搜索内容
	if(searchValue==""){
		$("#schresultul").append('<div style="color:#ff6000;text-align:center;">(<b>没有符合搜索条件的记录</b>)</div>');
	}else{
		//ajax请求搜索数据
		$.ajax({
			type: 'POST',
			url: 'menu_ab_doSearch.do',
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
						str+='<li value="'+v.addressBookId+'"><div class="litem"><span class="name" id="'+v.addressBookId+'">'+v.name;
						//防止机构人员同步过来的数据deparment为null
						if(v.deparment){
							str+='</span><span class="department">（'+v.deparment+'）</span></div></li>';
						}else{
							str+='</span><span class="department"></span></div></li>';
						}
					});
					$("#schresultul").append(str);	
					//记录渲染样式、添加点击事件
					$(".litem").mouseenter(function(){
						$(this).addClass("liwraphov");
					}).mouseleave(function(){
						$(this).removeClass("liwraphov");
					}).click(function(){
						$(this).toggleClass("liactive");
						$(this).parent().siblings().find(".litem").removeClass("liactive");
					});
					//记录上点击
					$(".schres li").click(function(){
						//ajax请求个人详细通讯录信息
						$.ajax({
							type: 'POST',
							url: 'menu_ab_getABdetail.do',
							async: false,//因为要实时改变分页数据，只能同步请求
							dataType:"json",//返回数据类型为json时（controller使用@ResponseBody）必须设置返回的数据类型为json
							data:{  
								'addressBookId':$(this).val()
							},
							success : function(data) {
								//有记录，填充详情界面
								if(data) {
									//赋值到全局变量，做重置用
									g_data = data;
									//头像
									if(data.headImg&&$.trim(data.headImg)!=""){
										$('#abHeadImg').attr('src','menu_ab_getHeadImg.do?imgPath='+data.headImg+'&timestamp='+Math.random());
										$('#rChangeHeadBtn').text("修改头像");
										$('#imgPath').val(data.headImg);
									}else{//默认头像
										$('#abHeadImg').attr('src','../res/images/photo1.png');
										$('#rChangeHeadBtn').text("上传头像");
										$('#imgPath').val('');
									}
									//姓名
									$('#abName').val(data.name);
									//把通讯录主键放在隐藏域
									$('#abAddressBookId').val(data.addressBookId);
									//设置userId
									$('#userId').val(data.userId);
									//设置公司
									if(data.company){
										$('#abCompany').val(data.company);
									}else{
										$('#abCompany').val("");
									}
									//设置部门
									$('#abDeparment').val(data.deparment);		
									//设置性别（easyDropDown下拉框选中要根据index）
									var tmpGender = data.gender;
									if(tmpGender=='未知'){
										$("#abGender").easyDropDown('select',0);
									}else if(tmpGender=='男'){
										$("#abGender").easyDropDown('select',1);
									}else{
										$("#abGender").easyDropDown('select',2);
									}
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
									var  tmpUserCata = data.userCata;
									if(tmpUserCata=='系统用户'){
										$("#abUserCata").easyDropDown('select',0);
									}else{
										$("#abUserCata").easyDropDown('select',1);
									}
									
								}
							}//success回调函数结束
						});//ajax结束
					});
				
				}else{//没记录
					$("#schresultul").append('<div style="color:#ff6000;text-align:center;">(<b>没有符合搜索条件的记录</b>)</div>');
				}
			}//success回调函数结束
		});//ajax结束
	}

}

//关闭对话框
function closeMsgbox() {
	$('#tipBox').hide();
}
//生成confirm对话框
function showConfirmDiag(){
	$('#label22').html('').html('确定要删除吗?');
	$('.msgbox-ft1').html('').html('<div class="menu1" onclick="dodelete();">确&nbsp;定</div>');
	$('#tipBox').show();
}
//生成提示对话框
function showTipDiag(tipMsg){
	$('#label22').html('').html(tipMsg);
	$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox();">确&nbsp;定</div>');
	$('#tipBox').show();
}
//删除通讯录记录（各种检查和确认）
function deleteABrecord(){
	var id = $('#abAddressBookId').val();
	//主键没有
	if(id==""){
		showTipDiag("请选择一条通讯录！");
	}else{
		//确认框
		showConfirmDiag();
	}
}
//真正删除通讯录记录
function dodelete(){
	var id = $('#abAddressBookId').val();
	//ajax执行删除
	$.ajax({
		type: 'POST',
		url: 'menu_ab_delABrec.do',
		async: false,//因为要实时改变分页数据，只能同步请求
		dataType:"text",//返回数据类型为json时（controller使用@ResponseBody）必须设置返回的数据类型为json
		data:{  
			'addressBookId':id
		},
		success : function(data) {
			if(data=='success'){
				showTipDiag("删除成功！");
				//删除左侧记录
				$('ul li').remove('li[value='+id+']');
				//清空右侧数据
				clearRightAB();
				//清空全局变量
				g_data=null;
			}else{
				showTipDiag("删除失败！");
			}
		},//success回调函数结束
		error:function(){
			showTipDiag("删除失败！");
		}
	});//ajax结束
}
//清空数据
function clearRightAB(){
	//设置头像
	$('#abHeadImg').attr('src','../res/images/photo1.png');
	//姓名
	$('#abName').val('');
	//设置隐藏主键
	$('#abAddressBookId').val('');
	//清空头像路径
	$('imgPath').val('');
	//设置userId
	$('userId').val('');
	//设置公司
	$('#abCompany').val('');
	//设置部门
	$('#abDeparment').val('');		
	//设置性别
	$("#abGender").easyDropDown('select',0);
	//设置手机
	$('#abMobileNo').val('');		
	//设置固话
	$('#abFixedLineTel').val('');	
	//设置邮箱
	$('#abEmail').val('');	
	//设置职务
	$('#abTitle').val('');	
	//设置级别
	$('#abAdministrativeLevel').val('');
	//设置类型
	$("#abUserCata").easyDropDown('select',0);
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
