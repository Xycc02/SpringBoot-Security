package com.xuyuchao.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @Author: xuyuchao
 * @Date: 2022-05-21-13:50
 * @Description:    AOP思想,横切进去
 */
@EnableWebSecurity//这个注解是开启WebSecurity模式
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //授权
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //首页所有人都能访问,功能页只有对应有权限的人才能访问,利用了链式编程

        //请求授权的规则
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/level1/**").hasRole("vip1")
                .antMatchers("/level2/**").hasRole("vip2")
                .antMatchers("/level3/**").hasRole("vip3");

        //没有权限设置跳到登录页
        http.formLogin().loginPage("/toLogin").loginProcessingUrl("/login").usernameParameter("username").passwordParameter("password");

        //注销功能,并跳到首页
        http.logout().logoutSuccessUrl("/index");

        //关闭csrf,登陆或注销失败可能存在的原因
        http.csrf().disable();
        //记住我功能,定制remember me,需指定前端参数
        http.rememberMe().rememberMeParameter("remember");
    }

    //认证 springboot 2.1.x 可以直接使用
    //不能直接使用要 密码编码:PasswordEncoder
    //在spring security5.0+ 新增了很多加密方法
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        //一下是从内存中读取的,正常的应该从数据库中读取
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("admin1").password(new BCryptPasswordEncoder().encode("admin1")).roles("vip1")
                .and()
                .withUser("admin2").password(new BCryptPasswordEncoder().encode("admin2")).roles("vip2")
                .and()
                .withUser("admin3").password(new BCryptPasswordEncoder().encode("admin3")).roles("vip3")
                .and()
                .withUser("root").password(new BCryptPasswordEncoder().encode("root")).roles("vip1","vip2","vip3");

    }
}
