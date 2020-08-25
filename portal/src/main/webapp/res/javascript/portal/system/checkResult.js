var userCheckInfo=(function(){
	var checkRegisterId=null;
	var checkUserName=null;
	var checkUserId=null;
	var checkLoginId=null;
	var checkMobilePhone=null;
//	var checkCreateTime=null;
	var checkGender=null;
	var checkPassword=null;
	//添加审核窗弹窗
	var showCheckResult=function(registerId1,userName1,userId1,loginId1,mobilePhone1,gender1,password1) {
	 	checkRegisterId=registerId1;
	 	checkUserName=userName1;
	 	checkUserId=userId1;
	 	checkLoginId=loginId1;
	 	checkMobilePhone=mobilePhone1;
//	 	checkCreateTime=createTime1; 
	 	checkGender=gender1;
	 	checkPassword=password1;
	 	window.document .getElementById ('checkInfoTitleSpan').innerHTML='信息审核结果' ;
	 	var bg = document.getElementById("checkUserAddMask"); 
	 	var con = document.getElementById("checkAddUserOrg"); 
	 	var w = document.body.scrollWidth; //网页正文全文宽 
	 	var h = document.body.scrollHeight; //网页正文全文高 
	 	bg.style.display = "block"; 
	 	bg.style.width = w + "px"; 
	 	bg.style.height = h + "px"; 
	 	con.style.display = "block"; 
	 }
	/*关闭*/ 
	var closeCheckResult=function() { 
		checkRegisterId=null;
		checkUserName=null;
		checkUserId=null;
		checkLoginId=null;
		checkMobilePhone=null;
//		checkCreateTime=null;
		checkGender=null;
		checkPassword=null;
	 	$("#tongGuo").removeAttr("checked");
		$("#buTongGuo").removeAttr("checked");
	 	$('#checkOpinion').val('');
	 	var bg = document.getElementById("checkUserAddMask"); 
		var con = document.getElementById("checkAddUserOrg"); 
		bg.style.display = "none"; 
		con.style.display = "none"; 
	}
	//审核通过or不通过，修改保存相应信息
	var checkResult=function(){
		var checkIfTongguo=$('#checkWrap input[name="checkIfTongguo"]:checked ').val();
		alert(checkIfTongguo);
		var checkOpinion=$('#checkOpinion').val();
		$.ajax( {
				type:'POST',
				url:ctx+"/accountApplication/checkResult.do",
				data:{
					'checkIfTongguo':checkIfTongguo,
					'registerId':checkRegisterId,
					'userName':checkUserName,
					'userId':checkUserId,
					'loginId':checkLoginId,
					'mobilePhone':checkMobilePhone,
					'gender':checkGender,
					'password':checkPassword,
					'comment':checkOpinion
				},
				success:function(msg) {
					if(msg=='success') {
						showDialogForUserOrg('操作成功！');
						window.frames['mainFrame'].qryAccCheck(1,10);
					} else {
						showDialogForUserOrg2('操作失败！');
					}
				}
			});
	}
	var showDialogForUserOrg=function(msg) {
		$('#checkMmsgDialogForUserOrg').html(msg).show();
		setTimeout(function() {
			$('#checkMmsgDialogForUserOrg').html(msg).hide();
			closeCheckResult();
		}, 2000);	
	}
	var showDialogForUserOrg2=function(msg) {
		$('#checkMmsgDialogForUserOrg').html(msg).show();
		setTimeout(function() {
			$('#checkMmsgDialogForUserOrg').html(msg).hide();
		}, 2000);	
	}
	return {
		showCheckResult:showCheckResult,
		checkResult:checkResult,
		closeCheckResult:closeCheckResult
	}
})();