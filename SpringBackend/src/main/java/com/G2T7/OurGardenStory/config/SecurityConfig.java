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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    public static final String SIGNUP_URL = "/api/users/sign-up";
    public static final String SIGNIN_URL = "/api/users/sign-in";

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final AwsCognitoJwtAuthFilter awsCognitoJwtAuthenticationFilter;

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

        List<String> permitAllEndpointList = Arrays.asList(SIGNUP_URL, SIGNIN_URL);

        http.csrf().disable().cors().disable()// .authorizeHttpRequests()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(permitAllEndpointList.toArray(new String[permitAllEndpointList.size()])).permitAll()

                .antMatchers(HttpMethod.GET, "/").permitAll()

                .antMatchers(HttpMethod.GET, "/garden").authenticated()
                .antMatchers(HttpMethod.POST, "/garden").authenticated()
                .antMatchers(HttpMethod.PUT, "/garden").authenticated()
                .antMatchers(HttpMethod.DELETE, "/garden").authenticated()

                .antMatchers(HttpMethod.GET, "/window").authenticated()
                .antMatchers(HttpMethod.POST, "/window").authenticated()
                .antMatchers(HttpMethod.PUT, "/window").authenticated()
                .antMatchers(HttpMethod.DELETE, "/window").authenticated()
                .antMatchers(HttpMethod.GET, "/window/{id}/garden").authenticated()
                .antMatchers(HttpMethod.POST, "/window/{id}/garden").authenticated()
                .antMatchers(HttpMethod.PUT, "/window/{id}/garden").authenticated()
                .antMatchers(HttpMethod.DELETE, "/window/{id}/garden").authenticated()
        
                .antMatchers(HttpMethod.GET, "/api/users/user").authenticated()

                .antMatchers(HttpMethod.GET, "/window/{winId}/ballot").authenticated()
                .antMatchers(HttpMethod.POST, "/window/{winId}/ballot").authenticated()
                .antMatchers(HttpMethod.PUT, "/window/{winId}/ballot").authenticated()
                .antMatchers(HttpMethod.DELETE, "/window/{winId}/ballot").authenticated()
                .antMatchers(HttpMethod.DELETE, "/window/{winId}/allBallot").authenticated()

                .antMatchers(HttpMethod.GET, "my-plant").authenticated()
                .antMatchers(HttpMethod.POST, "my-plant").authenticated()
                .antMatchers(HttpMethod.DELETE, "my-plant").authenticated()

                .antMatchers(HttpMethod.GET, "/community").authenticated()

                .antMatchers(HttpMethod.GET, "/payment").authenticated()
                .antMatchers(HttpMethod.POST, "/payment").authenticated()

                .anyRequest().permitAll();

        http.addFilterBefore(awsCognitoJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class).cors();

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("HEAD",
                "GET", "POST", "PUT", "DELETE", "PATCH"));
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(true);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
