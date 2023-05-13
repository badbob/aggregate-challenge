package com.vladimir.loshchin.review.reviewservice

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Bean
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.http.HttpMethod
import org.springframework.web.bind.annotation.RequestMethod

@Configuration
@EnableWebSecurity
class SecurityConfig {
    
    @Bean
    fun userDetailsService() : UserDetailsService {
        val user =
             User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        return InMemoryUserDetailsManager(user);
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity) : SecurityFilterChain {

        http
         .csrf().disable()
         .authorizeHttpRequests()
            .requestMatchers(HttpMethod.GET).anonymous()
            .anyRequest().authenticated()
            .and()
            .httpBasic()

        return http.build();
    }
}