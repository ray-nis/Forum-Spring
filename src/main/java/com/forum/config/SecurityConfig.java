package com.forum.config;

import com.forum.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationFailureHandler loginFailureHandler;

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
                .antMatchers("/css/**", "/js/**", "/static/**", "/webjars/**").permitAll()
                .antMatchers("/about", "/contact", "/search", "/rules").permitAll()
                .antMatchers("/", "/login", "/signup", "/resendverification","/resetPassword", "/forgotpassword", "/forgotpasswordsent","/registrationConfirm").permitAll()
                .antMatchers("/profile", "/profile/*", "/editprofile", "/favorites").authenticated()
                .antMatchers("/changepassword", "/changeusername", "/changeemail").authenticated()
                .antMatchers("/chat", "/chat/**","/web-socket/**", "/messages/**").authenticated()
                .antMatchers("/post/**").permitAll()
                .antMatchers(HttpMethod.GET, "/category/**/new").authenticated()
                .antMatchers(HttpMethod.POST, "/category/**/new").authenticated()
                .antMatchers("/category/**/post/**/favorite").authenticated()
                .antMatchers("/category/**/post/**/like").authenticated()
                .antMatchers("/category/**/post/**/edit").authenticated()
                .antMatchers(HttpMethod.POST, "/category/**/post/**/report").authenticated()
                .antMatchers(HttpMethod.POST, "/category/**/post/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/category/**/post/**").authenticated()
                .antMatchers("/category/**/post/**").permitAll()
                .antMatchers("/category/**").permitAll()
                .antMatchers("/moderator", "/moderator/**").hasRole("MODERATOR")
                .anyRequest().denyAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .failureHandler(loginFailureHandler)
                .defaultSuccessUrl("/")
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .rememberMe().tokenValiditySeconds(86400);
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_ADMIN > ROLE_MODERATOR \n ROLE_MODERATOR > ROLE_USER";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    @Bean
    public DefaultWebSecurityExpressionHandler expressionHandler() {
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }
}
