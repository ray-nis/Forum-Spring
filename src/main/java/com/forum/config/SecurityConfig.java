package com.forum.config;

import com.forum.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .and()
                .headers().frameOptions().disable()
                .and()
                .csrf().ignoringAntMatchers("/h2-console/**")
                .and()
                .cors().disable();

        http.authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/static/**").permitAll()
                .antMatchers("/about", "/contact", "/search", "/rules").permitAll()
                .antMatchers("/", "/login", "/signup", "/resetPasswordToken", "/resetpassword","/registrationConfirm").permitAll()
                .antMatchers("/profile", "/profile/*").authenticated()
                .antMatchers("/post/**").permitAll()
                .antMatchers(HttpMethod.GET, "/category/**/new").authenticated()
                .antMatchers(HttpMethod.POST, "/category/**/new").authenticated()
                .antMatchers("/category/**/post/**").permitAll()
                .antMatchers("/category/**").permitAll()
                .anyRequest().denyAll()
                .and()
                .formLogin()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .rememberMe().tokenValiditySeconds(86400);


    }
}
