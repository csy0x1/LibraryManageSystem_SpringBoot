package com.example.vuetest.mapper;

import com.example.vuetest.entity.MainUser;
import com.example.vuetest.entity.Role;
import com.example.vuetest.services.imple.RedisMybatisCache;
import org.apache.ibatis.annotations.*;

import java.util.List;

@CacheNamespace(implementation = RedisMybatisCache.class)
@Mapper
public interface UserMapper {

    @Select("select id,username,password,role from testdb.users where id=#{id}")
    MainUser getUserByID(int id);

//    @Select("select id,username,password,role, roles.Roleid,roles.Rolename from testdb.users inner join testdb.roles on users.newRole=roles.Roleid where username=#{username}")
//    MainUser getUserByUsername(String username);

    @Select("select * from testdb.users where username=#{username}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "newrole", column = "newRole", many =
                @Many(select = "selectRoles")
            )
    })
    MainUser getUserByUsername(String username);

    @Insert("insert into testdb.users(username,password) values(#{username},#{password})")
    int registerUser(@Param("username") String username, @Param("password") String password);

    @Select("select * from testdb.roles where Roleid = #{Roleid}")
    @Results({
            @Result(property = "roleID", column = "Roleid"),
            @Result(property = "roleName", column = "Rolename")
    })
    Role selectRoles(int Roleid);
}
