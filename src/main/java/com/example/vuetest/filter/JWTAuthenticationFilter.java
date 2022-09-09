package com.example.vuetest.filter;

import com.example.vuetest.utils.Jwtutil;
import io.jsonwebtoken.Claims;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

    private static RedisTemplate<Object,Object> template;

    public static void setTemplate(RedisTemplate<Object,Object> template){
        JWTAuthenticationFilter.template = template;
    }

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader("Authentication");  //获取header中携带的token
        System.out.println("JWTFilter");
        System.out.println(token);

        if(token==null||!token.startsWith("JWT_TOKEN")){    //未携带token
            SecurityContextHolder.clearContext();
            chain.doFilter(request,response);
            return;
        }

        String tokenValue = token.replace("JWT_TOKEN","");
        UsernamePasswordAuthenticationToken authentication = null;
        Claims claims = Jwtutil.verifyJwt(token);
        try{
            String PreviousToken = (String) template.opsForValue().get(claims.get("username"));
            if(!token.equals(PreviousToken)){
                SecurityContextHolder.clearContext();
                chain.doFilter(request,response);
                return;
            }
        } catch (Exception e){
            e.printStackTrace();
            chain.doFilter(request,response);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(claims.get("username"),token, Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))));
        //setAuthentication需要权限参数
        chain.doFilter(request,response);
    }
}
