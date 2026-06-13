package com.carapp.security.jwt;

import com.carapp.security.service.UserDetailServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    private final UserDetailServiceImpl userDetailService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = parseJwt(request);

        if (jwt != null
                && jwtUtils.validateJwtToken(jwt)) {

            String email =
                    jwtUtils.getEmailFromToken(jwt);

            UserDetails userDetails =
                    userDetailService
                            .loadUserByUsername(email);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(auth);
        }

        filterChain.doFilter(
                request,
                response
        );
    }

    private String parseJwt(
            HttpServletRequest request) {

        String header =
                request.getHeader("Authorization");

        if (header != null
                && header.startsWith("Bearer ")) {

            return header.substring(7);
        }

        return null;
    }

    @Override
    protected boolean shouldNotFilter(
            HttpServletRequest request) {

        String path =
                request.getRequestURI();

        return path.startsWith("/api/auth/login");
    }
}