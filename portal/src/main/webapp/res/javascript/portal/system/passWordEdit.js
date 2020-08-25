var editPwd=(function(){
	
	var pwdFlag = true;
	$('#submitKey').on('click',function(){submitKeyMsg();});
	$('#resetKey').on('click',function(){resetKeyMsg();});
	$('#closePasDialog').on('click',function(){closeDiv();});
	$('#keyOld1').on('focus',function(){clearPass('keyOld1','keyOld');});
	$('#keyNew1').on('focus',function(){clearPass('keyNew1','keyNew');});
	$('#keyAgain1').on('focus',function(){clearPass('keyAgain1','keyAgain');});
	
//	旧密码显示强弱
//	$('#keyOld1').keyup(function(event) {
//		var fromPwd = document.getElementById("keyOld1"); 
//		var keycode=event.keyCode;
//		//回车
//		if(keycode == 13) {
//			$("#keyNew1").focus();
//		//退格
//		}else if(keycode != 8){
//			changKeyLevel(fromPwd.value);
//		}
//	});
	
	$('#keyNew1').keyup(function(event) {
		var fromPwd = document.getElementById("keyNew1"); 
		var keycode=event.keyCode;
		//回车
		if(keycode == 13) {
			$("#keyAgain1").focus();
		//退格
		}else if(keycode != 8){
			if(fromPwd.value.length>0){
				changKeyLevel(fromPwd.value);
			}
		}
	});
	
	var submitKeyMsg=function(){
			
			if(!pwdFlag) {
				return;
			}
		   	var keyOld = $('#keyOld1').val();
		   	var keyNew = $('#keyNew1').val();
		 	var keyAgain = $('#keyAgain1').val();
			if((keyOld.length == 0) ||(keyNew.length == 0) ||(keyAgain.length == 0)) {
					showError();
					$('#warn_tip').addClass('red').html('密码不能为空！');
					$('#keyOld1').focus();
					return;
				}
			if(keyNew.length <8 ) {
					showError();
					$('#warn_tip').addClass('red').html('密码长度至少为8位！');
					$('#keyNew1').focus();
					return;
			}
			if(keyNew != keyAgain) {
				showError();
				$('#warn_tip').addClass('red').html('新密码两次输入不一致！');
				$('#keyAgain1').focus();
				return;
			}
		    /*var regu = /[0-9]+/;  
		    var regu2 = /[a-zA-Z]+/;
		    if (!regu.test(keyNew) || !regu2.test(keyNew)) { 
		    	showError();
				$('#warn_tip').addClass('red').html('密码必须包含字母和数字！');
				$('#keyNew1').focus();
				return;
		    }*/
			var regUpper = /[A-Z]/;
		     var regLower = /[a-z]/;
		     var regStr = /[0-9]/;
		     var pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？%+\\-\\_]");
		     var complex = 0;
		     if (pattern.test(keyNew)) {
		     	++complex;
		     }
		     if (regLower.test(keyNew)) {
		         ++complex;
		     }
		     if (regUpper.test(keyNew)) {
		         ++complex;
		     }
		     if (regStr.test(keyNew)) {
		         ++complex;
		     }
		     if (complex < 3) {
		    	 layer.alert('新密码包含至少3种大写字母+小写字母+数字+特殊符号的组合!', {
					  skin: 'layui-layer-orange' //样式类名
					  ,btn:'确定',title:'提示'
					});
		     	return ;
		     }
			//密码加盐加密并把原来值替换，提交加密参数以提供安全
			keyOld = md5(loginId+""+keyOld);
	 		keyNew = md5(loginId+""+keyNew);
	 		keyAgain = md5(loginId+""+keyAgain);
	 		$.ajax( {
					type:'POST',
					url:"personMsg/menu_toPage_updatePwd.do",
					data:{
						oldPassword:keyOld,
						newPassword:keyNew
					},
					success:function(msgMap) {
						if(msgMap) {
							
							$('#warn_area').show();
							$('#pwd_error').hide();
							$('#pwd_right').show();
							$('#warn_tip').addClass('green').html('密码修改成功');
							setTimeout(function() {
									closeDiv();
								}, 1000);
							
						} else {
							resetKeyMsg();
							showError();
							if($('#warn_tip').hasClass('green')){
								$('#warn_tip').removeClass('green')
							}
							$('#warn_tip').addClass('red').html('密码修改失败');
							$('#keyOld1').focus();
						}
					}
				});
	}
	
	//清空密码框
	var clearPass=function(fromPwd,toPwd){
	    $("#"+fromPwd).val("");
	    $("#"+toPwd).val(""); 
	}
	
	
	var clearPassNew=function(fromPwd,toPwd){
	    $("#keyNew1").val('');
	    $("#pwdHeight").removeClass("pwd_more pwd_weak pwd_strong");
	}
	
	/*关闭*/ 
	 var closeDiv=function() { 
	//	$(".btn").removeClass("btn_sp");
		$("#warn_tip").html('');
		$('#keyOld1').val('');
		$('#keyNew1').val('');
		$('#keyAgain1').val('');
		$("#pwdHeight").removeClass("pwd_more pwd_weak pwd_strong");
		var bg = document.getElementById("mask"); 
		var con = document.getElementById("popAddKey"); 
		bg.style.display = "none"; 
		con.style.display = "none"; 
	};
	
	//重置
	var resetKeyMsg=function(){
		$('#warn_tip').html('');
		$('#keyOld1').val('');
		$('#keyNew1').val('');
		$('#keyAgain1').val('');
		$("#pwdHeight").removeClass("pwd_more pwd_weak pwd_strong");
		$('#pwd_error').html('');
		
	}
	
	//修改密码弹窗
	 var showDivKey=function() { 
	 	var bg = document.getElementById("mask"); 
	 	var con = document.getElementById("popAddKey"); 
	 	var w = document.body.scrollWidth; //网页正文全文宽 
	 	var h = document.body.scrollHeight; //网页正文全文高 
	 	$('#warn_area').hide();
	 	bg.style.display = "block"; 
	 	bg.style.width = w + "px"; 
	 	bg.style.height = h + "px"; 
	 	con.style.display = "block"; 
	 };
	 
	 //计算密码强度
	 var changKeyLevel=function(keyValue){
	 	$("#pwdHeight").removeClass("pwd_weak pwd_more pwd_strong");
	 		//0、1=表示弱2表示中3表示强
	 		var keyMode = 0;
	 		//包含数字
	 		var num = 0;
	 		//包含小写字母
	 		var schar = 0;
	 		//包含大写字母
	 		var lchar = 0;
	 		//其他字符
	 		var specialChar=0;
	 		
	 		//局部变量存放，密码串中的单个字符（多次使用，需要缓存）
	 		var charCode;
	 		//检查密码的每个字符
	 		for(var i=0; i<keyValue.length;i++){
	 			charCode = keyValue.charCodeAt(i);
	 			//判断包含数字
	 			if(num==0){
		 			if(charCode>=48&&charCode<=57){
		 				num=1;
		 			}
	 			}
	 			//判断包含小写字母
	 			if(schar==0){
		 			if(charCode>=65&&charCode<=90){
		 				schar=1;
		 			}
	 			}
	 			//判断大写字母
	 			if(lchar==0){
		 			if(charCode>=97&&charCode<=122){
		 				lchar=1;
		 			}
	 			}
	 			
	 			//判断特殊字符
	 			if(specialChar==0){
		 			if(charCode<48||(charCode>57&&charCode<65)||(charCode>90&&charCode<97)||charCode>122){
		 				specialChar=1;
		 			}
	 			} 			
	 			//全部为1跳出循环
	 			if(num==1&&schar==1&&lchar==1&&specialChar==1){
	 				break;
	 			}
	 		}
	 		keyMode = num+schar+lchar+specialChar;
			if(keyValue.length<8 || keyMode==0 || keyMode==1){
				$("#pwdHeight").addClass("pwd_weak");
			}
			if(keyValue.length>=8 && keyMode==2){
				$("#pwdHeight").addClass("pwd_more");
			}
			if(keyValue.length>=8 && keyMode>=3){
				$("#pwdHeight").addClass("pwd_strong");
			}
	 }
	 
	var showError=function(){
			    $('#pwd_error').show();
				$('#pwd_right').hide();
		    	$('#warn_area').show();
		}
	return {
		showDivKey:showDivKey
	}	
})();