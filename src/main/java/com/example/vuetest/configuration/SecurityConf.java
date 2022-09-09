package com.example.vuetest.configuration;

import com.example.vuetest.filter.JWTAuthenticationFilter;
import com.example.vuetest.handler.onAuthSuccess;
import com.example.vuetest.handler.onLogOutSuccess;
import com.example.vuetest.repo.RedisTokenRepository;
import com.example.vuetest.services.UserAuthService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConf extends WebSecurityConfigurerAdapter {

    @Resource
    UserAuthService userAuthService;

    @Resource
    onAuthSuccess authSuccess;

    @Resource
    RedisTokenRepository tokenRepository;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userAuthService).passwordEncoder(new BCryptPasswordEncoder());
        //需要passwordEncoder
    }

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/api/**").antMatchers("/api/javalogin");
//    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors()
                .and()
                .authorizeRequests().antMatchers("/static/**").permitAll()
//                .antMatchers("/api/**").permitAll()
//                .antMatchers("api/**").permitAll()
                .antMatchers("/api/auth/**").permitAll()
                //  .antMatchers("/api/**").hasAnyRole("user")
                .anyRequest().authenticated()
                .and().formLogin()
//                .loginPage("/api/auth/login")
                .loginProcessingUrl("/api/auth/login")
                .successHandler(successHandler())
                .and()
                .logout()
                .logoutUrl("/api/auth/logout")
                .logoutSuccessHandler(logoutsuccessHandler())
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                //.defaultSuccessUrl("/index").permitAll();
                //.and()
                .rememberMe()
                .rememberMeParameter("remember")
                .tokenRepository(tokenRepository);
    }

    public onAuthSuccess successHandler(){
        return new onAuthSuccess();
    }
    public onLogOutSuccess logoutsuccessHandler(){ return new onLogOutSuccess(); }


}
