package com.G2T7.OurGardenStory.config;

import java.util.*;

import com.G2T7.OurGardenStory.security.AwsCognitoJwtAuthFilter;
import com.G2T7.OurGardenStory.security.JwtAuthenticationEntryPoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    public static final String SIGNUP_URL = "/api/users/sign-up";
    public static final String SIGNIN_URL = "/api/users/sign-in";

    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private AwsCognitoJwtAuthFilter awsCognitoJwtAuthenticationFilter;

    @Autowired
    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, AwsCognitoJwtAuthFilter awsCognitoJwtAuthenticationFilter) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.awsCognitoJwtAuthenticationFilter = awsCognitoJwtAuthenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain customJwtSecurityChain(HttpSecurity http) throws Exception {

        List<String> permitAllEndpointList = Arrays.asList(SIGNUP_URL, SIGNIN_URL); // add "/home" etc.?

        http.csrf().disable().cors().disable()// .authorizeHttpRequests()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(permitAllEndpointList.toArray(new String[permitAllEndpointList.size()])).permitAll()
                .antMatchers(HttpMethod.GET, "/").permitAll()
                .antMatchers(HttpMethod.GET, "/garden").permitAll()
                .antMatchers(HttpMethod.POST, "/garden").permitAll()
                .antMatchers(HttpMethod.PUT, "/garden").permitAll()
                .antMatchers(HttpMethod.DELETE, "/garden").permitAll()

                .antMatchers(HttpMethod.GET, "/ballots").permitAll()
                .antMatchers(HttpMethod.GET, "/ballot").permitAll()
                .antMatchers(HttpMethod.POST, "/ballot").permitAll() // combine to line 43, permitAllEndpointList?

                .antMatchers(HttpMethod.GET, "/geocode").permitAll()
                .antMatchers(HttpMethod.PUT, "/geocode").permitAll()

                .antMatchers(HttpMethod.POST, "/window").permitAll()
                .antMatchers(HttpMethod.GET, "/window").authenticated()
                .antMatchers(HttpMethod.GET, "/window/{id}/garden").permitAll()
                .antMatchers(HttpMethod.POST, "/window/{id}/garden").permitAll()
                .antMatchers(HttpMethod.PUT, "/window/{id}/garden").permitAll()
                .antMatchers(HttpMethod.DELETE, "/window/{id}/garden").permitAll()
                .antMatchers(HttpMethod.PUT, "/window").permitAll()
                .antMatchers(HttpMethod.DELETE, "/window").permitAll()
        
                .antMatchers(HttpMethod.GET, "/api/users/user").permitAll()

                .antMatchers(HttpMethod.GET, "/magic").permitAll()

                .antMatchers(HttpMethod.GET, "/window/{winId}/ballot").permitAll()
                .antMatchers(HttpMethod.POST, "/window/{winId}/ballot").permitAll()
                .antMatchers(HttpMethod.PUT, "/window/{winId}/ballot").permitAll()
                .antMatchers(HttpMethod.DELETE, "/window/{winId}/ballot").permitAll()
                .antMatchers(HttpMethod.DELETE, "/window/{winId}/allBallot").permitAll()


                .antMatchers(HttpMethod.GET, "/community/{garden}").permitAll()

                .antMatchers(HttpMethod.GET, "/payment").authenticated()
                .antMatchers(HttpMethod.POST, "/payment").authenticated()

                .anyRequest().permitAll();

        http.addFilterBefore(awsCognitoJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class).cors();

        return http.build();
    }
}
