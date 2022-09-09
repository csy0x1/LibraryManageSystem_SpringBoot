package com.example.vuetest.mapper;

import com.example.vuetest.entity.Books;
import com.example.vuetest.entity.MainUser;
import com.example.vuetest.services.imple.RedisMybatisCache;
import org.apache.ibatis.annotations.*;

import java.util.List;

@CacheNamespace(implementation = RedisMybatisCache.class)
@Mapper
public interface BookMapper {

    @Select("select id,bookname,author,borrowedby from testdb.books")
    List<Books> getBookList();

    @Select("select id,bookname,author,borrowedby from testdb.books where id=#{bookid}")
    Books getBook(@Param("bookid") int id);

    @Insert("insert into testdb.books(bookname,author) values(#{bookname},#{author})")
    int addBook(@Param("bookname") String bookname,@Param("author") String author);

    @Deprecated
    @Update("update testdb.books set borrowedby = #{userid} where id = #{bookid}")
    int borrowBook(@Param("userid") int userid, @Param("bookid") int bookid);

    @Update("update testdb.books set borrowedby = null where id = #{bookid} and borrowedby = #{userid}")
    int returnBook(@Param("bookid") int bookid, @Param("userid") int userid);

    @Delete("delete from testdb.books where id = #{bookid}")
    int removeBook(@Param("bookid") int bookid);

//    @Results({
//            @Result(id = true, column = "id", property = "id"),
//            @Result(column = "username", property = "username"),
//    })

    /**
     * 多对多查询
     */
    @Select("select b.bookname,b.id,u.username,u.id from testdb.books b join testdb.books_user_m2m bum2m on " +
            "b.id = bum2m.bid join testdb.users u on u.id = bum2m.uid where b.id = #{bookid}")
    List<MainUser> getBorrower(int bookid);

    Books borrowBookM2M(@Param("userid") int userid, @Param("bookid") int bookid);
}
