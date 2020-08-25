$(document).ready(function(){
	genderItem();
});

//性别设置
function genderItem(){
	var count=$("#gender option").length; 
	var index = 0;
	for(var i=0;i<count;i++){
		if( $("#gender").get(0).options[i].value==genderValue){
			index = i ;
			break;
		}
	}
	$("#gender").easyDropDown('select',index);
}


//保存个人信息
function submitUserMsg(){
	
	var $originalLoginId=$('#originalLoginId');//初始登录号
	var $loginId=$("#loginId");//登录号
	var $userName=$("#userName");//姓名
	var $phoneNum=$("#phoneNum");//手机号码
	if($.trim($loginId.val())==''){
		showDialog("请填写登录号");
		return;
	}
	if($.trim($userName.val())==''){
		showDialog("请填写姓名");
		return;
	}
	if($phoneNum.val()==''){
		showDialog("请填写手机号码");
		return;
	}
	var regu = /(86|)[1][3-8]+\d{9}/;
	var phoneNum = $phoneNum.val();
    if (!regu.test(phoneNum)||phoneNum.length!=11) {  
		showDialog("手机号码格式不对");;	
		return;
    }
    if($.trim($loginId.val())==$.trim($originalLoginId.val())){
    	updateUserInfo();
    }else{
    	
    	//判断是否检测过loginId是否可用
        var $isValidLoginId=$('#isValidLoginId');
        if($isValidLoginId.val()=='0'){
    		showDialog("请先检测登录号是否可用");;	
    		return;
        }

        //执行检测，并修改检测标识#isValidLoginId（可能存在了检测之后，又修改了登录号的情况）
    	$.ajax({
    		method:'POST',
    		url:'menu_toPage_checkLoginIdCanUse.do',
    		dataType:'text',
    		async : false,
    		data:{
    			loginId:$.trim($("#loginId").val())
    		},
    		success:function(msg){
    			if(msg=='success'){//检测可用之后更新个人信息
    				$isValidLoginId.val(1);
    				//更新个人信息
    				updateUserInfo();
    				
    			}else{
    				showDialog("登录号不可用");
    				$isValidLoginId.val(2);
    			}
    		}
    	});//ajax结束
    	
    }


}

//更新个人信息
function updateUserInfo(){
	var $loginId=$("#loginId");//登录号
	var $userName=$("#userName");//姓名
	var $phoneNum=$("#phoneNum");//手机号码
	$.ajax({
		method:'POST',
		url:'menu_toPage_updateUserMsg.do',
		async : false,
		data:{
			loginId:$.trim($loginId.val()),
			userName:$.trim($userName.val()),
			phoneNum:$.trim($phoneNum.val()),
			gender:$("#gender").val()
		},
		success:function(msg){
			if(msg){
				showDialog("修改成功");
			}else{
				showDialog("修改失败");
			}
		}
	});//ajax结束
}

//弹窗提示
function showDialog(msg) {
	$('#msgDialog').html(msg).show();
	setTimeout(function() {
		$('#msgDialog').hide().html('');
	}, 3000);
}

function resetUserMsg(){
	$("#phoneNum").val('');
}

//检测登录号是否可用
function checkLoginIdInUse(){
	//初始登录号
	var $originalLoginId=$('#originalLoginId');
	//判断是否检测过了，loginId是否可用
    var $isValidLoginId=$('#isValidLoginId');
    var $loginId=$("#loginId");//登录号
    //先判断有没有改变登录号
    if($.trim($loginId.val())==$.trim($originalLoginId.val())){
    	$isValidLoginId.val(1);
    	showDialog("登录号可用");
    	return;
    }
    if($.trim($loginId.val())==''){
		showDialog("请填写登录号");
		return;
    }
    //执行检测，并修改检测标识#isValidLoginId
	$.ajax({
		method:'POST',
		url:'menu_toPage_checkLoginIdCanUse.do',
		dataType:'text',
		async : false,
		data:{
			"loginId":$.trim($loginId.val())
		},
		success:function(msg){
			if(msg=='success'){
				showDialog("登录号可用");
				$isValidLoginId.val(1);
			}else{
				showDialog("登录号不可用");
				$isValidLoginId.val(2);
			}
		}
	});//ajax结束
}

