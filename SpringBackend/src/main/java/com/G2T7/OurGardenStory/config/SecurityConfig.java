package com.G2T7.OurGardenStory.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    public static final String SIGNUP_URL = "/api/users/sign-up";
    public static final String SIGNIN_URL = "/api/users/sign-in";

    AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain customJwtSecurityChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService);
        authenticationManager = authenticationManagerBuilder.build();

        List<String> permitAllEndpointList = Arrays.asList(SIGNUP_URL, SIGNIN_URL);

        http.csrf().disable().cors().disable().authorizeHttpRequests().antMatchers(permitAllEndpointList
                        .toArray(new String[permitAllEndpointList.size()])).permitAll()
                .antMatchers(HttpMethod.GET, "/api/users/detail").authenticated()
                .anyRequest().authenticated()
                .and()
                .authenticationManager(authenticationManager)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();
    }
}


//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        List<String> permitAllEndpointList = Arrays.asList(SIGNUP_URL, SIGNIN_URL);
//
//        http.cors().and().csrf().disable()
//                .authorizeRequests(expressionInterceptUrlRegistry -> expressionInterceptUrlRegistry
//                        .antMatchers(permitAllEndpointList
//                                .toArray(new String[permitAllEndpointList.size()]))
//                        .permitAll().anyRequest().authenticated())
//                .oauth2ResourceServer().jwt();
//    }
//}