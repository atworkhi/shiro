package com.hanxx.shiro.controller;

import com.hanxx.shiro.model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Created by hangx on 2018/4/1720:15
 */

@Controller
public class ShiroController {

    @RequestMapping("/unauthorized")
    public String unauthorized(){
        return "403";
    }
    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/logout")
    public String logout(){
        // 取出验证后登陆的用户
        Subject subject = SecurityUtils.getSubject();
        if (subject != null){
            subject.logout();
        }
        return "login";
    }
    @RequestMapping("/admin")
    @ResponseBody
    public String admin(){
        return "admin success";
    }

    @RequestMapping("/update")
    @ResponseBody
    public String edit(){
        return "update success";
    }

    @RequestMapping("/loginuser")
    public String loginUser(@RequestParam("username") String username,
                            @RequestParam("password") String password,
                            HttpSession session){
        // 需要用账号密码转成token实例
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        System.out.println(token);
        Subject subject = SecurityUtils.getSubject();
        try {
            // 登陆
            subject.login(token);
            // 获取登陆用户
            User user = (User)subject.getPrincipal();
            session.setAttribute("user",user);
            // System.out.println(user);
            return "index";
        } catch (Exception e){
            // System.out.println("账号密码错误");
            e.printStackTrace();
            return "login";
        }
    }


}
