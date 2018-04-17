package com.hanxx.shiro.model;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author hanxx
 * @Date 2018/4/1715:51
 */
public class Role {

    private Integer id;

    private String name;

    //每个角色有多个权限
    private Set<Permission> permissions = new HashSet<>();

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    // 每个角色包含多个用户
    private Set<User> users = new HashSet<>();
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }
}
