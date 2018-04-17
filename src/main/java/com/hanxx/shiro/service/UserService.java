package com.hanxx.shiro.service;

import com.hanxx.shiro.model.User;

/**
 * @Author hanxx
 * @Date 2018/4/1716:00
 */
public interface UserService {

    User findByUsername(String username);
}
