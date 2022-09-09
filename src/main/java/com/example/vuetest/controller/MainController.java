package com.example.vuetest.controller;

import com.example.vuetest.entity.Books;
import com.example.vuetest.entity.MainUser;
import com.example.vuetest.mapper.BookMapper;
import com.example.vuetest.mapper.UserMapper;
import com.example.vuetest.services.UserAuthService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
public class MainController {

    @Resource
    UserMapper userMapper;
    @Resource
    BookMapper bookMapper;

//    @RequestMapping("/index")
//    @ResponseBody
//    public String index(){
////        context.publishEvent(new ListenerEvent("有人访问了登录界面！"));
//        System.out.println("登录界面");
//        return "主页测试aeeasdasda";
//    }
//    @CrossOrigin(origins = "*")
//    @RequestMapping("/javalogin")
//    @ResponseBody
//    public int logintest(HttpServletResponse resp){
//        resp.setHeader("Access-Control-Allow-Origin","*");
//        System.out.println("login1");
//        return 200;
//    }

//    @CrossOrigin(origins = "*")
//    @RequestMapping("/api/javalogin")
//    @ResponseBody
//    public int logintest2(HttpServletResponse resp, HttpServletRequest req, @RequestBody Map<String,String> request){
//        resp.setHeader("Access-Control-Allow-Origin","*");
//        System.out.println(HtmlUtils.htmlEscape(request.get("username")));
//        System.out.println(req.getParameter("password"));
//        System.out.println("login");
//        return 200;
//    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/api/get/booklist")
    @ResponseBody
    public List<Map> booklist(HttpServletResponse resp){
//        resp.setHeader("Access-Control-Allow-Origin","*");

        Map<String,List<Books>> bookListMap = new HashMap<>();
        List<Books> booksList = bookMapper.getBookList();
        bookListMap.put("booksList",booksList);

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();
        System.out.println(UserAuthService.getLoginStatus(context));
        User user = (User) auth.getPrincipal();
        Map<String, Integer> useridmap = new HashMap<>();
        useridmap.put("userid",userMapper.getUserByUsername(user.getUsername()).getId());

        List<Map> listmap = new ArrayList<Map>();
        listmap.add(bookListMap);
        listmap.add(useridmap);

        return listmap;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/api/books/addBook")
    @ResponseBody
    public int AddBook(HttpServletResponse resp,@RequestParam Map<String,String> param){
//        resp.setHeader("Access-Control-Allow-Origin","*");
        String bookname = param.get("bookname");
        String author = param.get("author");
        try{
            if(bookMapper.addBook(bookname,author)>0){
                return 201;
            }
        } catch (Exception e){
            System.out.println(e);
            return 400;
        }
        return 400;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/api/books/removeBook")
    public void removeBook(HttpServletResponse resp, @RequestParam Map<String,String> param) throws IOException {
        int bookid = Integer.parseInt(param.get("bookid"));
        PrintWriter writer = resp.getWriter();
        try {
                if(bookMapper.getBook(bookid).getBorrowedby()==null){
                    bookMapper.removeBook(bookid);
                    writer.write("removed");
                }
        } catch (Exception e){
                System.out.println(e);
                resp.sendError(433,"failed!"+e);
            }
    }


    @CrossOrigin(origins = "*")
    @RequestMapping("/api/books/borrow")
//    @ResponseBody
    public void BorrowBooks(HttpServletResponse resp,@RequestParam Map<String, String> param) throws IOException {
        int bookid = Integer.parseInt(param.get("bookid"));
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();
        User user = (User) auth.getPrincipal();
        PrintWriter writer = resp.getWriter();
        try{
            if(bookMapper.getBook(bookid).getBorrowedby()==null){
                MainUser user1 = userMapper.getUserByUsername(user.getUsername());
                bookMapper.borrowBook(user1.getId(),bookid);
                System.out.println("success");
                writer.write("borrowed");
            } else{
                resp.sendError(433,"already borrowed by someone");
            }

        }catch (Exception e){
            System.out.println(e);
            resp.sendError(433, String.valueOf(e));
//            return 400;
        }
    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/api/books/return")
    public void ReturnBooks(HttpServletResponse resp,@RequestParam Map<String, String> param) throws IOException {
        int bookid = Integer.parseInt(param.get("bookid"));
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();
        User user = (User) auth.getPrincipal();
        PrintWriter writer = resp.getWriter();
        try{
            MainUser user1 = userMapper.getUserByUsername(user.getUsername());
            if(bookMapper.getBook(bookid).getBorrowedby()==user1.getId()){
                bookMapper.returnBook(bookid, user1.getId());
                System.out.println("success");
                writer.write("returned");
            } else{
                resp.sendError(433,"Unauthorized");
            }

        }catch (Exception e){
            System.out.println(e);
            resp.sendError(433, String.valueOf(e));
        }
    }
}
