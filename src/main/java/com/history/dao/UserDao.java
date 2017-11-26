package com.history.dao;

import com.history.bean.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {
    //注册
    public int register(User user);
}
