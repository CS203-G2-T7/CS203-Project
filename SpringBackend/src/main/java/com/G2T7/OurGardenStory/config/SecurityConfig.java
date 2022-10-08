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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    public static final String SIGNUP_URL = "/api/users/sign-up";
    public static final String SIGNIN_URL = "/api/users/sign-in";

    AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsService userDetailsService;

    // JWT
    @Autowired
    private JwtRequestFilter jwtRequestFilter;
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain customJwtSecurityChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService);
        authenticationManager = authenticationManagerBuilder.build();

        List<String> permitAllEndpointList = Arrays.asList(SIGNUP_URL, SIGNIN_URL); // add "/home" etc.?

        http.csrf().disable().cors().disable().authorizeHttpRequests()
                .antMatchers(permitAllEndpointList.toArray(new String[permitAllEndpointList.size()]))
                .permitAll()
                .antMatchers(HttpMethod.GET, "/garden").permitAll()
                .antMatchers(HttpMethod.POST, "/garden").permitAll()
                .antMatchers(HttpMethod.GET, "/ballots").permitAll()
                .antMatchers(HttpMethod.GET, "/ballot").permitAll()
                .antMatchers(HttpMethod.POST, "/ballot").permitAll() // combine to line 43, permitAllEndpointList?
                .antMatchers(HttpMethod.PUT, "/geocode").permitAll()
                .antMatchers(HttpMethod.POST, "/window").permitAll()
                .antMatchers(HttpMethod.GET, "/windows").permitAll()
                .antMatchers(HttpMethod.GET, "/window").permitAll()
                .antMatchers(HttpMethod.GET, "/magic").permitAll()
                .antMatchers(HttpMethod.PUT, "/window").permitAll()
                .antMatchers(HttpMethod.GET, "/geocode").permitAll()
                .anyRequest().permitAll().and() // all other routes, including undefined ones must be authenticated.
                .authenticationManager(authenticationManager)
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
