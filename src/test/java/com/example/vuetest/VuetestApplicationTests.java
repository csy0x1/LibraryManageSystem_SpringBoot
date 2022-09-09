package com.example.vuetest;

import com.example.vuetest.mapper.BookMapper;
import com.example.vuetest.mapper.UserMapper;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.Objects;

@SpringBootTest
class VuetestApplicationTests {

    @Resource
    UserMapper userMapper;

    @Resource
    BookMapper bookMapper;

    @Autowired
    StringRedisTemplate template;

    @Test
    void contextLoads() {
        System.out.println(bookMapper.getBorrower(6));
    }

    @Test
    void RandomUsers(){
        for(int i=0;i<=20;i++){
            System.out.println(userMapper.registerUser("TEST-" + new RandomString(10).nextString(), "123456"));
        }
        System.out.println("Finished");
    }

    @Test
    void RandomBooks(){
        for(int i=0;i<=20;i++){
            System.out.println(bookMapper.addBook("TESTBOOK-" + new RandomString(10).nextString(),"TESTAUTHOR-" + new RandomString(10).nextString()));
        }
        System.out.println("Finished");
    }

    @Test
    void redisTest(){
        ValueOperations<String,String> operations = template.opsForValue();
        operations.set("java","springboot");
        operations.get("java");
        System.out.println(operations.get("java"));
        // assert Objects.equals(operations.get("java"), "test");
    }

}
