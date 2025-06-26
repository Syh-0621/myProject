package com.example.demo.controller;

import com.alibaba.fastjson2.JSON;
import com.example.demo.entity.Account;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Random;

@Slf4j
@Controller
public class UserController {

    @Autowired
    JavaMailSender sender;

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/code")
    @ResponseBody
    public String code(HttpSession httpSession, @RequestParam("email") String email) {
        if (email == null) {
            return "发送失败！";
        }
        log.info("邮箱：{}", email);
        Random random = new Random();
        int code = random.nextInt(9000) + 1000;
        httpSession.setAttribute("code", code);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("3021560264@qq.com");
        message.setTo(email);
        message.setSubject("验证码");
        message.setText("您的验证码是：" + code);
        sender.send(message);
        return "验证码发送成功！有效时间1分钟！";
    }

    @PostMapping("/expire")
    public void expire(HttpSession httpSession) {
        httpSession.removeAttribute("code");
    }

    @PostMapping("/register")
    @ResponseBody
    public String register(Account user,
                           @RequestParam("code") String code,
                           HttpSession httpSession,
                           MultipartFile[] profilePicture1) {
        Integer realCode = (Integer) httpSession.getAttribute("code");
        System.out.println(code);
        System.out.println(user);
        if (realCode != null && realCode == Integer.parseInt(code)) {
            if (userService.register(user, profilePicture1) > 0) {
                return "register access";
            }
            else {
                return "register failed";
            }
        }
        if (realCode == null) {
            return "code expired";
        }
        if (!realCode.equals(Integer.parseInt(code))) {
            return "code error";
        }
        return "register failed";
    }

    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {
        model.addAttribute("user", userService.SelectUserByUsername(authentication.getName()));
        model.addAttribute("products", productService.showMyProduct(authentication.getName()));
        return "home";
    }

    @PostMapping("/getUser")
    @ResponseBody
    public String getUser(String username) {
        return JSON.toJSONString(userService.SelectUserByUsername(username));
    }

    @GetMapping("/editUser")
    public String editUser(Authentication authentication, Model model) {
        model.addAttribute("user", userService.SelectUserByUsername(authentication.getName()));
        return "edituser";
    }

    @PostMapping("/editUser")
    @ResponseBody
    public String editUser(Account user, MultipartFile[] profilePicture1) {
        System.out.println(user);
        if (userService.editUser(user, profilePicture1) > 0) {
            return "success";
        }
        return "failed";
    }

    @GetMapping("/user/{username}")
    public String user(@PathVariable("username") String username, Model model, Authentication authentication) {
        if (Objects.equals(authentication.getName(), username)){
            return "redirect:/home";
        }
        model.addAttribute("user", userService.SelectUserByUsername(username));
        model.addAttribute("products", productService.showMyProduct(username));
        return "userinfo";
    }
}
