package com.hanxx.shiro.service.impl;

import com.hanxx.shiro.mapper.UserMapper;
import com.hanxx.shiro.model.User;
import com.hanxx.shiro.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author hanxx
 * @Date 2018/4/1716:01
 */
@Service
public class UserServiceImpl implements UserService{
    // 引入usermapper
    @Resource
    private UserMapper userMapper;
    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }
}
