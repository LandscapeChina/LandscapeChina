package com.history.service;

import com.alibaba.fastjson.JSON;
import com.history.bean.User;
import com.history.dao.UserDao;
import com.history.utils.project.ConstansUtil;
import com.history.utils.project.Md5Util;
import com.history.utils.project.RedisUtil;
import com.history.utils.project.YunZhiXunPropertiesUtil;
import com.history.utils.yunzhixun.Send;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class UserService {
   private Logger log=Logger.getLogger(UserService.class);
   @Resource
    private UserDao userDao;
    //发送验证码
    public String getCode(User user){
        //查询该电话号码是否被注册
        User user1= userDao.findTel(user);
        if (user1!=null){
            return "该手机号已被注册！";
        }
        Send send=new Send();
        String accountSid= YunZhiXunPropertiesUtil.getProperty("accountSid");
        String token= YunZhiXunPropertiesUtil.getProperty("token");
        String appId= YunZhiXunPropertiesUtil.getProperty("appId");
        String templateId= YunZhiXunPropertiesUtil.getProperty("templateId");
        String para=String.valueOf((int)((Math.random()*9+1)*1000));
        try {
            RedisUtil.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获取今日发送验证码的次数
        List<String> list= RedisUtil.jedis.hmget(user.getTel(),"num","code");
        //获取次数
        String number= list.get(0);
        String resp=null;
        if (number!=null){
            int num=Integer.parseInt(number);
           if (num<5){
               String response=send.sendCode(accountSid, token, appId, templateId, user.getTel(), para);
                resp=(String) JSON.parseObject(response).getJSONObject("resp").get("respCode");
           }else{
               return "一个手机号一天只能发送五次哦！";
           }
        }
        if (number==null){
            String response=send.sendCode(accountSid, token, appId, templateId, user.getTel(), para);
            resp=(String) JSON.parseObject(response).getJSONObject("resp").get("respCode");
        }
        if (resp.equals(ConstansUtil.SUCCESS_CODE)){
            log.info("发送验证码手机号为："+user.getTel()+",验证码为："+para);
            Map<String,String> map=new HashMap<>(2);
            map.put("code",para);
            if (number!=null){
                int num=Integer.parseInt(number);
                num++;
                map.put("num",number);
                RedisUtil.jedis.hmset(user.getTel(),map);
            }else {
                map.put("num",String.valueOf(1));
                RedisUtil.jedis.hmset(user.getTel(),map);
            }
            return ConstansUtil.SUCCESS;
        }else{
            return ConstansUtil.FAIL;
        }
    }
    //注册
    @Transactional(rollbackFor=Exception.class)
    public String register(User user){
        //查询该电话号码是否被注册
        User user1= userDao.findTel(user);
        if (user1!=null){
            return "该手机号已被注册！";
        }
        try {
            RedisUtil.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> list= RedisUtil.jedis.hmget(user.getTel(),"num","code");
        if (user.getCode()!=null||list.get(1)!=null){
            if (user.getCode().equals(list.get(1))){
                //MD5加密
                user.setPassword(Md5Util.md5(user.getPassword()));
                if(userDao.register(user)==1){
                    return ConstansUtil.SUCCESS;
                }
            }
        }
        return ConstansUtil.FAIL;
    }
    //登录
    public String login(User user){
       boolean flag=Pattern.matches(ConstansUtil.TELEPHONE_VALIDATE,user.getTel());
       if (!flag){
           user.setAccount(user.getTel());
           user.setTel(null);
       }
       user.setPassword(Md5Util.md5(user.getPassword()));
       User user1=userDao.login(user);
       if (userDao.login(user)!=null){
           return ConstansUtil.SUCCESS;
       }else {
           return ConstansUtil.FAIL;
       }
    }
}
