**apache shiro**
### 环境搭建：
spring boot 1.5x apache shiro 1.2.3
```$xslt
<!--导入apache shiro-->
		<dependency>
			<groupId>org.apache.shiro</groupId>
			<artifactId>shiro-core</artifactId>
			<version>1.2.3</version>
		</dependency>
		<!--导入shiro spring-->
		<dependency>
			<groupId>org.apache.shiro</groupId>
			<artifactId>shiro-spring</artifactId>
			<version>1.2.3</version>
		</dependency>
		<!--阿里数据库连接池-->
		<!-- https://mvnrepository.com/artifact/com.alibaba/druid -->
		<dependency>
			<groupId>com.alibaba</groupId>
			<artifactId>druid</artifactId>
			<version>1.0.29</version>
		</dependency>
		<!--工具类-->
		<!-- https://mvnrepository.com/artifact/org.apache.commons/commons-lang3 -->
		<dependency>
			<groupId>org.apache.commons</groupId>
			<artifactId>commons-lang3</artifactId>
			<version>3.4</version>
		</dependency>
		<!--spring 上下文-->
		<!-- https://mvnrepository.com/artifact/org.springframework/spring-context-support -->
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-context-support</artifactId>
			<version>4.3.16.RELEASE</version>
		</dependency>
		<!--jsp模版的处理-->
		<!-- https://mvnrepository.com/artifact/org.apache.tomcat.embed/tomcat-embed-jasper -->
		<dependency>
			<groupId>org.apache.tomcat.embed</groupId>
			<artifactId>tomcat-embed-jasper</artifactId>
		</dependency>
		<!--servlet-->
		<!-- https://mvnrepository.com/artifact/javax.servlet/javax.servlet-api -->
		<dependency>
			<groupId>javax.servlet</groupId>
			<artifactId>javax.servlet-api</artifactId>
		</dependency>
		<!--jstl表达式-->
		<!-- https://mvnrepository.com/artifact/javax.servlet/jstl -->
		<dependency>
			<groupId>javax.servlet</groupId>
			<artifactId>jstl</artifactId>
		</dependency>
```
```$xslt
# 数据库配置
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/shiro?characterEncoding=UTF-8&useSSL=true
spring.datasource.username=root
spring.datasource.password=root
```
初始化数据表：
```$xslt
-- 权限表 --
CREATE TABLE permission(
pid int(11) not null AUTO_INCREMENT,
name VARCHAR(255) not NULL DEFAULT '',
url VARCHAR(255) DEFAULT '',
PRIMARY KEY (pid)
)ENGINE = InnoDB DEFAULT charset = utf8;

INSERT  INTO permission VALUES ('1','add','');
INSERT  INTO permission VALUES ('2','update','');
INSERT  INTO permission VALUES ('3','delete','');
INSERT  INTO permission VALUES ('4','edit','');

-- 用户表 --
CREATE TABLE USER (
 uid INT(11) not NULL AUTO_INCREMENT,
 username VARCHAR (255) Not NULL DEFAULT '',
 password VARCHAR (255) NOT NULL DEFAULT '',
 PRIMARY KEY (uid)
)ENGINE = InnoDB DEFAULT charset = utf8;

INSERT INTO USER VALUES ('1','admin','admin');
INSERT INTO USER VALUES ('2','user','123456');

-- 角色表 --
CREATE TABLE role(
rid INT (11) not null AUTO_INCREMENT,
rname VARCHAR (255) not null DEFAULT '',
PRIMARY KEY (rid)
)ENGINE = InnoDB DEFAULT charset = utf8;

INSERT INTO role VALUES ('1','admin');
INSERT INTO role VALUES ('2','user');

-- 角色权限关系表 --
CREATE TABLE permission_role(
 rid int(11) not null,
 pid int(11) not null,
 KEY idx_rid(rid),
 KEY idx_pid(pid)
)ENGINE = InnoDB DEFAULT charset = utf8;

INSERT INTO permission_role VALUES ('1','1');
INSERT INTO permission_role VALUES ('1','2');
INSERT INTO permission_role VALUES ('1','3');
INSERT INTO permission_role VALUES ('1','4');
INSERT INTO permission_role VALUES ('2','1');
INSERT INTO permission_role VALUES ('2','4');

-- 用户角色关系表--
CREATE TABLE user_role(
 uid int(11) not null,
 rid int(11) not null,
 KEY idx_rid(uid),
 KEY idx_pid(rid)
)ENGINE = InnoDB DEFAULT charset = utf8;

INSERT INTO user_role VALUES ('1','1');
INSERT INTO user_role VALUES ('2','2');
```
初始化实体类
```$xslt
用户表
    private Integer id;

    private String username;

    private String password;

    //每个用户有多个角色
    private Set<Role> roles = new HashSet<>();
角色表
    private Integer id;

    private String name;

    //每个角色有多个权限
    private Set<Permission> permissions = new HashSet<>();
权限表
    private Integer id;

    private String name;

    private String url;
```
配置dao:
```$xslt
User findByUsername(@Param("username") String username);
```
配置service及实现类
```$xslt
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
```
配置mybatis的mapper.xml
```$xslt
<mapper namespace="com.hanxx.shiro.mapper.UserMapper">
    <resultMap id="usermap" type="com.hanxx.shiro.model.User">
        <id property="id" column="uid"></id>
        <result property="username" column="username" />
        <result property="password" column="password" />
        <collection property="roles" ofType="com.hanxx.shiro.model.Role">
            <id property="id" column="rid"></id>
            <result property="name" column="rname" />
            <collection property="permissions" ofType="com.hanxx.shiro.model.Permission">
                <id property="id" column="pid"></id>
                <result property="name" column="name" />
                <result property="url" column="url" />
            </collection>
        </collection>
    </resultMap>

    <select id="findByUsername" parameterType="String" resultMap="usermap">
         SELECT u.*, r.*, p.*
                FROM USER u
                  INNER JOIN user_role ur on ur.uid = u.uid
                  INNER JOIN role r ON r.rid = ur.rid
                  INNER JOIN permission_role pr ON pr.rid = r.rid
                  INNER JOIN permission p ON pr.pid = p.pid
                  WHERE u.username = #{username}
    </select>
</mapper>
```
配置mybatis的配置文件：
```$xslt
# mybatis
mybatis.mapper-locations= mapper/*.xml
mybatis.type-aliases-package=com.hanxx.shiro.model;
```
配置启动项：
```$xslt
@SpringBootApplication
// 扫描mapper
@MapperScan(basePackages = {"com.hanxx.shiro.mapper"})
// 扫描注解
@ComponentScan
```

Authrealms的授权管理与认证登陆：
```$xslt
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
```
自定义密码校验：
```$xslt
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
```
shiro的配置类： (自定义校验：CredentialMatcher--->AuthRealm--->SecurityManager---->ShiroFilterFactoryBean)
ShiroFilterFactoryBean 中有权限验证 setFilterChainDefinitionMap

```$xslt
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
        filter.put("/index","authc");   //需要登陆
        filter.put("/login","anon");    //不做验证
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
```
创建controller：
```
@RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/loginuser")
    public String loginUser(@RequestParam("username") String username,
                            @RequestParam("password") String password,
                            HttpSession session){
        // 需要用账号密码转成token实例
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            // 登陆
            subject.login(token);
            // 获取登陆用户
            User user = (User)subject.getPrincipal();
            session.setAttribute("user",user);
            return "index";
        } catch (Exception e){
            return "login";
        }
    }
```
创建jsp配置与jsp页面：
```
# jsp
spring.mvc.view.prefix=/pages/
spring.mvc.view.suffix=.jsp

<form action="/loginuser" method="post">
    <input type="text" name="username" />
    <input type="password" name="password" />
    <input type="submit" value="提交" />
</form>
```

登陆验证已完成：

权限配置：
```
权限配置文件中增加啊登陆界面都可以访问 与其他界面需要登陆访问
filter.put("/loginuser","anon");    //都可以访问
filter.put("/**","user");    //登陆就可以访问
```
不同角色访问不同接口：
```
filter.put("/admin","roles[admin]");    //admin角色才能访问
配置AuthRealm: 把角色添加进去
    // 授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 遍历
        User user = (User) principalCollection.fromRealm(this.getClass().getName()).iterator().next();
        List<String> plist = new ArrayList<>();  //权限
        List<String> rlist = new ArrayList<>(); //角色
        Set<Role> roleSet = user.getRoles();
        //如果觉得不为空则遍历
        if(CollectionUtils.isEmpty(roleSet)){
            for (Role r :roleSet){
                rlist.add(r.getName()); //把觉得名放进去
                Set<Permission> permissionSet = r.getPermissions();
                //如果权限不为空遍历权限
                if(!CollectionUtils.isEmpty(permissionSet)){
                    for (Permission p : permissionSet){
                        //获取权限并添加
                        plist.add(p.getName());
                    }
                }
            }
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermissions(plist);
        info.addRoles(rlist);
        return info;
    }
```

```

```