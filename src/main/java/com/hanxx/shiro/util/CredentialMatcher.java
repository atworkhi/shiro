package com.hanxx.shiro.util;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * 密码校验重写
 * @Author hanxx
 * @Date 2018/4/1717:01
 */
public class CredentialMatcher extends SimpleCredentialsMatcher{
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        // token的密码
        String password = new String(usernamePasswordToken.getPassword());
        // 数据库密码
        String dbpassword = (String)info.getCredentials();
        // 校验
        return this.equals(password,dbpassword);
    }
}
