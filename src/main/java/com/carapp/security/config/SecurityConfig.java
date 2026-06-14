package com.carapp.security.config;

import com.carapp.security.jwt.JwtAuthenticationEntryPoint;
import com.carapp.security.jwt.JwtAuthenticationFilter;
import com.carapp.security.service.UserDetailServiceImpl;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailServiceImpl userDetailService;

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))

                .exceptionHandling(ex ->
                        ex.authenticationEntryPoint(
                                authenticationEntryPoint))

                .authorizeHttpRequests(auth -> auth

                        // LOGIN HERKES
                        .requestMatchers("/api/auth/**").permitAll()

                        // PRODUCT GÖRME → USER + ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/products/**")
                        .hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                        // PRODUCT EKLEME → USER + ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/products/**")
                        .hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                        // PRODUCT GÜNCELLEME → USER  ADMIN
                        .requestMatchers(HttpMethod.PUT, "/api/products/**")
                        .hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        // PRODUCT SİLME → SADECE ADMIN
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**")
                        .hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                        // USER İŞLEMLERİ → SADECE ADMIN
                        .requestMatchers("/api/users/**")
                        .hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .authenticationProvider(
                        authenticationProvider())

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();

        provider.setUserDetailsService(
                userDetailService);

        provider.setPasswordEncoder(
                passwordEncoder());

        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config)
            throws Exception {

        return config.getAuthenticationManager();
    }
}