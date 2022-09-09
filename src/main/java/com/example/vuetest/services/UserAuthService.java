package com.example.vuetest.services;

import com.example.vuetest.entity.MainUser;
import com.example.vuetest.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserAuthService implements UserDetailsService {
    @Resource
    UserMapper userMapper;

    //private final StringRedisTemplate stringRedisTemplate;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username is: "+username);
        MainUser user = userMapper.getUserByUsername(username);
        System.out.println(user);
        System.out.println(encoder.encode(user.getPassword()));
        if(user==null){
            throw new RuntimeException("Error No this User");
        }
        UserDetails userDetails = User.withUsername(user.getUsername()).password(user.getPassword()).roles(user.getNewrole().getRoleName()).build();
        return userDetails;
        //需要配置权限，.roles(..)不能省
    }

    public static Boolean getLoginStatus(SecurityContext context){
        Authentication auth = context.getAuthentication();
        return auth instanceof AnonymousAuthenticationToken;
    }



}
