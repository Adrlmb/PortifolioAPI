package com.example.portfolioAPI.auth.filter;

import com.example.portfolioAPI.auth.jwt.JwtService;
import com.example.portfolioAPI.users.entity.UserEntity;
import com.example.portfolioAPI.users.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException{

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith(("Bearer "))) {
            String token = header.substring(7);
            String email = jwtService.getEmailFromToken(token);

            UserEntity user = userRepository.findByEmail(email).orElse(null);

            if(user != null){
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());

                SecurityContextHolder.getContext().setAuthentication(auth);
            }

chain.doFilter(request, response);        }
    }
}
