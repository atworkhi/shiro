package com.hanxx.shiro.realms;

import com.hanxx.shiro.model.Permission;
import com.hanxx.shiro.model.Role;
import com.hanxx.shiro.model.User;
import com.hanxx.shiro.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 授权管理
 * @Author hanxx
 * @Date 2018/4/1716:46
 */
public class AuthRealm extends AuthorizingRealm{

    @Autowired
    private UserService userService;
    // 授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 遍历
        User user = (User) principalCollection.fromRealm(this.getClass().getName()).iterator().next();
        List<String> list = new ArrayList<>();
        Set<Role> roleSet = user.getRoles();
        //如果觉得不为空则遍历
        if(!CollectionUtils.isEmpty(roleSet)){
            for (Role r :roleSet){
                Set<Permission> permissionSet = r.getPermissions();
                //如果权限不为空遍历权限
                if(!CollectionUtils.isEmpty(permissionSet)){
                    for (Permission p : permissionSet){
                        //获取权限并添加
                        list.add(p.getName());
                    }
                }
            }
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermissions(list);
        return info;
    }

    // 认证登陆
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = usernamePasswordToken.getUsername();
        User user = userService.findByUsername(username);
        return new SimpleAuthenticationInfo(user,user.getPassword(),this.getClass().getName());

    }
}
