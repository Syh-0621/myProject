package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){
        log.info(new BCryptPasswordEncoder().encode("syh666"));
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                    .authorizeHttpRequests((requests) -> {
                        requests.requestMatchers("/code").permitAll();
                        requests.requestMatchers("/register").permitAll();
                        requests.requestMatchers("/js/**").permitAll();
                        requests.anyRequest().authenticated();
                    })
                    .formLogin((form) -> {
                        form.loginPage("/login");
                        form.loginProcessingUrl("/doLogin");
                        form.defaultSuccessUrl("/index");
                        form.permitAll();
                        form.usernameParameter("username");
                        form.passwordParameter("password");
                    })
                    .logout((logout) -> {
                        logout.logoutUrl("/doLogout");
                        logout.logoutSuccessUrl("/login");
                        logout.permitAll();
                    })
                    .rememberMe((rememberMe) -> {
                        rememberMe.alwaysRemember(false);
                        rememberMe.rememberMeParameter("remember-me");
                    })
                    .csrf((csrf) -> {
                        csrf.disable();
                    })
                    .build();
    }
}
