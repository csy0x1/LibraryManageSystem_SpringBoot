package com.example.vuetest.controller;

import com.example.vuetest.entity.MainUser;
import com.example.vuetest.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

@Controller
@Slf4j
public class AuthController {
    @Resource
    UserMapper userMapper;

    @CrossOrigin(origins = "*")
    @RequestMapping("/api/auth/register")
    @ResponseBody
    public int Register(HttpServletResponse resp, HttpServletRequest req,
                        @RequestParam Map<String,String> params){
        resp.setHeader("Access-Control-Allow-Origin","*");

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String username = params.get("username");

        if(!Objects.equals(username, "")){
            String password = encoder.encode(params.get("password"));
            int user = userMapper.registerUser(username,password);
            return 200;
        }
        return 400;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/api/test/authStatus")
    @ResponseBody
    public int AuthStatus(HttpServletResponse resp, HttpServletRequest req){
        SecurityContext context = SecurityContextHolder.getContext();
        if(context.getAuthentication() instanceof AnonymousAuthenticationToken){
            System.out.println("unauthed");
            return 433; //匿名认证，即未登录
        } else{
            System.out.println("authed");
            return 233;
        }
    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/api/auth/test")
    @ResponseBody
    public int TestStatus(HttpServletResponse resp, HttpServletRequest req){
        MainUser user = userMapper.getUserByUsername("admin");
        log.info("role: " + user.getNewrole());
        log.info(String.valueOf(user));
        return 200;
    }
}
