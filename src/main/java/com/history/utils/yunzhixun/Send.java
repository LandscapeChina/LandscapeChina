package com.history.utils.yunzhixun;

import com.history.utils.yunzhixun.client.AbsRestClient;
import com.history.utils.yunzhixun.client.JsonReqClient;
import com.history.utils.yunzhixun.client.XmlReqClient;

public class Send {
    private String accountSid;
    private String authToken;

    public String getAccountSid() {
        return accountSid;
    }
    public void setAccountSid(String accountSid) {
        this.accountSid = accountSid;
    }
    public String getAuthToken() {
        return authToken;
    }
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
    static AbsRestClient InstantiationRestAPI(boolean enable) {
        if(enable) {
            return new JsonReqClient();
        } else {
            return new XmlReqClient();
        }
    }
    public String sendCode(String accountSid,String token,String appId,String templateId,String to,String para){
        return InstantiationRestAPI(true).templateSMS(accountSid, token, appId, templateId, to, para);
    }
}
