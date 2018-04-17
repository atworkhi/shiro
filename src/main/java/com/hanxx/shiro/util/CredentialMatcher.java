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
        // System.out.println("pass: " +password);
        // 数据库密码
        String dbpassword = (String)info.getCredentials();
        // System.out.println("db: "+dbpassword);
        // 校验
        return this.equals(password,dbpassword);
    }
}
