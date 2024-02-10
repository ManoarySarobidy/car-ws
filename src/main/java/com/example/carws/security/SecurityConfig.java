package com.example.carws.security;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.boot.web.servlet.DispatcherType;

@Configuration
@EnableMethodSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SecurityProperties restSecProps;

    @Autowired
    SecurityFilter tokenAuthenticationFilter;

    @Bean
    public AuthenticationEntryPoint restAuthenticationEntryPoint() {
        return (httpServletRequest, httpServletResponse, e) -> {
            Map<String, Object> errorObject = new HashMap<>();
            int errorCode = 401;
            e.printStackTrace();
            errorObject.put("message", e.getMessage());
            errorObject.put("error", HttpStatus.UNAUTHORIZED);
            errorObject.put("code", errorCode);
            errorObject.put("timestamp", new Timestamp(new Date().getTime()));
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.setStatus(errorCode);
            httpServletResponse.getWriter().write(objectMapper.writeValueAsString(errorObject));
        };
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // configuration.setExposedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain( HttpSecurity http) throws Exception {
         http
            .csrf().disable()
            .cors().configurationSource(this.corsConfigurationSource()).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .httpBasic().disable().exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint()).and()
            .authorizeRequests(request -> {
                request
                    .requestMatchers("/api/users/inscription_valide/**").permitAll()
                    .requestMatchers("/api/users/inscription").permitAll()
                    .requestMatchers("/api/users/login").permitAll()
                    .requestMatchers("/api/annonces/list").permitAll()
                    .requestMatchers("/api/annonces/{id}").permitAll()
                    .requestMatchers("/api/annonces/search").permitAll()
                    .requestMatchers("/api/moteurs").permitAll()
                    .requestMatchers("/api/marques").permitAll()
                    .requestMatchers("/api/modeles").permitAll()
                    .requestMatchers("/api/vitesses").permitAll()
                    .requestMatchers("/api/categories").permitAll()
                    .requestMatchers("/api/couleurs").permitAll()
                    .requestMatchers("/api/users/authentification").permitAll()
                    .requestMatchers("/api/users/token/**").permitAll()
                    .requestMatchers("/api/users/authentificationAdmin").permitAll()
                    .requestMatchers("/api/users/offline-auth").permitAll()
                    .anyRequest().authenticated();
            });
        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        // http.addFilterAfter(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}