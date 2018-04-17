package com.hanxx.shiro.mapper;

import com.hanxx.shiro.model.User;
import org.apache.ibatis.annotations.Param;

import javax.jws.soap.SOAPBinding;

/**
 * @Author hanxx
 * @Date 2018/4/1715:57
 */
public interface UserMapper {

    User findByUsername(@Param("username") String username);
}
