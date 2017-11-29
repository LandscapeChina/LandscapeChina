package com.history.action;

import com.history.bean.User;
import com.history.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class UserController {
    @Resource
    private UserService userService;
    //获取验证码
    @RequestMapping("Code.html")
    @ResponseBody
  public String getCode(User user){
       String code=userService.getCode(user);
       return  code;
  }
  @RequestMapping("register.html")
  @ResponseBody
  public String register(User user){
       return userService.register(user);
  }
}
