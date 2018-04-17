package com.hanxx.shiro.model;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author hanxx
 * @Date 2018/4/1715:51
 */
public class User {

    private Integer id;

    private String username;

    private String password;

    //每个用户有多个角色
    private Set<Role> roles = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
