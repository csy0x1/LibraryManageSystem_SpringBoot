package com.example.vuetest.services.imple;

import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class RedisMybatisCache implements Cache {

    private final String id;
    private static RedisTemplate<Object,Object> template;

    public RedisMybatisCache(String id){
        this.id = id;
    }

    public static void setTemplate(RedisTemplate<Object, Object> template){
        RedisMybatisCache.template = template;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void putObject(Object o, Object o1) {
        template.opsForValue().set(o,o1,5, TimeUnit.MINUTES);
    }

    @Override
    public Object getObject(Object o) {
        return template.opsForValue().get(o);
    }

    @Override
    public Object removeObject(Object o) {
        return template.delete(o);
    }

    @Override
    public void clear() {
        template.execute((RedisCallback<Void>) connection -> {
            connection.flushDb();
            return null;
        });
    }

    @Override
    public int getSize() {
        return Objects.requireNonNull(template.execute(RedisServerCommands::dbSize)).intValue();
        //导入的包是springframework.data.redis下的
    }
}
