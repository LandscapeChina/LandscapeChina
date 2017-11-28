package com.history.service;

import com.alibaba.fastjson.JSON;
import com.history.bean.User;
import com.history.dao.UserDao;
import com.history.utils.project.ConstansUtil;
import com.history.utils.project.RedisUtil;
import com.history.utils.project.YunZhiXunPropertiesUtil;
import com.history.utils.yunzhixun.Send;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class UserService {
   private Logger log=Logger.getLogger(UserService.class);
   private  String para=null;
   @Resource
    private UserDao userDao;
    //发送验证码
    public String getCode(User user){
        Send send=new Send();
        String accountSid= YunZhiXunPropertiesUtil.getProperty("accountSid");
        String token= YunZhiXunPropertiesUtil.getProperty("token");
        String appId= YunZhiXunPropertiesUtil.getProperty("appId");
        String templateId= YunZhiXunPropertiesUtil.getProperty("templateId");
        para=String.valueOf((int)((Math.random()*9+1)*100000));
        try {
            RedisUtil.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获取今日发送验证码的次数
        String number=null;
        String resp=null;
        if (number!=null){
                number = RedisUtil.jedis.get(user.getTel());
            int num=Integer.parseInt(number);
           if (num<5){
               String response=send.sendCode(accountSid, token, appId, templateId, user.getTel(), para);
                resp=(String) JSON.parseObject(response).getJSONObject("resp").get("respCode");
           }
        }
        if (number==null){
            String response=send.sendCode(accountSid, token, appId, templateId, user.getTel(), para);
            resp=(String) JSON.parseObject(response).getJSONObject("resp").get("respCode");
        }
        if (resp.equals(ConstansUtil.SUCCESS_CODE)){
            log.info("发送验证码手机号为："+user.getTel()+",验证码为："+para);
            if (number!=null){
                int num=Integer.parseInt(number);
                num++;
                RedisUtil.jedis.set(user.getTel(),String.valueOf(num));
            }else {
                RedisUtil.jedis.set(user.getTel(),String.valueOf(1));
            }
            return ConstansUtil.SUCCESS;
        }else{
            return ConstansUtil.FAIL;
        }
    }
    //注册
    @Transactional(rollbackFor=Exception.class)
    public String register(User user){
        if (user.getCode()!=null||para!=null){
            if (user.getCode().equals(para)){
                if(userDao.register(user)==1){
                    return ConstansUtil.SUCCESS;
                }
            }
        }
        return ConstansUtil.FAIL;

    }
}
