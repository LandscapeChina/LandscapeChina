package com.history.utils.project;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class YunZhiXunPropertiesUtil {

    protected static Logger log = Logger.getLogger(YunZhiXunPropertiesUtil.class);
    private static Properties pro = new Properties();
    static InputStream inStream =  YunZhiXunPropertiesUtil.class.getClassLoader()
            .getResourceAsStream("yunzhixun.properties");

    static{
        try {
            pro.load(inStream);
        } catch (IOException e) {
            log.error("配置文件sysParameter.properties加载出错！");
            e.printStackTrace();
        }
    }

    public static String  getProperty(String msg)  {
        return pro.getProperty(msg);
    }

}
