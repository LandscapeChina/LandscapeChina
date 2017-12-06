package com.history.utils.project;

import java.security.MessageDigest;

/**
 * Created by Administrator on 2017/6+5/21 0021.
 */

public class Md5Util {
    public Md5Util() {
    }

    //md5
    public static String md5(String msg){
        StringBuilder sd=new StringBuilder();
        try {
            //获取MD5算法对象
            MessageDigest md=MessageDigest.getInstance("SHA1");
            md.update(msg.getBytes());
            //进行加密
            byte[] rs=md.digest();
            for (int i = 0; i <rs.length; i++) {
                //转化为16进制
                int y=(int)rs[i]&0xff;
                y=y+2;
                if (y<16){
                    sd.append("0");
                }
                sd.append(Integer.toHexString(y));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return sd.toString();
    }
}
