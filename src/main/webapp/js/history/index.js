var countdown = 60, timerTag,time;
var tel;
var pam=/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,16}$/;
var pamCode=/^\d{4}$/;
//验证手机格式
function testTel() {
    var flag=false;
    tel = $("#tel").val();
    var regex = /^1[3|4|5|7|8][0-9]{9}$/;
    if (!regex.test(tel)) {
        $("#identcode").attr("disabled", true);
        layer.tips('手机号格式不正确！', '#tel');
    } else {
        $("#identcode").removeAttr("disabled");
        flag=true;
    }
    return flag;
}
//发送验证码
function setTime(obj) {
    if (countdown == 60) {
        $.post("Code.html", {tel: tel}, function (data) {
            var response=replaceStr(data);
            if ("FAIL"==response) {
                layer.alert("服务错误，正在联系管理员处理，请稍后！", {title: '系统提示', icon: 0});
            }else if("SUCCESS"!=response){
                layer.alert(response,{title:'系统提示',icon:0});
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
//校验昵称
function testAccount() {
    var flag=false;
    var account = $("#account").val();
    if (account.length > 6) {
        layer.tips('昵称长度不能大于6位！', '#account');
    } else if (account.indexOf(" ") != -1||account.length<=0) {
        layer.tips('昵称中不能包含空格！', '#account');
    } else {
        flag=true;
    }
    return flag;
}
//校验密码
function testPwd1() {
    var flag=false;
    var pwd1 = $("#pwd1").val();
   if(!pam.test(pwd1)){
        layer.tips('密码包含且只包含大小写字母和数字！', '#pwd1');
    }else{
        flag=true;
    }
    return flag;
}
//校验密码
function testPwd2() {
    var flag=false;
    var pwd2 = $("#pwd2").val();
   if(!pam.test(pwd2)){
        layer.tips('密码包含且只包含大小写字母和数字！', '#pwd2');
    }else{
        flag=true;
    }
    return flag;
}
//校验验证码
function testCode() {
    var flag=false;
    var code = $("#code").val();
    if(!pamCode.test(code)){
        layer.tips('验证码为4位数字！', '#code');
    }else{
        flag=true;
    }
    return flag;
}
$("#register").click(function () {
    var form = $("#f1").serialize();
    form=decodeURIComponent(form,true);
    if($("#font").html()=="注&nbsp;&nbsp;册") {
        if ($("#pwd1").val() != $("#pwd2").val()) {
            layer.alert('两次密码输入不一致！', {title: '系统提示', icon: 0});
        } else if (!(!testTel() || !testAccount() || !testPwd1() || !testPwd2() || !testCode())) {
            $.post("Register.html", form, function (d) {
                var response = replaceStr(d);
                if ("SUCCESS" == response) {
                    layer.alert("注册成功，请登录！", {title: '系统提示', icon: 1});
                } else {
                    layer.alert("验证码不正确，请重新输入！", {title: '系统提示', icon: 0});
                }
            });
        }
    }else {
        $.post("Login.html", form, function(d){
            var response = replaceStr(d);
            if ("SUCCESS" == response) {
                layer.alert("注册成功，请登录！", {title: '系统提示', icon: 1});
            } else {
                layer.alert("用户名或密码错误，请重新输入！", {title: '系统提示', icon: 0});
            }
        });
    }
});
//显示登录页面
function loginShow() {
    $("#d2").removeClass("d2");
    $("#d2").addClass("d1");
    $("#f3").html($("#f1").html());
    $("#f1").html($("#f2").html());
    $("#font").html("登&nbsp;&nbsp;&nbsp;录");
    $("#show").html('&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" id="registerShow" onclick="registerShow()">注&nbsp;&nbsp;&nbsp;册</a>&nbsp;&nbsp;&nbsp;<a href="#" onclick="searchPassword()">找回密码</a>');
}
//显示注册页面
function registerShow() {
    $("#d2").removeClass("d1");
    $("#d2").addClass("d2");
    $("#f1").html($("#f3").html());
    $("#f3").html($("#f2").html());
    $("#font").html("注&nbsp;&nbsp;&nbsp;册");
    $("#show").html('已有账号?>>><a href="#" id="loginShow" onclick="loginShow()">登&nbsp;&nbsp;&nbsp;录</已有账号a>');
}
