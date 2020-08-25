$(function(){
	//如果全局对象存在，不是null
	if(g_ptlAddressBook){
		//初始化下拉框（性别）
		var tmpGender = g_ptlAddressBook.gender;
		if(tmpGender){
			if(tmpGender=='未知'){
				$("#abGender").easyDropDown('select',0);
			}else if(tmpGender=='男'){
				$("#abGender").easyDropDown('select',1);
			}else{
				$("#abGender").easyDropDown('select',2);
			}
		}
		var tmpUserCata = g_ptlAddressBook.userCata;
		//初始化下拉框（用户类型）
		if(tmpUserCata){
			if(tmpUserCata=='系统用户'){
				$("#abUserCata").easyDropDown('select',0);
			}else{
				$("#abUserCata").easyDropDown('select',1);
			}
		}
		var tmpImgPath = g_ptlAddressBook.headImg;
		//初始化头像
		if(tmpImgPath){
			$('#abHeadImg').attr('src','menu_ab_getHeadImg.do?imgPath='+tmpImgPath+'&timestamp='+Math.random());
			$('#rChangeHeadBtn').text("修改头像");
		}
	}
	
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
				'deparment':$.trim($('#abDeparment').val()),
				'email':email,
				'gender':$("#abGender").val(),
				'company':$.trim($('#abCompany').val()),
				'mobileNo':mobileNo,
				'fixedLineTel':fixedLineTel,
				'userCata':$('#abUserCata').val(),
				'headImg':$('#imgPath').val(),
				'userId':$('#userId').val()
			},
			success : function(data) {
				if(data=='success'){
					showTipDiag('保存成功！');
				}else{
					showTipDiag('保存失败！');
				}
			},//success回调函数结束
			error:function(XMLHttpRequest, textStatus, errorThrown){
				showTipDiag('保存失败！');
			}
		});//ajax结束
	});
	
	//重置按钮点击事件
	$('#btnReset').click(function(){
		//g_ptlAddressBook不为null
		if(g_ptlAddressBook){
			//头像
			if(g_ptlAddressBook.headImg&&$.trim(g_ptlAddressBook.headImg)!=''){
				$('#abHeadImg').attr('src','menu_ab_getHeadImg.do?imgPath='+g_ptlAddressBook.headImg+'&timestamp='+Math.random());
				$('imgPath').val(g_ptlAddressBook.headImg);
			}else{//默认头像
				$('#abHeadImg').attr('src','../res/images/photo1.png');
				$('imgPath').val();
			}
			//姓名
			$('#abName').val(g_ptlAddressBook.name);
			//把通讯录主键放在隐藏域
			$('#abAddressBookId').val(g_ptlAddressBook.addressBookId);
			//设置userId
			$('#userId').val(g_ptlAddressBook.userId);
			//设置公司
			if(g_ptlAddressBook.company){
				$('#abCompany').val(g_ptlAddressBook.company);
			}else{
				$('#abCompany').val('');
			}
			//设置部门
			$('#abDeparment').val(g_ptlAddressBook.deparment);		
			//设置性别（easyDropDown下拉框选中要根据index）
			var tmpGender = g_ptlAddressBook.gender;
			if(tmpGender=='未知'){
				$('#abGender').easyDropDown('select',0);
			}else if(tmpGender=='男'){
				$('#abGender').easyDropDown('select',1);
			}else{
				$('#abGender').easyDropDown('select',2);
			}
			//设置手机
			$('#abMobileNo').val(g_ptlAddressBook.mobileNo);		
			//设置固话
			$('#abFixedLineTel').val(g_ptlAddressBook.fixedLineTel);	
			//设置邮箱
			$('#abEmail').val(g_ptlAddressBook.email);	
			//设置职务
			$('#abTitle').val(g_ptlAddressBook.title);	
			//设置级别
			$('#abAdministrativeLevel').val(g_ptlAddressBook.administrativeLevel);
			//设置类型
			var  tmpUserCata = g_ptlAddressBook.userCata;
			if(tmpUserCata=='系统用户'){
				$('#abUserCata').easyDropDown('select',0);
			}else{
				$('#abUserCata').easyDropDown('select',1);
			}
		}
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
            		showTipDiag('请上传图片文件，提示参见头像下方！');
            	}else{
            		var fileName = file.fileName;
            		//用上传后的图片地址显示头像，并在隐藏区域赋值（为了保存）
            		$('#abHeadImg').attr('src','menu_ab_getHeadImg.do?imgPath='+fileName+'&timestamp='+Math.random());
            		$('#imgPath').val(fileName);
            	}
            });
        }
	 });//头像上传结束
	
});


//关闭对话框
function closeMsgbox() {
	$('#tipBox').hide();
}
//生成提示对话框
function showTipDiag(tipMsg){
	$('#label22').html('').html(tipMsg);
	$('.msgbox-ft1').html('').html('<div class="menu1" onclick="closeMsgbox();">确&nbsp;定</div>');
	$('#tipBox').show();
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