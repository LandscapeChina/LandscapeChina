package com.history.dao;

import com.history.bean.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {
    //注册
    public int register(User user);
    //查询手机号是否有被注册过
    public User findTel(User user);
    //登录
    public User login(User user);
}
