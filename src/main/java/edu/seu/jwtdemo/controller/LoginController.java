package edu.seu.jwtdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 登录url控制器
 * @author xuyitjuseu
 */
@Controller
public class LoginController {

    @RequestMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/toMain")
    public String toMain() {
        return "main";
    }
}