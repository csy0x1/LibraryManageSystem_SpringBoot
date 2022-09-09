package com.example.vuetest.handler;

import com.example.vuetest.utils.Jwtutil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class onAuthSuccess implements AuthenticationSuccessHandler {

    private static RedisTemplate<Object,Object> template;   //通过setTemplate，从MainConfiguration设置redis

    public static void setTemplate(RedisTemplate<Object,Object> template){
        onAuthSuccess.template = template;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setStatus(233);
        httpServletResponse.getWriter().write("Login success");
        SecurityContext context = SecurityContextHolder.getContext();
//        UsernamePasswordAuthenticationToken token =
//                new UsernamePasswordAuthenticationToken(
//                        authentication.getPrincipal(),
//                        authentication.getCredentials(),
//                        AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_user")
//                        );
//        context.setAuthentication(token);
        Authentication auth = context.getAuthentication();
        User user = (User) auth.getPrincipal();
        String token = Jwtutil.generateToken(user.getUsername());
        template.opsForValue().set(user.getUsername(),token);
        System.out.println(user);
        System.out.println(token);
        System.out.println(Jwtutil.verifyJwt(token).get("username"));
        httpServletResponse.setHeader("Authentication",token);   //给header新增token
    }
}
