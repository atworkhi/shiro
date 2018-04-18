package com.hanxx.shiro.config;

import com.hanxx.shiro.realms.AuthRealm;
import com.hanxx.shiro.util.CredentialMatcher;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

/**
 * 配置规则类
 * @Author hanxx
 * @Date 2018/4/1717:05
 */
@Configuration
public class ShiroConfigration {

    // ShiroFilterFactoryBean 管理 SecurityManager
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager manager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(manager);

        bean.setLoginUrl("/login");
        bean.setSuccessUrl("/index");
        bean.setUnauthorizedUrl("/unauthorized");
        // 权限配置 DefaultFilter.class
        LinkedHashMap<String,String> filter = new LinkedHashMap<>();
        filter.put("/index","authc");   // 需要登陆
        filter.put("/login","anon");    // 不做验证user
        filter.put("/loginuser","anon");    // 都可以访问
        filter.put("/admin","roles[admin]");    // admin角色才能访问
        filter.put("/update","perms[update]");    // 需要有update权限才能访问
        filter.put("/druid/**","anon");    // 配置druid/*路径都不允许访问，增加对数据库监控
        filter.put("/**","user");    // 登陆就可以访问
        bean.setFilterChainDefinitionMap(filter);
        return bean;
    }


    // 使用 SecurityManager 使用 authRealm
    @Bean("securityManager")
    public SecurityManager securityManager(@Qualifier("authRealm") AuthRealm authRealm){

        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();

        manager.setRealm(authRealm);

        return manager;
    }

    // 使用上下文 AuthRealm 实现 CredentialMatcher
    @Bean("authRealm")
    public AuthRealm authRealm(@Qualifier("credentialMatcher") CredentialMatcher matcher){
        AuthRealm authRealm = new AuthRealm();
        // 使用缓存 缓存的内存
        authRealm.setCacheManager(new MemoryConstrainedCacheManager());
        authRealm.setCredentialsMatcher(matcher);
        return authRealm;
    }
    // 使用自定义校验规则
    @Bean("credentialMatcher")
    public CredentialMatcher credentialMatcher(){
        return new CredentialMatcher();
    }

    // shiro 与 spring
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager manager){
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(manager);
        return advisor;
    }
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator proxyCreator =new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }
}
