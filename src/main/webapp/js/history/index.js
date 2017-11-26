var countdown = 60, timerTag;
var tel;
//验证手机格式
$("#tel").change(function () {
    tel = $(this).val();
    var regex = /^1[3|4|5|7|8][0-9]{9}$/;
    if (!regex.test(tel)) {
        $("#identcode").attr("disabled", true);
        layer.tips('手机号格式不正确', '#tel');
    } else {
        $("#identcode").removeAttr("disabled");
    }
});
//发送验证码
function setTime(obj) {
    if (countdown == 60) {
        $.post("Code.html", {tel: tel}, function (data) {
            if (data == "0") {
                layer.alert("服务错误，正在联系管理员处理，请稍后！", {title: '系统提示', icon: 0});
            }
        });
    }
    if (countdown <= 0) {
        obj.removeAttribute("disabled");
        $(obj).html("重新获取");
        countdown = 60;
        clearTimeout(timerTag);
    } else {
        obj.setAttribute("disabled", true);
        $(obj).html("重新发送(" + countdown + ")");
        countdown--;
        timerTag = setTimeout(function () {
            setTime(obj);
        }, 1000);
    }
}
$("#register").click(function () {
   var form=$("#f1").serialize();
   $.post("register.html",form,function (data) {
       alert(data);
   })
});