package com.example.vuetest.configuration;

import com.example.vuetest.filter.JWTAuthenticationFilter;
import com.example.vuetest.handler.onAuthSuccess;
import com.example.vuetest.services.imple.RedisMybatisCache;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Configuration
public class MainConfiguration {

    @Resource
    RedisTemplate<Object,Object> template;

    @PostConstruct
    public void init(){
        RedisMybatisCache.setTemplate(template);
        onAuthSuccess.setTemplate(template);    //设置redis
        JWTAuthenticationFilter.setTemplate(template);
    }
}
